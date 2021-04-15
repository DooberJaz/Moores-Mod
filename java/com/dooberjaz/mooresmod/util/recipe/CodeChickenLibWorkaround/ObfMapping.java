//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dooberjaz.mooresmod.util.recipe.CodeChickenLibWorkaround;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.commons.Remapper;
//So this is some code I borrowed from another mod (codechickenlib) that I couldnt get working in tandem with this for development.
//It is used mostly for parsing of the moores machine json files
public class ObfMapping {
    public static ObfMapping.ObfRemapper obfMapper = new ObfMapping.ObfRemapper();
    public static Remapper mcpMapper = null;
    public static final boolean obfuscated;
    public String s_owner;
    public String s_name;
    public String s_desc;

    public static void loadMCPRemapper() {
        if (mcpMapper == null) {
            mcpMapper = new ObfMapping.MCPRemapper();
        }

    }

    public static void init() {
        if (!obfuscated) {
            loadMCPRemapper();
        }

    }

    public ObfMapping(String owner) {
        this(owner, "", "");
    }

    public ObfMapping(String owner, String name) {
        this(owner, name, "");
    }

    public ObfMapping(String owner, String name, String desc) {
        this.s_owner = owner;
        this.s_name = name;
        this.s_desc = desc;
        if (this.s_owner.contains(".")) {
            throw new IllegalArgumentException(this.s_owner);
        }
    }

    public ObfMapping(ObfMapping descmap, String subclass) {
        this(subclass, descmap.s_name, descmap.s_desc);
    }

