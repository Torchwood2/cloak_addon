package tk.jacobempire.cloakpaneladdon.block;

import com.swdteam.common.block.AbstractRotateableWaterLoggableBlock;
import com.swdteam.common.init.DMDimensions;
import com.swdteam.common.init.DMFlightMode;
import com.swdteam.common.init.DMTardis;
import com.swdteam.common.tardis.Location;
import com.swdteam.common.tardis.TardisData;
import com.swdteam.common.tileentity.TardisTileEntity;
import com.swdteam.util.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tk.jacobempire.cloakpaneladdon.data.ITardisTileEntityMixin;

public class ShieldPanelBlock extends CloakingPanelBlock {
    public ShieldPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
                .setValue(FACING, Direction.NORTH))
                .setValue(WATERLOGGED, false)
                .setValue(BlockStateProperties.ENABLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> state) {
        super.createBlockStateDefinition(state);
        state.add(BlockStateProperties.ENABLED);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide || hand.equals(Hand.OFF_HAND))
            return ActionResultType.PASS;
        if (world.dimension().equals(DMDimensions.TARDIS)) {
            TardisData data = DMTardis.getTardisFromInteriorPos(blockPos);
            if (data != null && !data.isInFlight() && data.getCurrentLocation() != null && !DMFlightMode.isInFlight(data.getGlobalID())) {
                Location location = data.getCurrentLocation();
                ServerWorld serverWorld = world.getServer().getLevel(data.getCurrentLocation().dimensionWorldKey());
                TileEntity tile = serverWorld.getBlockEntity(location.getPosition().toBlockPos());
                if (tile instanceof TardisTileEntity) {
                    ITardisTileEntityMixin iTardisTileEntityMixin = (ITardisTileEntityMixin) tile;
                    iTardisTileEntityMixin.setHasShield(!iTardisTileEntityMixin.hasShield());
                    world.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.ENABLED, iTardisTileEntityMixin.hasShield()));
                    ChatUtil.sendCompletedMsg(playerEntity, String.format("Shield Activated : %b", iTardisTileEntityMixin.hasShield()), ChatUtil.MessageType.STATUS_BAR);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }
}
