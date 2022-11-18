package com.jwt.demo.util.bean;

import com.jwt.demo.util.bean.vo.BeanTestVO;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 对象操作工具类
 * @author: jiangwentao
 * @email: jiangwentao@whty.com.cn
 * @date: 2022/1/17 9:15
 */

public class BeanUtil extends BeanUtilsBean {

    public static void main(String[] args) {
        BeanTestVO vo1 = new BeanTestVO();
        vo1.setId("111");
        vo1.setName(null);

        BeanTestVO vo2 = new BeanTestVO();
        vo2.setId("222");
        vo2.setName("bbb");

        //将s1各属性的值复制给s2,并忽略掉为null的
        BeanUtils.copyProperties(vo1, vo2, getNullPropertyNames(vo1));
        System.out.println("复制后：");
        System.out.println("s2 ：" + vo2.toString());
    }

    /**
     * 获取对象中属性值为null的属性名称
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNameSet = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNameSet.add(pd.getName());
            }
        }
        String[] result = new String[emptyNameSet.size()];
        return emptyNameSet.toArray(result);
    }

    /**
     * 复制src属性值到target对象属性，忽略NULL值
     *
     * @param src    源对象bean
     * @param target 目标对象bean
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
