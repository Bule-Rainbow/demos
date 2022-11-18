package com.jwt.demo.util.excel;

import lombok.Data;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className DynamicHeader.java
 * @description
 * @createTime 2022年09月05日 9:52
 */
@Data
public class DynamicHeader {
    /**
     * 占用列数
     */
    private Integer occupiedColumn;

    /**
     * 内容
     */
    private String content;

    public DynamicHeader(Integer occupiedColumn, String content) {
        this.occupiedColumn = occupiedColumn;
        this.content = content;
    }
}
