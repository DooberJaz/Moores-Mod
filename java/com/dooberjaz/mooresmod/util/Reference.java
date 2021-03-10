package com.dooberjaz.mooresmod.util;

import net.minecraft.block.properties.PropertyInteger;

public class Reference {
    public static final String MOD_ID = "mooresmod";
    public static final String NAME = "Moores Mod";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSIONS = "(1.12.2)";
    public static final String CLIENT_PROXY_CLASS = "com.dooberjaz.mooresmod.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.dooberjaz.mooresmod.proxy.CommonProxy";

    public static final int BIT_SIZE = 4;

    public static final PropertyInteger CONST_POWER = PropertyInteger.create("power", 0, (int) (Math.pow(2, BIT_SIZE) - 1));
}
