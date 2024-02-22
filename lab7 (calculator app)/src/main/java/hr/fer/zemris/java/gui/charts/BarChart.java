package hr.fer.zemris.java.gui.charts;

import java.util.List;

public class BarChart {

    private List<XYValue> values;
    private String descX;
    private String descY;
    private int minY;
    private int maxY;
    private int spacing;

    public BarChart(List<XYValue> values, String descX, String descY, int minY, int maxY, int spacing) {

        if (minY < 0) {
            throw new BarChartException("Minimal Y cannot be negative.");
        }
        if (maxY <= minY) {
            throw new BarChartException("Maximum Y has to be strictly larger than minimum Y.");
        }

        for (XYValue value : values) {
            if (value.getY() < minY) {
                throw new BarChartException("Y values cannot be lower than the defined minimum Y value");
            }
        }

        this.values = values;
        this.descX = descX;
        this.descY = descY;

        this.minY = minY;

        this.maxY = maxY;
        this.spacing = spacing;
    }

    public List<XYValue> getValues() {
        return values;
    }

    public String getDescX() {
        return descX;
    }

    public String getDescY() {
        return descY;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getSpacing() {
        return spacing;
    }
}
