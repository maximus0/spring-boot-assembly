package com.xinhuanet.microService.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理类型
 * 
 * 根据文档
 * map1 2 .. 8 竖坐标
 * list1 2 3 横坐标
 * 
 * 返回符合条件的竖坐标。
 * 
 */
public class MethodUtils {
    // [0,2,3,7]-[1]
    private static Map map1 = null;
    // [0,2,3,7]-[11]
    private static Map map2 = null;
    // [0,2,3,7]-[12]
    private static Map map3 = null;
    // [5,6]-[1]
    private static Map map4 = null;
    // [5,6]-[11]
    private static Map map5 = null;
    // [1,4,5,6]-[null]
    private static Map map6 = null;
    // [11,4,5,6]-[null]
    private static Map map7 = null;
    // [4,6]-[7]
    private static Map map8 = null;

    private static List<String> list1 = Lang.list("authEnterprise", "authInstitution", "authMedia");
    private static List<String> list2 = Lang.list("authPersonalRealname");
    private static List<String> list3 = Lang.list("authPersonalIdentity");
    private static Map<Map, List> mapRules = null;

    public static Integer getMethodNum(Map message) {
        String authType = (String) message.get("authType");
        Integer oldState = (Integer) message.get("oldState");
        Integer newState = (Integer) message.get("newState");

        Integer num = 0;
        for (Map<String, String> map : mapRules.keySet()) {
            num = num + 1;
            List<String> vList = mapRules.get(map);
            if (vList.contains(authType)) { // 横坐标
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    String[] ks = key.split(",");
                    for (String k : ks) {
                        // 竖坐标条件
                        if (k.equals(String.valueOf(oldState)) && value.equals(String.valueOf(newState))) {
                            return num;
                        }
                    }
                }
            }
        }

        return null;
    }

    static {
        map1 = new HashMap<String, String>() {
            {
                put("0,2,3,7", "1");
            }
        };
    }

    static {
        map2 = new HashMap<String, String>() {
            {
                put("0,2,3,7", "11");
            }
        };
    }

    static {
        map3 = new HashMap<String, String>() {
            {
                put("0,2,3,7", "12");
            }
        };
    }

    static {
        map4 = new HashMap<String, String>() {
            {
                put("5,6", "1");
            }
        };
    }

    static {
        map5 = new HashMap<String, String>() {
            {
                put("5,6", "11");
            }
        };
    }

    static {
        map6 = new HashMap<String, String>() {
            {
                put("4,6", "7");
                put("1,4,5,6", "null");
            }
        };
    }

    static {
        map7 = new HashMap<String, String>() {
            {
                put("4,6", "7");
                put("11,4,5,6", "null");
            }
        };
    }

    static {
        map8 = new HashMap<String, String>() {
            {
                put("4,6", "7");
            }
        };
    }

    static {
        mapRules = new LinkedHashMap<Map, List>() {
            {
                put(map1, list1);
                put(map2, list2);
                put(map3, list3);
                put(map4, list1);
                put(map5, list2);
                put(map6, list1);
                put(map7, list2);
                put(map8, list3);
            }
        };
    }
}
