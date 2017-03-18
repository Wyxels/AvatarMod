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

import com.crowsofwar.avatar.common.bending.AbilityAi;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;

import net.minecraft.entity.EntityLiving;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiFireball extends AbilityAi {
	
	private int timeExecuting;
	
	/**
	 * @param ability
	 * @param entity
	 * @param bender
	 */
	protected AiFireball(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
		timeExecuting = 0;
	}
	
	@Override
	protected void startExec() {
		execAbility();
		
	}
	
	@Override
	public boolean continueExecuting() {
		System.out.println("Continue " + timeExecuting);
		if (timeExecuting >= 200) {
			BendingData data = bender.getData();
			execStatusControl(StatusControl.THROW_FIREBALL);
			timeExecuting = 0;
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean shouldExecute() {
		return entity.getAttackTarget() != null;
	}
	
	@Override
	public void updateTask() {
		timeExecuting++;
	}
	
}
