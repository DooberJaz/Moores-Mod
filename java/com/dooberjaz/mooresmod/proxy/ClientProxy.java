package com.dooberjaz.mooresmod.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy{
    //Used for private worlds (not multiplayer)
    public void registerItemRenderer(Item item, int metadata, String id) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
