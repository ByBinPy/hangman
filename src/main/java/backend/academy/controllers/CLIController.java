package backend.academy.controllers;

import java.io.PrintStream;

public class CLIController {
    private final static String GameMenu = """
        Привет!
        Это игра в виселицу %)
        Выбери уровень сложности и начинай играть

        Уровни сложности
        1 - легкий 2- средний 3 - сложный

        Сделай выбор нажатием клавиши уровня сложности

        Текущий уровень сложности - 1
        """;
    private static PrintStream printer = new PrintStream(System.out);
    public static void main(String[] args) throws InterruptedException {
        printer.print(GameMenu);
        printer.flush();
        Thread.sleep(4000);
        printer = new PrintStream(System.out);
        printer.print("####END###");
    }
}
