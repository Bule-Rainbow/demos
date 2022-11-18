package com.jwt.demo.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className ClusterVO.java
 * @description 集群统计数据
 * @createTime 2022年09月05日 11:22
 */
@Data
@Accessors(chain = true)
public class ClusterStatisticsVO {
    /**
     * 集群名称
     */
    @ExcelProperty(value = "集群",index = 0)
    private String clusterName;

    /**
     * 计算资源统计
     */
    private List<NodeStatisticsVO> computeList;

    /**
     * 存储资源统计：共享存储总量
     */
    @ExcelProperty(value = "共享存储总量",index = 14)
    private String storageShareSum;

    /**
     * 存储资源统计：共享存储已使用量
     */
    @ExcelProperty(value = "共享存储已使用量",index = 15)
    private String storageShareUsed;

    /**
     * 存储资源统计：共享存储使用占比
     */
    @ExcelProperty(value = "共享存储使用占比",index = 16)
    private String storageShareUsage;

    /**
     * 存储资源统计：非共享存储总量
     */
    @ExcelProperty(value = "非共享存储总量",index = 17)
    private String storageNoShareSum;

    /**
     * 存储资源统计：非共享存储已使用量
     */
    @ExcelProperty(value = "非共享存储已使用量",index = 18)
    private String storageNoShareUsed;

    /**
     * 存储资源统计：非共享存储使用占比
     */
    @ExcelProperty(value = "非共享存储使用占比",index = 19)
    private String storageNoShareUsage;

    /**
     * 存储资源统计：存储总量
     */
    @ExcelProperty(value = "存储总量",index = 20)
    private String storageSum;

    /**
     * 存储资源统计：总存储使用占比
     */
    @ExcelProperty(value = "存储使用占比",index = 21)
    private String storageUsage;

    /**
     * 网络资源统计：子网数量
     */
    @ExcelProperty(value = "子网数量",index = 22)
    private Integer subnetCnt;

    /**
     * 网络资源统计：ip总量
     */
    @ExcelProperty(value = "ip总量",index = 23)
    private Integer ipSum;

    /**
     * 网络资源统计：ip用量
     */
    @ExcelProperty(value = "ip用量",index = 24)
    private Integer ipUsed;

    /**
     * 网络资源统计：ip使用比例
     */
    @ExcelProperty(value = "ip使用比例",index = 25)
    private String ipUsage;
}
