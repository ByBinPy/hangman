package backend.academy.sessions;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import backend.academy.states.State;
import backend.academy.states.StateBegin;
import backend.academy.states.StateContinue;
import backend.academy.states.StateEnd;
import backend.academy.states.StateInit;
import backend.academy.states.StateMessageOnClient;
import java.util.Set;
import java.util.stream.Collectors;

public class GameSession implements Session {
    private static final int COUNT_UNDERLINES_FOR_DEFAULT_DRAWING_STAGE = 10;
    private String word;
    private Set<Character> chars;
    private State state = new StateInit(0, "", Level.EASY, "");
    private final GuessService guessService;

    public GameSession(GuessService guessService) {
        this.guessService = guessService;
    }

    public GameSession(State state, GuessService guessService) {
        this.state = state;
        this.guessService = guessService;
    }

    public State getCurrentState() {
        return state;
    }

    @Override
    public State start() throws UnimplementedLevelException {
        Level defaultLevel = Level.EASY;
        word = guessService.getRandomWordWithLevel(defaultLevel);
        chars =
            word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        state = new StateBegin(
            0, "_ ".repeat(word.length()), defaultLevel, "-".repeat(COUNT_UNDERLINES_FOR_DEFAULT_DRAWING_STAGE));
        return state;
    }

    @Override
    public State start(Level level) throws UnimplementedLevelException {
        word = guessService.getRandomWordWithLevel(level);
        chars =
            word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        state =
            new StateBegin(0,
                "_ ".repeat(word.length()),
                level,
                "-".repeat(COUNT_UNDERLINES_FOR_DEFAULT_DRAWING_STAGE));
        return state;
    }

    @Override
    public State sendMessage(String message) {
        state =
            new StateMessageOnClient(state.deathScore(), state.currencyWord(),
                state.level(), guessService.getCurrStage(state), message);
        return state;
    }


    @Override
    public State guess(Character letter) {
        if (chars.contains(letter)) {
            String currencyWord = state.currencyWord();
            StringBuilder nextWordState = new StringBuilder(word.length());
            for (int i = 0; i < currencyWord.length(); i += 2) {
                if ('а' < currencyWord.charAt(i) && currencyWord.charAt(i) < 'я') {
                    nextWordState.append(currencyWord.charAt(i)).append(' ');
                } else {
                    nextWordState.append(word.charAt(i / 2) == letter ? letter : '_')
                        .append(' ');
                }
            }
            String nextWord = nextWordState.toString();

            state = nextWord.contains("_")
                ? new StateContinue(state.deathScore(),
                guessService.getCurrStage(state), nextWord, state.level(),
                guessService.getMaxScore())
                : new StateEnd(state.deathScore(),
                guessService.getCurrStage(state), word, state.level(),
                "Ура!!!\nВы угадали слово");
        } else {
            state = guessService.isLoss(state)
                ? new StateEnd(state.deathScore(),
                guessService.getNextStage(state), word, state.level(),
                "Вы проиграли(\nВыберите более простой уровень")
                : new StateContinue(
                state.deathScore() + state.level().mistakeDeathScore(),
                guessService.getNextStage(state), state.currencyWord(),
                state.level(), guessService.getMaxScore());
        }
        return state;
    }

    public String getWordCategory() {
        return guessService.getCategory(word);
    }
}
