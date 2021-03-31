package com.dooberjaz.mooresmod.util.handlers;

import com.dooberjaz.mooresmod.blocks.MooresMachineBlock;
import com.dooberjaz.mooresmod.util.containers.ContainerMooresMachine;
import com.dooberjaz.mooresmod.util.containers.GuiMooresMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    //0 = MooresMachine

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0 )
        {
            return new ContainerMooresMachine(player.inventory, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0)
        {
            return new GuiMooresMachine(player.inventory, world,
                    "moores_machine", x, y, z);
        }
        return null;
    }
}
