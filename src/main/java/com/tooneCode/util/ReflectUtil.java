package com.tooneCode.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectUtil {
    private static Map<String, Class<?>> classMap = new ConcurrentHashMap();
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap();
    private static final Map<Class<?>, Method[]> declaredMethodCache = new ConcurrentHashMap();
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

    public ReflectUtil() {
    }

    public static <T> T getStaticField(Class<?> targetClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get((Object) null);
    }

    public static <T> T getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> target = (Class) classMap.get(className);
        if (target == null) {
            target = Class.forName(className);
            classMap.put(className, target);
        }

        return target;
    }

    public static List<Field> getAllFields(Class clazz) {
        List<Field> result = new ArrayList();
        Class<?> targetClass = clazz;

        do {
            Field[] fields = getDeclaredFields(targetClass);
            if (fields != null && fields.length > 0) {
                result.addAll(Arrays.asList(fields));
            }

            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return result;
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Field[] result = (Field[]) declaredFieldsCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, result.length == 0 ? EMPTY_FIELD_ARRAY : result);
            } catch (Throwable var3) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", var3);
            }
        }

        return result;
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {
        Method[] result = (Method[]) declaredMethodCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredMethods();
                declaredMethodCache.put(clazz, result.length == 0 ? EMPTY_METHOD_ARRAY : result);
            } catch (Throwable var3) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", var3);
            }
        }

        return result;
    }

    public static List<Method> getAllMethod(Class<?> clazz) {
        List<Method> result = new ArrayList();
        Class<?> targetClass = clazz;

        do {
            Method[] methods = getDeclaredMethods(targetClass);
            if (methods != null && methods.length > 0) {
                result.addAll(Arrays.asList(methods));
            }

            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return result;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Class<?> targetClass = clazz;

        do {
            Method method = null;

            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var6) {
            }

            if (method != null) {
                return method;
            }

            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return null;
    }
}
