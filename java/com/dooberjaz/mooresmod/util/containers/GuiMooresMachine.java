package com.dooberjaz.mooresmod.util.containers;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityMooresMachine;
import com.dooberjaz.mooresmod.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiMooresMachine extends GuiContainer {

    //Loads the actual GUI of the moores machine
    public ContainerMooresMachine container;
    private final String blockName;

    protected int xSize = 256;
    protected int ySize = 256;

    public GuiMooresMachine(
            InventoryPlayer playerInventory,
            World worldIn,
            String blockName,
            int x, int y, int z)
    {
        super(new ContainerMooresMachine(playerInventory, worldIn, x, y, z));
        container = (ContainerMooresMachine) inventorySlots;
        this.blockName = blockName;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(
                new ResourceLocation(Reference.MOD_ID + ":textures/gui/containers/moores_machine3.png"));
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    }
}
