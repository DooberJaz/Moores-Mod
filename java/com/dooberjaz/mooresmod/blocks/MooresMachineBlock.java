package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.Main;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityMooresMachine;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import com.dooberjaz.mooresmod.proxy.ClientProxy;
import com.dooberjaz.mooresmod.util.IHasModel;
import com.dooberjaz.mooresmod.util.Reference;
import com.dooberjaz.mooresmod.util.handlers.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;

public class MooresMachineBlock extends Block {


    //Why do I have the facing property?
    public static final PropertyDirection FACING = PropertyDirection.create(
            "facing", EnumFacing.Plane.HORIZONTAL);

    //constructor
    public MooresMachineBlock(String name, Material material) {
        super(material);
        //Sets the name used by the code and for loading the mod
        setUnlocalizedName(name);
        setRegistryName(name);

        //Places it in the redstone tab for spawning in items in creative mode
        setCreativeTab(CreativeTabs.REDSTONE);

        //Add it to the list of blocks in the mod
        ModBlocks.BLOCKS.add(this);
        //Add it's item-block into the list of items (needed for crafting and such)
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
    }

    //Open the Moores Machine GUI when the block is activated (right clicked)
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote) {
            playerIn.openGui(Main.instance, 0, //0 = MooresMachine GUI
                    worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    /* NOT NEEDED CODE

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMooresMachine();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }*/

    //Called when the block is placed
    @Override
    public void onBlockAdded(
            World worldIn,
            BlockPos pos,
            IBlockState IBlockState
    )
    {
        if (!worldIn.isRemote)
        {

            EnumFacing enumfacing = (EnumFacing)IBlockState.getValue(FACING);

            worldIn.setBlockState(pos, IBlockState.withProperty(
                    FACING, enumfacing), 2);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    public static int getBlockID(){
        return MooresMachineBlock.getIdFromBlock(ModBlocks.MOORES_MACHINE);
    }

}
