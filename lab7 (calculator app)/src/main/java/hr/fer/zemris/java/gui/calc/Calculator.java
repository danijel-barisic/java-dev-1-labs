package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

//buttonu treba addat action listener i u taj add mu proslijedit lambdu "a->{radi nešto, myb nešto s modelom,
// ujedno labela.setText(novi text)}. Samo nez di tu CalcValueListener stupa

/**
 * GUI for calculator.
 *
 * @author Danijel Barišić
 */
public class Calculator extends JFrame {
//    digitButton, funcButton, binOpButton, oni su zaduženi za izvrašavnje akcija/operacija,
//    npr. funcButton ima reference na dve unarne operacije, jedna inverz druge

    /**
     * Reference to model.
     */
    private CalcModel calcModel;

    /**
     * Reference to display label.
     */
    private JLabel display;

    /**
     * Checkbox for inverting functions.
     */
    private JCheckBox inverseF;

    /**
     * Calculator stack.
     */
    private Stack<Double> calcStack;

    /**
     * Constructor.
     */
    public Calculator() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Java Calculator v1.0");
        setLocation(300, 300);
        setSize(600, 480);
        calcModel = new CalcModelImpl();
        display = display();
        inverseF = new JCheckBox("Inv");
        calcStack = new Stack<>();

