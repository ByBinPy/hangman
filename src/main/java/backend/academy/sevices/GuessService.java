package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.states.Level;
import backend.academy.states.State;
import java.util.List;
import java.util.random.RandomGenerator;

public class GuessService {
    private static final int MAX_LEN_FOR_EASY_WORD = 3;
    private static final int MIN_LEN_FOR_HARD_WORD = 6;
    private final StagesRepository stagesRepository;
    private final WordsRepository wordsRepository;
    private final RandomGenerator randomizer;

    public GuessService(
        StagesRepository stagesRepository,
        WordsRepository wordsRepository,
        RandomGenerator randomizer
    ) {
        this.stagesRepository = stagesRepository;
        this.wordsRepository = wordsRepository;
        this.randomizer = randomizer;
    }

    public String getRandomWordWithLevel(Level level) throws UnimplementedLevelException {
        switch (level) {
            case EASY -> {
                List<String> easyWords =
                    wordsRepository.getWords().stream().filter(word -> word.length() < MAX_LEN_FOR_EASY_WORD).toList();
                return easyWords.get(getRandomNumberBetweenZeroAnd(easyWords.size()));
            }
            case MEDIUM -> {
                List<String> mediumWords =
                    wordsRepository.getWords().stream().filter(word -> (word.length() > MAX_LEN_FOR_EASY_WORD)
                            && (word.length() < MIN_LEN_FOR_HARD_WORD))
                        .toList();
                return mediumWords.get(getRandomNumberBetweenZeroAnd(mediumWords.size()));
            }
            case HARD -> {
                List<String> hardWords =
                    wordsRepository.getWords().stream().filter(word -> word.length() >= MIN_LEN_FOR_HARD_WORD).toList();
                return hardWords.get(getRandomNumberBetweenZeroAnd(hardWords.size()));
            }
            default -> {
                return "Nothing";
            }
        }
    }

    public String getNextStage(State currentState) {
        return stagesRepository.getStage(currentState.deathScore() + currentState.level().mistakeDeathScore());
    }

    public String getCurrStage(State currentState) {
        return stagesRepository.getStage(currentState.deathScore());
    }

    public Integer getMaxScore() {
        return stagesRepository.getCountStages();
    }

    public Boolean isLoss(State currentState) {
        return getMaxScore() <= (currentState.deathScore() + currentState.level().mistakeDeathScore());
    }

    public String getCategory(String word) {
        return wordsRepository.getCategory(word);
    }

    private Integer getRandomNumberBetweenZeroAnd(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }
        return randomizer.nextInt(bound);
    }
}
