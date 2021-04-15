package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class XorGateBlock  extends LogicBlock{

    //Look, Im not gonna document every extension of logic block or blulogic block. They all work the
    //Same way, except for calculateOutput, which performs the operation of the block (AND does &&, NOR does NOT and ||, etc.)
    //Things like adders also work the same way

    public XorGateBlock(String name, Material material){
        super(name, material);
    }

    @Override
    protected boolean calculateOutput(boolean input1, boolean input2){
        return (input1 || input2) && !(input1 && input2);
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        Boolean powered = unpoweredState.getValue(POWERED);
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return ModBlocks.XOR_GATE.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, !powered);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        Boolean powered = poweredState.getValue(POWERED);
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return ModBlocks.XOR_GATE.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, !powered);
    }
}