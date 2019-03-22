package com.samalex.slucapstone;

import java.util.HashMap;
import java.util.Map;

public class InterventionMap {
    private Map<Integer, InterventionDisplayData> map;

    public InterventionMap() {
        this.map = new HashMap<Integer, InterventionDisplayData>();
    }

    public InterventionDisplayData get(int key) {
        return this.map.get(key);
    }

    public void put(int key, InterventionDisplayData value) {
        this.map.put(key, value);
    }
}
