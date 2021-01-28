package com.dooberjaz.mooresmod.init;

import com.dooberjaz.mooresmod.blocks.*;
import com.dooberjaz.mooresmod.items.ItemBase;
import com.dooberjaz.mooresmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block AND_GATE = new AndGateBlock("and_gate", Material.ROCK);
    public static final Block XOR_GATE = new XorGateBlock("xor_gate", Material.ROCK);
    public static final Block OR_GATE = new OrGateBlock("or_gate", Material.ROCK);
    public static final Block NOT_GATE = new BlockBase("not_gate", Material.ROCK);
    public static final Block NAND_GATE = new NandGateBlock("nand_gate", Material.ROCK);
    public static final Block NOR_GATE = new NorGateBlock("nor_gate", Material.ROCK);
    public static final Block BLUSTONE = new BluStone("blustone", Material.ROCK);
    public static final Block BLU_AND_GATE = new BluAndGateBlock("blu_and_gate", Material.ROCK);
    public static final Block BLU_OR_GATE = new BluOrGateBlock("blu_or_gate", Material.ROCK);
}
