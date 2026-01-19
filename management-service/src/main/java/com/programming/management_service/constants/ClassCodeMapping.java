package com.programming.management_service.constants;

import java.util.HashMap;
import java.util.Map;

//format: {year}-{classCode}-{sequence}
public class ClassCodeMapping {
    private static final Map<String, String> CLASS_CODE_MAP = new HashMap<>();
    
    static {
        CLASS_CODE_MAP.put("cỏ non", "CN-0");
        CLASS_CODE_MAP.put("khai tâm 1", "KT-1");
        CLASS_CODE_MAP.put("khai tâm 2", "KT-2");
        
        CLASS_CODE_MAP.put("rước lễ 1", "RL-1");
        CLASS_CODE_MAP.put("rước lễ 2", "RL-2");
        
        CLASS_CODE_MAP.put("thêm sức 1", "TS-1");
        CLASS_CODE_MAP.put("thêm sức 2", "TS-2");
        
        CLASS_CODE_MAP.put("bao đồng 1", "BD-1");
        CLASS_CODE_MAP.put("bao đồng 2", "BD-2");
        
        CLASS_CODE_MAP.put("vào đời 1", "VD-1");
        CLASS_CODE_MAP.put("vào đời 2", "VD-2");
        
        CLASS_CODE_MAP.put("dự trưởng", "DT-0");
    }
    
    //return Class code (vd: "KT-2")
    public static String getClassCode(String className) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        
        String normalizedName = className.trim().toLowerCase();
        
        String classCode = CLASS_CODE_MAP.get(normalizedName);
        
        if (classCode == null) {
            throw new IllegalArgumentException(
                "Unknown class name: " + className + 
                ". Please check ClassCodeMapping for valid class names."
            );
        }
        
        return classCode;
    }

    public static boolean isValidClassName(String className) {
        if (className == null || className.trim().isEmpty()) {
            return false;
        }
        String normalizedName = className.trim().toLowerCase();
        return CLASS_CODE_MAP.containsKey(normalizedName);
    }
    
    public static Map<String, String> getAllMappings() {
        return new HashMap<>(CLASS_CODE_MAP);
    }
}
