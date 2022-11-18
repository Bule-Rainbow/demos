package com.jwt.demo.util.other;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author jiangwentao
 * @version 2.0.0
 * @className LineFitUtil.java
 * @description 线性拟合
 * @createTime 2022年07月21日 10:17
 */
public class LineFitUtil {

    public static void main(String[] args) {
//        List<Double> data = Arrays.asList(100.0, 101.6, 103.3, 111.5, 116.5, 120.1, 120.3, 100.6, 83.6, 84.7, 88.7,
//                98.9, 111.9, 122.9, 131.9, 134.2, 131.6, 132.2, 139.8, 142.0, 140.5, 153.1, 159.2, 162.3,
//                159.1, 155.1, 161.2, 171.5, 168.4, 180.4, 201.6, 218.7, 247.0, 253.7, 261.4, 273.2, 279.4);
        //使用Stream集合初始化，使用Arrays.asList()生成的集合不能增删
        List<Double> data = Stream.of(100.0, 101.6, 103.3, 105.6, 104.3, 104.6, 105.3, 103.6, 104.3, 102.6, 105.3).collect(toList());

        //得到一次线性拟合函数，形如 f(x) = 102.2954545454545 + 0.27727272727272967x
        String result = linearFit(data);
        System.out.println(result);

        //拟合函数，进行预测，得到预测结果
        List<Double> doubles1 = lineFit(data, 2);
        doubles1.forEach(System.out::println);
    }

    /**
     * 将目标数据转换成数组格式
     *
     * @return 数据集合二维数组
     */
    public static double[][] linearScatters(List<Double> srcData) {
        List<double[]> data = new ArrayList<>();
        for (int i = 0; i < srcData.size(); i++) {
            double[] xy = {i, srcData.get(i)};
            data.add(xy);
        }
        return data.toArray(new double[0][]);
    }

    /**
     * 线性拟合
     *
     * @param srcData 拟合数据
     * @return 拟合结果：拟合函数表达式
     */
    public static String linearFit(List<Double> srcData) {
        //转成二维数组格式
        double[][] data = linearScatters(srcData);

        //使用SimpleRegression保存拟合函数的参数
        SimpleRegression regression = new SimpleRegression();
        // 数据集
        regression.addData(data);
        /*
         * RegressionResults 中是拟合的结果
         * 其中重要的几个参数如下：
         *   parameters:
         *      0: b
         *      1: k
         *   globalFitInfo
         *      0: 平方误差之和, SSE
         *      1: 平方和, SST
         *      2: R 平方, RSQ
         *      3: 均方误差, MSE
         *      4: 调整后的 R 平方, adjRSQ
         *
         * */
        RegressionResults results = regression.regress();
        double b = results.getParameterEstimate(0);
        double k = results.getParameterEstimate(1);
        double r2 = results.getRSquared();

        // 重新计算生成拟合曲线
        for (double[] datum : data) {
            double[] xy = {datum[0], k * datum[0] + b};
        }

        return "f(x) =" +
                (b >= 0 ? " " : " - ") +
                Math.abs(b) +
                (k > 0 ? " + " : " - ") +
                Math.abs(k) +
                "x";
    }

    public static List<Double> lineFit(List<Double> srcData, int fitTimes) {
        if (CollectionUtils.isEmpty(srcData) || fitTimes < 1) {
            return srcData;
        }
        //转成二维数组格式
        double[][] data = linearScatters(srcData);

        //从SimpleRegression获取拟合函数的参数
        SimpleRegression regression = new SimpleRegression();
        regression.addData(data);
        RegressionResults results = regression.regress();
        double b = results.getParameterEstimate(0);
        double k = results.getParameterEstimate(1);
        double r2 = results.getRSquared();
        double next = k * (srcData.size() + 1) + b;
        NumberFormat nf = new DecimalFormat("0.0 ");
        next = Double.parseDouble(nf.format(next));
        srcData.add(next);
        return lineFit(srcData, --fitTimes);
    }
}
