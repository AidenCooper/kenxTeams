package me.majeek.kenxteams.configs.resolver;

public class StringResolver implements Resolver<String> {
    @Override
    public String resolve(Object value) {
        return value.toString();
    }
}