    public static ObfMapping fromDesc(String s) {
        int lastDot = s.lastIndexOf(46);
        if (lastDot < 0) {
            return new ObfMapping(s, "", "");
        } else {
            int sep = s.indexOf(40);
            int sep_end = sep;
            if (sep < 0) {
                sep = s.indexOf(32);
                sep_end = sep + 1;
            }

            if (sep < 0) {
                sep = s.indexOf(58);
                sep_end = sep + 1;
            }

            return sep < 0 ? new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "") : new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1, sep), s.substring(sep_end));
        }
    }

    public ObfMapping subclass(String subclass) {
        return new ObfMapping(this, subclass);
    }

    public boolean isClass(String name) {
        return name.replace('.', '/').equals(this.s_owner);
    }

    public boolean matches(String name, String desc) {
        return this.s_name.equals(name) && this.s_desc.equals(desc);
    }

    public String javaClass() {
        return this.s_owner.replace('/', '.');
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ObfMapping)) {
            return false;
        } else {
            ObfMapping desc = (ObfMapping)obj;
            return this.s_owner.equals(desc.s_owner) && this.s_name.equals(desc.s_name) && this.s_desc.equals(desc.s_desc);
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.s_desc, this.s_name, this.s_owner});
    }

    public String toString() {
        if (this.s_name.length() == 0) {
            return "[" + this.s_owner + "]";
        } else {
            return this.s_desc.length() == 0 ? "[" + this.s_owner + "." + this.s_name + "]" : "[" + (this.isMethod() ? this.methodDesc() : this.fieldDesc()) + "]";
        }
    }

    public String methodDesc() {
        return this.s_owner + "." + this.s_name + this.s_desc;
    }

    public String fieldDesc() {
        return this.s_owner + "." + this.s_name + ":" + this.s_desc;
    }

    public boolean isClass() {
        return this.s_name.length() == 0;
    }

    public boolean isMethod() {
        return this.s_desc.contains("(");
    }

    public boolean isField() {
        return !this.isClass() && !this.isMethod();
    }

    public ObfMapping map(Remapper mapper) {
        if (mapper == null) {
            return this;
        } else {
            if (this.isMethod()) {
                this.s_name = mapper.mapMethodName(this.s_owner, this.s_name, this.s_desc);
            } else if (this.isField()) {
                this.s_name = mapper.mapFieldName(this.s_owner, this.s_name, this.s_desc);
            }

            this.s_owner = mapper.mapType(this.s_owner);
            if (this.isMethod()) {
                this.s_desc = mapper.mapMethodDesc(this.s_desc);
            } else if (this.s_desc.length() > 0) {
                this.s_desc = mapper.mapDesc(this.s_desc);
            }

            return this;
        }
    }

    public ObfMapping toRuntime() {
        this.map(mcpMapper);
        return this;
    }

    public ObfMapping toClassloading() {
        if (!obfuscated) {
            this.map(mcpMapper);
        } else if (obfMapper.isObf(this.s_owner)) {
            this.map(obfMapper);
        }

        return this;
    }

    public ObfMapping copy() {
        return new ObfMapping(this.s_owner, this.s_name, this.s_desc);
    }

    static {
        boolean obf = true;

        try {
            obf = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
        } catch (IOException var2) {
        }

        obfuscated = obf;
    }

    public static class MCPRemapper extends Remapper implements LineProcessor<Void> {
        private HashMap<String, String> fields = new HashMap();
        private HashMap<String, String> funcs = new HashMap();

        public static File[] getConfFiles() {
            File notchSrg = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg"));
            File csvDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.csvDir"));
            if (notchSrg.exists() && csvDir.exists()) {
                File fieldCsv = new File(csvDir, "fields.csv");
                File methodCsv = new File(csvDir, "methods.csv");
                if (notchSrg.exists() && fieldCsv.exists() && methodCsv.exists()) {
                    return new File[]{notchSrg, fieldCsv, methodCsv};
                }
            }

            throw new RuntimeException("Failed to grab mappings from GradleStart args.");
        }

        public MCPRemapper() {
            File[] mappings = getConfFiles();

            try {
                Resources.readLines(mappings[1].toURI().toURL(), Charsets.UTF_8, this);
                Resources.readLines(mappings[2].toURI().toURL(), Charsets.UTF_8, this);
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        }

        public String mapMethodName(String owner, String name, String desc) {
            String s = (String)this.funcs.get(name);
            return s == null ? name : s;
        }

        public String mapFieldName(String owner, String name, String desc) {
            String s = (String)this.fields.get(name);
            return s == null ? name : s;
        }

        public boolean processLine(@Nonnull String line) throws IOException {
            int i = line.indexOf(44);
            String srg = line.substring(0, i);
            int i2 = i + 1;
            i = line.indexOf(44, i2);
            String mcp = line.substring(i2, i);
            (srg.startsWith("func") ? this.funcs : this.fields).put(srg, mcp);
            return true;
        }

        public Void getResult() {
            return null;
        }
    }

    public static class ObfRemapper extends Remapper {
        private HashMap<String, String> fields = new HashMap();
        private HashMap<String, String> funcs = new HashMap();

        public ObfRemapper() {
            try {
                Field rawFieldMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawFieldMaps");
                Field rawMethodMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawMethodMaps");
                rawFieldMapsField.setAccessible(true);
                rawMethodMapsField.setAccessible(true);
                Map<String, Map<String, String>> rawFieldMaps = (Map)rawFieldMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
                Map<String, Map<String, String>> rawMethodMaps = (Map)rawMethodMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
                if (rawFieldMaps == null) {
                    throw new IllegalStateException("codechicken.lib.asm.ObfMapping loaded too early. Make sure all references are in or after the asm transformer load stage");
                } else {
                    Iterator var5 = rawFieldMaps.values().iterator();

                    Map map;
                    Iterator var7;
                    Entry entry;
                    while(var5.hasNext()) {
                        map = (Map)var5.next();
                        var7 = map.entrySet().iterator();

                        while(var7.hasNext()) {
                            entry = (Entry)var7.next();
                            if (((String)entry.getValue()).startsWith("field")) {
                                this.fields.put(entry.getValue().toString(), ((String)entry.getKey()).substring(0, ((String)entry.getKey()).indexOf(58)));
                            }
                        }
                    }

                    var5 = rawMethodMaps.values().iterator();

                    while(var5.hasNext()) {
                        map = (Map)var5.next();
                        var7 = map.entrySet().iterator();

                        while(var7.hasNext()) {
                            entry = (Entry)var7.next();
                            if (((String)entry.getValue()).startsWith("func")) {
                                this.funcs.put(entry.getValue().toString(), ((String)entry.getKey()).substring(0, ((String)entry.getKey()).indexOf(40)));
                            }
                        }
                    }

                }
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            }
        }

        public String mapMethodName(String owner, String name, String desc) {
            String s = (String)this.funcs.get(name);
            return s == null ? name : s;
        }

        public String mapFieldName(String owner, String name, String desc) {
            String s = (String)this.fields.get(name);
            return s == null ? name : s;
        }

        public String map(String typeName) {
            return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
        }

        public String unmap(String typeName) {
            return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
        }

        public boolean isObf(String typeName) {
            return !this.map(typeName).equals(typeName) || !this.unmap(typeName).equals(typeName);
        }
    }
}
