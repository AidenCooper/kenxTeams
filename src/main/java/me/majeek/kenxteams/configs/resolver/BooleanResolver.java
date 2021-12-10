package me.majeek.kenxteams.configs.resolver;

public class BooleanResolver implements Resolver<Boolean> {
    @Override
    public Boolean resolve(Object value) {
        return Boolean.valueOf(value.toString());
    }
}
