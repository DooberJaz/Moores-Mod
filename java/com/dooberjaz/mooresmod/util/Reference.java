package com.dooberjaz.mooresmod.util;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;

public class Reference {
    public static final String MOD_ID = "mooresmod";
    public static final String NAME = "Moores Mod";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSIONS = "(1.12.2)";
    public static final String CLIENT_PROXY_CLASS = "com.dooberjaz.mooresmod.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.dooberjaz.mooresmod.proxy.CommonProxy";


    // It should be set up that changing this variable will change the allowed size of all necessary (blu) blocks
    public static final int BIT_SIZE = 4;

    public static final PropertyDirection CONST_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger CONST_POWER = PropertyInteger.create("power", 0, (int) (Math.pow(2, BIT_SIZE) - 1));
}
