import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.sevices.GameService;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import java.util.function.Function;
import backend.academy.states.StateContinue;
import backend.academy.states.StateEnd;
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
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceTests {

    @Mock
    private GuessService _mockedGuessService;
    @Mock
    private Map<String, GameSession> _mockedGameSessionsCache;
    @Mock
    private GameSession _mockedGameSession;
    @Mock
    private StateContinue _mockedState;
    private GameService _gameService = new GameService(_mockedGuessService, _mockedGameSessionsCache);
    private AutoCloseable _openMocks;

    @BeforeEach
    public void init() {
        _openMocks = MockitoAnnotations.openMocks(this);
        _gameService = new GameService(_mockedGuessService, _mockedGameSessionsCache);
        when(_mockedGameSessionsCache.computeIfAbsent(anyString(), any(Function.class))).thenReturn(_mockedGameSession);
        when(_mockedGameSessionsCache.getOrDefault(anyString(), any(GameSession.class))).thenReturn(_mockedGameSession);
        when(_mockedGameSession.getCurrentState()).thenReturn(_mockedState);
        when(_mockedGameSession.getWordCategory()).thenReturn("Category");
        when(_mockedGameSession.guess(anyChar())).thenReturn(_mockedState);
        when(_mockedState.getState()).thenReturn("""
                 Слово: %s
                 Осталось попыток: %d
                 %s
                """);
    }
    @AfterEach
    void dispose() throws Exception {
        _openMocks.close();
    }

    @Test
    public void testCreateSessionWithVerifyInvocationMethodPutInCacheShouldByOnce() {
        _gameService.createSession("Hello");
        verify(_mockedGameSessionsCache, times(1)).put(anyString(), any(GameSession.class));
    }
    @Test
    public void testStartSessionWithVerifyInvocationMethodStartAndPutShouldBeOnce() {
        try {
            _gameService.startSession("Hello", 1);
            verify(_mockedGameSessionsCache, times(1)).computeIfAbsent(anyString(), any(Function.class));
            verify(_mockedGameSession, times(1)).start(Level.MEDIUM);
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }
    @Test
    public void testStartWithInvalidLevelNumberShouldBeThrowUnimplementedLevelException() {
        try {
            _gameService.startSession("Hello", -1);
            Assertions.fail("Exception wasn`t invoke with invalid level!!!");
        } catch (UnimplementedLevelException e) {
            Assertions.assertTrue(true);
        }
    }
    @Test
    public void testGetGameStateShouldReturnContinueStateString() {
        Assertions.assertEquals(
            """
                 Слово: %s
                 Осталось попыток: %d
                 %s
                """,
            _gameService.getGameState("Macho"));

    }
    @Test
    public void testGuessingWithAnyCharacterShouldInvokeGuessInGameSession() {
        _gameService.tryGuess("Some", 'c');
        verify(_mockedGameSessionsCache, times(1)).getOrDefault(anyString(), any(GameSession.class));
        verify(_mockedGameSession, times(1)).guess(anyChar());
    }
    @Test
    public void testClueGetterWithAny() {
        _gameService.getClue("Kanevsk");
        verify(_mockedGameSessionsCache, times(2)).getOrDefault(anyString(), any(GameSession.class));
        verify(_mockedGameSession, times(1)).getWordCategory();
        verify(_mockedGameSession, times(1)).sendMessage(anyString());
    }
    @Test
    public void testEndCheckingWithContinueStateShouldReturnFalse() {
        StateContinue end = mock(StateContinue.class);
        when(_mockedGameSessionsCache.get(anyString())).thenReturn(_mockedGameSession);
        when(_mockedGameSession.getCurrentState()).thenReturn(end);
        Assertions.assertFalse(_gameService.isGameWasEnd("Some"));
    }
    @Test
    public void testEndCheckingWithEndStateShouldReturnTrue() {
        StateEnd end = mock(StateEnd.class);
        when(_mockedGameSessionsCache.get(anyString())).thenReturn(_mockedGameSession);
        when(_mockedGameSession.getCurrentState()).thenReturn(end);
        Assertions.assertTrue(_gameService.isGameWasEnd("Some"));
    }
}
