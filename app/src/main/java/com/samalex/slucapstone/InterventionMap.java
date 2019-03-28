package com.samalex.slucapstone;

import java.util.HashMap;
import java.util.Map;

public class InterventionMap {
    private Map<Integer, InterventionDisplayData> map;

    public InterventionMap() {
        this.map = new HashMap<Integer, InterventionDisplayData>();
    }

    public InterventionDisplayData get(int key) {
        if(this.map.get(key) == null) {
            // when the current cycle exceeds NUM_CYCLE value, will reset the key to start a new round.
            int newKey = key % (int) BoozymeterApplication.CYCLE_LENGTH;
            return this.map.get(newKey);
        }
        return this.map.get(key);
    }

    public void put(int key, InterventionDisplayData value) {
        this.map.put(key, value);
    }
}
