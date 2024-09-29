import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.sevices.GuessService;
import backend.academy.states.Level;
import backend.academy.states.State;
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

import java.security.SecureRandom;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GuessServiceTests {

    @Mock
    private StagesRepository _mockedStagesRepository;
    @Mock
    private WordsRepository _mockedWordRepository;

    private GuessService _guessService;

    private AutoCloseable _openMocks;

    @BeforeEach
    void init() {
        _openMocks = MockitoAnnotations.openMocks(this);
        _guessService = new GuessService(_mockedStagesRepository, _mockedWordRepository, new SecureRandom());

        Set<String> words = new TreeSet<>();
        words.add("cat");
        words.add("women");
        words.add("people");
        words.add("hippopotamus");

        when(_mockedWordRepository.getWords()).thenReturn(words);
        String thirdStage = """
            _________
            |       |
            |       O
            |
            |
            |
            |_________
            """;
        String lastStage = """
            _________
            |       |
            |       |
            |     (X X)
            |     --|--
            |      / \\
            |_________
            """;
        when(_mockedStagesRepository.getStage(2)).thenReturn(thirdStage);

        when(_mockedStagesRepository.getStage(11)).thenReturn(lastStage);

        when(_mockedStagesRepository.getStage(13)).thenReturn(lastStage);

        when(_mockedStagesRepository.getCountStages()).thenReturn(12);

        when(_mockedWordRepository.getCategory("волк")).thenReturn("не тот кто а тот кто никто");
    }

    @AfterEach
    void dispose() throws Exception {
        _openMocks.close();
    }

    @Test
    void testResultGetRandomWordWithLevelForEasyLevelMayLessThen4() {
        try {
            String randomEasyWord = _guessService.getRandomWordWithLevel(Level.EASY);
            Assertions.assertTrue(randomEasyWord.length() < 4);
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testResultGetRandomWordWithLevelForMediumLevelMayBetween3And6() {
        try {
            String randomMediumWord = _guessService.getRandomWordWithLevel(Level.MEDIUM);
            Assertions.assertTrue(randomMediumWord.length() > 3 && randomMediumWord.length() < 6);
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testResultGetRandomWordWithLevelForHardLevelMayGreaterThen5() {
        try {
            String randomHardWord = _guessService.getRandomWordWithLevel(Level.HARD);
            Assertions.assertTrue(randomHardWord.length() > 5);
        } catch (UnimplementedLevelException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void testCurrentStageGetterAfterSecondLooseAttemptWithDeathScoreLessThenMaxAvailableScoreMayReturnThirdStage() {
        State stateOnSecondLooseAttempt = new StateContinue(2, "", "", Level.EASY, 11);
        String currentStageAfterSecondTry = _guessService.getCurrStage(stateOnSecondLooseAttempt);
        Assertions.assertEquals("""
            _________
            |       |
            |       O
            |
            |
            |
            |_________
            """, currentStageAfterSecondTry);
    }

    @Test
    void testCurrentStageGetterOnStateWithDeathScoreGreaterThenMaxAvailableScoreMayMayReturnLastStage() {
        State stateWithDeathScoreGreaterThenMaxAvailableScore = new StateEnd(13, "", "", Level.EASY, "");
        String currentStateForDeathScoreGreaterThenMaxAvailable =
            _guessService.getCurrStage(stateWithDeathScoreGreaterThenMaxAvailableScore);
        Assertions.assertEquals(
            """
                _________
                |       |
                |       |
                |     (X X)
                |     --|--
                |      / \\
                |_________
                """,
            currentStateForDeathScoreGreaterThenMaxAvailable

        );
    }

    @Test
    void testNextStageGetterOnStateWithDeathScore1AndEasyLevelScoreMayReturnSecondStage() {
        State stateWithEasyLevelAndDeathScore1 = new StateContinue(1, "", "", Level.EASY, 11);
        String nextStageAfterSecondFailureAttempt = _guessService.getNextStage(stateWithEasyLevelAndDeathScore1);
        Assertions.assertEquals(
            """
                _________
                |       |
                |       O
                |
                |
                |
                |_________
                """,
            nextStageAfterSecondFailureAttempt
        );
    }

    @Test
    void testNexStageGetterOnStateWithDeathScore8AndMediumLevelScoreMayReturnLastStage() {
        State stateWithMediumLevelAndDeathScore8 = new StateContinue(8, "", "", Level.MEDIUM, 11);
        String nextStageWhoseShouldBeLastStage = _guessService.getNextStage(stateWithMediumLevelAndDeathScore8);
        Assertions.assertEquals(
            """
                _________
                |       |
                |       |
                |     (X X)
                |     --|--
                |      / \\
                |_________
                """,
            nextStageWhoseShouldBeLastStage
        );
    }

    @Test
    void testNextStageGetterOnStateWithHardLevelAndDeathScore0ShouldReturnLastStage() {
        State stateWithHardLevelAndDeathScore8 = new StateContinue(0, "", "", Level.HARD, 11);
        String nextStageWhoseShouldBeLastStage = _guessService.getNextStage(stateWithHardLevelAndDeathScore8);
        Assertions.assertEquals(
            """
                _________
                |       |
                |       |
                |     (X X)
                |     --|--
                |      / \\
                |_________
                """,
            nextStageWhoseShouldBeLastStage
        );
    }

    @Test
    void testMaxScoreGetterShouldReturnStagesCount12() {
        Assertions.assertEquals(
            12,
            _guessService.getMaxScore()
        );
    }

    @Test
    void testLooseCheckerOnLossStateWithDeathScoreGreater10ShouldReturnTrue() {
        State looseState = new StateEnd(11, "", "", Level.EASY, "");
        Assertions.assertTrue(_guessService.isLoss(looseState));
    }

    @Test
    void testLooseCheckerOnNoLostStateYetWithDeathScore10ShouldReturnFalse() {
        State nonLooseState = new StateEnd(10, "", "", Level.EASY, "");
        Assertions.assertFalse(_guessService.isLoss(nonLooseState));
    }

    @Test
    void testCategoryGetterOnWolfShouldReturnBoyishQuote() {
        Assertions.assertEquals(
            "не тот кто а тот кто никто",
            _guessService.getCategory("волк")
        );
    }
}
