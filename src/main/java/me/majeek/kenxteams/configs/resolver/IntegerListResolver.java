package me.majeek.kenxteams.configs.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntegerListResolver implements Resolver<List<Integer>> {
    @Override
    public List<Integer> resolve(Object value) {
        return new ArrayList<>((Collection<Integer>) value);
    }
}
