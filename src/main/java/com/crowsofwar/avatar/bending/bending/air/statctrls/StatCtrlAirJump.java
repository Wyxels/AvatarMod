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

package com.crowsofwar.avatar.bending.bending.air.statctrls;

import com.crowsofwar.avatar.bending.bending.Abilities;
import com.crowsofwar.avatar.bending.bending.Ability;
import com.crowsofwar.avatar.bending.bending.air.AbilityAirJump;
import com.crowsofwar.avatar.bending.bending.air.Airbending;
import com.crowsofwar.avatar.bending.bending.air.powermods.AirJumpPowerModifier;
import com.crowsofwar.avatar.client.controls.AvatarControl;
import com.crowsofwar.avatar.client.particle.ParticleBuilder;
import com.crowsofwar.avatar.entity.EntityShockwave;
import com.crowsofwar.avatar.util.AvatarEntityUtils;
import com.crowsofwar.avatar.util.AvatarUtils;
import com.crowsofwar.avatar.util.damageutils.AvatarDamageSource;
import com.crowsofwar.avatar.util.data.*;
import com.crowsofwar.avatar.util.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.util.data.ctx.BendingContext;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.crowsofwar.avatar.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.util.data.TickHandlerController.AIR_PARTICLE_SPAWNER;
import static com.crowsofwar.avatar.util.data.TickHandlerController.SMASH_GROUND;
import static com.crowsofwar.avatar.util.helper.GliderHelper.getIsGliderDeployed;

/**
 * @author CrowsOfWar
 */
public class StatCtrlAirJump extends StatusControl {

    public static Map<String, Integer> timesJumped = new HashMap<>();

    public StatCtrlAirJump() {
        super(0, AvatarControl.CONTROL_JUMP, CrosshairPosition.BELOW_CROSSHAIR);
    }

