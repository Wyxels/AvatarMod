package com.crowsofwar.avatar.common.bending.fire.tickhandlers;

import com.crowsofwar.avatar.common.bending.fire.AbilityFireBlast;
import com.crowsofwar.avatar.common.bending.fire.AbilityFlamethrower;
import com.crowsofwar.avatar.common.bending.fire.Firebending;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.EntityFlamethrower;
import com.crowsofwar.avatar.common.particle.ParticleBuilder;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import static com.crowsofwar.avatar.common.config.ConfigClient.CLIENT_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.gorecore.util.Vector.getEyePos;
import static java.lang.Math.toRadians;

public class FireblastUpdateTick extends TickHandler {

	public FireblastUpdateTick(int id) {
		super(id);
	}

	@Override
	public boolean tick(BendingContext ctx) {
		BendingData data = ctx.getData();
		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		World world = ctx.getWorld();
		AbilityData abilityData = data.getAbilityData("flamethrower");

		//Don't remove this, it makes sure the ability data works properly.
		if (!world.isRemote)
			abilityData = data.getAbilityData(new AbilityFlamethrower().getName());

		AbilityData.AbilityTreePath path = abilityData.getPath();

		int level = abilityData.getLevel();
		int flamesPerSecond;

		//TODO: Movement modifier


		flamesPerSecond = level <= 0 ? 1 : 2;
		if (level == 3 && path == AbilityData.AbilityTreePath.FIRST)
			flamesPerSecond = 3;
		else if (level == 3 && path == AbilityData.AbilityTreePath.SECOND)
			flamesPerSecond = 1;


		double powerRating = bender.calcPowerRating(Firebending.ID);

		float requiredChi = STATS_CONFIG.chiFlamethrowerSecond / 20;
		if (level == 3 && path == AbilityData.AbilityTreePath.FIRST) {
			requiredChi = STATS_CONFIG.chiFlamethrowerSecondLvl4_1 / 20;
		}
		if (level == 3 && path == AbilityData.AbilityTreePath.SECOND) {
			requiredChi = STATS_CONFIG.chiFlamethrowerSecondLvl4_2 / 20;
		}

		// Adjust chi to power rating
		// Multiply chi by a number (from 0..2) based on the power rating - powerFactor
		//  Numbers 0..1 would reduce the chi, while numbers 1..2 would increase the chi
		// maxPowerFactor: maximum amount that the chi can be multiplied by
		// e.g. 0.1 -> chi can be changed by 10%; powerFactor in between 0.9..1.1
		double maxPowerFactor = 0.4;
		double powerFactor = (powerRating + 100) / 100 * maxPowerFactor + 1 - maxPowerFactor;
		requiredChi *= powerFactor;

		if (bender.consumeChi(requiredChi)) {

			Vector eye = getEyePos(entity);
			boolean inWaterBlock = world.getBlockState(entity.getPosition()) instanceof BlockLiquid || world.getBlockState(entity.getPosition()).getBlock() == Blocks.WATER
					|| world.getBlockState(entity.getPosition()).getBlock() == Blocks.FLOWING_WATER;
			boolean headInLiquid = world.getBlockState(entity.getPosition().up()) instanceof BlockLiquid || world.getBlockState(entity.getPosition().up()).getBlock() == Blocks.WATER
					|| world.getBlockState(entity.getPosition().up()).getBlock() == Blocks.FLOWING_WATER;

			if (!(world.isRaining() && world.canSeeSky(entity.getPosition())) && !(headInLiquid || inWaterBlock)) {

				double speedMult = 15 + 5 * abilityData.getXpModifier();
				double randomness = 3.0 - 0.5 * (abilityData.getXpModifier() + Math.max(abilityData.getLevel(), 0));
				float range = 4;
				int fireTime = 2;
				float size = 0.75F;
				float damage = STATS_CONFIG.flamethrowerSettings.damage;
				float performanceAmount = 1;
				float xp = SKILLS_CONFIG.flamethrowerHit;
				int frequency = 3;

				switch (abilityData.getLevel()) {
					case 1:
						size = 1.125F;
						damage = 1.75F;
						fireTime = 3;
						frequency = 3;
						range = 5;
						performanceAmount = 2;
						break;
					case 2:
						size = 1.5F;
						fireTime = 4;
						damage = 2.5F;
						range = 7;
						frequency = 4;
						performanceAmount = 3;
						break;
				}
				if (level == 3 && path == AbilityData.AbilityTreePath.FIRST) {
					speedMult = 38;
					randomness = 0;
					fireTime = 5;
					size = 1.25F;
					damage = 4.5F;
					range = 11;
					performanceAmount = 4;
					frequency = 2;
				}
				if (level == 3 && path == AbilityData.AbilityTreePath.SECOND) {
					speedMult = 12;
					randomness = 9;
					fireTime = 10;
					size = 2.75F;
					damage = 1.5F;
					range = 6.5F;
					performanceAmount = 2;
					frequency = 5;
				}

				// Affect stats by power rating
				range += powerFactor / 100F;
				size += powerRating / 200F;
				damage += powerRating / 100F;
				fireTime += (int) (powerRating / 50F);
				speedMult += powerRating / 100f * 2.5f;
				randomness = randomness >= powerRating / 100f * 2.5f ? randomness - powerRating / 100F * 2.5 : 0;
				randomness = randomness < 0 ? 0 : randomness;

				double yawRandom = entity.rotationYaw + (Math.random() * 2 - 1) * randomness;
				double pitchRandom = entity.rotationPitch + (Math.random() * 2 - 1) * randomness;
				Vector look = randomness == 0 ? Vector.getLookRectangular(entity) : Vector.toRectangular(toRadians(yawRandom), toRadians(pitchRandom));


				Vector start = look.plus(eye.minusY(0.5));
				Vector end = start.plus(look.times(range));


				Vector knockback = look.times(speedMult / 10 * STATS_CONFIG.flamethrowerSettings.push);
				Vector position = look.times((double) flamesPerSecond / 1000D).plus(eye.minusY(0.5));

				//Spawn Entity Code.
				Vec3d height, rightSide;
				if (entity instanceof EntityPlayer) {
					height = entity.getPositionVector().add(0, 0.84, 0);
					height = height.add(entity.getLookVec().scale(0.1));
					//Right
					if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw + 90), 0).times(0.5).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					}
					//Left
					else {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw - 90), 0).times(0.5).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					}
				} else {
					height = entity.getPositionVector().add(0, 0.84, 0);
					height = height.add(entity.getLookVec().scale(0.1));
					if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw + 90), 0).times(0.5).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					} else {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw - 90), 0).times(0.5).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					}

				}

				//TODO: Tick handler so the player can create more dynamic shapes
				//data.addTickHandler();
				EntityFlamethrower fireblast = new EntityFlamethrower(world);
				fireblast.setTier(Math.max(abilityData.getLevel() + 1, 1));
				fireblast.setPosition(rightSide);
				fireblast.setVelocity(position.plus(look.times(speedMult)));
				fireblast.setBehaviour(new AbilityFireBlast.FireblastBehaviour());
				fireblast.setElement(new Firebending());
				fireblast.setAbility(new AbilityFireBlast());
				fireblast.setLifeTime(10 + AvatarUtils.getRandomNumberInRange(0, 4));
				fireblast.setExpandedHitbox(2, 2);
				fireblast.setDamage(damage);
				fireblast.setEntitySize(size);
				fireblast.setKnockback(knockback.toMinecraft());
				fireblast.setXp(xp);
				fireblast.setOwner(entity);
				world.spawnEntity(fireblast);
				entity.swingArm(EnumHand.MAIN_HAND);
				//Particle code.
				if (world.isRemote) {
					speedMult /= 28.75;
					if (CLIENT_CONFIG.fireRenderSettings.useFlamethrowerParticles) {
						for (double i = 0; i < flamesPerSecond; i += 3) {
							Vector start1 = look.times((i / (double) flamesPerSecond) / 10000).plus(eye.minusY(0.5));
							ParticleBuilder.create(ParticleBuilder.Type.FIRE).pos(start1.toMinecraft()).scale(size * 2F).time(22).collide(true).spawnEntity(entity).vel(look.times(speedMult).toMinecraft())
									.spawn(world);
						}
					}
					for (int i = 0; i < flamesPerSecond; i++) {
						Vector start1 = look.times((i / (double) flamesPerSecond) / 10000).plus(eye.minusY(0.5));
						if (CLIENT_CONFIG.fireRenderSettings.useFlamethrowerParticles) {
							ParticleBuilder.create(ParticleBuilder.Type.FIRE).pos(start.toMinecraft()).scale(size * 2F).time(22).collide(false).vel(look.times(speedMult / 1.25).toMinecraft()).
									ability(new AbilityFlamethrower()).spawn(world);
							ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(start1.toMinecraft()).time(12 + AvatarUtils.getRandomNumberInRange(0, 5)).vel(look.times(speedMult).toMinecraft()).
									clr(255, 30 + AvatarUtils.getRandomNumberInRange(0, 50), 15, 255).collide(true).element(new Firebending()).spawnEntity(entity).scale(size * 2)
									.ability(new AbilityFlamethrower()).spawn(world);
						} else if (!CLIENT_CONFIG.fireRenderSettings.useFlamethrowerParticles) {
							ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(start1.toMinecraft()).time(12 + AvatarUtils.getRandomNumberInRange(0, 5)).vel(look.times(speedMult).toMinecraft()).
									clr(235 + AvatarUtils.getRandomNumberInRange(0, 20), 10, 5, 255).collide(true).spawnEntity(entity).scale(size * 2).element(new Firebending())
									.ability(new AbilityFlamethrower()).spawn(world);
							ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(start1.toMinecraft()).time(12 + AvatarUtils.getRandomNumberInRange(0, 5)).vel(look.times(speedMult).toMinecraft()).
									clr(255, 60 + AvatarUtils.getRandomNumberInRange(1, 40), 10, 200).collide(true).spawnEntity(entity).scale(size * 2).element(new Firebending())
									.ability(new AbilityFlamethrower()).spawn(world);
						}
					}
				}

				if (ctx.getData().getTickHandlerDuration(this) % 4 == 0)
					world.playSound(null, entity.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE,
							SoundCategory.PLAYERS, 0.2f, 0.8f);


			} else {
				if (world.isRemote) {
					for (int i = 0; i < 5; i++)
						ParticleBuilder.create(ParticleBuilder.Type.SNOW).collide(true).time(15).vel(world.rand.nextGaussian() / 50, world.rand.nextGaussian() / 50, world.rand.nextGaussian() / 50)
								.scale(1.5F + abilityData.getLevel() / 2F).pos(Vector.getEyePos(entity).plus(Vector.getLookRectangular(entity)).toMinecraft()).clr(0.75F, 0.75F, 0.75f).spawn(world);

				}
				Vector pos = Vector.getEyePos(entity).plus(Vector.getLookRectangular(entity));
				if (!world.isRemote && world instanceof WorldServer) {
					WorldServer World = (WorldServer) world;
					World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.x(), pos.y(), pos.z(), 3 + Math.max(abilityData.getLevel(), 0),
							0, 0, 0, 0.0015);
				}
				entity.world.playSound(null, new BlockPos(entity), SoundEvents.BLOCK_FIRE_EXTINGUISH, entity.getSoundCategory(),
						1.0F, 0.8F + world.rand.nextFloat() / 10);
				//makes sure the tick handler is removed
				return true;
			}


		} else {
			// not enough chi
			return true;
		}
		return data.getTickHandlerDuration(this) > 4;
	}
}