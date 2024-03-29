package com.lagadd.biocubes.common.entities.creatures.sixgill;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class SixgillMoveController extends MoveControl {

    private final Sixgill sixgill;

    public SixgillMoveController(Sixgill sixgill) {
        super(sixgill);
        this.sixgill = sixgill;
    }

    @Override
    public void tick() {
        if (this.sixgill.isInWater() || this.sixgill.isUnderWater()) {
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
        }
        if (this.operation == MoveControl.Operation.MOVE_TO && !this.sixgill.getNavigation().isDone()) {
            Vec3 vec3d = new Vec3(this.wantedX - this.sixgill.getX(), this.wantedY - this.sixgill.getY(), this.wantedZ - this.sixgill.getZ());
            double d0 = vec3d.length();
            double d1 = vec3d.y / d0;
            float f = (float) (Mth.atan2(vec3d.z, vec3d.x) * (double) (180F / (float) Math.PI)) - 90F;
            float f1 = (float) (this.speedModifier * this.sixgill.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            float f2 = Mth.lerp(0.125F, this.sixgill.getSpeed(), f1);
            this.sixgill.setSpeed(f2);
            double d2 = Math.sin((double) (this.sixgill.tickCount + this.sixgill.getId()) * 0.5D) * 0.05D;
            double d3 = Math.cos(this.sixgill.getYRot() * ((float) Math.PI / 180F));
            double d4 = Math.sin(this.sixgill.getYRot() * ((float) Math.PI / 180F));
            double d5 = Math.sin((double) (this.sixgill.tickCount + this.sixgill.getId()) * 0.75D) * 0.05D;
            this.sixgill.yHeadRot = this.sixgill.getYRot();
            this.sixgill.setDeltaMovement(this.sixgill.getDeltaMovement().add(d2 * d3, d5 * (d4 + d3) * 0.25D + (double) f2 * d1 * 0.1D, d2 * d4));

            this.sixgill.setMoving(true);
        } else {
            this.sixgill.setSpeed(0F);
            this.sixgill.setXxa(0.0F);
            this.sixgill.setYya(0.0F);
            this.sixgill.setZza(0.0F);
            this.sixgill.setMoving(false);
        }
    }
}
