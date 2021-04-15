package com.dooberjaz.mooresmod.items;

import com.dooberjaz.mooresmod.Main;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel{
    //Base (unused) item class, but needed if I ever make an item that cant be placed as a block
    public ItemBase(String name){
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);

        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
