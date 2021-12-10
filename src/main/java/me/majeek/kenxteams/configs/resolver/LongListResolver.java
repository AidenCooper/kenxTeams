package me.majeek.kenxteams.configs.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LongListResolver implements Resolver<List<Long>> {
    @Override
    public List<Long> resolve(Object value) {
        return new ArrayList<>((Collection<Long>) value);
    }
}
