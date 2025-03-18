package samgido.samuel_mod.frozen_bushes;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;

public class FrozenSweetBerryBush extends SweetBerryBushBlock {
    public FrozenSweetBerryBush(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) { return false; }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
