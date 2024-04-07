package tk.jacobempire.cloakpaneladdon.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TardisShieldEntity extends MobEntity {
    public TardisShieldEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public void load(CompoundNBT p_70020_1_) {
        remove();
    }

    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    public void pushAwayFromTardisShield(AxisAlignedBB box) {
        List<Entity> entities = level.getEntities(this, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                double dx = entity.getX() - this.getX();
                double dy = entity.getY() - this.getY();
                double dz = entity.getZ() - this.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distance < 1.0) {
                    double pushForce = 0.1D;
                    entity.setDeltaMovement(entity.getDeltaMovement().x() - pushForce * dx / distance, entity.getDeltaMovement().y() - pushForce * dy / distance, entity.getDeltaMovement().z() - pushForce * dz / distance);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        AxisAlignedBB box = this.getBoundingBox().inflate(1.0);
        this.pushAwayFromTardisShield(box);
    }
}