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
package com.crowsofwar.avatar.common.bending.air;

import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.EntityAirBubble;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.FIRST;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.SECOND;

/**
 * @author CrowsOfWar
 */
public class AbilityAirBubble extends Ability {

	public AbilityAirBubble() {
		super(Airbending.ID, "air_bubble");
	}

	@Override
	public void execute(AbilityContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		World world = ctx.getWorld();
		BendingData data = ctx.getData();

		ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		boolean elytraOk = (STATS_CONFIG.allowAirBubbleElytra || chest.getItem() != Items.ELYTRA);

		if (!elytraOk) {
			ctx.getBender().sendMessage("avatar.airBubbleElytra");
		}

		if (!data.hasStatusControl(StatusControl.BUBBLE_CONTRACT) && elytraOk) {

			if (!bender.consumeChi(STATS_CONFIG.chiAirBubble)) return;

			float size = 1.5f;
			float health = 12;
			if (ctx.getLevel() > 0) {
				size = 2.5f;
				health = 18;
			}
			if (ctx.isMasterLevel(FIRST)) {
				size = 4f;
				health = 24;
			}
			if (ctx.isMasterLevel(SECOND)) health = 10f;

			EntityAirBubble bubble = new EntityAirBubble(world);
			bubble.setOwner(entity);
			bubble.setPosition(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
			bubble.setHealth(health);
			bubble.setMaxHealth(health);
			bubble.setSize(size);
			bubble.setVelocity(Vector.ZERO);
			bubble.setAllowHovering(ctx.isMasterLevel(SECOND));
			bubble.setAbility(this);
			world.spawnEntity(bubble);

			data.addStatusControl(StatusControl.BUBBLE_EXPAND);
			data.addStatusControl(StatusControl.BUBBLE_CONTRACT);
		}

	}

	@Override
	public int getCooldown(AbilityContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();

		int coolDown = 200;

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			coolDown = 0;
		}

		if (ctx.getLevel() == 1) {
			coolDown = 180;
		}
		if (ctx.getLevel() == 2) {
			coolDown = 160;
		}
		if (ctx.isMasterLevel(AbilityData.AbilityTreePath.FIRST)) {
			coolDown = 140;
		}
		if (ctx.isMasterLevel(AbilityData.AbilityTreePath.SECOND)) {
			coolDown = 130;
		}
		return coolDown;
	}

	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiAirBubble(this, entity, bender);
	}

}
