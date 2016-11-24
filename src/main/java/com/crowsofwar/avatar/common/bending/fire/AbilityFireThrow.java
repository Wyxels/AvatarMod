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

import com.crowsofwar.avatar.common.bending.AbilityContext;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.entity.EntityFireArc;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.avatar.common.util.Raytrace.Info;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityFireThrow extends BendingAbility<FirebendingState> {
	
	private final Raytrace.Info raytrace;
	
	/**
	 * @param controller
	 */
	public AbilityFireThrow(BendingController<FirebendingState> controller) {
		super(controller);
		this.raytrace = new Raytrace.Info();
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		EntityPlayer player = ctx.getPlayerEntity();
		AvatarPlayerData data = ctx.getData();
		FirebendingState fs = data.getBendingState(controller);
		
		EntityFireArc fire = fs.getFireArc();
		if (fire != null) {
			Vector look = Vector.fromYawPitch(Math.toRadians(player.rotationYaw),
					Math.toRadians(player.rotationPitch));
			fire.velocity().add(look.times(15));
			fire.setGravityEnabled(true);
			fs.setNoFireArc();
			data.sendBendingState(fs);
		}
	}
	
	@Override
	public int getIconIndex() {
		return -1;
	}
	
	@Override
	public Info getRaytrace() {
		return raytrace;
	}
	
}
