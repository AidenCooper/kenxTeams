package me.majeek.kenxteams.configs.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BooleanListResolver implements Resolver<List<Boolean>> {
    @Override
    public List<Boolean> resolve(Object value) {
        return new ArrayList<>((Collection<Boolean>) value);
    }
}
