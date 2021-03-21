package com.dooberjaz.mooresmod.blocks;

import com.dooberjaz.mooresmod.util.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.dooberjaz.mooresmod.util.Reference.BIT_SIZE;
import static com.dooberjaz.mooresmod.util.Reference.CONST_POWER;

public class PowerGeneratorBlock extends BlockBase {
    public static final PropertyInteger POWER = CONST_POWER;

    public PowerGeneratorBlock(String name, Material material) {
        super(name, material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        state = state.withProperty(POWER,  (state.getValue(POWER) + 1) % (int) (Math.pow(2, BIT_SIZE) - 1));
        worldIn.setBlockState(pos, state);
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        //CHANGE ME WHEN POWER GETS STRONKER
        int power = state.getValue(POWER);

        return power;
    }
}
