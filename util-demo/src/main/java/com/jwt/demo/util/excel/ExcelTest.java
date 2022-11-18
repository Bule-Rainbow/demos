package com.jwt.demo.util.excel;

import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.sun.jndi.toolkit.url.UrlUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className ExcelUtil.java
 * @description
 * @createTime 2022年09月05日 8:59
 */
@RestController
@RequestMapping("/excel")
public class ExcelTest {

    @PostMapping(value = "/export")
    public void export(HttpServletResponse response) throws IOException {
        List<DynamicHeader> rootTitle = new ArrayList<>(2);
        rootTitle.add(new DynamicHeader(26, "资源统计报表"));

        List<DynamicHeader> classTitle = new ArrayList<>(3);
        classTitle.add(new DynamicHeader(14, "计算"));
        classTitle.add(new DynamicHeader(8, "存储"));
        classTitle.add(new DynamicHeader(4, "网络"));

        List<Object> columnTitle = Arrays.asList("集群", "节点", "内存总量", "已分配内存", "内存分配占比", "CPU总量", "已分配CPU核数",
                "CPU分配占比", "运行的云主机", "异常云主机", "云主机总数量", "运行的容器", "异常容器", "容器总数量", "共享存储总量",
                "共享存储已使用量", "共享存储使用占比", "非共享存储总量", "非共享存储已使用量", "非共享存储使用占比", "存储总量",
                "总存储使用占比", "子网数量", "IP总量", "IP用量", "IP使用占比");

        List<List<Object>> rows = new ArrayList<>();
        rows.add(columnTitle);
        List<ClusterStatisticsVO> initData = getInitData();

        for (ClusterStatisticsVO cluster : initData) {
            for (NodeStatisticsVO node : cluster.getComputeList()) {
                List<Object> objectList = new ArrayList<>();
                objectList.add(cluster.getClusterName());
                objectList.add(node.getNodeName());
                objectList.add(node.getMemorySum());
                objectList.add(node.getMemoryUsed());
                objectList.add(node.getMemoryUsage());
                objectList.add(node.getCpuSum());
                objectList.add(node.getCpuUsed());
                objectList.add(node.getCpuUsage());
                objectList.add(node.getVmRunningCnt());
                objectList.add(node.getVmErrorCnt());
                objectList.add(node.getVmSum());
                objectList.add(node.getLxcRunningCnt());
                objectList.add(node.getLxcErrorCnt());
                objectList.add(node.getLxcSum());
                objectList.add(cluster.getStorageShareSum());
                objectList.add(cluster.getStorageShareUsed());
                objectList.add(cluster.getStorageShareUsage());
                objectList.add(cluster.getStorageNoShareSum());
                objectList.add(cluster.getStorageNoShareUsed());
                objectList.add(cluster.getStorageNoShareUsage());
                objectList.add(cluster.getStorageSum());
                objectList.add(cluster.getStorageUsage());
                objectList.add(cluster.getSubnetCnt());
                objectList.add(cluster.getIpSum());
                objectList.add(cluster.getIpUsed());
                objectList.add(cluster.getIpUsage());
                rows.add(objectList);
            }
        }

        //按时间戳生成随机数
        String fileName = IdUtil.randomUUID() + ".xlsx";
        // 通过工具类创建writer（写入到本地路径，可以指定路径）
        //BigExcelWriter writer = ExcelUtil.getBigWriter("d:/" + fileName);
        ExcelWriter writer = ExcelUtil.getBigWriter();
        //合并标题
        mergeHeadRow(writer, rootTitle, classTitle);
        //设置列宽度 非必须
        for (int i = 0; i < rows.get(0).size(); i++) {
            writer.setColumnWidth(i, 10);
        }
        // 一次性写出内容
        writer.write(rows, true);
        //合并行
        mergeDataRow(writer, initData);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + UrlUtil.encode("资源统计报表.xlsx", "utf-8"));
        writer.flush(outputStream, true);
        // 关闭writer，释放内存
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        List<DynamicHeader> rootTitle = new ArrayList<>(2);
        rootTitle.add(new DynamicHeader(26, "资源统计报表"));

