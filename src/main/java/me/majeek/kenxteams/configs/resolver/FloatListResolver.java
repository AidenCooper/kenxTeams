package me.majeek.kenxteams.configs.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FloatListResolver implements Resolver<List<Float>> {
    @Override
    public List<Float> resolve(Object value) {
        return new ArrayList<>((Collection<Float>) value);
    }
}
