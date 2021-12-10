package me.majeek.kenxteams.configs.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringListResolver implements Resolver<List<String>> {
    @Override
    public List<String> resolve(Object value) {
        return new ArrayList<>((Collection<String>) value);
    }
}
