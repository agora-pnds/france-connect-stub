package fr.gouv.franceconnect.stub;

import java.util.HashMap;
import java.util.Map;

class StubCache {

    private static final Map<String, String> cache = new HashMap<>();

    private StubCache() {
    }

    static Map<String, String> getInstance() {
        return cache;
   }
}
