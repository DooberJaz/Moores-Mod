package com.dooberjaz.mooresmod.util.handlers;

import com.dooberjaz.mooresmod.blocks.tileEntities.*;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

    //This handles all the registering that is done at the beginning of the game (on start up)

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
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

        for(Block block : ModBlocks.BLOCKS){
            if(block instanceof IHasModel){
                ((IHasModel) block).registerModels();
            }
        }
    }

    public static void registerTileEntities()
    {
        //Since each block has a seperate texture, they all need seperate tileentities even though each tile entiity is the same
        GameRegistry.registerTileEntity(TileEntityLogicBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluXnorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/xnor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluXorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/xor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNorGateBlock.class, new ResourceLocation("mooresmod", ":models/block/nor_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluOrGateBlock.class, new ResourceLocation("mooresmod", ":models/block/or_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNandGateBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluNotGateBlock.class, new ResourceLocation("mooresmod", ":models/block/not_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluAndGateBlock.class, new ResourceLocation("mooresmod", ":models/block/and_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluStone.class, new ResourceLocation("mooresmod", ":models/block/blustone.json"));
        GameRegistry.registerTileEntity(TileEntityAdderBlock.class, new ResourceLocation("mooresmod", ":models/block/adder.json"));
        GameRegistry.registerTileEntity(TileEntitySubtractorBlock.class, new ResourceLocation("mooresmod", ":models/block/subtractor.json"));
        GameRegistry.registerTileEntity(TileEntityMooresMachine.class, new ResourceLocation("mooresmod", ":models/block/moores_machine.json"));
        GameRegistry.registerTileEntity(TileEntityMultiplexor.class, new ResourceLocation("mooresmod", ":models/block/blu_multiplexor.json"));
        GameRegistry.registerTileEntity(TileEntityProductBlock.class, new ResourceLocation("mooresmod", ":models/block/product.json"));
        GameRegistry.registerTileEntity(TileEntityDivisionBlock.class, new ResourceLocation("mooresmod", ":models/block/division.json"));
        GameRegistry.registerTileEntity(TileEntityBluALUBlock.class, new ResourceLocation("mooresmod", ":models/block/blu_alu.json"));
        GameRegistry.registerTileEntity(TileEntityCounterBlock.class, new ResourceLocation("mooresmod", ":models/block/counter.json"));
    }


}
