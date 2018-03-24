package calculator.parser;

import calculator.exceptions.CalculatorException;
import calculator.exceptions.lexical.UnexpectedSymbolException;
import calculator.lexer.Lexer;
import calculator.lexer.Symbol;
import calculator.parser.ast.AbstractSyntaxTree;
import calculator.parser.ast.BinaryOperation;
import calculator.parser.ast.Token;
import calculator.util.Util;

import java.util.*;

public class Parser {
    private final Stack<Token> tokens;
    public Token workingToken;

    private Parser(Stack<Token> tokens) {
        this.tokens = tokens;
    }

    public void nextToken() {
        this.workingToken = tokens.pop();
    }

    public boolean accept(Symbol symbol) {
        if (this.workingToken.symbol == symbol) {
            if (!this.tokens.empty()) this.nextToken();
            return true;
        }

        return false;
    }

    public Token acceptMatch(Symbol symbol) {
        if (this.workingToken.symbol == symbol) {
            Token accepted = this.workingToken;
            if (!this.tokens.empty()) this.nextToken();
            return accepted;
        }

        return null;
    }

    public void expect(Symbol symbol) throws CalculatorException {
        if (this.accept(symbol)) return;
        throw new UnexpectedSymbolException(symbol, this.workingToken);
    }

    public Token expectMatch(Symbol symbol) throws CalculatorException {
        Token matched = this.acceptMatch(symbol);
        if (matched != null) return matched;

        throw new UnexpectedSymbolException(symbol, this.workingToken);
    }

    private AbstractSyntaxTree run() throws CalculatorException {
        this.nextToken();
        return this.go(Production.START);
    }

    public AbstractSyntaxTree go(Production production) throws CalculatorException {
        return production.apply(this);
    }

    public AbstractSyntaxTree repeatBinaryOperation(
        Symbol[] operators,
        Production lower,
        boolean rightAssociative
    ) throws CalculatorException {
        List<AbstractSyntaxTree> result = new ArrayList<>();
        result.add(this.go(lower));

        Set<Symbol> opSet = new HashSet<>(Arrays.asList(operators));
        while (opSet.contains(this.workingToken.symbol)) {
            result.add(this.workingToken);
            this.nextToken();
            result.add(this.go(lower));
        }

        if (result.size() == 1) return result.get(0);

        if (rightAssociative) {
            return Util.reduceRight(
                result,
                result.get(result.size() - 1),
                2,
                (AbstractSyntaxTree accumulator, Integer i) -> new BinaryOperation(
                    result.get(i - 2),
                    accumulator,
                    (Token) result.get(i - 1)
                )
            );
        }

        return Util.reduce(
            result,
            result.get(0),
            2,
            (AbstractSyntaxTree accumulator, Integer i) -> new BinaryOperation(
                accumulator,
                result.get(i + 2),
                (Token) result.get(i + 1)
            )
        );
    }

    public AbstractSyntaxTree repeatBinaryOperation(Symbol[] operators, Production lower) throws CalculatorException {
        return this.repeatBinaryOperation(operators, lower, false);
    }

    public static AbstractSyntaxTree parse(String source) throws CalculatorException {
        List<Token> tokens = Lexer.tokenize(source);
        Stack<Token> stack = new Stack<>();
        for (int i = tokens.size() - 1; i >= 0; i--) {
            stack.push(tokens.get(i));
        }

        return new Parser(stack).run();
    }
}
