package com.crowsofwar.avatar.client.render;

import com.crowsofwar.avatar.common.bending.air.AbilityAirblade;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.entity.EntityAirblade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderAirBlade1 extends RenderModel<EntityAirblade> {
	private static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/entity/air-blade.png");

	public RenderAirBlade1(RenderManager renderManager) {
		super(renderManager, new ModelAirBlade());
		setGlowing();
	}

	@Override
	protected void performGlTransforms(EntityAirblade entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.performGlTransforms(entity, x, y, z, entityYaw, partialTicks);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		GlStateManager.rotate(-entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.rotate(entity.rotationPitch, 1, 0, 0);
		GlStateManager.rotate(90, 0, 0, 1);
		GlStateManager.translate(-entity.getSizeMult() * 0.8,  -entity.getSizeMult() * 0.625, 0);
		GlStateManager.scale(entity.getSizeMult(), entity.getSizeMult(), entity.getSizeMult());
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityAirblade entity) {
		return TEXTURE;
	}
}