        List<DynamicHeader> classTitle = new ArrayList<>(3);
        classTitle.add(new DynamicHeader(14, "计算"));
        classTitle.add(new DynamicHeader(8, "存储"));
        classTitle.add(new DynamicHeader(4, "网络"));

        List<Object> columnTitle = Arrays.asList("集群", "节点", "内存总量", "已分配内存", "内存分配占比", "CPU总量", "已分配CPU核数",
                "CPU分配占比", "运行的云主机", "异常云主机", "云主机总数量", "运行的容器", "异常容器", "容器总数量", "共享存储总量",
                "共享存储已使用量", "共享存储使用占比", "非共享存储总量", "非共享存储已使用量", "非共享存储使用占比", "存储总量",
                "总存储使用占比", "子网数量", "IP总量", "IP用量", "IP使用占比");

        List<List<Object>> rows = new ArrayList<>();
        rows.add(columnTitle);
        List<ClusterStatisticsVO> initData = getInitData();

        for (ClusterStatisticsVO cluster : initData) {
            for (NodeStatisticsVO node : cluster.getComputeList()) {
                List<Object> objectList = new ArrayList<>();
                objectList.add(cluster.getClusterName());
                objectList.add(node.getNodeName());
                objectList.add(node.getMemorySum());
                objectList.add(node.getMemoryUsed());
                objectList.add(node.getMemoryUsage());
                objectList.add(node.getCpuSum());
                objectList.add(node.getCpuUsed());
                objectList.add(node.getCpuUsage());
                objectList.add(node.getVmRunningCnt());
                objectList.add(node.getVmErrorCnt());
                objectList.add(node.getVmSum());
                objectList.add(node.getLxcRunningCnt());
                objectList.add(node.getLxcErrorCnt());
                objectList.add(node.getLxcSum());
                objectList.add(cluster.getStorageShareSum());
                objectList.add(cluster.getStorageShareUsed());
                objectList.add(cluster.getStorageShareUsage());
                objectList.add(cluster.getStorageNoShareSum());
                objectList.add(cluster.getStorageNoShareUsed());
                objectList.add(cluster.getStorageNoShareUsage());
                objectList.add(cluster.getStorageSum());
                objectList.add(cluster.getStorageUsage());
                objectList.add(cluster.getSubnetCnt());
                objectList.add(cluster.getIpSum());
                objectList.add(cluster.getIpUsed());
                objectList.add(cluster.getIpUsage());
                rows.add(objectList);
            }
        }

