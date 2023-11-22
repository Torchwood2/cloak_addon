package tk.jacobempire.cloakpaneladdon.mixin;

import com.swdteam.common.tileentity.ExtraRotationTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.jacobempire.cloakpaneladdon.data.ITardisTileEntityMixin;

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

    public TardisTileEntityMixin(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Inject(at=@At("HEAD"), method = "save", remap = false)
    public void save(CompoundNBT compound, CallbackInfoReturnable<CompoundNBT> cir){
        compound.putBoolean("Invisible", invisible);
    }

    @Inject(at=@At("HEAD"), method = "load", remap = false)
    public void load(BlockState blockstate, CompoundNBT compound, CallbackInfo ci){
        if (compound.contains("Invisible")) {
            invisible = compound.getBoolean("Invisible");
        }
    }
}
