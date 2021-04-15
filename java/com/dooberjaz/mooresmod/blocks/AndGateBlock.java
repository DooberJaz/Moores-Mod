package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.Main;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

public class AndGateBlock extends LogicBlock{
    //Look, Im not gonna document every extension of logic block or blulogic block. They all work the
    //Same way, except for calculateOutput, which performs the operation of the block (AND does &&, NOR does NOT and ||, etc.)
    //Things like adders also work the same way

    public AndGateBlock(String name, Material material){
        super(name, material);
    }

    @Override
    protected boolean calculateOutput(boolean input1, boolean input2){
        return input1 && input2;
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
}
