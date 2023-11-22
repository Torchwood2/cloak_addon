package tk.jacobempire.chameleonaddon.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.swdteam.client.render.tileentity.RenderBlockTardis;
import com.swdteam.common.tileentity.DMTileEntityBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tk.jacobempire.chameleonaddon.data.ITardisTileEntityMixin;

@Mixin(RenderBlockTardis.class)
public class RenderBlockTardisMixin {

    @Inject(at = @At("HEAD"), method = "render(Lcom/swdteam/common/tileentity/DMTileEntityBase;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V", cancellable = true, remap = false)
    public void render(DMTileEntityBase dmTileEntityBase, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer,
                       int combinedLightIn, int combinedOverlayIn, CallbackInfo info) {
        if (((ITardisTileEntityMixin) dmTileEntityBase).isInvisible()){
            info.cancel();
        }
    }

}
