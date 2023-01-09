package com.jwt.demo.util.cache;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiangwentao
 * @description
 * @createTime 2022年12月20日 10:27
 */
@Data
@Accessors(chain = true)
public class CarVO {
    private String id;
    private String name;
}
