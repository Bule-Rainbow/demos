package com.jwt.demo.util.sort;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author jiangwentao
 * @version 2.0.0
 * @className SortUtil.java
 * @description 排序
 * @createTime 2022年06月13日 10:45
 */
public class SortUtil {

    public static void main(String[] args) {

//        // map按key（数字类型）排序
//        Map<String, String> map = new HashMap<>(5);
//        map.put("11", "cc");
//        map.put("1", "aa");
//        map.put("22", "ee");
//        map.put("2", "bb");
//        map.put("12", "dd");
//        Map<String, String> sortMap = sortMapByKey(map);
//        System.out.println("排序后：");
//        Set<String> keySet = sortMap.keySet();
//        keySet.forEach(k -> System.out.println(sortMap.get(k)));

//        // 数字字符串集合排序
//        List<String> strList = new ArrayList<>();
//        strList.add("11");
//        strList.add("1");
//        strList.add("10");
//        strList.add("12");
//        strList.add("2");
//        strList.sort(Comparator.comparing(Integer::new));
//        System.out.println("排序后：");
//        strList.forEach(System.out::println);

//        // 包含数字字符串属性的对象集合排序
//        List<SortTestVO> voList = new ArrayList<>();
//        voList.add(new SortTestVO("11", "ccc"));
//        voList.add(new SortTestVO("1", "aaa"));
//        voList.add(new SortTestVO("12", "ddd"));
//        voList.add(new SortTestVO("2", "bbb"));
//        voList.sort(Comparator.comparing(a -> new Integer(a.getId())));
//        System.out.println("排序后：");
//        voList.forEach(System.out::println);
    }

    /**
     * map按key（数字类型）排序
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (CollectionUtil.isEmpty(map)) {
            return map;
        }
        //转成list进行排序
        List<Map.Entry<String, String>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Comparator.comparing(a -> new Integer(a.getKey())));
        //转成map(LinkedHashMap保证有序)
        Map<String, String> resultMap = new LinkedHashMap<>();
        entryList.forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));
        return resultMap;
    }
}
