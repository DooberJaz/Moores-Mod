package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluAndGateBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluNotGateBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BluNotGateBlock extends BluLogicBlock {

    public BluNotGateBlock(String name, Material material) {
        super(name, material);
    }

    protected int calculateOutput(int input1){
        return nonComplementNot(input1);
    }

    private TileEntityBluNotGateBlock getTileEntity(World world, BlockPos pos) {
        return (TileEntityBluNotGateBlock) world.getTileEntity(pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBluNotGateBlock();
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        int firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY()), state.getValue(FACING).rotateY());
        int x = calculateOutput(firstInput);
        world.setBlockState(pos, state.withProperty(POWER, x));
        this.getTileEntity(world, pos).setOutputSignal(x);
        this.getTileEntity(world, pos).markDirty();
        return x;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return ModBlocks.BLU_NOT_GATE.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return ModBlocks.BLU_NOT_GATE.getDefaultState().withProperty(FACING, enumfacing);
    }
}

