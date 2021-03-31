package com.dooberjaz.mooresmod.blocks.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBluStone extends TileEntity {
    private int power;

    @Override
    public boolean isInvalid()
    {
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("OutputSignal", this.power);
        super.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.power = compound.getInteger("OutputSignal");
    }

    public int getPower()
    {
        return this.power;
    }

    public void setPower(int powerIn)
    {
        this.power = powerIn;
        this.markDirty();
    }
}