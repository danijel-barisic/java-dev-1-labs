package hr.fer.zemris.java.gui.charts;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

public class BarChartDemo extends JFrame {

    private BarChart model;
    private static Path modelPath;

    public BarChartDemo() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

//        model = new BarChart(
//                Arrays.asList(
//                        new XYValue(1, 8), new XYValue(2, 20), new XYValue(3, 22),
//                        new XYValue(4, 10), new XYValue(5, 4)
//                ),
//                "Number of people in the car",
//                "Frequency",
//                0, // y-os kreće od 0
//                22, // y-os ide do 22
//                2
//        );

        List<String> modelParams = null;
        try {
            modelParams = Files.readAllLines(modelPath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit(1);
        }

        if (modelParams.size() < 5) {
            throw new IllegalArgumentException("Invalid number of parameters in the file");
        }

        try {

            String descX = modelParams.get(0);
            String descY = modelParams.get(1);

            int minY = Integer.parseInt(modelParams.get(3));
            int maxY = Integer.parseInt(modelParams.get(4));

            int spacing = modelParams.size() >= 6 ? Integer.parseInt(modelParams.get(5)) : 1;

            String[] dots = modelParams.get(2).split("\\s+");

            List<XYValue> values = new ArrayList<>();
            for (String dot : dots) {
                String[] xAndY = dot.split(",");
                values.add(new XYValue(Integer.parseInt(xAndY[0]), Integer.parseInt(xAndY[1])));
            }

            model = new BarChart(
                    values,
                    descX,
                    descY,
                    minY,
                    maxY,
                    spacing
            );
        } catch (NumberFormatException ex) {
            System.err.println(ex.getMessage());
            exit(1);
        }
        setTitle("Chart");
        setLocation(20, 20);
        setSize(500, 500);

        initGUI();
//        pack();
    }

    protected void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JComponent chart = new BarChartComponent(model);

        JLabel label = null;
        try {
            label = new JLabel(modelPath.toFile().getCanonicalPath(), JLabel.CENTER);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            exit(1);
        }

        cp.add(label, BorderLayout.PAGE_START);
        cp.add(chart, BorderLayout.CENTER);

    }

    public static void main(String[] args) {

        if (args.length != 1) {
            throw new IllegalArgumentException("Invalid number of arguments, 1 expected.");
        }

        modelPath = Paths.get(args[0]);

        SwingUtilities.invokeLater(() -> {
            new BarChartDemo().setVisible(true);
        });
    }
}
