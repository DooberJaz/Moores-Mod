package com.dooberjaz.mooresmod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryMooresMachineInput extends InventoryCrafting
{

    public InventoryMooresMachineInput(Container eventHandlerIn, int width, int height) {
        super(eventHandlerIn, width, height);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        //size subject to change
        return 81;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }


    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isItemValidForSlot(int input, ItemStack itemStackIn)
    {
        return true;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public void openInventory(EntityPlayer playerIn)
    {

    }

    @Override
    public void closeInventory(EntityPlayer playerIn)
    {

    }

    @Override
    public int getField(int id)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getFieldCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

}