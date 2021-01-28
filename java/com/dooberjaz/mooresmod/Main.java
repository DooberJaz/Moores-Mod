package com.dooberjaz.mooresmod;

import com.dooberjaz.mooresmod.blocks.LogicBlock;
import com.dooberjaz.mooresmod.blocks.TileEntityBluLogicBlock;
import com.dooberjaz.mooresmod.blocks.TileEntityLogicBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.proxy.CommonProxy;
import com.dooberjaz.mooresmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.sql.Ref;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
    @Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){

    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){

    }

    /*@SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));

        registerTileEntities();
    }*/

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityLogicBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
        GameRegistry.registerTileEntity(TileEntityBluLogicBlock.class, new ResourceLocation("mooresmod", ":models/block/nand_gate.json"));
    }

}
