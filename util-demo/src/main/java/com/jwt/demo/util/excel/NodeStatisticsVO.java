package com.jwt.demo.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className NodeVO.java
 * @description
 * @createTime 2022年09月05日 13:21
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class NodeStatisticsVO {
    /**
     * 节点名称
     */
    @ExcelProperty(value = "节点",index = 1)
    private String nodeName;

    /**
     * 内存总量
     */
    @ExcelProperty(value = "内存总量",index = 2)
    private String memorySum;

    /**
     * 内存已分配量
     */
    @ExcelProperty(value = "内存已分配量",index = 3)
    private String memoryUsed;

    /**
     * 内存分配占比
     */
    @ExcelProperty(value = "内存分配占比",index = 4)
    private String memoryUsage;

    /**
     * cpu总量
     */
    @ExcelProperty(value = "cpu总量",index = 5)
    private String cpuSum;

    /**
     * cpu已分配量
     */
    @ExcelProperty(value = "cpu已分配量",index = 6)
    private String cpuUsed;

    /**
     * cpu分配占比
     */
    @ExcelProperty(value = "cpu分配占比",index = 7)
    private String cpuUsage;

    /**
     * 运行的云主机
     */
    @ExcelProperty(value = "运行的云主机",index = 8)
    private Integer vmRunningCnt;

    /**
     * 异常云主机
     */
    @ExcelProperty(value = "异常云主机",index = 9)
    private Integer vmErrorCnt;

    /**
     * 云主机总数量
     */
    @ExcelProperty(value = "云主机总数量",index = 10)
    private Integer vmSum;

    /**
     * 运行的容器
     */
    @ExcelProperty(value = "运行的容器",index = 11)
    private Integer lxcRunningCnt;

    /**
     * 异常容器
     */
    @ExcelProperty(value = "异常容器",index = 12)
    private Integer lxcErrorCnt;

    /**
     * 容器总数量
     */
    @ExcelProperty(value = "容器总数量",index = 13)
    private Integer lxcSum;

}
