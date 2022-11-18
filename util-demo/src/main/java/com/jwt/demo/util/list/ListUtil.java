package com.jwt.demo.util.list;

import com.jwt.demo.util.bean.vo.BeanTestVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className ListUtil.java
 * @description 集合工具类
 * @createTime 2022年06月13日 13:42
 */
public class ListUtil {

    public static void main(String[] args) {

        double[][] doubles = linearScatters();
        //二维数组输出
        Stream.of(doubles).forEach(t -> System.out.println(Arrays.toString(t)));


        List<String> list1 = new ArrayList<>();
        list1.add("11");
        list1.add("22");
        list1.add("22");
        List<String> list2 = new ArrayList<>();
        list2.add("22");
        list2.add("22");
        list2.add("33");

        //交集： 取两个集合相同元素保存在list1
        list1.retainAll(list2);

        //并集：取两个集合的所有元素
        list1.addAll(list2);

        //差集：取list1中有而list2中没有的元素
        list1.removeAll(list2);

        //去重，stream去重同时保证原有顺序
        list1 = list1.stream().distinct().collect(Collectors.toList());
        list1.forEach(System.out::println);

        List<BeanTestVO> list3 = new ArrayList<>();
        list3.add(new BeanTestVO("111", "111"));
        list3.add(new BeanTestVO("222", "bbb"));
        list3.add(new BeanTestVO("111", "ccc"));
        //生成 id-object的map，t->t 表示取对象本身，(n1, n2) -> n2 表示n1,n2重复则取n2
        Map<String, BeanTestVO> map = list3.stream().collect(Collectors.toMap(BeanTestVO::getId, t -> t, (n1, n2) -> n2));
    }








    public static double[][] linearScatters() {
        List<double[]> data = new ArrayList<>();
        for (double x = 0; x <= 10; x += 0.1) {
            double y = 1.5 * x + 0.5;
            // 随机数
            //y += Math.random() * 4 - 2;
            double[] xy = {x, y};
            data.add(xy);
        }
        return data.stream().toArray(double[][]::new);
    }

    /**
     * 计算平均值
     *
     * @param data double类型数据
     * @return 平均值
     */
    private static double getAverage(List<Double> data) {
        return data.stream().mapToDouble(Double::new).average().orElse(0);
    }

    /**
     * 判断集合是否有重复元素
     *
     * @param list 集合
     * @param <T>  类型
     * @return
     */
    public static <T> boolean checkAllEqual(List<T> list) {
        if (list.size() == 1) {
            return true;
        }
        List<T> newList = list.stream().distinct().collect(Collectors.toList());
        return newList.size() == 1;
    }
}
