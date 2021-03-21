package com.dooberjaz.mooresmod.util.handlers;

import com.dooberjaz.mooresmod.blocks.tileEntities.*;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event){
        registerTileEntities();
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event){
        for(Item item : ModItems.ITEMS){
            if(item instanceof IHasModel){
                ((IHasModel) item).registerModels();
            }
        }

        for(Block block : ModBlocks.BLOCKS){
            if(block instanceof IHasModel){
                ((IHasModel) block).registerModels();
            }
        }
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityLogicBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluXnorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/xnor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluXorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/xor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/nor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluOrGateBlock.class, new ResourceLocation("mooresmod", ":models/block/or_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNandGateBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNotGateBlock.class, new ResourceLocation("mooresmod", ":models/block/not_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluAndGateBlock.class, new ResourceLocation("mooresmod", ":models/block/and_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluStone.class, new ResourceLocation("mooresmod", ":models/block/blustone.json"));
    }

}
