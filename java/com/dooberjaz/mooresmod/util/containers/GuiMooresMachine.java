package com.dooberjaz.mooresmod.util.containers;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiMooresMachine extends GuiContainer {
    public ContainerMooresMachine container;
    private final String blockName;

    protected int xSize = 1197;
    protected int ySize = 837;

    public GuiMooresMachine(
            InventoryPlayer playerInventory,
            World worldIn,
            String blockName,
            int x, int y, int z)
    {
        super(new ContainerMooresMachine(playerInventory, worldIn, x,  y,  z));
        container = (ContainerMooresMachine) inventorySlots;
        this.blockName = blockName;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(
                new ResourceLocation("mooresmod:textures/gui/container/moores_machine.png"));

        int k = width / 2 - xSize / 2;
        int l = height / 2 - ySize / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    }
}
