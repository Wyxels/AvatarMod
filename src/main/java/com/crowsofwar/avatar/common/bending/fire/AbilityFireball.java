/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/
package com.crowsofwar.avatar.common.bending.fire;

import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.bending.BattlePerformanceScore;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.EntityFireball;
import com.crowsofwar.avatar.common.entity.EntityLightOrb;
import com.crowsofwar.avatar.common.entity.data.Behavior;
import com.crowsofwar.avatar.common.entity.data.FireballBehavior;
import com.crowsofwar.avatar.common.entity.data.LightOrbBehavior;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

import java.util.List;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.avatar.common.data.StatusControlController.THROW_FIREBALL;
import static com.crowsofwar.gorecore.util.Vector.getEyePos;
import static com.crowsofwar.gorecore.util.Vector.getLookRectangular;

/**
 * @author CrowsOfWar
 */
public class AbilityFireball extends Ability {

	public AbilityFireball() {
		super(Firebending.ID, "fireball");
		requireRaytrace(2.5, false);
	}

	@Override
	public void execute(AbilityContext ctx) {

		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		World world = ctx.getWorld();
		BendingData data = ctx.getData();


		if (bender.consumeChi(STATS_CONFIG.chiFireball)) {

			Vector target;
			if (ctx.isLookingAtBlock() && !world.isRemote) {
				target = ctx.getLookPos();
			} else {
				Vector playerPos = getEyePos(entity);
				target = playerPos.plus(getLookRectangular(entity).times(2.5));
			}

			float damage = STATS_CONFIG.fireballSettings.damage;
			int size = 16;
			boolean canUse = !data.hasStatusControl(THROW_FIREBALL);
			List<EntityFireball> fireballs = world.getEntitiesWithinAABB(EntityFireball.class,
					entity.getEntityBoundingBox().grow(3.5, 3.5, 3.5));
			canUse |= fireballs.size() < 3 && ctx.isDynamicMasterLevel(AbilityTreePath.FIRST);

			damage *= ctx.getLevel() >= 2 ? 1.75f : 1f;
			damage *= ctx.getPowerRatingDamageMod();

			if (ctx.getLevel() == 1) {
				size = 18;
			}

			if (ctx.getLevel() == 2) {
				size = 20;
			}

			if (ctx.isMasterLevel(AbilityTreePath.FIRST)) {
				size = 18;
				damage -= 2F;
			}
			if (ctx.isDynamicMasterLevel(AbilityTreePath.SECOND))
				size = 20;
			damage += size / 10F;

			if (canUse) {
				EntityFireball fireball = new EntityFireball(world);
				fireball.setPosition(target);
				fireball.setOwner(entity);
				fireball.setBehavior(new FireballBehavior.PlayerControlled());
				fireball.setDamage(damage);
				fireball.setPowerRating(bender.calcPowerRating(Firebending.ID));
				fireball.setSize(size);
				fireball.setPerformanceAmount((int) (BattlePerformanceScore.SCORE_MOD_SMALL * 1.5));
				fireball.setAbility(this);
				fireball.setFireTime(size / 5);
				fireball.setXp(SKILLS_CONFIG.fireballHit);


				data.addStatusControl(THROW_FIREBALL);
				if (!world.isRemote)
					world.spawnEntity(fireball);
			}

		}

	}

	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiFireball(this, entity, bender);
	}

	@Override
	public int getBaseTier() {
		return 3;
	}

	public static class FireballLightOrbBehavior extends LightOrbBehavior {

		@Override
		public Behavior onUpdate(EntityLightOrb entity) {
			if (entity.getEntityWorld().isRemote) entity.setLightRadius(15 + (int) (Math.random() * 5));
			Entity emitter = entity.getEmittingEntity();
			if (emitter == null)
				entity.setDead();
			if (emitter != null) {
				assert emitter instanceof EntityFireball;
				entity.setOrbSize(((EntityFireball) emitter).getSize() * 0.03125F);
				entity.motionX = emitter.motionX;
				entity.motionY = emitter.motionY;
				entity.motionZ = emitter.motionZ;
				entity.setPosition(emitter.getPositionVector().add(0, entity.height, 0));
			/*if (entity.getColourShiftRange() != 0) {
				float range = entity.getColourShiftRange();
				float r = entity.getInitialColourR();
				float g = entity.getInitialColourG();
				float b = entity.getInitialColourB();
				float a = entity.getInitialColourA();
				float amount = AvatarUtils.getRandomNumberInRange(-(int) (1 / entity.getColourShiftInterval()),
						(int) (1 / entity.getColourShiftInterval())) * entity.getColourShiftInterval();
				float red = r + amount > r + range ? r - range : r + range;
				red = r - amount < r - range ? r + range : r - range;
				float green = g + amount > g + range ? g - range : g + range;
				float blue = b + amount > b + range ? b - range : r + range;
				float alpha = a + amount > a + range ? a - range : a + range;
				entity.setColor(red, green, blue, alpha);
			}**/
			}
			return this;
		}

		@Override
		public void renderUpdate(EntityLightOrb entity) {

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
