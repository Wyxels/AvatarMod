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
package com.crowsofwar.gorecore.tree.test;

import com.crowsofwar.gorecore.tree.ArgumentPlayerName;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.NodeBuilder;
import com.crowsofwar.gorecore.tree.NodeFunctional;
import com.crowsofwar.gorecore.tree.TreeCommand;
import com.crowsofwar.gorecore.util.AccountUUIDs;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class GoreCoreCommand extends TreeCommand {
	
	@Override
	public String getCommandName() {
		return "gorecore";
	}
	
	@Override
	protected ICommandNode[] addCommands() {
		
		NodeFunctional reloadId = new NodeBuilder("relids").addArgument(new ArgumentPlayerName("player"))
				.build(popper -> {
					String username = popper.get();
					System.out.println("Reload " + username);
					boolean success = AccountUUIDs.tryFixId(username);
					
				});
		
		return new ICommandNode[] { reloadId };
	}
	
}
