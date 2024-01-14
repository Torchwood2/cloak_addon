package tk.jacobempire.cloakpaneladdon.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.jacobempire.cloakpaneladdon.CloakPanelAddon;
import tk.jacobempire.cloakpaneladdon.init.Entities;

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
}
