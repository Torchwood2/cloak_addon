package tk.jacobempire.chameleonaddon.block;

import com.swdteam.common.block.AbstractRotateableWaterLoggableBlock;
import com.swdteam.common.init.DMFlightMode;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.util.ChatUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tk.jacobempire.chameleonaddon.data.ITardisTileEntityMixin;

public class CloakingPanelBlock extends AbstractRotateableWaterLoggableBlock {
    public CloakingPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide || hand.equals(Hand.OFF_HAND)) return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);

        TardisData data = DMTardis.getTardisFromInteriorPos(blockPos);
        if (data != null && !data.isInFlight() && data.getCurrentLocation() != null && !DMFlightMode.isInFlight(data.getGlobalID())) {
            Location location = data.getCurrentLocation();
            ServerWorld serverWorld = world.getServer().getLevel(data.getCurrentLocation().dimensionWorldKey());
            TileEntity tile = serverWorld.getBlockEntity(location.getPosition().toBlockPos());
            if (tile instanceof TardisTileEntity) {
                ITardisTileEntityMixin iTardisTileEntityMixin = (ITardisTileEntityMixin) tile;
                iTardisTileEntityMixin.setInvisible(!iTardisTileEntityMixin.isInvisible());
                ChatUtil.sendCompletedMsg(playerEntity, String.format("Invisible : %b", iTardisTileEntityMixin.isInvisible()), ChatUtil.MessageType.STATUS_BAR);
            }
        }
        return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }
}
