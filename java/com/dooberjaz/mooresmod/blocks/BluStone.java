package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluLogicBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluOrGateBlock;
import com.dooberjaz.mooresmod.blocks.tileEntities.TileEntityBluStone;
import com.dooberjaz.mooresmod.init.ModBlocks;
import com.dooberjaz.mooresmod.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.dooberjaz.mooresmod.blocks.BluLogicBlock.FACING;
import static com.dooberjaz.mooresmod.util.Reference.CONST_FACING;
import static com.dooberjaz.mooresmod.util.Reference.CONST_POWER;

public class BluStone extends Block {
    //Id love to extend redstone wire so that this works perfectly with redstone, but the
    //PropertyEnum is private and it causes lots of issues upon block registration
    //So this is redstone wire in every way, except the power value doesn't decay with each block placed
    //This means that the value given to the redstone at the start is the same as the value at the end
    //No matter how long the wire
    //This means that an integer value can be transferred between blocks
    //This integer value (aka power) can be a representation of bits (base redstone has power from 0-15 or 4 bits)
    public static final PropertyEnum<BluStone.EnumAttachPosition> NORTH = PropertyEnum.<BluStone.EnumAttachPosition>create("north", BluStone.EnumAttachPosition.class);
    public static final PropertyEnum<BluStone.EnumAttachPosition> EAST = PropertyEnum.<BluStone.EnumAttachPosition>create("east", BluStone.EnumAttachPosition.class);
    public static final PropertyEnum<BluStone.EnumAttachPosition> SOUTH = PropertyEnum.<BluStone.EnumAttachPosition>create("south", BluStone.EnumAttachPosition.class);
    public static final PropertyEnum<BluStone.EnumAttachPosition> WEST = PropertyEnum.<BluStone.EnumAttachPosition>create("west", BluStone.EnumAttachPosition.class);
    public static final PropertyInteger POWER = CONST_POWER;

    //Backuped version reloaded tor eset a lot of changes due to whole startegy change
    //Blustone now will have a facing value and will only update the blustone it is facing towards
    //Maybe also check if another blustone's facing is towards it to make absolute sure no
    //Infinite loops occur?

    public static final PropertyDirection FACING = CONST_FACING;

    protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BluStone(String name, Material material){
        super(material);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.REDSTONE);
        this.hasTileEntity = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BluStone.EnumAttachPosition.NONE).withProperty(EAST, BluStone.EnumAttachPosition.NONE).withProperty(SOUTH, BluStone.EnumAttachPosition.NONE).withProperty(WEST, BluStone.EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)).withProperty(FACING, EnumFacing.NORTH));

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
    }

    private TileEntityBluStone getTileEntity(World world, BlockPos pos) {
        return (TileEntityBluStone) world.getTileEntity(pos);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBluStone();
    }

    private static int getAABBIndex(IBlockState state)
    {
        int i = 0;
        boolean flag = state.getValue(NORTH) != BluStone.EnumAttachPosition.NONE;
        boolean flag1 = state.getValue(EAST) != BluStone.EnumAttachPosition.NONE;
        boolean flag2 = state.getValue(SOUTH) != BluStone.EnumAttachPosition.NONE;
        boolean flag3 = state.getValue(WEST) != BluStone.EnumAttachPosition.NONE;

        if (flag || flag2 && !flag && !flag1 && !flag3)
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (flag1 || flag3 && !flag && !flag1 && !flag2)
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (flag2 || flag && !flag1 && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (flag3 || flag1 && !flag && !flag2 && !flag3)
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getActualState(state, world, pos);
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }


    private BluStone.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));

        if (!canConnectTo(worldIn.getBlockState(blockpos), direction, worldIn, blockpos) && (iblockstate.isNormalCube() || !canConnectUpwardsTo(worldIn, blockpos.down())))
        {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

            if (!iblockstate1.isNormalCube())
            {
                boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, EnumFacing.UP) || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

                if (flag && canConnectUpwardsTo(worldIn, blockpos.up()))
                {
                    if (iblockstate.isBlockNormalCube())
                    {
                        return BluStone.EnumAttachPosition.UP;
                    }

                    return BluStone.EnumAttachPosition.SIDE;
                }
            }

            return BluStone.EnumAttachPosition.NONE;
        }
        else
        {
            return BluStone.EnumAttachPosition.SIDE;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
        }

        return state;
    }

    protected IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState iblockstate = state;
        int i = (Integer) state.getValue(POWER);
        int j = 0;
        TileEntity tileEntity = worldIn.getTileEntity(pos1);
        if (tileEntity instanceof TileEntityBluStone)
        {
            TileEntityBluStone tileEnt = (TileEntityBluStone)tileEntity;
            j = tileEnt.getPower();
            tileEnt.setPower(i);
        }

        this.canProvidePower = false;
        int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;

        if (k > 0 && k > j - 1)
        {
            j = k;
        }

        int l = 0;

        //do more here

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (state.getValue(FACING) != enumfacing) {
                BlockPos blockpos = pos1.offset(enumfacing);
                boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

                if (flag) {
                    l = this.getMaxCurrentStrength(worldIn, blockpos, l, enumfacing);
                }

                if (worldIn.getBlockState(blockpos).isNormalCube() && !worldIn.getBlockState(pos1.up()).isNormalCube()) {
                    if (flag && pos1.getY() >= pos2.getY()) {
                        l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l, enumfacing);
                    }
                } else if (!worldIn.getBlockState(blockpos).isNormalCube() && flag && pos1.getY() <= pos2.getY()) {
                    l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l, enumfacing);
                }
            }
        }

        if (l > j)
        {
            j = l;
        }
        else if (j > 0)
        {
        }
        else
        {
            j = 0;
        }

        if (k > j)
        {
            j = k;
        }

        if (i != j)
        {
            state = state.withProperty(POWER, Integer.valueOf(j));

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
                //tile entity issues due to casting as it casts in the native getTileEntity code?

                if (tileEntity instanceof TileEntityBluStone)
                {
                    TileEntityBluStone tileEnt = (TileEntityBluStone)tileEntity;
                    tileEnt.setPower(j);
                }
            }

            this.blocksNeedingUpdate.add(pos1);

            for (EnumFacing enumfacing1 : EnumFacing.values())
            {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
                Block block = worldIn.getBlockState(pos1.offset(enumfacing1)).getBlock();
                if(isConnectedToSource(block)){
                    BluLogicBlock block2 = (BluLogicBlock) block;
                    block2.calculateInputStrength(worldIn, pos1.offset(enumfacing1), block2.getDefaultState());
                }
            }
            this.updateSurroundingRedstone(worldIn, pos1, state);
        }

        return state;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
                Block block = worldIn.getBlockState(pos.offset(enumfacing)).getBlock();
                if(isConnectedToSource(block)){
                    // fuck. Update tick wont work so probably have to somehow make it a neighbor? Blustone is so it must be possible.
                    //block.updateTick(worldIn, pos, block.getDefaultState(), 1);
                }
            }
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setTileEntity(pos, this.createTileEntity(worldIn, state));
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }


    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength, EnumFacing enumFacing)
    {
        //rewrite this
        Block block = worldIn.getBlockState(pos).getBlock();
        if (isConnectedToSource(block)) {
            BluLogicBlock x = (BluLogicBlock) block;
            EnumFacing facing = worldIn.getBlockState(pos).getValue(FACING);
            if(facing == enumFacing) {
                return strength;
            } else {
                return worldIn.getBlockState(pos).getValue(POWER);
            }
        } else if(isConnectedToVanillaSource(block)) {
            return 15;
        } else if(block == ModBlocks.POWER_GENERATOR) {
            return worldIn.getBlockState(pos).getValue(POWER);
        } else if (block == ModBlocks.BLUSTONE) {
            EnumFacing facing = worldIn.getBlockState(pos).getValue(FACING);
            if(enumFacing!= null) {
                if (facing == enumFacing.rotateY().rotateY()) {
                    return worldIn.getBlockState(pos).getValue(POWER);
                } else {
                    return strength;
                }
            } else {
                return strength;
            }
        } else {
            return strength;
        }
    }


    //rename to "isSource?"
    private boolean isConnectedToSource(Block block){
        if ((block == ModBlocks.BLU_AND_GATE) || (block == ModBlocks.BLU_OR_GATE) || (block == ModBlocks.BLU_NOR_GATE)
                || (block == ModBlocks.BLU_XOR_GATE) || (block == ModBlocks.BLU_XNOR_GATE)
                || (block == ModBlocks.BLU_NAND_GATE) || (block == ModBlocks.BLU_NOT_GATE)
                || (block == ModBlocks.BLU_ADDER) || (block == ModBlocks.BLU_SUBTRACTOR)
                || (block == ModBlocks.BLU_PRODUCT) || (block == ModBlocks.BLU_DIVISION)
                || (block == ModBlocks.BLU_ALU) || (block == ModBlocks.BLU_MULTIPLEXOR)
                || (block == ModBlocks.COUNTER)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isConnectedToVanillaSource(Block block){
        if ((block == Blocks.REDSTONE_TORCH) || (block == Blocks.REDSTONE_BLOCK)){
            return true;
        } else {
            return false;
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            if (this.canPlaceBlockAt(worldIn, pos))
            {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.BLUSTONE);
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int i = ((Integer)blockState.getValue(POWER)).intValue();

            if (i == 0)
            {
                return 0;
            }
            else if (side == EnumFacing.UP)
            {
                return i;
            }
            else
            {
                EnumSet<EnumFacing> enumset = EnumSet.<EnumFacing>noneOf(EnumFacing.class);

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    if(enumfacing != blockState.getValue(FACING)) {
                        if (this.isPowerSourceAt(blockAccess, pos, enumfacing)) {
                            enumset.add(enumfacing);
                        }
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty())
                {
                    return i;
                }
                else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
                {
                    return i;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();

        if (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()))
        {
            return true;
        }
        else if (canConnectTo(iblockstate, side, worldIn, pos))
        {
            return true;
        }
        else if (iblockstate.getBlock() == Blocks.POWERED_REPEATER && iblockstate.getValue(BlockRedstoneDiode.FACING) == side)
        {
            return true;
        }
        else
        {
            return !flag && canConnectUpwardsTo(worldIn, blockpos.down());
        }
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
    {
        return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
    }

    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos)
    {
        Block block = blockState.getBlock();

        if (block == Blocks.REDSTONE_WIRE)
        {
            return true;
        }
        else if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState))
        {
            EnumFacing enumfacing = (EnumFacing)blockState.getValue(BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }
        else if (Blocks.OBSERVER == blockState.getBlock())
        {
            return side == blockState.getValue(BlockObserver.FACING);
        }
        else
        {
            return blockState.getBlock().canConnectRedstone(blockState, world, pos, side);
        }
    }

    public boolean canProvidePower(IBlockState state)
    {
        return this.canProvidePower;
    }

    @SideOnly(Side.CLIENT)
    public static int colorMultiplier(int p_176337_0_)
    {
        float f = (float)p_176337_0_ / 15.0F;
        float f1 = f * 0.6F + 0.4F;

        if (p_176337_0_ == 0)
        {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        int i = ((Integer)stateIn.getValue(POWER)).intValue();

        if (i != 0)
        {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.BLUSTONE));
    }

    public IBlockState getStateFromMeta(int meta)
    {
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state)
    {
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

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, POWER, FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    protected static enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        private EnumAttachPosition(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this.name;
        }
    }
}
