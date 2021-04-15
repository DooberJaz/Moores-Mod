package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityAdderBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluALUBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.dooberjaz.mooresmod.util.Reference.BIT_SIZE;

public class BluALUBlock extends BluLogicBlock {

    //Look, Im not gonna document every extension of logic block or blulogic block. They all work the
    //Same way, except for calculateOutput, which performs the operation of the block (AND does &&, NOR does NOT and ||, etc.)
    //Things like adders also work the same way

    //There is an exception here, this block hasa third input for calculateOutput. This is obtained from a third side
    //of the block, and helps control the output

    public BluALUBlock(String name, Material material) {
        super(name, material);
    }

    protected int calculateOutput(int input1, int input2, int opCode){
        //write alu code
        int temp;
        if(opCode == 0){
            //assume this is plus
            temp = input1 + input2;
        } else if(opCode == 1) {
            //assume this is subtract
            temp = input1 - input2;
        }else if(opCode == 2){
            //assume this is multiply
            temp = input1 * input2;
        }
        else if(opCode == 4){
            //assumke this is divide
            if(input2 > 0) {
                temp = input1 / input2;
            } else return 0;
        } else {
            //there has been an error
            temp = 0;
        }
        if(temp >= (int) Math.pow(2, BIT_SIZE)){
            //overflow
            temp = 0;
        } else if(temp < 0){
            //underflow
            temp = (int) Math.pow(2, BIT_SIZE) - 1;
        }
        return temp;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBluALUBlock();
    }

    private TileEntityBluALUBlock getTileEntity(World world, BlockPos pos) {
        return (TileEntityBluALUBlock) world.getTileEntity(pos);
    }

    @Override
    protected int calculateInputStrength(@Nonnull World world, BlockPos pos, @Nonnull IBlockState state) {
        //right side input
        int firstInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateY()), state.getValue(FACING).rotateY());
        //left side input
        int secondInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW()), state.getValue(FACING).rotateYCCW());
        //opcode (what calculation to do), also mod performed on this to keep it only as allowed values
        int thirdInput = getPowerOnSide(world, pos.offset(state.getValue(FACING).rotateYCCW().rotateYCCW()), state.getValue(FACING).rotateYCCW().rotateYCCW());
        thirdInput = thirdInput % 4;
        //switched order so calculation goes left <opcode> right
        int x = calculateOutput(secondInput, firstInput, thirdInput);
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
