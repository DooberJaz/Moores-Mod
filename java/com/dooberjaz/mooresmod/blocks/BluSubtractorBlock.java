package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntitySubtractorBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BluSubtractorBlock extends BluLogicBlock {

    //Look, Im not gonna document every extension of logic block or blulogic block. They all work the
    //Same way, except for calculateOutput, which performs the operation of the block (AND does &&, NOR does NOT and ||, etc.)
    //Things like adders also work the same way

    public BluSubtractorBlock(String name, Material material) {
        super(name, material);
    }

    protected int calculateOutput(int input1, int input2){
        int temp = input1 - input2;
        if(temp < 0 ){
            temp = 0;
        }
        return temp;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntitySubtractorBlock();
    }

    private TileEntitySubtractorBlock getTileEntity(World world, BlockPos pos) {
        return (TileEntitySubtractorBlock) world.getTileEntity(pos);
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        int firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY()), state.getValue(FACING).rotateY());
        int secondInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW()), state.getValue(FACING).rotateYCCW());
        int x = calculateOutput(secondInput, firstInput);
        world.setBlockState(pos, state.withProperty(POWER, x));
        this.getTileEntity(world, pos).setOutputSignal(x);
        this.getTileEntity(world, pos).markDirty();
        return x;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        int power = unpoweredState.getValue(POWER);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWER, power);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        int power = poweredState.getValue(POWER);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWER, power);
    }
}
