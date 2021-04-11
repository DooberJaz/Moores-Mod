package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class NotGateBlock extends LogicBlock{
    public NotGateBlock(String name, Material material){
        super(name, material);
    }

    protected boolean calculateOutput(boolean input1){
        return !input1;
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        boolean firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY().rotateY()), state.getValue(FACING).rotateY().rotateY()) > 0;
        return calculateOutput(firstInput) ? 15 : 0;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        Boolean powered = unpoweredState.getValue(POWERED);
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return ModBlocks.AND_GATE.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, !powered);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        Boolean powered = poweredState.getValue(POWERED);
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return ModBlocks.AND_GATE.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, !powered);
    }

    @Override
    protected boolean calculateOutput(boolean input1, boolean input2) {
        return calculateOutput(input1);
    }
}
