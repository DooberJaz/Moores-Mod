package com.dooberjaz.mooresmod.util.containers;

import com.dooberjaz.mooresmod.blocks.MooresMachineBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityMooresMachine;
import com.dooberjaz.mooresmod.util.InventoryMooresMachineInput;
import com.dooberjaz.mooresmod.util.recipe.InventoryMooresCrafting;
import com.dooberjaz.mooresmod.util.recipe.MooresModRecipeManager;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMooresMachine extends Container
{
    //public InventoryCrafting craftMatrix = new InventoryCrafting(this, 9, 9);
   // public InventoryCrafting craftResult = new InventoryCrafting(this, 1, 1);
    public InventoryMooresMachineInput craftMatrix = new InventoryMooresMachineInput(this, 9, 9);
    public InventoryCraftResult craftResult = new InventoryCraftResult();

    private  World world;
    private  EntityPlayer player;
    public InventoryPlayer playerInventory;
    public int x = 0;
    public int y = 0;
    public int z = 0;

    public ContainerMooresMachine(
            InventoryPlayer playerInventoryIn,
            World worldIn,
            int x, int y, int z
    )
    {
        this.world = worldIn;
        this.player = playerInventoryIn.player;

        MooresModRecipeManager.init();

        //this.inventoryItemStacks.add(ItemStack.EMPTY);

        playerInventory = playerInventoryIn;

        //creation of input slots (9x9)
        //Switched x and y because I have a theory its causing a bug, I was right
        for(int iy = 0; iy < 9; ++iy)
        {
            for(int ix = 0; ix < 9; ++ix)
            {
                addSlotToContainer(new Slot(craftMatrix, ix +
                        (iy * 9),  (int)Math.round(ix * 16.5) - 24,
                        (int)Math.round(iy * 16.5) - 27));
            }
        }

        //creation of output slot (1)
        addSlotToContainer(new Slot(craftResult, 0,  190, 38));

        //creation of player inv slots
        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + (k * 9) + 9, (int)Math.round(i1 * 16.5) -24, 135 + (int)Math.round(k * 16.5)));
            }
        }

        //creation of player hotbar slots
        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, (int)Math.round(l * 16.5)-24, 189));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        craftResult.setInventorySlotContents(0, MooresModRecipeManager.getCraftingResult(craftMatrix, world));
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
    }

    /**
     * Called when a player shift-clicks on a slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);

                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack itemStackIn, Slot slotIn)
    {
        return !slotIn.inventory.equals(craftResult);
    }

    @Override
    public Slot getSlot(int slotIndex)
    {
        if(slotIndex >= inventorySlots.size())
            slotIndex = inventorySlots.size() - 1;
        return super.getSlot(slotIndex);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
