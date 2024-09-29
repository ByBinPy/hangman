package backend.academy.controllers;

import backend.academy.exceptions.InvalidInputGuessData;
import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.sevices.GameService;
import backend.academy.sevices.GuessService;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Scanner;

@SuppressWarnings({"UncommentedMain"})
public class CLIController {
    private final GameService gameService;
    private final Scanner scanner;
    private final PrintStream output;

    public CLIController(PrintStream output, Scanner scanner) {
        this.output = output;
        this.scanner = scanner;
        this.gameService = new GameService(new GuessService(
            new StagesRepository(), new WordsRepository(), new SecureRandom()));
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                output.print("\033[H\033[2J");
                output.flush();
            }
        } catch (Exception e) {
            output.println("Ошибка очистки отображения на экране");
        }
    }

    public void startGame() {
        while (true) {
            output.println(
                "Введите ваше имя для начала игры (для выхода наберите 'exit'):");
            String sessionName = scanner.nextLine();
            if (Objects.equals(sessionName, "exit")) {
                break;
            }
            try {
                clearConsole();
                output.println(
                    "Выберите уровень сложности (1-Легкий, 2-Средний, 3-Сложный):");
                String levelNumberInString = scanner.nextLine();
                int level = Integer.parseInt(levelNumberInString) - 1;

                gameService.createSession(sessionName);
                gameService.startSession(sessionName, level);
                playGame(sessionName);

            } catch (UnimplementedLevelException e) {
                output.println("Выбранный уровень сложности не поддерживается.");
            } catch (NumberFormatException e) {
                output.println("Некорректный ввод уровня. Пожалуйста, введите число.");
            }
        }
        output.println(partingMessage());
    }

    private String partingMessage() {
        return "Спасибо за игру\nДо скорых встреч игр";
    }

    private void playGame(String sessionName) {
        while (!gameService.isGameWasEnd(sessionName)) {
            clearConsole();
            String currentState = gameService.getGameState(sessionName);
            output.println(currentState);
            String input = scanner.nextLine().toLowerCase();
            if (input.isEmpty()) {
                output.println("Введите символ");
                continue;
            }
            if (input.equals("help")) {
                gameService.getClue(sessionName);
            } else {
                try {
                    gameService.tryGuess(sessionName, input);
                } catch (InvalidInputGuessData e) {
                    if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                        output.println("Введите только одну букву.");
                    }
                }
            }
        }
        output.println("Игра завершена!");
        output.println(gameService.getGameState(sessionName));
    }

    public static void main(String[] args) {
        CLIController gameController =
            new CLIController(System.out, new Scanner(System.in));
        gameController.startGame();
    }
}
