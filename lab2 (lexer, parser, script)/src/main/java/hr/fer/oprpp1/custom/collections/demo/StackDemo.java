package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

    public static void main(String[] args) {

        if (args.length != 1) {
            throw new RuntimeException("Invalid input format, should only be 1 argument (the expression)");
        }

        String expression = args[0];

        String[] expElements = expression.split(" ");

        ObjectStack stack = new ObjectStack();

        stack.clear();

        for (String expElement : expElements) {
            if (isNumber(expElement)) {
                stack.push(Integer.parseInt(expElement));
            } else {
                int op2 = (Integer) stack.pop();
                int op1 = (Integer) stack.pop();

                int result;

                switch (expElement) {
                    case "+":
                        result = op1 + op2;
                        break;

                    case "-":
                        result = op1 - op2;
                        break;

                    case "*":
                        result = op1 * op2;
                        break;

                    case "/":
                        result = op1 / op2;
                        break;

                    case "%":
                        result = op1 % op2;
                        break;

                    default:
                        throw new UnsupportedOperationException("Unknown operation \""
                                + expElement +
                                "\", invalid expression");
                }

                stack.push(result);
            }

        }

        if (stack.size() != 1) {
            throw new RuntimeException("Final stack size should contain exactly 1 element (the result)");
        } else {
            int evaluation = (Integer) stack.pop();
            System.out.println("Expression evaluates to " + evaluation + ".");
        }
    }

    public static boolean isNumber(String s) {

        try {
            Integer.parseInt(s);
        } catch (NumberFormatException exc) {
            return false;
        }

        return true;
    }
}
