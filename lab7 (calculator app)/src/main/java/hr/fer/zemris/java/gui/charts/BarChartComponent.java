package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class BarChartComponent extends JComponent {

    private BarChart barChart;

    public BarChartComponent(BarChart barChart) {
        this.barChart = barChart;
    }

    @Override
    protected void paintComponent(Graphics g) {

        List<XYValue> cols = barChart.getValues();

        int AXIS_EXTENSION_AMT = 20;

        int TOP_ARROW_POINT_X = 30 + g.getFontMetrics().stringWidth(Integer.toString(barChart.getMaxY()));
        int TOP_ARROW_POINT_Y = AXIS_EXTENSION_AMT;

        int DOWN_SPACING = 50;
        int LEFT_SPACING = 50;

        int BOT_ARROW_POINT_X = this.getParent().getWidth() - LEFT_SPACING;
        int BOT_ARROW_POINT_Y = this.getParent().getHeight() - DOWN_SPACING;


        g.setColor(Color.ORANGE);

        int maxBarHeight = BOT_ARROW_POINT_Y - TOP_ARROW_POINT_Y;
        int oneCarthesianYSpacing = (int) (((double) barChart.getSpacing() / ((double) barChart.getMaxY() - barChart.getMinY()))
                * (double) maxBarHeight);
        int maxChartWidth = BOT_ARROW_POINT_X - TOP_ARROW_POINT_X;
        int barWidth = maxChartWidth / cols.size();
        for (int i = 0; i < cols.size(); i++) {
//            int height = BOT_POINT_Y - cols.get(i).getY();

//            int height = (barChart.getMaxY() / barChart.getSpacing()) * cols.get(i).getY();
//            int height = cols.get(i).getY() * barChart.getSpacing();
//            g.fillRect(TOP_POINT_X + i * (width + 1) + 1, BOT_POINT_Y - height, width, height);
//            g.fillRect(TOP_POINT_X + i * (width + 1) + 1, 300 - height,
//                    width, BOT_POINT_Y - 380 + height); //height gotta be scalable, not fixed. Or? Is starting Y scalable? Is both?

            int height = (int) (((float) cols.get(i).getY() - barChart.getMinY()) / barChart.getSpacing());

            //keep relations of bars. I.e. ratio, linear interpolation, normalisation, etc. ig.

//            int height = maxBarHeight;
            g.fillRect(TOP_ARROW_POINT_X + i * (barWidth + 1) + 1,
                    maxBarHeight - height * oneCarthesianYSpacing + TOP_ARROW_POINT_Y,
                    barWidth, height * oneCarthesianYSpacing); //height gotta be scalable, not fixed. Or? Is starting Y scalable? Is both?


//            g.fillRect(TOP_ARROW_POINT_X + i * (width + 1) + 1, maxBarHeight - height * oneCarthesianYSpacing,
//                    width, height * oneCarthesianYSpacing); //height gotta be scalable, not fixed. Or? Is starting Y scalable? Is both?
            //svoje kroz cijelo puta nova baza tj. novi maks il neš?

        }

        g.setColor(Color.GRAY);

        int EXTENDED_TOP_ARROW_POINT_Y = TOP_ARROW_POINT_Y - AXIS_EXTENSION_AMT;

        //Y-axis
        g.drawLine(TOP_ARROW_POINT_X, EXTENDED_TOP_ARROW_POINT_Y, TOP_ARROW_POINT_X, BOT_ARROW_POINT_Y);
        g.drawLine(TOP_ARROW_POINT_X, EXTENDED_TOP_ARROW_POINT_Y, TOP_ARROW_POINT_X - 4,
                EXTENDED_TOP_ARROW_POINT_Y + 5);
        g.drawLine(TOP_ARROW_POINT_X, EXTENDED_TOP_ARROW_POINT_Y, TOP_ARROW_POINT_X + 4,
                EXTENDED_TOP_ARROW_POINT_Y + 5);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform defaultAt = g2d.getTransform();

        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);
        g2d.setTransform(at);
        String descY = barChart.getDescY();
        g2d.drawString(descY, -((this.getHeight() + g.getFontMetrics().stringWidth(descY)) / 2 + TOP_ARROW_POINT_Y
                + DOWN_SPACING), 20);
        g2d.setTransform(defaultAt);

        //Y numeration
        for (int i = 0, y = barChart.getMinY(); y <= barChart.getMaxY(); y += barChart.getSpacing(), i++) {
            String numToDraw = Integer.toString(y);
            g.drawLine(TOP_ARROW_POINT_X - 5, BOT_ARROW_POINT_Y - i * oneCarthesianYSpacing, TOP_ARROW_POINT_X,
                    BOT_ARROW_POINT_Y - i * oneCarthesianYSpacing);
            g.drawString(numToDraw, TOP_ARROW_POINT_X - g.getFontMetrics().stringWidth(numToDraw) - 10,
                    BOT_ARROW_POINT_Y - oneCarthesianYSpacing * i + g.getFontMetrics().getAscent() / 3);
        }

        int EXTENDED_BOT_ARROW_POINT_X = BOT_ARROW_POINT_X + AXIS_EXTENSION_AMT;
        //X-axis
        g.drawLine(TOP_ARROW_POINT_X, BOT_ARROW_POINT_Y, EXTENDED_BOT_ARROW_POINT_X, BOT_ARROW_POINT_Y);
        g.drawLine(EXTENDED_BOT_ARROW_POINT_X, BOT_ARROW_POINT_Y, EXTENDED_BOT_ARROW_POINT_X - 4, BOT_ARROW_POINT_Y - 5);
        g.drawLine(EXTENDED_BOT_ARROW_POINT_X, BOT_ARROW_POINT_Y, EXTENDED_BOT_ARROW_POINT_X - 4, BOT_ARROW_POINT_Y + 5);

        String descX = barChart.getDescX();
        g.drawString(descX, (this.getWidth() - g.getFontMetrics().stringWidth(descX)) / 2,
                this.getHeight() - 6);

        //X numeration
        for (int i = 0; i < barChart.getValues().size(); i++) {
            int x = barChart.getValues().get(i).getX();
            String numToDraw = Integer.toString(x);
            g.drawLine(TOP_ARROW_POINT_X + (barWidth + 1) * i, BOT_ARROW_POINT_Y,
                    TOP_ARROW_POINT_X + (barWidth + 1) * i,
                    BOT_ARROW_POINT_Y + 5);
            g.drawString(numToDraw, TOP_ARROW_POINT_X + (barWidth + 1) * i + barWidth / 2,
                    BOT_ARROW_POINT_Y + 16);
        }

    }
}
