package backend.academy.sessions;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import backend.academy.states.State;
import backend.academy.states.StateBegin;
import backend.academy.states.StateContinue;
import backend.academy.states.StateEnd;
import java.util.Set;
import java.util.stream.Collectors;

public class GameSession implements Session {
    private String _word;
    private Set<Character> _chars;
    private State _state;
    private final GuessService _guessService = new GuessService();

    @Override
    public State start() throws UnimplementedLevelException {
        Level defaultLevel = Level.EASY;
        _word = _guessService.getRandomWordWithLevel(defaultLevel);
        _chars = _word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        _state = new StateBegin(0, "_ ".repeat(_word.length()), defaultLevel);
        return _state;
    }

    @Override
    public State start(Level level) throws UnimplementedLevelException {
        _word = _guessService.getRandomWordWithLevel(level);
        _chars = _word.chars().mapToObj(ch -> (char) ch).collect(Collectors.toSet());
        _state = new StateBegin(0, "_ ".repeat(_word.length()), level);
        return _state;
    }

    @Override
    public State guess(Character letter) {
        if (_chars.contains(letter)) {
            String currencyWord = _state.currencyWord();
            StringBuilder nextWordState = new StringBuilder(_word.length());
            for (int i = 0; i < currencyWord.length(); i++) {
                nextWordState.append(_word.charAt(i) == letter ? letter : '_').append(' ');
            }
            String nextWord = nextWordState.toString();

            return nextWord.contains("_")
                ? new StateContinue(
                _state.deathScore(),
                _guessService.getCurrStage(_state),
                nextWord,
                _state.level())
                : new StateEnd(
                _state.deathScore(),
                _guessService.getCurrStage(_state),
                _word,
                _state.level(),
                "Ура!!!\nВы угадали слово"
            );
        } else {
            return _guessService.isLoss(_state)
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
                _word,
                _state.level()
            );
        }
    }
}
