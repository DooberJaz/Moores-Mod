package com.dooberjaz.mooresmod.util.recipe.CodeChickenLibWorkaround;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.objectweb.asm.Type;

public class ReflectionManager {
    private static Field modifiersField;

    public ReflectionManager() {
    }

    /** @deprecated */
    @Deprecated
    public static boolean isStatic(int modifiers) {
        return (modifiers & 8) != 0;
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static Class<?> findClass(ObfMapping mapping, boolean init) {
        try {
            return Class.forName(mapping.javaClass(), init, ReflectionManager.class.getClassLoader());
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static Class<?> findClass(ObfMapping mapping) {
        return findClass(mapping, true);
    }

    public static boolean classExists(ObfMapping mapping) {
        return findClass(mapping, false) != null;
    }

    public static Class<?> findClass(String name) {
        return findClass(new ObfMapping(name.replace(".", "/")), true);
    }

    public static void setField(ObfMapping mapping, Object instance, Object value) {
        try {
            Field field = getField(mapping);
            field.setAccessible(true);
            removeFinal(field);
            field.set(instance, value);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <R> R callMethod(ObfMapping mapping, Class<R> returnType, Object instance, Object... params) {
        try {
            return callMethod_Unsafe(mapping, returnType, instance, params);
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    public static <R> R callMethod_Unsafe(ObfMapping mapping, Class<R> returnType, Object instance, Object... params) throws InvocationTargetException, IllegalAccessException {
        mapping.toRuntime();
        Class<?> clazz = findClass(mapping);
        Method method = null;
        Method[] var6 = clazz.getDeclaredMethods();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Method m = var6[var8];
            if (m.getName().equals(mapping.s_name) && Type.getMethodDescriptor(m).equals(mapping.s_desc)) {
                method = m;
                break;
            }
        }

        if (method != null) {
            method.setAccessible(true);
            return (R) method.invoke(instance, params);
        } else {
            return null;
        }
    }

    public static <R> R newInstance(ObfMapping mapping, Class<R> returnType, Object... params) {
        try {
            return newInstance_Unsafe(mapping, returnType, params);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <R> R newInstance_Unsafe(ObfMapping mapping, Class<R> returnType, Object... params) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = findClass(mapping);
        Constructor<?> constructor = null;
        Constructor[] var5 = clazz.getDeclaredConstructors();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Constructor<?> c = var5[var7];
            if (Type.getConstructorDescriptor(c).equals(mapping.s_desc)) {
                constructor = c;
                break;
            }
        }

        if (constructor != null) {
            constructor.setAccessible(true);
            return (R) constructor.newInstance(params);
        } else {
            return null;
        }
    }

    public static boolean hasField(ObfMapping mapping) {
        try {
            getField_Unsafe(mapping);
            return true;
        } catch (NoSuchFieldException var2) {
            return false;
        }
    }

    public static <R> R getField(ObfMapping mapping, Object instance, Class<R> clazz) {
        try {
            Field field = getField(mapping);
            return (R) field.get(instance);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static Field getField(ObfMapping mapping) {
        mapping.toRuntime();

        try {
            return getField_Unsafe(mapping);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static Field getField_Unsafe(ObfMapping mapping) throws NoSuchFieldException {
        mapping.toRuntime();
        Class<?> clazz = findClass(mapping);
        Field field = clazz.getDeclaredField(mapping.s_name);
        field.setAccessible(true);
        removeFinal(field);
        return field;
    }

    public static void removeFinal(Field field) {
        if ((field.getModifiers() & 16) != 0) {
            try {
                if (modifiersField == null) {
                    modifiersField = getField(new ObfMapping("java/lang/reflect/Field", "modifiers"));
                    modifiersField.setAccessible(true);
                }

                modifiersField.set(field, field.getModifiers() & -17);
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }
    }
}