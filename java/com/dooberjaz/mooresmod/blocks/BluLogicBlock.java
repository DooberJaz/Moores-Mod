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

    public static final PropertyDirection FACING = CONST_FACING;
    public static final PropertyInteger POWER = CONST_POWER;
    protected static final AxisAlignedBB LOGIC_BLOCK_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BluLogicBlock(String name, Material material) {
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
    }

    @Override
    public boolean canConnectRedstone(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return LOGIC_BLOCK_BB;
    }


    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        //Copy this and put it in every single logic block with a changed tileentity type
        TileEntityBluLogicBlock tileEntity = (TileEntityBluLogicBlock)world.getTileEntity(pos);
        try{
            return state.withProperty(POWER, tileEntity.getOutputSignal());
        } catch(NullPointerException e){
            return state;
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBluLogicBlock();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setTileEntity(pos, this.createTileEntity(worldIn, state));
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
       return new BlockStateContainer(this, new IProperty[] {FACING, POWER});
    }

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

    //Make this work
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

    protected int getDelay(IBlockState state) {
        return 1;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }


    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return (downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
    }

    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
    }
    /*

    On for now because it might not be the cause of a lot of bugs and also entirely not needed
    */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
            boolean flag = this.shouldBePowered(worldIn, pos, state);

            if (state.getValue(POWER) > 0 && !flag)
            {
                worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
            }
            else if (state.getValue(POWER) == 0)
            {
                worldIn.setBlockState(pos, this.getPoweredState(state), 2);

                if (!flag)
                {
                    worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                }
            }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
    {
        return this.calculateInputStrength(worldIn, pos, state) > 0;
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        int i = worldIn.getRedstonePower(blockpos, enumfacing);

        if (i >= 15)
        {
            return i;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }

    protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        EnumFacing enumfacing1 = enumfacing.rotateY();
        EnumFacing enumfacing2 = enumfacing.rotateYCCW();
        return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
    }

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
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (this.shouldBePowered(worldIn, pos, state))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
        EnumFacing input1 = (EnumFacing) state.getValue(FACING).rotateY();
        EnumFacing input2 = (EnumFacing) state.getValue(FACING).rotateYCCW();
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.of(input1.getOpposite(), input2.getOpposite()), false).isCanceled())
            return;
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
    }

    @Override
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

    public int nonComplementNot(int beforeNot){
        int afterNot = 0;
        for(int i = BIT_SIZE-1; i >= 0; i--){
            if(((double)beforeNot)/Math.pow(2, i) > 1.0){
                beforeNot -= i;
            } else {
                afterNot += i;
            }
        }
        return afterNot;
    }
}
