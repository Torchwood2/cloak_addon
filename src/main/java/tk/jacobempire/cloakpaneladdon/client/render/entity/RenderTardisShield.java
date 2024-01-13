package tk.jacobempire.cloakpaneladdon.client.render.entity;

import com.swdteam.model.javajson.JSONModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import tk.jacobempire.cloakpaneladdon.client.model.entity.TardisShieldModel;
import tk.jacobempire.cloakpaneladdon.entity.TardisShieldEntity;

public class RenderTardisShield extends MobRenderer<TardisShieldEntity, TardisShieldModel> {
    public RenderTardisShield(EntityRendererManager p_i46168_1_) {
        super(p_i46168_1_, new TardisShieldModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TardisShieldEntity p_110775_1_) {
        JSONModel.ModelInformation modelData = model.getModel().getModelData();
        return modelData.getTexture();
    }
}
