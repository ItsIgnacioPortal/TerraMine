package terramine.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import terramine.common.utility.CorruptionHelper;

import java.util.Random;

public class CorruptedFallingBlock extends CorruptionHelper {
    public CorruptedFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        level.scheduleTick(blockPos, this, this.getDelayAfterPlace());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        levelAccessor.scheduleTick(blockPos, this, this.getDelayAfterPlace());
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (!FallingBlock.isFree(serverLevel.getBlockState(blockPos.below())) || blockPos.getY() < serverLevel.getMinBuildHeight()) {
            return;
        }
        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(serverLevel, blockPos, blockState);
        this.falling(fallingBlockEntity);
    }

    protected void falling(FallingBlockEntity fallingBlockEntity) {
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    public static boolean isFree(BlockState blockState) {
        Material material = blockState.getMaterial();
        return blockState.isAir() || blockState.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        BlockPos blockPos2;
        if (random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(blockPos2 = blockPos.below()))) {
            double d = (double)blockPos.getX() + random.nextDouble();
            double e = (double)blockPos.getY() - 0.05;
            double f = (double)blockPos.getZ() + random.nextDouble();
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState), d, e, f, 0.0, 0.0, 0.0);
        }
    }
}