package calculator.parser;

import calculator.exceptions.CalculatorException;
import calculator.exceptions.semantical.ExpectedExpressionException;
import calculator.lexer.Symbol;
import calculator.parser.ast.*;
import calculator.util.CheckedFunction;

import java.util.ArrayList;
import java.util.List;

public enum Production {
    /*
     * Start
     *  = Command EOF
     */
    START((Parser p) -> {
        AbstractSyntaxTree command = p.go(Production.valueOf("COMMAND"));
        p.expect(Symbol.EOF);
        return command;
    }),

    /*
     * Command
     *  = "let" Assignment
     *  | Expression
     */
    COMMAND((Parser p) -> {
        if (p.accept(Symbol.LET)) {
            return p.go(Production.valueOf("ASSIGNMENT"));
        }

        return p.go(Production.valueOf("EXPRESSION"));
    }),

    /*
     * Assignment
     *  = Identifier "=" Expression
     *  | IDENTIFIER "(" (IDENTIFIER ("," IDENTIFIER):*):? ")" "=" Expression
     */
    ASSIGNMENT((Parser p) -> {
        Token identifier = p.expectMatch(Symbol.IDENTIFIER);

        if (p.accept(Symbol.EQUAL)) {
            AbstractSyntaxTree expression = p.go(Production.valueOf("EXPRESSION"));
            return new VariableAssignment(identifier, expression);
        }

        if (p.accept(Symbol.LPAREN)) {
            List<Token> params = new ArrayList<>();

            if (!p.accept(Symbol.RPAREN)) {
                params.add(p.expectMatch(Symbol.IDENTIFIER));
                while (p.accept(Symbol.COMMA)) {
                    params.add(p.expectMatch(Symbol.IDENTIFIER));
                }

                p.expect(Symbol.RPAREN);
            }

            p.expect(Symbol.EQUAL);
            AbstractSyntaxTree body = p.go(Production.valueOf("EXPRESSION"));
            return new FunctionAssignment(
                identifier,
                params.toArray(new Token[params.size()]),
                body
            );
        }

        throw new ExpectedExpressionException("assignment statement", p.workingToken);
    }),

    /*
     * Expression
     *  = Addition
     */
    EXPRESSION((Parser p) -> p.go(Production.valueOf("ADDITION"))),

    /*
     * Addition
     *  = Multiplication (("+" | "-") Multiplication):*
     */
    ADDITION((Parser p) ->
        p.repeatBinaryOperation(
            new Symbol[]{ Symbol.PLUS, Symbol.MINUS },
            Production.valueOf("MULTIPLICATION")
        )
    ),

    /*
     * Multiplication
     *  = Exponentiation (("*" | "/") Exponentiation):*
     */
    MULTIPLICATION((Parser p) ->
        p.repeatBinaryOperation(
            new Symbol[]{ Symbol.ASTERISK, Symbol.SLASH },
            Production.valueOf("EXPONENTIATION")
        )
    ),

    /*
     * Exponentiation
     *  = Prefix ("^" Prefix):*
     */
    EXPONENTIATION((Parser p) ->
        p.repeatBinaryOperation(
            new Symbol[]{ Symbol.CARET },
            Production.valueOf("PREFIX"),
            true
        )
    ),

    /*
     * Prefix
     *  = ("-" | "+"):? Postfix
     */
    PREFIX((Parser p) -> {
        Token prefix = p.acceptMatch(Symbol.MINUS);
        if (prefix == null) prefix = p.acceptMatch(Symbol.PLUS);
        if (prefix == null) return p.go(Production.valueOf("POSTFIX"));

        return new UnaryOperation(p.go(Production.valueOf("POSTFIX")), prefix);
    }),

    /*
     * Postfix
     *  = Application "!":?
     */
    POSTFIX((Parser p) -> {
        AbstractSyntaxTree term = p.go(Production.valueOf("APPLICATION"));

        Token postfix = p.acceptMatch(Symbol.EXCLAMATION);
        if (postfix == null) return term;

        return new UnaryOperation(term, postfix, true);
    }),

    /*
     * Application
     *  = Value ("(" Expression ("," Expression):* ")"):?
     */
    APPLICATION((Parser p) -> {
        AbstractSyntaxTree value = p.go(Production.valueOf("VALUE"));

        if (p.accept(Symbol.LPAREN)) {
            List<AbstractSyntaxTree> arguments = new ArrayList<>();

            if (!p.accept(Symbol.RPAREN)) {
                arguments.add(p.go(Production.valueOf("EXPRESSION")));
                while (p.accept(Symbol.COMMA)) {
                    arguments.add(p.go(Production.valueOf("EXPRESSION")));
                }

                p.expect(Symbol.RPAREN);
            }

            return new FunctionApplication(
                value,
                arguments.toArray(new AbstractSyntaxTree[arguments.size()])
            );
        }

        return value;
    }),

    /*
     * Value
     *  = NUMBER
     *  | IDENTIFIER
     *  | "(" Expression ")"
     *  | "[" Expression ("," Expression):* (";" Expression ("," Expression):*):* "]"
     */
    VALUE((Parser p) -> {
        Token accepted;

        accepted = p.acceptMatch(Symbol.NUMBER);
        if (accepted != null) return accepted;

        accepted = p.acceptMatch(Symbol.IDENTIFIER);
        if (accepted != null) return accepted;

        if (p.accept(Symbol.LPAREN)) {
            AbstractSyntaxTree exp = p.go(Production.valueOf("EXPRESSION"));
            p.expect(Symbol.RPAREN);
            return exp;
        }

        if (p.accept(Symbol.LBRACKET)) {
            AbstractSyntaxTree exp = p.go(Production.valueOf("EXPRESSION"));
            List<List<AbstractSyntaxTree>> matrix = new ArrayList<>();
            matrix.add(new ArrayList<>());
            matrix.get(0).add(exp);

            int size;
            if (!p.accept(Symbol.RBRACKET)) {
                while (p.accept(Symbol.COMMA)) {
                    matrix.get(0).add(p.go(Production.valueOf("EXPRESSION")));
                }

                size = matrix.get(0).size();
                int index = 0;
                while (p.accept(Symbol.SEMICOLON)) {
                    index++;
                    matrix.add(new ArrayList<>());
                    matrix.get(index).add(p.go(Production.valueOf("EXPRESSION")));

                    while (p.accept(Symbol.COMMA)) {
                        matrix.get(index).add(p.go(Production.valueOf("EXPRESSION")));
                    }

                    if (matrix.get(index).size() != size) {
                        throw new ExpectedExpressionException(String.format("matrix row to match size of %s", size), p.workingToken);
                    }
                }

                p.expect(Symbol.RBRACKET);

                AbstractSyntaxTree[][] res = new AbstractSyntaxTree[matrix.size()][];
                for (int i = 0; i < res.length; i++) {
                    AbstractSyntaxTree[] row = new AbstractSyntaxTree[matrix.get(i).size()];
                    for (int j = 0; j < row.length; j++) {
                        row[j] = matrix.get(i).get(j);
                    }

                    res[i] = row;
                }

                return new Matrix(res);
            }
        }

        throw new ExpectedExpressionException("value", p.workingToken);
    });

    private final CheckedFunction<Parser, AbstractSyntaxTree> action;

    Production(CheckedFunction<Parser, AbstractSyntaxTree> action) {
        this.action = action;
    }

    public AbstractSyntaxTree apply(Parser parser) throws CalculatorException {
        return this.action.apply(parser);
    }
}
