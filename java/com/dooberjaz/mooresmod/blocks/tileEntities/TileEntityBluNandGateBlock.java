package com.dooberjaz.mooresmod.blocks.tileEntities;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityBluNandGateBlock extends TileEntity {
    private int outputSignal;

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("OutputSignal", this.outputSignal);
        super.writeToNBT(compound);
        return compound;
    }

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
