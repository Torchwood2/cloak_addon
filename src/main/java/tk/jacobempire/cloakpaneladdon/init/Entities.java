package tk.jacobempire.cloakpaneladdon.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tk.jacobempire.cloakpaneladdon.CloakPanelAddon;
import tk.jacobempire.cloakpaneladdon.entity.TardisShieldEntity;

public class Entities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CloakPanelAddon.MODID);
    public static final RegistryObject<EntityType<TardisShieldEntity>> TARDIS_SHIELD_ENTITY = ENTITY_TYPES.register("tardis_shield",
            () -> EntityType.Builder.of(TardisShieldEntity::new, EntityClassification.MONSTER).sized(5f, 4f)
                    .build((new ResourceLocation(CloakPanelAddon.MODID, "tardis_shield").toString())));
}
