package com.dooberjaz.mooresmod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
//Base tile entity class, but I will explain them all here
public class TileEntityBluLogicBlock extends TileEntity {
    //Output signal is the power value stored in the tile entity
    private int outputSignal;

    //Writes the data into the tileenttiy data
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("OutputSignal", this.outputSignal);
        return compound;
    }

    //Reads the tileentity data into the block
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.outputSignal = compound.getInteger("OutputSignal");
    }

    public int getOutputSignal()
    {
        return this.outputSignal;
    }

    public void setOutputSignal(int outputSignalIn)
    {
        this.outputSignal = outputSignalIn;
    }
}
