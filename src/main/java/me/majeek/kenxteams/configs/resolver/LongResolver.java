package me.majeek.kenxteams.configs.resolver;

public class LongResolver implements Resolver<Long> {
    @Override
    public Long resolve(Object value) {
        return Long.valueOf(value.toString());
    }
}
