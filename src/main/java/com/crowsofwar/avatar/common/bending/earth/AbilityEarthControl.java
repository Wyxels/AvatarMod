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

package com.crowsofwar.avatar.common.bending.earth;

import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFloatingBlock;
import com.crowsofwar.avatar.common.entity.data.FloatingBlockBehavior;
import com.crowsofwar.gorecore.util.Vector;
import com.crowsofwar.gorecore.util.VectorI;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

/**
 * @author CrowsOfWar
 */
public class AbilityEarthControl extends Ability {

	private final Random random;

	public AbilityEarthControl() {
		super(Earthbending.ID, "earth_control");
		this.random = new Random();
		requireRaytrace(-1, true);
	}

	@Override
	public boolean isUtility() {
		return true;
	}

	@Override
	public void execute(AbilityContext ctx) {

		BendingData data = ctx.getData();
		//EntityLivingBase entity = ctx.getBenderEntity();
		//Bender bender = ctx.getBender();
		//World world = ctx.getWorld();

		EntityFloatingBlock currentBlock = AvatarEntity.lookupEntity(ctx.getWorld(),
				EntityFloatingBlock.class,
				fb -> fb.getBehavior() instanceof FloatingBlockBehavior.PlayerControlled
						&& fb.getOwner() == ctx.getBenderEntity());

		if (currentBlock != null) {
			return;
		} else {
			VectorI target = ctx.getLookPosI();
			if (target != null) {

				pickupBlock(ctx, target.toBlockPos());

				// EnumFacing direction = entity.getHorizontalFacing();
				// pickupBlock(ctx, target.toBlockPos().offset(direction));
				// pickupBlock(ctx,
				// target.toBlockPos().offset(direction.getOpposite()));

			}
		}
	}

	private void pickupBlock(AbilityContext ctx, BlockPos pos) {

		World world = ctx.getWorld();
		Bender bender = ctx.getBender();
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();

		IBlockState ibs = world.getBlockState(pos);
		Block block = ibs.getBlock();

		if (!world.isAirBlock(pos) && STATS_CONFIG.bendableBlocks.contains(block)) {

			if (bender.consumeChi(STATS_CONFIG.chiPickUpBlock)) {

				AbilityData abilityData = ctx.getData().getAbilityData(this);

				EntityFloatingBlock floating = new EntityFloatingBlock(world, ibs);
				floating.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				floating.setItemDropsEnabled(!bender.isCreativeMode());

				float damageMult = abilityData.getLevel() >= 2 ? 2 : 1;
				damageMult *= ctx.getPowerRatingDamageMod();

				double dist = 2.5;
				Vector force = new Vector(0, Math.sqrt(20 * dist), 0);
				floating.setVelocity(force);
				floating.setBehavior(new FloatingBlockBehavior.PickUp());
				floating.setOwner(entity);
				floating.setAbility(this);
				floating.setDamageMult(damageMult);

				if (STATS_CONFIG.preventPickupBlockGriefing) {
					floating.setItemDropsEnabled(false);
				} else {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}

				world.spawnEntity(floating);

				SoundType sound = block.getSoundType();
				if (sound != null) {
					world.playSound(null, floating.getPosition(), sound.getBreakSound(),
							SoundCategory.PLAYERS, sound.getVolume(), sound.getPitch());
				}

				data.addStatusControl(StatusControl.PLACE_BLOCK);
				data.addStatusControl(StatusControl.THROW_BLOCK);

			}

		} else {
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS,
					1, (float) (random.nextGaussian() / 0.25 + 0.375));
		}

	}
}