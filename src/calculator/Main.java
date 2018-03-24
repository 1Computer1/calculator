package calculator;

import calculator.calculator.Calculator;
import calculator.calculator.CalculatorResult;

import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("∴ ");

            String input = sc.nextLine();
            CalculatorResult res = calculator.handle(input);

            switch (res.result) {
                case INFO:
                    System.out.println(res.message);
                    break;
                case ERROR:
                    // This should be err but streams are not synchronized
                    System.out.println(res.message);
                    break;
                case EXIT:
                    System.out.println(res.message);
                    return;
            }
        }
    }
}
