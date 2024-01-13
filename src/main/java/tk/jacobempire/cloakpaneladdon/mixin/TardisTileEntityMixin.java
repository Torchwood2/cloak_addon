package tk.jacobempire.cloakpaneladdon.mixin;

import com.swdteam.common.init.DMBlockEntities;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisDoor;
import com.swdteam.common.tardis.TardisState;
import com.swdteam.common.tileentity.ExtraRotationTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.jacobempire.cloakpaneladdon.data.ITardisTileEntityMixin;
import tk.jacobempire.cloakpaneladdon.tardis.TardisShieldManager;

@Mixin(TardisTileEntity.class)
public abstract class TardisTileEntityMixin extends ExtraRotationTileEntityBase implements ITickableTileEntity, ITardisTileEntityMixin {
    private boolean invisible = false;
    public boolean isInvisible(){
        return invisible;
    };
    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public TardisShieldManager shieldManager = new TardisShieldManager();
    private boolean hasShield = false;
    public boolean hasShield(){
        return hasShield;
    };
    @Override
    public void setHasShield(boolean hasShield) {
        if(this.hasShield != hasShield){
            if(hasShield) shieldManager.createShield(this.getLevel(), this.getBlockPos());
            else shieldManager.removeShield();
        }
        this.hasShield = hasShield;
    }

    public TardisTileEntityMixin(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Inject(at=@At("HEAD"), method = "save", remap = true)
    public void save(CompoundNBT compound, CallbackInfoReturnable<CompoundNBT> cir){
        compound.putBoolean("Invisible", invisible);
        compound.putBoolean("HasShield", hasShield);
    }

    @Inject(at=@At("HEAD"), method = "load", remap = true)
    public void load(BlockState blockstate, CompoundNBT compound, CallbackInfo ci){
        if (compound.contains("Invisible")) {
            invisible = compound.getBoolean("Invisible");
        }
        if (compound.contains("HasShield")){
            setHasShield(compound.getBoolean("HasShield"));
            // if it has a shield - raytrace upwards to find it
        }
    }
}
