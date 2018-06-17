package net.upd4ting.gameapi.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class Reflector 
{
    public static Class<?> getCraftClass(final String ClassName) {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf(46) + 1) + ".";
        final String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Reflection failed for getCraftClass > " + className);
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }
        return c;
    }
    
    public static Field getField(final Class<?> cl, final String fieldName) {
        try {
            return cl.getDeclaredField(fieldName);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getField classe > " + cl.getClass().getName() + " field > " + fieldName);
            Bukkit.getServer().shutdown();
            return null;
        }
    }
    
    public static void setField(final Object obj, final String field, final Object value) {
        try {
            final Field maxUsesField = obj.getClass().getDeclaredField(field);
            maxUsesField.setAccessible(true);
            maxUsesField.set(obj, value);
            maxUsesField.setAccessible(!maxUsesField.isAccessible());
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for changeField " + obj.getClass().getName() + " field > " + field + " value > " + value);
            Bukkit.getServer().shutdown();
        }
    }
    
    public static Method getMethod(final Class<?> cl, final String method, final Class<?>... args) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }
    
    public static boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
    
    public static Method getMethod(final Class<?> cl, final String method) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
    
    public static Object getHandle(final Entity entity) {
        try {
            return getMethod(entity.getClass(), "getHandle").invoke(entity);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getHandle Entity > " + entity.getType().toString());
            Bukkit.getServer().shutdown();
            return null;
        }
    }
    
    public static Object getHandle(final World world) {
        try {
            return getMethod(world.getClass(), "getHandle").invoke(world);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getHandle World > " + world.getName());
            Bukkit.getServer().shutdown();
            return null;
        }
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static Field getFieldFromAll(Class clazz, String field) {
        List<Field> fields = new ArrayList<>();
        Reflector.getAllFields(fields, clazz);

        for (Field f : fields)
            if (f.getName().equals(field))
                return f;
        return null;
    }
    
    public static void setFieldFinalModifiable(final Field nameField) throws Exception {
        nameField.setAccessible(true);
        int modifiers = nameField.getModifiers();
        final Field modifierField = nameField.getClass().getDeclaredField("modifiers");
        modifiers &= 0xFFFFFFEF;
        modifierField.setAccessible(true);
        modifierField.setInt(nameField, modifiers);
    }
}