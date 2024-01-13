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
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tk.jacobempire.cloakpaneladdon.data.ITardisTileEntityMixin;

public class CloakingPanelBlock extends AbstractRotateableWaterLoggableBlock {
    public CloakingPanelBlock(Properties properties) {
        super(properties);
    }

    public static final VoxelShape PANEL_SHAPE_BASE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    protected static final VoxelShape TEST = VoxelShapes.or(PANEL_SHAPE_BASE, new VoxelShape[0]);
    ;


    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return TEST;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return TEST;
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (world.isClientSide || hand.equals(Hand.OFF_HAND))
            return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
        if (world.dimension().equals(DMDimensions.TARDIS)) {
            TardisData data = DMTardis.getTardisFromInteriorPos(blockPos);
            if (data != null && !data.isInFlight() && data.getCurrentLocation() != null && !DMFlightMode.isInFlight(data.getGlobalID())) {
                Location location = data.getCurrentLocation();
                ServerWorld serverWorld = world.getServer().getLevel(data.getCurrentLocation().dimensionWorldKey());
                TileEntity tile = serverWorld.getBlockEntity(location.getPosition().toBlockPos());
                if (tile instanceof TardisTileEntity) {
                    ITardisTileEntityMixin iTardisTileEntityMixin = (ITardisTileEntityMixin) tile;
                    iTardisTileEntityMixin.setInvisible(!iTardisTileEntityMixin.isInvisible());
                    iTardisTileEntityMixin.setHasShield(!iTardisTileEntityMixin.hasShield());
                    ChatUtil.sendCompletedMsg(playerEntity, String.format("Invisible : %b", iTardisTileEntityMixin.isInvisible()), ChatUtil.MessageType.STATUS_BAR);
                }
            }
        }
        return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }


    public BlockState updateShape(BlockState state1, Direction dir, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2) {
        return dir == Direction.DOWN && !this.canSurvive(state2, world, pos1) ? Blocks.AIR.defaultBlockState() : super.updateShape(state1, dir, state2, world, pos1, pos2);
    }


}
