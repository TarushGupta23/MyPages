package com.example.mypages.chartsRes;

import java.io.Serializable;
import java.util.Map;

public class ModelChart implements Serializable{
    private String key, name;
    private Map<String, Double> data;
    private int chartType;

    public static final int PIE = 0, LINE = 1, BAR = 2;

    public ModelChart() {}

    public String getKey() { return key; }
    public Map<String, Double> getData() { return data; }
    public String getName() { return name; }
    public int getChartType() { return chartType; }


    public void setChartType(int chartType) { this.chartType = chartType; }
    public void setKey(String key) { this.key = key; }
    public void setData(Map<String, Double> data) { this.data = data; }
    public void setName(String name) { this.name = name; }

    public static String getChartPath(ModelChart chart) {
        if (chart.getChartType() == ModelChart.PIE) {
            return "pieChart_folder";
        } else if (chart.getChartType() == ModelChart.LINE) {
            return "lineChart_folder";
        } else {
            return "barChart_folder";
        }
    }
}
