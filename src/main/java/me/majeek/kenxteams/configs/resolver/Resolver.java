package me.majeek.kenxteams.configs.resolver;

public interface Resolver<T> {
    T resolve(Object value);
}