        initGUI();

    }

    /**
     * GUI initialisation.
     */
    private void initGUI() {
        Container cp = getContentPane();

        cp.setLayout(new CalcLayout(3)); //try spacing 50 n it gets tad clearer to you what might be wrong
        cp.add(display, new RCPosition(1, 1));
        cp.add(new sb("=", () -> {
                    if (calcModel.isActiveOperandSet()) {
                        if (calcModel.getPendingBinaryOperation() != null) {
                            calcModel.setValue(calcModel.getPendingBinaryOperation()
                                    .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
                            calcModel.setPendingBinaryOperation(null);
                        }
                    }
                }),
                new RCPosition(1, 6));
        cp.add(new sb("clr", () -> calcModel.clear()), new RCPosition(1, 7));
        cp.add(new fb("1/x", "1/x", x -> 1 / x, x -> 1 / x), new RCPosition(2, 1));
        cp.add(new fb("sin", "arcsin", Math::sin, Math::asin), new RCPosition(2, 2));
        cp.add(new db("7"), new RCPosition(2, 3));
        cp.add(new db("8"), new RCPosition(2, 4));
        cp.add(new db("9"), new RCPosition(2, 5));
        cp.add(new ob("/", (x, y) -> x / y), new RCPosition(2, 6));
        cp.add(new sb("reset", () -> calcModel.clearAll()), new RCPosition(2, 7));
        cp.add(new fb("log", "10^x", Math::log10, x -> Math.pow(10, x)), new RCPosition(3, 1));
        cp.add(new fb("cos", "arccos", Math::cos, Math::acos), new RCPosition(3, 2));
        cp.add(new db("4"), new RCPosition(3, 3));
        cp.add(new db("5"), new RCPosition(3, 4));
        cp.add(new db("6"), new RCPosition(3, 5));
        cp.add(new ob("*", (x, y) -> x * y), new RCPosition(3, 6));
        cp.add(new pushb("push", x -> calcStack.push(x)), new RCPosition(3, 7));
        cp.add(new fb("ln", "e^x", Math::log, Math::exp), new RCPosition(4, 1));
        cp.add(new fb("tan", "arctan", Math::tan, Math::atan), new RCPosition(4, 2));
        cp.add(new db("1"), new RCPosition(4, 3));
        cp.add(new db("2"), new RCPosition(4, 4));
        cp.add(new db("3"), new RCPosition(4, 5));
        cp.add(new ob("-", (x, y) -> x - y), new RCPosition(4, 6));
        cp.add(new popb("pop", () -> calcStack.pop()), new RCPosition(4, 7));
        cp.add(new bfb("x^n", "x^(1/n)", Math::pow, (x, n) -> Math.pow(x, 1. / n)),
                new RCPosition(5, 1));
        cp.add(new fb("ctg", "arcctg", x -> 1 / Math.tan(x), x -> 1 / Math.atan(x)),
                new RCPosition(5, 2));
        cp.add(new db("0"), new RCPosition(5, 3));
        cp.add(new sb("+/-", () -> calcModel.swapSign()), new RCPosition(5, 4));
        cp.add(new sb(".", () -> calcModel.insertDecimalPoint()), new RCPosition(5, 5));
        cp.add(new ob("+", (x, y) -> x + y), new RCPosition(5, 6));
        cp.add(inverseF, new RCPosition(5, 7));

    }

    /**
     * Special button.
     */
    private class sb extends JButton {
        public sb(String text, CalcWorker worker) {
            super(text);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            addActionListener(a -> worker.doWork());
        }
    }

    /**
     * Stack push button.
     */
    private class pushb extends JButton {
        public pushb(String text, Consumer<Double> c) {
            super(text);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            addActionListener(a -> c.accept(calcModel.getValue()));
        }
    }

    /**
     * Stack pop button.
     */
    private class popb extends JButton {
        public popb(String text, Supplier<Double> s) {
            super(text);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            addActionListener(a -> {
                if (calcStack.isEmpty()) {
                    calcModel.clear();
                    display.setText("Empty stack");
                } else {
                    calcModel.setValue(s.get());
                }
            });
        }
    }

    /**
     * Function button.
     */
    private class fb extends JButton {

        DoubleUnaryOperator f1, f2;
        String reg, inv;

        public fb(String reg, String inv, DoubleUnaryOperator f1, DoubleUnaryOperator f2) {
            super(reg);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            this.f1 = f1;
            this.f2 = f2;

            this.reg = reg;
            this.inv = inv;

            addActionListener(a -> {

                calcModel.setValue((inverseF.isSelected() ? f2 : f1)
                        .applyAsDouble(calcModel.getValue()));
            });

            inverseF.addActionListener(a -> setText(inverseF.isSelected() ? inv : reg));
        }

    }

    /**
     * Bi-function button.
     */
    private class bfb extends JButton {

        DoubleBinaryOperator f1, f2;
        String reg, inv;

        public bfb(String reg, String inv, DoubleBinaryOperator f1, DoubleBinaryOperator f2) {
            super(reg);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            this.f1 = f1;
            this.f2 = f2;

            this.reg = reg;
            this.inv = inv;

            addActionListener(a -> {

                if (calcModel.getPendingBinaryOperation() != null) {
                    calcModel.setValue(calcModel.getPendingBinaryOperation()
                            .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
                    calcModel.setActiveOperand(calcModel.getValue());
                    calcModel.setPendingBinaryOperation(inverseF.isSelected() ? f2 : f1);
                    calcModel.freezeValue(Double.toString(calcModel.getValue()));
                    calcModel.clear(); //sus, želimo frozen value na displayu tj. toStringu, a ne 0.
                } else {
                    calcModel.setActiveOperand(calcModel.getValue());
                    calcModel.setPendingBinaryOperation(inverseF.isSelected() ? f2 : f1);
                    calcModel.freezeValue(Double.toString(calcModel.getValue()));
                    calcModel.clear();
                }
            });

            inverseF.addActionListener(a -> setText(inverseF.isSelected() ? inv : reg));
        }
    }

    /**
     * Digit button.
     */
    private class db extends JButton {

        public db(String text) {
            super(text);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setFont(getFont().deriveFont(30f));

            setOpaque(true);
            addActionListener(a -> {
                if (calcModel.hasFrozenValue()) {
                    calcModel.clear();
                }
                calcModel.insertDigit(Integer.parseInt(text));
            });
        }
    }

    /**
     * Binary operation button.
     */
    private class ob extends JButton {

        public ob(String text, DoubleBinaryOperator operation) {
            super(text);
            setBackground(Color.getHSBColor(0.667f, 0.133f, 1));
            setOpaque(true);
            addActionListener(a -> {
                if (calcModel.getPendingBinaryOperation() != null) {
                    calcModel.setValue(calcModel.getPendingBinaryOperation()
                            .applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
                }
                calcModel.setActiveOperand(calcModel.getValue());
                calcModel.setPendingBinaryOperation(operation);
                calcModel.freezeValue(Double.toString(calcModel.getValue()));
                calcModel.clear(); //sus, želimo frozen value na displayu tj. toStringu, a ne 0.
            });
        }

    }

    /**
     * @return initialised calculator display label
     */
    private JLabel display() {
        JLabel l = new JLabel();
        l.setHorizontalAlignment(JLabel.RIGHT);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        l.setFont(l.getFont().deriveFont(30f));

        //lambda is for valueChanged, called in model when value is changed, changed within that model due calculations
        calcModel.addCalcValueListener(model -> {
            String displayText = calcModel.toString();
            if (displayText.endsWith(".0")) {
                displayText = displayText.substring(0, displayText.indexOf(".0"));
            }
            l.setText(displayText);
            l.repaint();
        });

        return l;
    }

    /**
     * Main function for calculator starting.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }

}
