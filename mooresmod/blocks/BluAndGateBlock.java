package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluAndGateBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluLogicBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BluAndGateBlock extends BluLogicBlock {

    public BluAndGateBlock(String name, Material material) {
        super(name, material);
    }

    protected int calculateOutput(int input1, int input2){
        return input1 & input2;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBluAndGateBlock();
    }

    private TileEntityBluAndGateBlock getTileEntity(World world, BlockPos pos) {
        return (TileEntityBluAndGateBlock) world.getTileEntity(pos);
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        int firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY()), state.getValue(FACING).rotateY());
        int secondInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW()), state.getValue(FACING).rotateYCCW());
        int x = calculateOutput(firstInput, secondInput);
        world.setBlockState(pos, state.withProperty(POWER, x));
        this.getTileEntity(world, pos).setOutputSignal(x);
        this.getTileEntity(world, pos).markDirty();
        return x;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return ModBlocks.BLU_AND_GATE.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return ModBlocks.BLU_AND_GATE.getDefaultState().withProperty(FACING, enumfacing);
    }
}
