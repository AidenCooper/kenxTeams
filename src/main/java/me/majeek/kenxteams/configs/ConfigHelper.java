package me.majeek.kenxteams.configs;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import me.majeek.kenxteams.configs.resolver.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ConfigHelper {
    private static final Map<Type, Resolver<?>> resolverMap = Maps.newConcurrentMap();
    private static final Table<Class<?>, Class<?>, Resolver<?>> resolverTable = HashBasedTable.create();

    static {
        resolverMap.put(String.class, new StringResolver());
        resolverMap.put(Boolean.class, new BooleanResolver());
        resolverMap.put(boolean.class, new BooleanResolver());
        resolverMap.put(Integer.class, new IntegerResolver());
        resolverMap.put(int.class, new IntegerResolver());
        resolverMap.put(Long.class, new LongResolver());
        resolverMap.put(long.class, new LongResolver());
        resolverTable.put(List.class, String.class, new StringListResolver());
        resolverTable.put(List.class, Boolean.class, new BooleanListResolver());
        resolverTable.put(List.class, Integer.class, new IntegerListResolver());
        resolverTable.put(List.class, Long.class, new LongListResolver());
        resolverTable.put(List.class, Float.class, new FloatListResolver());
    }

    public static Map<Type, Resolver<?>> getResolverMap() {
        return resolverMap;
    }

    public static Table<Class<?>, Class<?>, Resolver<?>> getResolverTable() {
        return resolverTable;
    }
}
