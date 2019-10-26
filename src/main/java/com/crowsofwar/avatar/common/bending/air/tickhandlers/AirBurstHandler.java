package com.crowsofwar.avatar.common.bending.air.tickhandlers;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.bending.air.AbilityAirBurst;
import com.crowsofwar.avatar.common.bending.air.Airbending;
import com.crowsofwar.avatar.common.damageutils.AvatarDamageSource;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.*;
import com.crowsofwar.avatar.common.entity.data.Behavior;
import com.crowsofwar.avatar.common.entity.data.ShockwaveBehaviour;
import com.crowsofwar.avatar.common.particle.ParticleBuilder;
import com.crowsofwar.avatar.common.util.AvatarEntityUtils;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

public class AirBurstHandler extends TickHandler {
	public static final UUID AIRBURST_MOVEMENT_MODIFIER_ID = UUID.fromString
			("f82d325c-9828-11e8-9eb6-529269fb1459");

	public AirBurstHandler(int id) {
		super(id);
	}

	@Override
	public boolean tick(BendingContext ctx) {
		World world = ctx.getWorld();
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		Bender bender = ctx.getBender();
		AbilityData abilityData = ctx.getData().getAbilityData("air_burst");
		float charge;
		//3 stages, max charge of 3.
		//boolean spawned = false;
		//This is so the tick handler spawns the entity before returning.

		//TODO: Airburst charging!
		if (abilityData != null) {

			float powerRating = ((float) bender.getDamageMult(Airbending.ID));
			float xpMod = abilityData.getXpModifier();

			int duration = data.getTickHandlerDuration(this);
			double damage = STATS_CONFIG.airBurstSettings.damage;
			//Default 5
			float movementMultiplier = 0.6f - 0.7f * MathHelper.sqrt(duration / 40F);
			double knockBack = STATS_CONFIG.airBurstSettings.push;
			//Default 2 + Power rating
			float radius = STATS_CONFIG.airBurstSettings.radius;
			//Default 4
			float durationToFire = STATS_CONFIG.airBurstSettings.durationToFire;
			//Default 40
			double upwardKnockback = STATS_CONFIG.airBurstSettings.push / 40;
			double suction = 0.05;
			int performanceAmount = STATS_CONFIG.airBurstSettings.performanceAmount;

			if (abilityData.getLevel() == 1) {
				damage *= 1.5;
				//7.5
				knockBack *= 1.25;
				radius *= 1.25;
				//4
				durationToFire *= 0.825F;
				//30
				upwardKnockback *= 1.5;
				performanceAmount += 3;
			}

			if (abilityData.getLevel() >= 2) {
				damage *= 2;
				//8
				knockBack *= 2;
				radius *= 1.75F;
				//7
				durationToFire *= 0.75F;
				//20
				upwardKnockback *= 1.75;
				performanceAmount += 5;
			}

			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
				//Piercing Winds
				damage *= 1.25;
				//Blinds enemies

			}

			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
				//Maximum Pressure
				//Pulls enemies in then blasts them out
				radius *= 1.5;
				//10.5
				upwardKnockback = STATS_CONFIG.airBurstSettings.push / 10;
				durationToFire *= (4 / 3F);
				//Back to the original amount.
				performanceAmount += 2;
			}

			durationToFire *= (1 / powerRating);
			durationToFire -= xpMod * 10;
			damage *= powerRating * xpMod;
			radius *= powerRating * xpMod;
			knockBack *= powerRating * xpMod;

			//Makes sure the charge is never 0.
			charge = Math.max((int) (3 * (duration / durationToFire)), 1);

			//Affect things by the charge. The charge, at stage 3, should set everything to its max.
			damage *= (0.25 + 0.25 * charge);
			//Results in a bigger radius so that it blocks projectiles.
			radius *= (0.60 + 0.133333333333 * charge);
			knockBack *= (0.60 + 0.133333333333 * charge);
			performanceAmount *= (0.25 + 0.25 * charge);