        //按时间戳生成随机数
        String fileName = IdUtil.randomUUID() + ".xlsx";
        // 通过工具类创建writer
        BigExcelWriter writer = ExcelUtil.getBigWriter("d:/" + fileName);
        //合并标题
        mergeHeadRow(writer, rootTitle, classTitle);
        //设置列宽度 非必须
        for (int i = 0; i < rows.get(0).size(); i++) {
            writer.setColumnWidth(i, 10);
        }
        // 一次性写出内容
        writer.write(rows, true);
        //合并行
        mergeDataRow(writer, initData);
        // 关闭writer，释放内存
        writer.close();

    }

    /**
     * 按需合并表头
     *
     * @param writer         写对象
     * @param dynamicHeaders 动态表头信息
     */
    private static void mergeHeadRow(ExcelWriter writer, List<DynamicHeader>... dynamicHeaders) throws IOException {
        //生成空表头
        for (int i = 0; i < dynamicHeaders.length; i++) {
            //创建一行空表头占位置
            writer.writeHeadRow(new LinkedList());
        }
        //合并动态表格
        for (int i = 0; i < dynamicHeaders.length; i++) {
            //动态表格开始列
            int startCol = 0;
            for (int j = 0; j < dynamicHeaders[i].size(); j++) {
                DynamicHeader dynamicHeader = dynamicHeaders[i].get(j);
                //开始合并行
                Integer firstColumn = startCol;
                //结束合并行
                int lastColumn = (startCol + dynamicHeader.getOccupiedColumn()) - 1;
                if (!firstColumn.equals(lastColumn)) {
                    //合并单元格后的标题行，使用默认标题样式
                    writer.merge(i, i, firstColumn, lastColumn, dynamicHeader.getContent(), true);
                }
                startCol += dynamicHeader.getOccupiedColumn();
            }
        }
    }

    /**
     * 合并数据行
     *
     * @param writer   写对象
     * @param dataList 数据
     */
    private static void mergeDataRow(ExcelWriter writer, List<ClusterStatisticsVO> dataList) {
        //需要进行行合并的列下标
        List<Integer> needMergeColumn = Arrays.asList(0, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        //前三行为表头，默认从第四行开始合并
        int startRow = 3;
        for (ClusterStatisticsVO clusterVO : dataList) {
            int endRow = startRow + clusterVO.getComputeList().size() - 1;
            if (startRow != endRow) {
                for (Integer column : needMergeColumn) {
                    writer.merge(startRow, endRow, column, column, null, true);
                }
            }
            startRow = endRow + 1;
        }
    }

    private static List<ClusterStatisticsVO> getInitData() {
        List<ClusterStatisticsVO> initDataList = new ArrayList<>();
        ClusterStatisticsVO vo1 = new ClusterStatisticsVO();
        vo1.setClusterName("cluster1").setStorageShareSum("2T").setStorageShareUsed("1T").setStorageShareUsage("50%")
                .setStorageNoShareSum("3T").setStorageNoShareUsed("1.5T").setStorageNoShareUsage("50%")
                .setStorageSum("5T").setStorageUsage("50%");
        List<NodeStatisticsVO> nodeStatisticsVOList = new ArrayList<>();
        NodeStatisticsVO nodeVo11 = new NodeStatisticsVO("node1", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo12 = new NodeStatisticsVO("node2", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo13 = new NodeStatisticsVO("node3", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        vo1.setSubnetCnt(20).setIpSum(500).setIpUsed(250).setIpUsage("50%");

        nodeStatisticsVOList.add(nodeVo11);
        nodeStatisticsVOList.add(nodeVo12);
        nodeStatisticsVOList.add(nodeVo13);
        vo1.setComputeList(nodeStatisticsVOList);
        initDataList.add(vo1);

        ClusterStatisticsVO vo2 = new ClusterStatisticsVO();
        vo2.setClusterName("cluster2").setStorageShareSum("6T").setStorageShareUsed("1.5T").setStorageShareUsage("25%")
                .setStorageNoShareSum("3T").setStorageNoShareUsed("1.5T").setStorageNoShareUsage("50%")
                .setStorageSum("9T").setStorageUsage("44%");
        List<NodeStatisticsVO> nodeStatisticsVOList2 = new ArrayList<>();
        NodeStatisticsVO nodeVo21 = new NodeStatisticsVO("node4", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo22 = new NodeStatisticsVO("node5", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo23 = new NodeStatisticsVO("node6", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        vo2.setSubnetCnt(10).setIpSum(400).setIpUsed(100).setIpUsage("25%");
        nodeStatisticsVOList2.add(nodeVo21);
        nodeStatisticsVOList2.add(nodeVo22);
        nodeStatisticsVOList2.add(nodeVo23);
        vo2.setComputeList(nodeStatisticsVOList2);
        initDataList.add(vo2);

        ClusterStatisticsVO vo3 = new ClusterStatisticsVO();
        vo3.setClusterName("cluster3").setStorageShareSum("2T").setStorageShareUsed("1T").setStorageShareUsage("50%")
                .setStorageNoShareSum("5T").setStorageNoShareUsed("1.5T").setStorageNoShareUsage("30%")
                .setStorageSum("7T").setStorageUsage("36%");
        List<NodeStatisticsVO> nodeStatisticsVOList3 = new ArrayList<>();
        NodeStatisticsVO nodeVo31 = new NodeStatisticsVO("node7", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo32 = new NodeStatisticsVO("node8", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        NodeStatisticsVO nodeVo33 = new NodeStatisticsVO("node9", "200G", "50G", "25%", "64", "32", "50%",
                20, 3, 23, 30, 5, 35);
        vo3.setSubnetCnt(10).setIpSum(300).setIpUsed(60).setIpUsage("20%");
        nodeStatisticsVOList3.add(nodeVo31);
        nodeStatisticsVOList3.add(nodeVo32);
        nodeStatisticsVOList3.add(nodeVo33);
        vo3.setComputeList(nodeStatisticsVOList3);
        initDataList.add(vo3);

        return initDataList;
    }
}
