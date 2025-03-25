package com.example.mobilecalculator;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {
    private enum TokenType {
        number,
        div,
        mul,
        add,
        sub,
        percent,
        flip,
        openBrace,
        closeBrace
    }

    private static class Token {
        public Token(TokenType t, String str) {
            this.type = t;
            this.content = str;
        }

        public TokenType type;
        public String content;
    }

    static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private static List<Token> tokenize(String formula) {
        var result = new ArrayList<Token>();
        for (int i = 0; i < formula.length(); ++i) {
            char curr = formula.charAt(i);
            if (isNumber(curr)) {
                int numEndIndex = i + 1;
                while (numEndIndex < formula.length()
                        && (isNumber(formula.charAt(numEndIndex)) || formula.charAt(numEndIndex) == '.')) {
                    numEndIndex++;
                }
                result.add(new Token(TokenType.number, formula.substring(i, numEndIndex)));
                i = numEndIndex - 1;
            } else if (curr == '(') {
                result.add(new Token(TokenType.openBrace, "("));
            } else if (curr == ')') {
                result.add(new Token(TokenType.closeBrace, ")"));
            } else if (curr == '%') {
                result.add(new Token(TokenType.percent, "%"));
            } else if (curr == '*') {
                result.add(new Token(TokenType.mul, "*"));
            } else if (curr == '/') {
                result.add(new Token(TokenType.div, "/"));
            } else if (curr == '+') {
                result.add(new Token(TokenType.add, "+"));
            } else if (curr == '-') {
                if (result.isEmpty() || result.get(result.size() - 1).type != TokenType.number)
                    result.add(new Token(TokenType.flip, "-"));
                else result.add(new Token(TokenType.sub, "-"));
            }
        }
        return result;
    }

    private static double evalHelper(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return 0;
        }
        if (tokens.size() == 1) {
            if (tokens.get(0).type == TokenType.number) {
                return Double.parseDouble(tokens.get(0).content);
            } else {
                // TODO handle error
                return 0;
            }
        }

        boolean isMul = false;
        boolean isDiv = false;
        boolean isAdd = false;
        boolean isSub = false;
        boolean isPercent = false;
        boolean isFlip = false;
        boolean isSubexpression = false;
        int mulFoundIndex = 0;
        int divFoundIndex = 0;
        int addFoundIndex = 0;
        int subFoundIndex = 0;
        int percentFoundIndex = 0;
        int flipFoundIndex = 0;
        int subexpressionStartIndex = 0;
        int subexpressionEndIndex = 0;

        double result = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            var currentToken = tokens.get(i).type;
            if (currentToken == TokenType.mul) {
                isMul = true;
                mulFoundIndex = i;
            } else if (currentToken == TokenType.div) {
                isDiv = true;
                divFoundIndex = i;
            } else if (currentToken == TokenType.add) {
                isAdd = true;
                addFoundIndex = i;
            } else if (currentToken == TokenType.sub) {
                isSub = true;
                subFoundIndex = i;
            } else if (currentToken == TokenType.percent) {
                isPercent = true;
                percentFoundIndex = i;
            } else if (currentToken == TokenType.flip) {
                isFlip = true;
                flipFoundIndex = i;
            } else if (currentToken == TokenType.openBrace) {
                isSubexpression = true;
                i++;
                int beginIndex = i;
                int openBraceCount = 1;
                int closeBraceCount = 0;
                for (; i < tokens.size(); ++i) {
                    Token curr = tokens.get(i);
                    if (curr.type == TokenType.openBrace) openBraceCount++;
                    else if (curr.type == TokenType.closeBrace) closeBraceCount++;

                    if (openBraceCount == closeBraceCount) break;
                }

                subexpressionStartIndex = beginIndex;
                subexpressionEndIndex = i;
            }
        }

        if (isAdd) {
            var arg1 = evalHelper(tokens.subList(0, addFoundIndex));
            var arg2 = evalHelper(tokens.subList(addFoundIndex + 1, tokens.size()));
            result = arg1 + arg2;
        } else if (isSub) {
            var arg1 = evalHelper(tokens.subList(0, subFoundIndex));
            var arg2 = evalHelper(tokens.subList(subFoundIndex + 1, tokens.size()));
            result = arg1 - arg2;
        } else if (isMul) {
            var arg1 = evalHelper(tokens.subList(0, mulFoundIndex));
            var arg2 = evalHelper(tokens.subList(mulFoundIndex + 1, tokens.size()));
            result = arg1 * arg2;
        } else if (isDiv) {
            var arg1 = evalHelper(tokens.subList(0, divFoundIndex));
            var arg2 = evalHelper(tokens.subList(divFoundIndex + 1, tokens.size()));
            if (arg2 == 0) {
                arg2 = 1;
                // throw new IllegalArgumentException("Divide by zero error");
            }
            result = arg1 / arg2;

        } else if (isPercent) {
            var arg1 = evalHelper(tokens.subList(0, percentFoundIndex));
            result = arg1 * 0.01;
        } else if (isFlip) {
            var arg1 = evalHelper(tokens.subList(flipFoundIndex + 1, tokens.size()));
            result = -arg1;
        } else if (isSubexpression) {
            result = evalHelper(tokens.subList(subexpressionStartIndex, subexpressionEndIndex));
        }

        return result;
    }

    public static double evaluate(String formula) {
        List<Token> tokens = tokenize(formula);

        return evalHelper(tokens);
    }
}
