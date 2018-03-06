package com.eloancn.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 映射utils
 * Created by qinxf on 2018/1/18.
 */
public class ReflectUtils {
    private static final Logger logger = Logger.getLogger(ReflectUtils.class);
    private static final Map<String, Class> typeMap = new HashMap() {
        {
            this.put("int", Integer.class);
            this.put("long", Long.class);
            this.put("double", Double.class);
            this.put("float", Float.class);
            this.put("boolean", Boolean.class);
            this.put("char", Character.class);
            this.put("byte", Byte.class);
            this.put("short", Short.class);
        }
    };

    public ReflectUtils() {
    }

    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[0], new Object[0]);
    }

    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, (Class)null);
    }

    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class type = propertyType != null?propertyType:value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if(field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            Object result = null;

            try {
                result = field.get(obj);
            } catch (IllegalAccessException var5) {
                logger.error("error:", var5);
            }

            return result;
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if(field != null) {
            if(!field.getType().isAssignableFrom(value.getClass())) {
                value = getSimpleObjectValue(value, field.getType());
            }

            if(null != value) {
                try {
                    field.set(obj, value);
                } catch (IllegalAccessException var5) {
                    logger.error("error:", var5);
                }
            }
        }

    }

    public static Field getAccessibleField(Object obj, String fieldName) {
        Assert.notNull(obj, "object not null");
        Assert.hasText(fieldName, "fieldName");
        HashSet names = new HashSet();

        for(Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field[] e = superClass.getDeclaredFields();
                int len$ = e.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Field df = e[i$];
                    names.add(df.getName());
                }

                if(names.contains(fieldName)) {
                    Field var9 = superClass.getDeclaredField(fieldName);
                    var9.setAccessible(true);
                    return var9;
                }
            } catch (NoSuchFieldException var8) {
                throw new RuntimeException(var8);
            }
        }

        return null;
    }

    public static Object getSimpleObjectValue(Object oldValue, Class targeType) {
        try {
            return Class.forName(((Class)typeMap.get(targeType.getSimpleName())).getName()).getConstructor(new Class[]{String.class}).newInstance(new Object[]{oldValue.toString()});
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Set<Field> getAccessibleField(Object obj) {
        Assert.notNull(obj, "object not null");
        HashSet names = new HashSet();

        for(Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] arr$ = superClass.getDeclaredFields();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Field df = arr$[i$];
                names.add(df);
            }
        }

        return names;
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if(method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    public static Method getAccessibleMethod(Object obj, String methodName, Class... parameterTypes) {
        Assert.notNull(obj, "object not null");
        Class superClass = obj.getClass();
        if(superClass != Object.class) {
            try {
                Method e = superClass.getDeclaredMethod(methodName, parameterTypes);
                e.setAccessible(true);
                return e;
            } catch (NoSuchMethodException var5) {
                throw new RuntimeException(var5);
            }
        } else {
            return null;
        }
    }

    public static <T> Class<T> getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if(!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if(index < params.length && index >= 0) {
                if(!(params[index] instanceof Class)) {
                    logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "\'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return (RuntimeException)(!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)?(e instanceof InvocationTargetException ?new RuntimeException("Reflection Exception.", ((InvocationTargetException)e).getTargetException()):(e instanceof RuntimeException?(RuntimeException)e:new RuntimeException("Unexpected Checked Exception.", e))):new IllegalArgumentException("Reflection Exception.", e));
    }

    public static String getErrorMsg(Throwable error) {
        PrintWriter out = null;

        String var3;
        try {
            StringWriter e = new StringWriter();
            out = new PrintWriter(e);
            error.printStackTrace(out);
            var3 = "\r\n" + e.toString() + "\r\n";
            return var3;
        } catch (Exception var7) {
            var3 = "bad getErrorMap";
        } finally {
            if(null != out) {
                out.close();
            }

        }

        return var3;
    }
}
