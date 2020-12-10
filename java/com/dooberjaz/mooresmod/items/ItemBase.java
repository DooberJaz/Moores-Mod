package com.dooberjaz.mooresmod.items;

import com.dooberjaz.mooresmod.Main;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(String name){
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);

        ModItems.ITEMS.add(this);
    }

}
