package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Layout for calculator.
 *
 * @author Danijel Barišić
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * Spacing between components.
     */
    private final int spacing;

    /**
     * Positions of components.
     */
    private Map<Component, RCPosition> constraints;

    /**
     * Constructor.
     *
     * @param spacing spacing between components
     */
    public CalcLayout(int spacing) {
        this.spacing = spacing;
        constraints = new LinkedHashMap<>();
    }

    /**
     * Constructor.
     */
    public CalcLayout() {
        spacing = 0;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {

        if (comp == null) {
            throw new NullPointerException("Component cannot be null.");
        }

        if (constraints == null) {
            throw new NullPointerException("Constraints cannot be null.");
        }

        if (!(constraints.getClass().equals(RCPosition.class)
                || constraints.getClass().equals(String.class))) {
            throw new IllegalArgumentException("Invalid constraints object type.");
        }

        if (constraints.getClass().equals(String.class)) {
            this.constraints.put(comp, RCPosition.parse((String) constraints));
        } else {
            this.constraints.put(comp, (RCPosition) constraints);
        }

    }

    @Override
    public Dimension maximumLayoutSize(Container target) {

//        return null;
//        for (Component c : target.getComponents()) {
//            calculateLayoutSize(target, c::getMaximumSize);

//        }

        if (target.getComponents().length == 0 || !target.getComponents()[0].isMaximumSizeSet()) {
            return new Dimension(100, 100);
        }

        float sizeX = target.getComponents()[0].getX();
        float sizeY = target.getComponents()[0].getY();

        List<Dimension> dimensions =
                Arrays.stream(target.getComponents()).map(Component::getMaximumSize).collect(Collectors.toList());
//
//        int maxRow = 0;
//        int maxCol = 0;
//        for (Component c : constraints.keySet()) {
//            if (constraints.get(c).getRow() > maxRow) {
//                maxRow = constraints.get(c).getRow();
//            }
//
//            if (constraints.get(c).getColumn() > maxCol) {
//                maxCol = constraints.get(c).getColumn();
//            }
//        }
//
//        if (constraints.containsValue(new RCPosition(1, 1))
//                && maxCol < 5) {
//            maxCol = 5;
//        }


//        for (Dimension d : dimensions) {
//            sizeX += d.width;
//            sizeY += d.height;
//        }

        //get whole container max, derive from 1,1 if it's the only one component
        if (target.getComponents()[0].getMaximumSize().equals(new Dimension(5, 1))) {

            return new Dimension((r(((target.getComponents()[0].getMaximumSize().width - 4 * spacing) / 5.) * 7 +
                    (2 + 2) * spacing)),
                    r(target.getComponents()[0].getMaximumSize().height * 5 + spacing * (4 + 2)));
        } else {
            return new Dimension(r(target.getComponents()[0].getMaximumSize().width * 7 + spacing * (6 + 2)),
                    r(target.getComponents()[0].getMaximumSize().height * 5 + spacing * (4 + 2))
            );
        }

//        target.getComponents()[1]::getMaximumSize
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }


    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        if (parent.getComponents().length == 0 || !parent.getComponents()[0].isPreferredSizeSet()) {
            return new Dimension(100, 100);
        }

        //component.isPreferredSizeSet
        if (parent.getComponents()[0].getPreferredSize().equals(new Dimension(5, 1))) {

            return new Dimension((r(((parent.getComponents()[0].getPreferredSize().width - 4 * spacing) / 5.) * 7 +
                    (2 + 2) * spacing)),
                    r(parent.getComponents()[0].getPreferredSize().height * 5 + spacing * (4 + 2)));
        } else {
            return new Dimension(r(parent.getComponents()[0].getPreferredSize().width * 7 + spacing * (6 + 2)),
                    r(parent.getComponents()[0].getPreferredSize().height * 5 + spacing * (4 + 2))
            );
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {

        if (parent.getComponents().length == 0 || !parent.getComponents()[0].isMinimumSizeSet()) {
            return new Dimension(100, 100);
        }

        if (parent.getComponents()[0].getMinimumSize().equals(new Dimension(5, 1))) {

            return new Dimension((r(((parent.getComponents()[0].getMinimumSize().width - 4 * spacing) / 5.) * 7 +
                    (2 + 2) * spacing)),
                    r(parent.getComponents()[0].getMinimumSize().height * 5 + spacing * (4 + 2)));
        } else {
            return new Dimension(r(parent.getComponents()[0].getMinimumSize().width * 7 + spacing * (6 + 2)),
                    r(parent.getComponents()[0].getMinimumSize().height * 5 + spacing * (4 + 2))
            );
        }
    }

    @Override
    public void layoutContainer(Container parent) {

        if (parent.getComponents().length == 0) return;

        Dimension dim = parent.getSize();
        for (Map.Entry<Component, RCPosition> entry : constraints.entrySet()) {
            if (entry.getValue().equals(new RCPosition(1, 1))) {
                entry.getKey().setBounds(spacing, spacing, r(5 * ((dim.getWidth() - (6 + 2) * spacing) / 7)),
                        r((dim.getHeight() - (4 + 2) * spacing) / 5));
            } else {
                entry.getKey().setBounds(
                        r(spacing +
                                (entry.getValue().getColumn() - 1) * (dim.getWidth() - (6 + 1) * spacing) / 7),
                        r(spacing +
                                (entry.getValue().getRow() - 1) * (dim.getHeight() - (4 + 1) * spacing) / 5),
                        r(((dim.getWidth() - (6 + 2) * spacing) / 7)),
                        r((dim.getHeight() - (4 + 2) * spacing) / 5));
            }
        }
    }

//    private Dimension calculateLayoutSize() {
//        //Container parent, Supplier<Dimension> s
////        float sizeX = parent.getComponents()[0].getX();
////        float sizeY = parent.getComponents()[0].getY();
////
//////        for (Component c : parent.getComponents()) {
////        sizeX += s.get().width;
////        sizeY += s.get().height;
//////        }
//    }

    /**
     * Rounding of double.
     *
     * @param d number to round
     * @return rounded number
     */
    private static int r(double d) {
        return (int) (d + 0.5);
    }
}
