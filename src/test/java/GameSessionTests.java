import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import backend.academy.states.State;
import backend.academy.states.StateContinue;
import backend.academy.states.StateEnd;
import backend.academy.states.StateMessageOnClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameSessionTests {
    @Mock
    private GuessService _mockedGuessService;
    @Mock
    private State _mockedState;
    private GameSession _gameSession;
    AutoCloseable _openMocks;

    @BeforeEach
    public void init() throws UnimplementedLevelException {
        _openMocks = MockitoAnnotations.openMocks(this);
        _gameSession = new GameSession(_mockedState, _mockedGuessService);
        when(_mockedGuessService.getCategory(anyString())).thenReturn("Домашнее животное");
        when(_mockedGuessService.getRandomWordWithLevel(Level.EASY)).thenReturn("Кот");
        when(_mockedGuessService.getRandomWordWithLevel(Level.MEDIUM)).thenReturn("Котик");
        when(_mockedGuessService.getRandomWordWithLevel(Level.HARD)).thenReturn("Котяра");
        when(_mockedGuessService.getCurrStage(any(State.class))).thenReturn("HIPHIHIPHI");
        when(_mockedGuessService.getMaxScore()).thenReturn(12);
    }

    @AfterEach
    public void dispose() throws Exception {
        _openMocks.close();
    }

    @Test
    void testStartWithoutLevelShouldReturnStateWithEasyLevel() {
        try {
            Assertions.assertEquals(Level.EASY, _gameSession.start().level());
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testStartWithLevelShouldReturnSateWithInjectLevel() {
        try {
            Assertions.assertEquals(Level.MEDIUM, _gameSession.start(Level.MEDIUM).level());
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testGetCurrentStateAfterStartingWithoutLevelShouldReturnStateWithEasyLevel() {
        try {
            _gameSession.start();
            Assertions.assertEquals(Level.EASY, _gameSession.getCurrentState().level());
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testMessageSenderShouldReturnValueWhoseInstanceOfStateMessageOnClient() {
        try {
            _gameSession.start();
            Assertions.assertInstanceOf(StateMessageOnClient.class, _gameSession.sendMessage("Hello"));
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testGuessingWhenDeathScoreLessThenMaxAvailableShouldReturnContinueSate() {
        when(_mockedGuessService.isLoss(any(State.class))).thenReturn(false);
        try {
            _gameSession.start(Level.MEDIUM);
            Assertions.assertInstanceOf(StateContinue.class, _gameSession.guess('f'));
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testGuessingWhenDeathScoreGreaterThenMaxAvailableShouldReturnEndState() {
        when(_mockedGuessService.isLoss(any(State.class))).thenReturn(true);
        try {
            _gameSession.start(Level.HARD);
            Assertions.assertInstanceOf(StateEnd.class, _gameSession.guess('f'));
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testCategoryGetterForCatShouldReturnPet() {
        try {
            _gameSession.start();
            Assertions.assertEquals("Домашнее животное", _gameSession.getWordCategory());
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }
}
