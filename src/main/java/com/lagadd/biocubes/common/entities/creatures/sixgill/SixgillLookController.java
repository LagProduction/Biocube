package com.lagadd.biocubes.common.entities.creatures.sixgill;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class SixgillLookController extends LookControl {
    public SixgillLookController(Sixgill sixgill) {
        super(sixgill);
    }

    @Override
    public void tick() {
        if (this.lookAtCooldown > 0) {
            this.getYRotD().ifPresent(yRot -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, yRot, this.yMaxRotSpeed));
            this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), this.getXRotD().get(), this.xMaxRotAngle));
        } else {
            if (this.mob.getNavigation().isDone()) {
                this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
            } else {
                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
            }
        }
        float wrappedDegrees = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
        float angleLimit = this.mob.getPassengers().isEmpty() ? 10.0F : 5.0F;
        if (wrappedDegrees < -angleLimit) {
            this.mob.yBodyRot -= 4.0F;
        } else if (wrappedDegrees > angleLimit) {
            this.mob.yBodyRot += 4.0F;
        }
    }
}
