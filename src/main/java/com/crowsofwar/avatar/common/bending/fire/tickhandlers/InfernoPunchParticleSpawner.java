package com.crowsofwar.avatar.common.bending.fire.tickhandlers;

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.bending.fire.Firebending;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.particle.ParticleBuilder;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.avatar.common.util.PlayerViewRegistry;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.crowsofwar.avatar.common.bending.StatusControl.*;

public class InfernoPunchParticleSpawner extends TickHandler {

	public InfernoPunchParticleSpawner(int id) {
		super(id);
	}

	@Override
	public boolean tick(BendingContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		World world = ctx.getWorld();
		AbilityData abilityData = AbilityData.get(entity, "inferno_punch");

		int particleCount = 1;
		int level = abilityData.getLevel();
		if (level == 1 || level == 2) {
			particleCount = 2;

		}
		if (abilityData.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
			particleCount = 3;
		}
		if (abilityData.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
			particleCount = 2;
		}
		if ((data.hasStatusControl(INFERNO_PUNCH_MAIN) || data.hasStatusControl(INFERNO_PUNCH_FIRST) || data.hasStatusControl(INFERNO_PUNCH_SECOND))) {

			Vec3d height, rightSide;
			if (entity instanceof EntityPlayer) {
				if (!AvatarMod.realFirstPersonRender2Compat && (PlayerViewRegistry.getPlayerViewMode(entity.getUniqueID()) >= 2 || PlayerViewRegistry.getPlayerViewMode(entity.getUniqueID()) <= -1)) {
					height = entity.getPositionVector().add(0, 1.6, 0);
					height = height.add(entity.getLookVec().scale(0.8));
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
					if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw + 90), 0).times(0.385).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					} else {
						rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw - 90), 0).times(0.385).withY(0).toMinecraft();
						rightSide = rightSide.add(height);
					}
				}
			} else {
				height = entity.getPositionVector().add(0, 0.84, 0);
				if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
					rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw + 90), 0).times(0.385).withY(0).toMinecraft();
					rightSide = rightSide.add(height);
				} else {
					rightSide = Vector.toRectangular(Math.toRadians(entity.rotationYaw - 90), 0).times(0.385).withY(0).toMinecraft();
					rightSide = rightSide.add(height);
				}

			}
			/*if (!world.isRemote) {
				WorldServer World = (WorldServer) world;
				World.spawnParticle(AvatarParticles.getParticleFlames(),
						rightSide.x, rightSide.y, rightSide.z, particleCount, 0, 0, 0, 0.015);
			}**/
			if (world.isRemote)
				for (int i = 0; i < particleCount; i++) {
					ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(rightSide).time(6 + AvatarUtils.getRandomNumberInRange(0, 4)).vel(world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 40,
							world.rand.nextGaussian() / 40).clr(255, 15, 5).collide(true).
							scale(abilityData.getLevel() < 1 ? 0.9F : 0.9F + abilityData.getLevel() / 5F).element(new Firebending()).spawn(world);
					ParticleBuilder.create(ParticleBuilder.Type.FLASH).pos(rightSide).time(6 + AvatarUtils.getRandomNumberInRange(0, 4)).vel(world.rand.nextGaussian() / 40, world.rand.nextGaussian() / 40,
							world.rand.nextGaussian() / 40).clr(255, 60 + AvatarUtils.getRandomNumberInRange(0, 60), 10).collide(true).
							scale(abilityData.getLevel() < 1 ? 0.9F : 0.9F + abilityData.getLevel() / 5F).element(new Firebending()).spawn(world);
				}


			return false;
		} else return true;
	}

}
