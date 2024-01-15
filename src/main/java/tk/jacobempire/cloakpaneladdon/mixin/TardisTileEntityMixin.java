package tk.jacobempire.cloakpaneladdon.mixin;

import com.swdteam.common.init.DMBlockEntities;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMFlightMode;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tardis.TardisDoor;
import com.swdteam.common.tardis.TardisState;
import com.swdteam.common.teleport.TeleportRequest;
import com.swdteam.common.tileentity.ExtraRotationTileEntityBase;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.util.SWDMathUtils;
import com.swdteam.util.TeleportUtil;
import com.swdteam.util.math.Position;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tk.jacobempire.cloakpaneladdon.CloakPanelAddon;
import tk.jacobempire.cloakpaneladdon.data.ITardisTileEntityMixin;
import tk.jacobempire.cloakpaneladdon.entity.TardisShieldEntity;
import tk.jacobempire.cloakpaneladdon.init.Entities;
import tk.jacobempire.cloakpaneladdon.tardis.TardisShieldManager;

import java.util.List;
import java.util.function.Predicate;

@Mixin(TardisTileEntity.class)
public abstract class TardisTileEntityMixin extends ExtraRotationTileEntityBase implements ITickableTileEntity, ITardisTileEntityMixin {
    @Shadow public abstract void snowCheck();

    @Shadow protected abstract void doorAnimation();

    @Shadow public long lastTickTime;
    @Shadow public TardisState state;
    @Shadow private boolean demat;
    @Shadow public long animStartTime;
    @Shadow public float dematTime;

    @Shadow public abstract void setState(TardisState state);

    @Shadow public float pulses;
    @Shadow public int bobTime;
    @Shadow public TardisData tardisData;
    @Shadow public int globalID;
    @Shadow public static AxisAlignedBB defaultAABB;
    @Shadow public boolean doorOpenLeft;
    @Shadow public boolean doorOpenRight;
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

    @Inject(at=@At("RETURN"), method = "save", remap = true, cancellable = true)
    public void save(CompoundNBT compound, CallbackInfoReturnable<CompoundNBT> cir){
        CloakPanelAddon.LOGGER.info("SAVE THE TARDIS");
        CompoundNBT returnCompound = cir.getReturnValue();
        returnCompound.putBoolean("IsInvisible", isInvisible());
        returnCompound.putBoolean("HasShield", hasShield());
        cir.setReturnValue(returnCompound);
    }

    @Inject(at=@At("HEAD"), method = "load", remap = true)
    public void load(BlockState blockstate, CompoundNBT compound, CallbackInfo ci){
        CloakPanelAddon.LOGGER.info("LOAD THE TARDIS");
        if (compound.contains("IsInvisible")) {
            CloakPanelAddon.LOGGER.info(compound.getBoolean("IsInvisible"));
            invisible = compound.getBoolean("IsInvisible");
        }
        if (compound.contains("HasShield")){
            CloakPanelAddon.LOGGER.info(compound.getBoolean("HasShield"));
            hasShield = compound.getBoolean("HasShield");
        }
    }

    @Inject(at=@At("HEAD"), method = "tick", remap = true, cancellable = true)
    public void tick(CallbackInfo ci) {
        ci.cancel();
        if (this.level.isClientSide && this.level.random.nextInt(100) == 50) {
            this.snowCheck();
        }
        if(!this.level.isClientSide && hasShield && !shieldManager.shieldExists()){
            shieldManager.createShield(this.getLevel(), this.getBlockPos());
        }

        this.doorAnimation();
        long tickTime = System.currentTimeMillis() - this.lastTickTime;
        this.lastTickTime = System.currentTimeMillis();
        if (this.state == TardisState.DEMAT) {
            this.demat = true;
            if (this.animStartTime == 0L) {
                this.animStartTime = System.currentTimeMillis();
            }

            if (tickTime > 100L) {
                this.animStartTime += tickTime;
            }

            this.dematTime = (float)((double)(System.currentTimeMillis() - this.animStartTime) / 10000.0);
            if (this.dematTime >= 1.0F) {
                this.dematTime = 1.0F;
            }

            if (this.dematTime == 1.0F) {
                this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
                this.animStartTime = 0L;
            }
        } else if (this.state == TardisState.REMAT) {
            this.demat = false;
            if (this.animStartTime == 0L) {
                this.animStartTime = System.currentTimeMillis();
            }

            if (tickTime > 100L) {
                this.animStartTime += tickTime;
            }

            if (System.currentTimeMillis() - this.animStartTime > 9000L) {
                this.dematTime = 1.0F - (float)((double)(System.currentTimeMillis() - (this.animStartTime + 9000L)) / 10000.0);
            }

            if (this.dematTime <= 0.0F) {
                this.dematTime = 0.0F;
            }

            if (this.dematTime == 0.0F) {
                this.setState(TardisState.NEUTRAL);
                this.animStartTime = 0L;
            }
        }

        this.pulses = 1.0F - this.dematTime + MathHelper.cos(this.dematTime * 3.141592F * 10.0F) * 0.25F * MathHelper.sin(this.dematTime * 3.141592F);
        if (this.getLevel().getBlockState(this.getBlockPos().offset(0, -1, 0)).getMaterial() == Material.AIR) {
            ++this.bobTime;
            ++this.rotation;
        } else {
            this.bobTime = 0;
            this.rotation = SWDMathUtils.SnapRotationToCardinal(this.rotation);
        }

        if (!this.level.isClientSide) {
            this.tardisData = DMTardis.getTardis(this.globalID);
            if (this.tardisData != null && (this.doorOpenLeft || this.doorOpenRight)) {
                defaultAABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
                AxisAlignedBB bounds = defaultAABB.move(this.getBlockPos()).inflate(-0.30000001192092896, 0.0, -0.30000001192092896);
                bounds = bounds.move(Math.sin(Math.toRadians((double)this.rotation)) * 0.5, 0.0, -Math.cos(Math.toRadians((double)this.rotation)) * 0.5);
                List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, bounds);
                Predicate<Entity> inFlight = (entity) -> {
                    return entity instanceof PlayerEntity && DMFlightMode.isInFlight((PlayerEntity)entity);
                };
                Predicate<Entity> isRiding = (entity) -> {
                    return entity.isPassenger();
                };
                Predicate<Entity> isShield = (entity) -> {
                    return entity.getType() == Entities.TARDIS_SHIELD_ENTITY.get();
                };
                entities.removeIf(inFlight);
                entities.removeIf(isRiding);
                entities.removeIf(isShield);
                if (!entities.isEmpty()) {
                    Entity e = (Entity)entities.get(0);
                    Position vec = this.tardisData.getInteriorSpawnPosition();
                    if (!TeleportUtil.TELEPORT_REQUESTS.containsKey(e) && vec != null) {
                        Location loc = new Location(new Vector3d(vec.x(), vec.y(), vec.z()), DMDimensions.TARDIS);
                        loc.setFacing(this.tardisData.getInteriorSpawnRotation() + e.getYHeadRot() - this.rotation);
                        TeleportUtil.TELEPORT_REQUESTS.put(e, new TeleportRequest(loc));
                    }
                }
            }
        }

    }
}