    @Override
    public boolean execute(BendingContext ctx) {

        Bender bender = ctx.getBender();
        EntityLivingBase entity = ctx.getBenderEntity();
        BendingData data = ctx.getData();
        World world = ctx.getWorld();
        AbilityData abilityData = data.getAbilityData("air_jump");
        AbilityAirJump jump = (AbilityAirJump) Abilities.get("air_jump");

        String uuid = Objects.requireNonNull(bender.getInfo().getId()).toString();


        if (jump == null)
            return true;

        if (!timesJumped.containsKey(uuid)) timesJumped.put(uuid, 0);

        boolean allowDoubleJump = abilityData.getLevel() == 3 && abilityData.getPath() == AbilityTreePath.FIRST && timesJumped.get(uuid) < 2;


        int jumps = timesJumped.get(uuid);
        // Figure out whether entity is on ground by finding collisions with
        // ground - if found a collision box, then is not on ground
        List<AxisAlignedBB> collideWithGround = world.getCollisionBoxes(entity, entity.getEntityBoundingBox().grow(0.25, 0.625, 0.25));
        boolean onGround = !collideWithGround.isEmpty() || entity.collidedVertically || world.getBlockState(entity.getPosition()).getBlock() == Blocks.WEB;

        if (onGround || entity instanceof EntityPlayer && getIsGliderDeployed((EntityPlayer) entity) ||
                (allowDoubleJump && bender.consumeChi(jump.getChiCost(abilityData)))) {

            int lvl = abilityData.getLevel();
            double multiplier = jump.getProperty(Ability.JUMP_HEIGHT, abilityData).floatValue() / 5;
            double powerModifier = 10;
            double powerDuration = 3;
            int numberOfParticles = 10;
            double particleSpeed = 0.2;
            if (lvl >= 1) {
                multiplier = 0.75;
                powerModifier = 15;
                powerDuration = 4;
                numberOfParticles = 15;
                particleSpeed = 0.3;
            }
            if (lvl >= 2) {
                multiplier = 0.875;
                powerModifier = 20;
                powerDuration = 5;
                numberOfParticles = 20;
                particleSpeed = 0.35;
            }
            if (lvl >= 3) {
                powerModifier = 25;
                powerDuration = 6;
            }
            if (abilityData.isMasterPath(AbilityTreePath.SECOND)) {
                numberOfParticles = 35;
                particleSpeed = 0.5;
            }

            if (world.isRemote) {
                for (int i = 0; i < numberOfParticles; i++)
                    ParticleBuilder.create(ParticleBuilder.Type.FLASH).collide(true).pos(AvatarEntityUtils.getBottomMiddleOfEntity(entity).add(0, 0.1, 0))
                            .clr(0.8F, 0.8F, 0.8F).time(14).vel(world.rand.nextGaussian() * particleSpeed / 10, world.rand.nextGaussian() * particleSpeed / 20,
                            world.rand.nextGaussian() * particleSpeed / 10).scale(1F + (float) particleSpeed).spawn(world);
            }

            Vector velocity = Vector.getLookRectangular(entity);
            velocity = velocity.times(multiplier * 1.25);
            velocity = velocity.withY(Math.max(velocity.y(), 0.15));
            if (!world.isRemote) {
                if (!onGround) {
                    velocity = velocity.times(0.875);
                    entity.motionX = velocity.x();
                    entity.motionY = velocity.y();
                    entity.motionZ = velocity.z();
                } else
                    entity.addVelocity(velocity.x(), velocity.y(), velocity.z());
            }
            AvatarUtils.afterVelocityAdded(entity);

            float fallAbsorption = jump.getProperty(Ability.FALL_ABSORPTION, abilityData).floatValue();
            float speed = jump.getProperty(Ability.KNOCKBACK, abilityData).floatValue() / 3;
            float size = jump.getProperty(Ability.EFFECT_RADIUS, abilityData).floatValue();
            float chiCost, chiOnHit, exhaustion, burnout;
            int cooldown;

            speed *= abilityData.getDamageMult() * abilityData.getXpModifier();
            size *= abilityData.getDamageMult() * abilityData.getXpModifier();

            data.getMiscData().setFallAbsorption(fallAbsorption);

            data.addTickHandler(AIR_PARTICLE_SPAWNER, ctx);
            if (jump.getBooleanProperty(Ability.GROUND_POUND, abilityData)) {
                data.addTickHandler(SMASH_GROUND, ctx);
            }

            abilityData.addXp(jump.getProperty(Ability.XP_USE, abilityData).floatValue());

            entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.PLAYERS, 1, .7f);

            PowerRatingModifier powerRatingModifier = new AirJumpPowerModifier(powerModifier);
            powerRatingModifier.setTicks((int) (powerDuration * 20));
            //noinspection ConstantConditions
            data.getPowerRatingManager(Airbending.ID).addModifier(powerRatingModifier, ctx);


            EntityShockwave wave = new EntityShockwave(world);
            wave.setDamage(0);
            wave.setFireTime(0);
            wave.setRange(lvl > 0 ? 2.25F + lvl / 4F : 2.25F);
            wave.setSpeed(lvl > 0 ? 0.5F + lvl / 30F : 0.5f);
            wave.setKnockbackHeight(lvl > 0 ? 0.0125F + lvl / 80F : 0.0125F);
            wave.setParticle(EnumParticleTypes.EXPLOSION_NORMAL);
            wave.setPerformanceAmount(10);
            wave.setKnockbackMult(new Vec3d(0.5, 0.2, 0.5));
            wave.setParticleAmount(2);
            wave.setDamageSource("avatar_Air_shockwave");
            wave.setAbility(jump);
            wave.setElement(new Airbending());
            wave.setParticleSpeed(lvl > 0 ? 0.02F + lvl / 40F : 0.02F);
            wave.setPosition(entity.getPositionVector().add(0, 0.5, 0));
            wave.setOwner(entity);
            wave.setRenderNormal(false);
            wave.setKnockbackMult(new Vec3d(speed, speed * 2, speed));
            wave.setXp(jump.getProperty(Ability.XP_HIT, abilityData).floatValue());
            if (!world.isRemote) {
                world.spawnEntity(wave);
                jumps++;
            }
            timesJumped.replace(uuid, jumps);
            //If you return when it's greater than 1, it resets, and you can double jump infinitely.
            boolean isDone = jumps > 2;
            if (isDone) timesJumped.replace(uuid, 0);
            abilityData.setRegenBurnout(true);
            return true;

        }

        return false;

    }

}
