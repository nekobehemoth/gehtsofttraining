package com.gehtsoft.training;

import java.util.*;

public class ArithmeticExpressionEvaluator {
    private final static Map<String, Integer> operatorPriority = new HashMap<>();


    //Prepare the precedence of operators
    static {
        operatorPriority.put("+", 1);
        operatorPriority.put("-", 1);
        operatorPriority.put("*", 2);
        operatorPriority.put("/", 2);
        operatorPriority.put("u-", 3);
    }


    //Initial parsing of string, separate numbers and operators
    private static List<String> parseExpression(String expr) {
        List<String> elements = new ArrayList<>();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c) || c == '.') {
                number.append(c);
            } else {
                if(number.length() > 0) {
                    elements.add(number.toString());
                    number.setLength(0);
                }
                //Check if current character is operator
                if ("()+-*/".indexOf(c) >= 0) {
                    elements.add(String.valueOf(c));
                } else {
                    throw new IllegalArgumentException("Unsupported character: " + c);
                }
            }
        }
        if (number.length() > 0) {
            elements.add(number.toString());
            number.setLength(0);
        }
        return elements;
    }


    //Prepare expression before execution. Sorting it rely on PEMDAS using stack's "last in first out"
    private static List<String> preProcess(String expr) {
        List<String> result = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        List<String> parsedString = parseExpression(expr);
        String prev = "";
        for (String entry : parsedString) {
            if ((prev.equals("num") || prev.equals(")")) && (entry.equals("(") || entry.matches("\\d+(\\.\\d+)?"))) {
                throw new IllegalArgumentException("Missing operator between: '" + prev + "' and '" + entry + "'");
            }
            if (entry.matches("\\d+(\\.\\d+)?")) {
                result.add(entry);
                prev = "num";
            } else if (entry.equals("(")) {
                operators.push(entry);
                prev = "(";
            } else if (entry.equals(")")) {
                while(!operators.peek().equals("(")){
                    result.add(operators.pop());
                }
                operators.pop();
                prev=")";
            } else if ("+-*/".contains(entry)) {
                if (entry.equals("-") && (prev.equals("") || "+-*/(".contains(prev))) {
                    entry = "u-";
                }
                while (!operators.isEmpty()
                        && operatorPriority.containsKey(operators.peek())
                        && operatorPriority.get(entry) <= operatorPriority.get(operators.peek())) {
                    result.add(operators.pop());
                }
                operators.push(entry);
                prev = entry;
            }
        }
        while (!operators.isEmpty()) {
            result.add(operators.pop());
        }
        return result;
    }

    //Evaluate the expression in correct order
    private static double preEvaluate(List<String> entries) throws EmptyStackException {
        Stack<Double> stack = new Stack<>();
        for (String entry : entries) {
            if (entry.matches("\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(entry));
            } else if (entry.equals("u-")) {
                double a = stack.pop();
                stack.push(-a);
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (entry) {
                    case "+": stack.push(a + b);break;
                    case "-": stack.push(a - b);break;
                    case "*": stack.push(a * b);break;
                    case "/":
                        if (b == 0.0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        stack.push(a / b);
                        break;
                }
            }
        }
        return stack.pop();
    }

    public static double evaluate(String expr) {
        try {
            List<String> preProcess = preProcess(expr);
            return preEvaluate(preProcess);
        } catch (ArithmeticException e) {
            throw e;
        }
    }
}
