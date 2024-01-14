package tk.jacobempire.cloakpaneladdon.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.swdteam.client.model.IModelPartReloader;
import com.swdteam.client.model.ModelReloaderRegistry;
import com.swdteam.model.javajson.JSONModel;
import com.swdteam.model.javajson.ModelLoader;
import com.swdteam.model.javajson.ModelWrapper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import tk.jacobempire.cloakpaneladdon.CloakPanelAddon;
import tk.jacobempire.cloakpaneladdon.entity.TardisShieldEntity;

import java.util.function.Function;

public class TardisShieldModel extends SegmentedModel<TardisShieldEntity> implements IModelPartReloader {
    public TardisShieldModel() {
        super();
        ModelReloaderRegistry.register(this);
    }

    public JSONModel model;
    protected ModelRenderer shield;

    @Override
    public JSONModel getModel() {
        return model;
    }
    @Override
    public void init() {
        this.model = ModelLoader.loadModel(new ResourceLocation(CloakPanelAddon.MODID, "models/entity/tardis_shield.json"));
        ModelWrapper modelWrapper = this.model.getModelData().getModel();
        this.shield = modelWrapper.getPart("shield");
    }
    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(shield);
    }
    @Override
    public void setupAnim(TardisShieldEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }
}
