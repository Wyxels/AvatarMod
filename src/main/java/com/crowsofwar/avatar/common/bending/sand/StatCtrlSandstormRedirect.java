package com.crowsofwar.avatar.common.bending.sand;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntitySandstorm;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

public class StatCtrlSandstormRedirect extends StatusControl {

	public StatCtrlSandstormRedirect() {
		super(0, AvatarControl.CONTROL_RIGHT_CLICK_DOWN, CrosshairPosition.RIGHT_OF_CROSSHAIR);
	}

	@Override
	public boolean execute(BendingContext ctx) {

		EntityLivingBase entity = ctx.getBenderEntity();
		World world = ctx.getWorld();

		EntitySandstorm sandstorm = AvatarEntity.lookupOwnedEntity(world, EntitySandstorm
				.class, entity);

		if (sandstorm != null) {

			Raytrace.Result raytrace = Raytrace.getTargetBlock(entity, 20, false);
			if (raytrace.hitSomething()) {
				Vector targetPos = raytrace.getPosPrecise();
				Vector newVelocity = sandstorm.position().minus(targetPos).times(5);
				sandstorm.setVelocity(newVelocity);
			}

		}

		return true;
	}

}
