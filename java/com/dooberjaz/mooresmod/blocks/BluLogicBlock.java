package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluLogicBlock;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.dooberjaz.mooresmod.util.Reference.*;

public abstract class BluLogicBlock extends Block {

    //Facing value for the block, needed for blustone calculations, synced with CONST_FACING so it works with other blocks that use facing
    public static final PropertyDirection FACING = CONST_FACING;
    //Power value, synced with CONST_POWER so it works with other blocks (and is more convenient for coding changes)
    public static final PropertyInteger POWER = CONST_POWER;
    //Bounding box for the block (1x1x1)
    protected static final AxisAlignedBB LOGIC_BLOCK_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    //Constuctor
    public BluLogicBlock(String name, Material material) {
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

        //default state is needed for placement
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
    }

    //Returns true as this block always has a tileentity
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    //Returns the bounding box
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return LOGIC_BLOCK_BB;
    }

    //Used by forge to get the state of the block (used in many places)
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        //Copy this and put it in every single logic block with a changed tileentity type
        TileEntityBluLogicBlock tileEntity = (TileEntityBluLogicBlock)world.getTileEntity(pos);
        try{
            return state.withProperty(POWER, tileEntity.getOutputSignal()).withProperty(FACING, state.getValue(FACING));
        } catch(NullPointerException e){
            return state;
        }
    }

    //Creates a tile entity of the same type as this block
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBluLogicBlock();
    }

    //Code for when a block of this type is added to the world
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        //Adds a tile entity to the position this block is at
        worldIn.setTileEntity(pos, this.createTileEntity(worldIn, state));
        super.onBlockAdded(worldIn, pos, state);
    }

    //Creates the blockstate values needed for this block (facing and power)
    @Override
    protected BlockStateContainer createBlockState() {
       return new BlockStateContainer(this, new IProperty[] {FACING, POWER});
    }

    //Used when loading the block from the world data
    @Override
    public IBlockState getStateFromMeta(int meta) {
        int directionInt = meta;

        if (directionInt == 0) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        } else if (directionInt == 1) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.EAST);
        } else if (directionInt == 2) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
        } else {
            return this.getDefaultState().withProperty(FACING, EnumFacing.WEST);
        }
    }

    //Used when saving the blocks data in the world data (if the block is unloaded this stores its state)
    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(FACING) == EnumFacing.NORTH) {
            return 0;
        } else if (state.getValue(FACING) == EnumFacing.EAST) {
            return 1;
        } else if (state.getValue(FACING) == EnumFacing.SOUTH) {
            return 2;
        } else {
            return 3;
        }
    }

    //Called when the block is destroyed by the player
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(POWER) > 0)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }

        /*if(worldIn.getTileEntity(pos) != null) {
            worldIn.removeTileEntity(pos);
        }*/

        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    //called by the world when the block needs an update (usually redstone based)
    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {

        boolean flag = this.shouldBePowered(worldIn, pos, state);

        if ((state.getValue(POWER) > 0) != flag && !worldIn.isBlockTickPending(pos, this)) {
            int i = -1;

            if ((state.getValue(POWER) > 0)) {
                i = -2;
            }

            worldIn.updateBlockTick(pos, this, this.getDelay(state), i);
        }
    }

    //Called whena  neighboring block's state changes (usually for redstone/blustone stuff)
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (this.canBlockStay(worldIn, pos))
        {
            this.updateState(worldIn, pos, state);
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    //Decides how long the block waits in between updates (0 is instant, but 1 gives the game a little break for larger builds)
    protected int getDelay(IBlockState state) {
        return 1;
    }

    //I mean... This is logical
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    //This block can only be placed on top of a solid block (like redstone)
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return (downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    //If the block below this stops being solid, the block is destroyed and dropped
    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
    }

    //Another method needed for block updates
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
            boolean flag = this.shouldBePowered(worldIn, pos, state);

            if (!flag) {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
            }
    }

    //To save from bugs, I just have it return true. Technically fancy maths can be used and would save processing
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    //Intermediary function used by block updating
    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }

    //This function returns the output strength this block obtains, but doesnt here as it is overriden by the inheriting block types
    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        int i = worldIn.getRedstonePower(blockpos, enumfacing);

        if (i >= (Math.pow(2, BIT_SIZE) - 1))
        {
            return i;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }

    //Unused, but it gets mad sometimes if not here
    protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        EnumFacing enumfacing1 = enumfacing.rotateY();
        EnumFacing enumfacing2 = enumfacing.rotateYCCW();
        return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
    }

    //Returns the power on a specific side of the block
    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (this.isAlternateInput(iblockstate))
        {
            if (block == Blocks.REDSTONE_BLOCK)
            {
                return 15;
            } else if (block == ModBlocks.BLUSTONE) {
                //Needed to make blustone work
                return (Integer) iblockstate.getValue(BluStone.POWER);
            } else {
                return (block == Blocks.REDSTONE_WIRE) ? (Integer) iblockstate.getValue(BlockRedstoneWire.POWER) : worldIn.getStrongPower(pos, side);
            }
        }
        else
        {
            return 0;
        }
    }

    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState();
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (this.shouldBePowered(worldIn, pos, state))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    @Override
    //Called when the block is broken, and also removes the tile entity
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if(worldIn.getTileEntity(pos) != null) {
            worldIn.removeTileEntity(pos);
        }
        super.breakBlock(worldIn, pos, state);
        if (!worldIn.isRemote)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return true;
    }

    protected boolean isAlternateInput(IBlockState state)
    {
        return state.canProvidePower();
    }

    protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        return state.getValue(POWER);
    }


    protected int getTickDelay(IBlockState state)
    {
        return this.getDelay(state);
    }

    protected abstract IBlockState getPoweredState(IBlockState unpoweredState);

    protected abstract IBlockState getUnpoweredState(IBlockState poweredState);


    /* ======================================== FORGE START =====================================*/
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        if (super.rotateBlock(world, pos, axis))
        {
            IBlockState state = world.getBlockState(pos);
            state = getUnpoweredState(state);
            world.setBlockState(pos, state);

            if (shouldBePowered(world, pos, state))
            {
                world.scheduleUpdate(pos, this, 1);
            }
            return true;
        }
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.SOLID;
    }


    //Function I made to make NOT gates work outside of twos complement (as I dont use it currently)
    public int nonComplementNot(int beforeNot){
        int afterNot = 0;
        //Loops through the bits using clever power maths
        for(int i = BIT_SIZE-1; i >= 0; i--){
            //If there is a 1 present in that bit slot (i), remove its value. Otherwise add it
            if(((double)beforeNot)/Math.pow(2, i) > 1.0){
                beforeNot -= i;
            } else {
                afterNot += i;
            }
        }
        return afterNot;
    }
}
