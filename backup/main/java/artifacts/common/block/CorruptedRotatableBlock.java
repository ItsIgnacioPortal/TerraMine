package terracraft.common.block;

import terracraft.common.utility.CorruptionHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CorruptedRotatableBlock extends CorruptionHelper {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public CorruptedRotatableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(blockState, rotation);
    }

    public static BlockState rotatePillar(BlockState blockState, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (blockState.getValue(AXIS)) {
                    case X: {
                        return (BlockState)blockState.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z: {
                        return (BlockState)blockState.setValue(AXIS, Direction.Axis.X);
                    }
                }
                return blockState;
            }
        }
        return blockState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        builder.add(SNOWY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (BlockState)this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis());
    }
}
