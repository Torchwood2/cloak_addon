package tk.jacobempire.cloakpaneladdon.mixin;

import com.swdteam.client.init.DMEntityRenderRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.jacobempire.cloakpaneladdon.client.init.EntityRenderRegistry;

@Mixin(DMEntityRenderRegistry.class)
public class DMEntityRenderRegistryMixin {
    @Inject(method = "registryEntityRenders", at = @At("TAIL"), remap = false)
    private static void registerEntityRenders(CallbackInfo ci){
        EntityRenderRegistry.registerEntityRenders();
    }
}
