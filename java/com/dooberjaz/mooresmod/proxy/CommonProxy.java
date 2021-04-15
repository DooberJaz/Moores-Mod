package com.dooberjaz.mooresmod.proxy;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class CommonProxy {
    //Base proxy class
    public void registerItemRenderer(Item itemBase, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(itemBase, meta, new ModelResourceLocation(itemBase.getRegistryName(), id));
    }

    public void registerBlockRenderer(Block blockBase, int meta, String id) {

    }
}