			applyMovementModifier(entity, MathHelper.clamp(movementMultiplier, 0.1f, 1));
			double inverseRadius = (durationToFire - duration) / 10;
			//gets smaller
			suction -= (float) duration / 400;

			if (world.isRemote) {
				for (int i = 0; i < 12; i++) {
					Vector lookpos = Vector.toRectangular(Math.toRadians(entity.rotationYaw +
							i * 30), 0).times(inverseRadius).withY(entity.getEyeHeight() / 2);
					ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(AvatarEntityUtils.getBottomMiddleOfEntity(entity).add(lookpos.toMinecraft()))
							.collide(true).scale(abilityData.getXpModifier() * 1.5F).vel(world.rand.nextGaussian() / 60, world.rand.nextGaussian() / 60,
							world.rand.nextGaussian() / 60).clr(0.8F, 0.8F, 0.8F).spawn(world);
				}
			}

			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
				AxisAlignedBB box = new AxisAlignedBB(entity.posX + radius, entity.posY + radius, entity.posZ + radius, entity.posX - radius, entity.posY - radius, entity.posZ - radius);
				List<Entity> collided = world.getEntitiesWithinAABB(Entity.class, box, entity1 -> entity1 != entity);
				if (!collided.isEmpty()) {
					for (Entity e : collided) {
						if (e.canBePushed() && e.canBeCollidedWith() && e != entity) {
							pullEntities(e, entity, suction);
						}
					}
				}
			}


			if (!data.hasStatusControl(StatusControl.RELEASE_AIR_BURST)) {

				int particleController = abilityData.getLevel() > 0 ? 60 - (5 * abilityData.getLevel()) : 60;
				EntityShockwave shockwave = new EntityShockwave(world);
				shockwave.setOwner(entity);
				shockwave.setPosition(AvatarEntityUtils.getBottomMiddleOfEntity(entity));
				shockwave.setRenderNormal(false);
				shockwave.setElement(new Airbending());
				shockwave.setParticleSpeed(0.5F * radius / STATS_CONFIG.airBurstSettings.radius);
				shockwave.setDamageSource(AvatarDamageSource.AIR);
				shockwave.setKnockbackHeight(upwardKnockback);
				shockwave.setKnockbackMult(new Vec3d(knockBack * 2, knockBack / 4, knockBack * 2));
				shockwave.setDamage((float) damage);
				shockwave.setParticleAmount(1);
				shockwave.setRange(radius);
				shockwave.setPerformanceAmount(performanceAmount);
				shockwave.setParticleController(particleController);
				shockwave.setAbility(new AbilityAirBurst());
				shockwave.setSpeed((float) knockBack / 4);
				shockwave.setBehaviour(new AirburstShockwave());
				world.spawnEntity(shockwave);


				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(AIRBURST_MOVEMENT_MODIFIER_ID);

				world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
						SoundCategory.BLOCKS, 1, 0.5F);

				//spawned = true;
				return true;
			}
			//	System.out.println("Has status control: " + data.hasStatusControl(StatusControl.RELEASE_AIR_BURST));

			return !data.hasStatusControl(StatusControl.RELEASE_AIR_BURST);
		} else return true;
	}

	private void applyMovementModifier(EntityLivingBase entity, float multiplier) {

		IAttributeInstance moveSpeed = entity.getEntityAttribute(SharedMonsterAttributes
				.MOVEMENT_SPEED);

		moveSpeed.removeModifier(AIRBURST_MOVEMENT_MODIFIER_ID);

		moveSpeed.applyModifier(new AttributeModifier(AIRBURST_MOVEMENT_MODIFIER_ID,
				"Airburst charge modifier", multiplier - 1, 1));

	}

	private void pullEntities(Entity collided, Entity attacker, double suction) {
		Vector velocity = Vector.getEntityPos(collided).minus(Vector.getEntityPos(attacker));
		velocity = velocity.times(suction).times(-1);

		double x = (velocity.x());
		double y = (velocity.y());
		double z = (velocity.z());

		if (!collided.world.isRemote) {
			collided.addVelocity(x, y, z);

			if (collided instanceof AvatarEntity) {
				if (!(collided instanceof EntityWall) && !(collided instanceof EntityWallSegment) && !(collided instanceof EntityIcePrison) && !(collided instanceof EntitySandPrison)) {
					AvatarEntity avent = (AvatarEntity) collided;
					avent.addVelocity(x, y, z);
				}
				collided.isAirBorne = true;
				AvatarUtils.afterVelocityAdded(collided);
			}
		}
	}

	public static class AirburstShockwave extends ShockwaveBehaviour {

		@Override
		public Behavior onUpdate(EntityShockwave entity) {
			World world = entity.world;
			if (world.isRemote) {
				if (entity.ticksExisted == 2) {
					double x1, y1, z1, xVel, yVel, zVel;
					for (double theta = 0; theta <= 180; theta += 1) {
						double dphi = (entity.getParticleController() - entity.getParticleAmount()) / Math.sin(Math.toRadians(theta));
						for (double phi = 0; phi < 360; phi += dphi) {
							double rphi = Math.toRadians(phi);
							double rtheta = Math.toRadians(theta);

							x1 = entity.ticksExisted * entity.getSpeed() * Math.cos(rphi) * Math.sin(rtheta);
							y1 = entity.ticksExisted * entity.getSpeed() * Math.sin(rphi) * Math.sin(rtheta);
							z1 = entity.ticksExisted * entity.getSpeed() * Math.cos(rtheta);
							xVel = x1 * entity.getParticleSpeed();
							yVel = y1 * entity.getParticleSpeed();
							zVel = z1 * entity.getParticleSpeed();

							ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(x1 + entity.posX, y1 + entity.posY, z1 + entity.posZ).vel(xVel, yVel, zVel)
									.clr(0.8F, 0.8F, 0.8F).time(13 + (int) (2 * entity.getRange() / STATS_CONFIG.airBurstSettings.radius)).collide(true)
									.scale(3.25F * (float) entity.getRange() / STATS_CONFIG.airBurstSettings.radius).spawn(world);

						}
					}
					ParticleBuilder.create(ParticleBuilder.Type.SPHERE).clr(1.0F, 1.0F, 1.0F).entity(entity).time(16).scale((float) entity.getRange())
							.pos(AvatarEntityUtils.getBottomMiddleOfEntity(entity)).spawn(world);
				}
			}
			double middleY = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minZ;
			middleY /= 2;
			AxisAlignedBB box = new AxisAlignedBB(entity.posX + (entity.ticksExisted * entity.getSpeed()), entity.getEntityBoundingBox().minY + middleY + (entity.ticksExisted * entity.getSpeed()),
					entity.posZ + (entity.ticksExisted * entity.getSpeed()), entity.posX - (entity.ticksExisted * entity.getSpeed()),
					entity.getEntityBoundingBox().minY + middleY - (entity.ticksExisted * entity.getSpeed()), entity.posZ - (entity.ticksExisted * entity.getSpeed()));

			List<Entity> nearby = world.getEntitiesWithinAABB(Entity.class, box);
			for (Entity target : nearby) {
				if (!world.isRemote) {
					if (target instanceof IOffensiveEntity && target instanceof AvatarEntity) {
						if (((AvatarEntity) target).isProjectile()) {
							if (((AvatarEntity) target).getAbility().getTier() < entity.getAbility().getTier() ||
									((IOffensiveEntity) target).getDamage() < entity.getDamage() || ((AvatarEntity) target).velocity().magnitude() < entity.getSpeed())
								((IOffensiveEntity) target).Dissipate((AvatarEntity) target);

						}
					}
					if (target instanceof EntityArrow || target instanceof EntityThrowable)
						target.addVelocity(target.motionX * -1.1, target.motionY * -1.1, target.motionZ * -1.1);

				}
			}
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {

		}

		@Override
		public void toBytes(PacketBuffer buf) {

		}

		@Override
		public void load(NBTTagCompound nbt) {

		}

		@Override
		public void save(NBTTagCompound nbt) {

		}
	}
}
