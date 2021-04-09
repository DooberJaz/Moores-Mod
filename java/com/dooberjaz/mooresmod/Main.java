package com.dooberjaz.mooresmod;

import com.dooberjaz.mooresmod.blocks.BluAndGateBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluLogicBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityLogicBlock;
import com.dooberjaz.mooresmod.proxy.ClientProxy;
import com.dooberjaz.mooresmod.proxy.CommonProxy;
import com.dooberjaz.mooresmod.util.Reference;
import com.dooberjaz.mooresmod.util.handlers.GuiHandler;
import com.dooberjaz.mooresmod.util.recipe.MooresModRecipeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
    @Instance
    public static Main instance;


    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){

    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){

    }

    /*@SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        MooresModRecipeManager.init();
    }*/
}


