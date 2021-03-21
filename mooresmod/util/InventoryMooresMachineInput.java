package com.dooberjaz.mooresmod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryMooresMachineInput implements IInventory
{
    private final ItemStack[] stackResult = new ItemStack[81];

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory()
    {
        //size subject to change
        return 81;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
    public ItemStack getStackInSlot(int input)
    {
        return stackResult[input];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number
     * (second arg) of items and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int stack, int par2)
    {
        if(stackResult[stack] != null)
        {
            ItemStack itemstack = stackResult[stack];
            stackResult[stack] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be
     * crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int input, ItemStack par2ItemStack)
    {
        stackResult[input] = par2ItemStack;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int input, ItemStack itemStackIn)
    {
        return true;
    }

    public boolean isEmpty()
    {
        for(int i = 0; i < stackResult.length; i++ )
        {
            if(stackResult[i] != null)
                return false;
        }
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

    @Override
    public void clear()
    {
        for(int i = 0;i<stackResult.length;i++)
            stackResult[i] = null;
    }

}