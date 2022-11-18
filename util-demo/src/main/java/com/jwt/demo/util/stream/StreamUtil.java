package com.jwt.demo.util.stream;

import com.jwt.demo.util.bean.vo.BeanTestVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className StreamUtil.java
 * @description stream相关操作util
 * @createTime 2022年06月14日 15:29
 */
public class StreamUtil {
    public static void main(String[] args) {

        List<BeanTestVO> list1 = new ArrayList<>();
        list1.add(new BeanTestVO("111", "aaa"));
        list1.add(new BeanTestVO("222", "bbb"));
        list1.add(new BeanTestVO("333", "ccc"));

        //使用stream转换成id-name的map
        Map<String, String> id2NameMap = list1.stream().collect(Collectors.toMap(BeanTestVO::getId, BeanTestVO::getName));

        //使用stream转换成id-testVO的map
        //Map<String, TestVO> id2ObjectMap = list1.stream().collect(Collectors.toMap(TestVO::getId, t -> t));
        //在上面代码中，map的ID不能重复，如果重复就会报错，下面我们引入个重载方法
        Map<String, BeanTestVO> map = list1.stream().collect(Collectors.toMap(BeanTestVO::getId, t -> t, (n1, n2) -> n2));

        Optional<BeanTestVO> max = list1.stream().max(Comparator.comparing(BeanTestVO::getId));
        System.out.println(max.get().getName());

        Optional<BeanTestVO> min = list1.stream().min(Comparator.comparing(BeanTestVO::getId));
    }
}
