package calculator.calculator;

import calculator.exceptions.CalculatorException;
import calculator.interpreter.Interpreter;
import calculator.interpreter.datatypes.CValue;
import calculator.parser.Parser;
import calculator.parser.ast.AbstractSyntaxTree;

public class Calculator {
    private final Interpreter interpreter = new Interpreter(Variables.getVariables());

    public CalculatorResult handle(String input) {
        if (input.startsWith(":")) {
            return this.runCommand(input.substring(1).split("\\s+"));
        }

        try {
            AbstractSyntaxTree ast = Parser.parse(input);
            CValue res = this.interpreter.process(ast);
            return CalculatorResult.info(res);
        } catch (CalculatorException ex) {
            return CalculatorResult.error(ex);
        }
    }

    private CalculatorResult runCommand(String[] args) {
        if (args[0].length() == 0) {
            return CalculatorResult.error("Unknown command. Try :help.");
        }

        if (args[0].equalsIgnoreCase("quit")) {
            return CalculatorResult.exit("Exiting program.");
        }

        if (args[0].equalsIgnoreCase("help")) {
            return CalculatorResult.info("Help Information (WIP)");
        }

        return CalculatorResult.error("Unknown command. Try :help.");
    }
}
