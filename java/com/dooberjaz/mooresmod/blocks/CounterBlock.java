package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityAdderBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityCounterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.dooberjaz.mooresmod.util.Reference.BIT_SIZE;

public class CounterBlock extends BluLogicBlock {

    public CounterBlock(String name, Material material) {
        super(name, material);
    }

    protected int calculateOutput(int input1, int input2, int control) {
        int temp = 0;
        //hold on, isnt this just a multiplier/divider????
        if (control == 0) {
            //assume its being used for division and will output the count not the result
            if(input2 > 0) {
                while (input1 - input2 > -1) {
                    temp++;
                    input1 -= input2;
                }
            } else {
                //invalid inputs (div by 0 error)
                return 0;
            }
        } else {
            //assume its being used for multiplication so output the mltiplication of 1 and 2
            if(input2 > 0) {
                for (int i = 1; i <= input2; i++) {
                    temp += input1;
                }
            } else {
                return 0;
            }
        }
        return temp;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCounterBlock();
    }

    private TileEntityCounterBlock getTileEntity(World world, BlockPos pos) {
        return (TileEntityCounterBlock) world.getTileEntity(pos);
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        int firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY()), state.getValue(FACING).rotateY());
        int secondInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW()), state.getValue(FACING).rotateYCCW());
        int thirdInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW().rotateYCCW()), state.getValue(FACING).rotateYCCW().rotateYCCW());
        int x = calculateOutput(secondInput, firstInput, thirdInput);
        world.setBlockState(pos, state.withProperty(POWER, x));
        this.getTileEntity(world, pos).setOutputSignal(x);
        this.getTileEntity(world, pos).markDirty();
        return x;
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }
}
