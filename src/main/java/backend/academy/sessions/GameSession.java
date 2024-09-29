package backend.academy.sessions;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import backend.academy.states.State;
import backend.academy.states.StateBegin;
import backend.academy.states.StateContinue;
import backend.academy.states.StateEnd;
import backend.academy.states.StateInit;
import backend.academy.states.StateMessageOnClient;
import java.security.SecureRandom;
import java.util.Set;
import java.util.stream.Collectors;

public class GameSession implements Session {
    private String _word;
    private Set<Character> _chars;
    private State _state = new StateInit(0, "", Level.EASY, "");
    private final GuessService _guessService;

    public GameSession(GuessService guessService) {
        _guessService = guessService;
    }

    public State getCurrentState() {
        return _state;
    }
    @Override
    public State start() throws UnimplementedLevelException {
        Level defaultLevel = Level.EASY;
        _word = _guessService.getRandomWordWithLevel(defaultLevel);
        _chars = _word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        _state = new StateBegin(0, "_ ".repeat(_word.length()), defaultLevel, "-".repeat(10));
        return _state;
    }

    @Override
    public State start(Level level) throws UnimplementedLevelException {
        _word = _guessService.getRandomWordWithLevel(level);
        _chars = _word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        _state = new StateBegin(0, "_ ".repeat(_word.length()), level, "-".repeat(10));
        return _state;
    }

    @Override
    public State sendMessage(String message) {
        _state = new StateMessageOnClient(_state.deathScore(), _state.currencyWord(), _state.level(), _guessService.getCurrStage(_state),  message);
        return _state;
    }
    @Override
    public State guess(Character letter) {
        if (_chars.contains(letter)) {
            String currencyWord = _state.currencyWord();
            StringBuilder nextWordState = new StringBuilder(_word.length());
            for (int i = 0; i < currencyWord.length(); i+=2) {
                if ('а' < currencyWord.charAt(i) && currencyWord.charAt(i) < 'я') nextWordState.append(currencyWord.charAt(i)).append(' ');
                else nextWordState.append(_word.charAt(i/2) == letter ? letter : '_').append(' ');
            }
            String nextWord = nextWordState.toString();

            _state =  nextWord.contains("_")
                ? new StateContinue(
                _state.deathScore(),
                _guessService.getCurrStage(_state),
                nextWord,
                _state.level(),
                _guessService.getMaxScore())
                : new StateEnd(
                _state.deathScore(),
                _guessService.getCurrStage(_state),
                _word,
                _state.level(),
                "Ура!!!\nВы угадали слово"
            );
        } else {
            _state = _guessService.isLoss(_state)
                ? new StateEnd(
                _state.deathScore(),
                _guessService.getNextStage(_state),
                _word,
                _state.level(),
                "Вы проиграли(\nВыберите более простой уровень"
            )
                : new StateContinue(
                _state.deathScore() + _state.level().mistakeDeathScore(),
                _guessService.getNextStage(_state),
                _state.currencyWord(),
                _state.level(),
                _guessService.getMaxScore()
            );
        }
        return _state;
    }
    public String getWordCategory() {
        return _guessService.getCategory(_word);
    }
    public String getWord() {
        return _word;
    }
}
