package com.crowsofwar.avatar.common.bending.air;

import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import static com.crowsofwar.avatar.common.config.ConfigClient.CLIENT_CONFIG;

public class SlipstreamCooldownHandler extends TickHandler {
	@Override
	public boolean tick(BendingContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		AbilityData aD = data.getAbilityData("slipstream");
		int duration = data.getTickHandlerDuration(this);
		int coolDown = 160;

		if (aD.getLevel() == 1) {
			coolDown = 140;
		}
		if (aD.getLevel() == 2) {
			coolDown = 120;
		}
		if (aD.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
			coolDown = 130;
		}
		if (aD.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
			coolDown = 110;
		}

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			coolDown = 0;
		}

		if (!CLIENT_CONFIG.shaderSettings.useSlipstreamShaders) {
			data.setVision(null);
		}

		return duration >= coolDown;
	}
}
