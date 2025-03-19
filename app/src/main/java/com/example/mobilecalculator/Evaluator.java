package com.example.mobilecalculator;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {
    private enum TokenType {
        number,
        openParen,
        closeParen,
        div,
        mul,
        add,
        sub,
        percent
    }
    static private class Token {
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

    static private List<Token> tokenize(String formula) {
        var result = new ArrayList<Token>();
        for (int i = 0; i < formula.length(); ++i) {
            char curr = formula.charAt(i);
            if (isNumber(curr)) {
                int numEndIndex = i + 1;
                while (numEndIndex < formula.length() && (isNumber(formula.charAt(numEndIndex)) || formula.charAt(numEndIndex) == '.')) {
                    numEndIndex++;
                }
                result.add(new Token(TokenType.number, formula.substring(i, numEndIndex)));
                i = numEndIndex - 1;
            }
            else if (curr == '(') {
                result.add(new Token(TokenType.openParen, "("));
            }
            else if (curr == ')') {
                result.add(new Token(TokenType.closeParen, ")"));
            }
            else if (curr == '%') {
                result.add(new Token(TokenType.percent, "%"));
            }
            else if (curr == '*') {
                result.add(new Token(TokenType.mul, "*"));
            }
            else if (curr == '/') {
                result.add(new Token(TokenType.div, "/"));
            }
            else if (curr == '+') {
                result.add(new Token(TokenType.add, "+"));
            }
            else if (curr == '-') {
                result.add(new Token(TokenType.sub, "-"));
            }

        }
        return result;
    }

    static private double evalHelper(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return 0;
        }
        if (tokens.size() == 1) {
            if (tokens.get(0).type == TokenType.number) {
                return Double.parseDouble(tokens.get(0).content);
            }
            else {
                // TODO handle error
                return 0;
            }
        }

        if (tokens.get(0).type == TokenType.openParen) {
            if (tokens.get(tokens.size() - 1).type == TokenType.closeParen) {
                return evalHelper(tokens.subList(1, tokens.size() - 1));
            }
            return evalHelper(tokens.subList(1, tokens.size()));
        }

        boolean isMul = false;
        boolean isDiv = false;
        boolean isAdd = false;
        boolean isSub = false;
        int foundIndex = 0;

        double result = 0;
        for (int i = 0; i < tokens.size(); ++i) {
            var currentToken = tokens.get(i).type;
            if (currentToken == TokenType.mul) {
                isMul = true;
                foundIndex = i;
            }
            else if (currentToken == TokenType.div) {
                isDiv = true;
                foundIndex = i;
            }
            else if (currentToken == TokenType.add) {
                isAdd = true;
                foundIndex = i;
                break;
            }
            else if (currentToken == TokenType.sub) {
                isSub = true;
                foundIndex = i;
                break;
            }
            else if (currentToken == TokenType.openParen) {
                while (i < tokens.size() && tokens.get(i).type != TokenType.closeParen)
                    i++;
            }
        }

        var arg1 = evalHelper(tokens.subList(0, foundIndex));
        var arg2 = evalHelper(tokens.subList(foundIndex + 1, tokens.size()));

        if(isAdd) {
            result = arg1 + arg2;
        }
        else if(isSub) {
            result = arg1 - arg2;
        }
        else if (isMul) {
            result = arg1 * arg2;
        }
        else if (isDiv) {
            if (arg2 == 0) {
                throw new IllegalArgumentException("Divide by zero error");
            }
            result = arg1 / arg2;

        }

        return result;
    }

    static public double evaluate(String formula) {
        List<Token> tokens = tokenize(formula);

        return evalHelper(tokens);
    }
}
