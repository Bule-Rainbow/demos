package com.jwt.demo.util.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author jiangwentao
 * @description
 * @createTime 2022年11月18日 14:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BeanTestVO {
    private String id;

    private String name;
}
