package com.crowsofwar.avatar.common.network;

import java.util.UUID;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.bending.AbilityContext;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.network.packets.PacketCPlayerData;
import com.crowsofwar.avatar.common.network.packets.PacketCRemoveStatusControl;
import com.crowsofwar.avatar.common.network.packets.PacketSRequestData;
import com.crowsofwar.avatar.common.network.packets.PacketSUseAbility;
import com.crowsofwar.avatar.common.network.packets.PacketSUseBendingController;
import com.crowsofwar.avatar.common.network.packets.PacketSUseStatusControl;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.GoreCorePlayerUUIDs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Implements IPacketHandler. Acts as a packet handler for integrated and
 * dedicated servers. Is a singleton and is accessible via {@link #instance}.
 *
 */
public class PacketHandlerServer implements IPacketHandler {
	
	public static final IPacketHandler instance;
	
	static {
		instance = new PacketHandlerServer();
	}
	
	private PacketHandlerServer() {}
	
	@Override
	public IMessage onPacketReceived(IMessage packet, MessageContext ctx) {
		AvatarLog.debug("Recieved packet");
		
		if (packet instanceof PacketSUseAbility) return handleKeypress((PacketSUseAbility) packet, ctx);
		
		if (packet instanceof PacketSRequestData) return handleRequestData((PacketSRequestData) packet, ctx);
		
		if (packet instanceof PacketSUseBendingController)
			return handleUseBendingController((PacketSUseBendingController) packet, ctx);
		
		if (packet instanceof PacketSUseStatusControl)
			return handleUseStatusControl((PacketSUseStatusControl) packet, ctx);
		
		AvatarLog.warn("Unknown packet recieved: " + packet.getClass().getName());
		return null;
	}
	
	@Override
	public Side getSide() {
		return Side.SERVER;
	}
	
	private IMessage handleKeypress(PacketSUseAbility packet, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player,
				"Error while processing UseAbility packet");
		System.out.println("use ability...");
		if (data != null) {
			
			// TODO Verify that the client can actually use that ability
			System.out.println("data not null.");
			System.out.println("exec");
			data.getState().update(player, packet.getTargetPos(), packet.getSideHit());
			packet.getAbility().execute(new AbilityContext(data,
					new Raytrace.Result(packet.getTargetPos(), packet.getSideHit())));
			
		}
		
		return null;
	}
	
	private IMessage handleRequestData(PacketSRequestData packet, MessageContext ctx) {
		
		UUID id = packet.getAskedPlayer();
		EntityPlayer player = GoreCorePlayerUUIDs
				.findPlayerInWorldFromUUID(ctx.getServerHandler().playerEntity.worldObj, id);
		
		if (player == null) {
			
			AvatarLog.warnHacking(ctx.getServerHandler().playerEntity.getName(),
					"Sent request data for a player with account '" + id
							+ "', but that player is not in the world.");
			return null;
			
		}
		
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player,
				"Error while" + " processing RequestData packet");
		
		return data == null ? null : new PacketCPlayerData(data);
		
	}
	
	// TODO remove packet s usebendingcontroller
	private IMessage handleUseBendingController(PacketSUseBendingController packet, MessageContext ctx) {
		
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		World world = player.worldObj;
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player,
				"Error while processing" + " UseBendingController packet");
		
		if (data != null) {
			
			if (data.hasBending(packet.getBendingControllerId())) {
			} else {
				AvatarLog.warn("Player '" + player.getName() + "' attempted to activate a BendingController "
						+ "they don't have; hacking?");
			}
			
		}
		
		return null;
	}
	
	/**
	 * @param packet
	 * @param ctx
	 * @return
	 */
	private IMessage handleUseStatusControl(PacketSUseStatusControl packet, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player,
				"Error while processing UseStatusControl packet");
		
		if (data != null) {
			if (packet.getStatusControl().execute(new AbilityContext(data,
					new Raytrace.Result(packet.getLookPos(), packet.getLookSide())))) {
				
				data.removeStatusControl(packet.getStatusControl());
				AvatarMod.network.sendTo(new PacketCRemoveStatusControl(packet.getStatusControl()), player);
				
			}
			
		}
		
		return null;
	}
	
}
