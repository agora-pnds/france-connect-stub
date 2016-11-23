package fr.gouv.franceconnect.stub;

import java.util.HashMap;
import java.util.Map;

public class CacheApplicatif {
    
    private static final Map<String, String> cache = new HashMap<String, String>();
    
    private CacheApplicatif(){        
    }    
    
    public static final Map<String, String> getInstance(){
        return cache;
   }
}
