package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {

    private boolean isEditable;
    private boolean isSigned;
    private String currValText;
    private double currValNum;
    private String frozenValText; //different from curr val, because frozen is what's on display while curr val is
    // actual current val in the background, current number "being written" I believe

    private Double activeOperand; // i.e. "remembered" operand, whilst you're writing a new number rn
    private DoubleBinaryOperator pendingOperation;

    private List<CalcValueListener> listeners;

    // when a number is written and then an operator button is pressed: 1) number is written in active operand
    // 2) operation is "scheduled" i.e. written in pendingOperation, but still can't be executed without a second operand
    // 3a) upon writing another operand n hitting new operation, the result is written in active operand
    // 3b) upon writing another operand n hitting "=", the result is written in current value of the calc

    public CalcModelImpl() {
        isEditable = true;
        isSigned = false;
        currValText = "";
        currValNum = 0;
        frozenValText = null;
        activeOperand = null;
        pendingOperation = null;
        listeners = new ArrayList<>();
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(l);
    }

    @Override
    public double getValue() {
        return currValNum;
    }

    @Override
    public void setValue(double value) {
        currValNum = value;
        currValText = Double.toString(currValNum);
        isEditable = false;
        fire();
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clear() {
        currValText = "";
        currValNum = 0;
        isEditable = true;
        fire();
    }

    @Override
    public void clearAll() {
        clear();
        clearActiveOperand();
        setPendingBinaryOperation(null);
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!isEditable()) {
            throw new CalculatorInputException("Value isn't editable.");
        }

        currValNum = -currValNum;
        isSigned = !isSigned;
//        if (currValText.startsWith("-")) {
//            currValText = currValText.substring(1);
//        } else {
//            currValText = "-" + currValText;
//        }
        fire();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!isEditable()) {
            throw new CalculatorInputException("Value isn't editable.");
        }

        if (currValText.isEmpty()) {
            throw new CalculatorInputException("Cannot add point to an empty number.");
        }

        if (currValText.contains(".")) {
            throw new CalculatorInputException("Multiple decimal points.");
        }

        currValText += ".";

        fire();
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
//        if (!isEditable()) {
//            throw new CalculatorInputException("Value isn't editable.");
//        }
        frozenValText = null;
        isEditable = true;

        if (currValText.length() == 308) {
            throw new CalculatorInputException("Too big number.");
        }

        try {
            String tmpCurrValText = (currValText.equals("0") ? "" : currValText) + digit;
            currValNum = Double.parseDouble((isSigned ? "-" : "") + tmpCurrValText);
            currValText = tmpCurrValText;
            fire();
        } catch (NumberFormatException e) {
            throw new CalculatorInputException("Invalid digit input.");
        }
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (activeOperand == null) {
            throw new IllegalStateException("No active operand set.");
        }

        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        this.activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    @Override
    public void freezeValue(String value) {
        isEditable = false;
        frozenValText = value;
        fire();
    }

    @Override
    public boolean hasFrozenValue() {
        return frozenValText != null;
    }

    @Override
    public String toString() {

        if (hasFrozenValue()) {
            return frozenValText;
        }

        if (Double.isInfinite(currValNum)) {
            return (currValNum < 0 ? "-" : "") + "Infinity";
        }

        if (Double.isNaN(currValNum)) {
            return "NaN";
        }

        if (currValText.isEmpty() || currValText.equals("0")) {
            return isSigned ? "-0" : "0";
        } else {
            return isSigned ? "-" + currValText : currValText;
        }
    }

    private void fire() {
        for (CalcValueListener listener : listeners) {
            listener.valueChanged(this);
        }
    }

}
