package tk.jacobempire.cloakpaneladdon.client.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import tk.jacobempire.cloakpaneladdon.client.render.entity.RenderTardisShield;
import tk.jacobempire.cloakpaneladdon.init.Entities;

public class EntityRenderRegistry {

    public static void registerEntityRenders(){
        registerRender(Entities.TARDIS_SHIELD_ENTITY, RenderTardisShield::new);
    }

    public static <T extends Entity> void registerRender(RegistryObject<EntityType<T>> entityClass, IRenderFactory<? super T> renderFactory) {
        if (entityClass == null) return;
        RenderingRegistry.registerEntityRenderingHandler(entityClass.get(), renderFactory);
    }
}
