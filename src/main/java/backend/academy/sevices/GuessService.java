package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.states.Level;
import backend.academy.states.State;
import java.security.SecureRandom;
import java.util.List;
import java.util.random.RandomGenerator;

public class GuessService {

    private final StagesRepository _stagesRepository = new StagesRepository();
    private final WordsRepository _wordsRepository = new WordsRepository();
    private final RandomGenerator _randomizer = new SecureRandom();

    public String getRandomWordWithLevel(Level level) throws UnimplementedLevelException {
        switch (level) {
            case EASY -> {
                List<String> easyWords =
                    _wordsRepository.getWords().stream().filter(word -> word.length() < 4).toList();
                return easyWords.get(getRandomNumberBetweenZeroAnd(easyWords.size()));
            }
            case MEDIUM -> {
                List<String> mediumWords =
                    _wordsRepository.getWords().stream().filter(word -> word.length() >= 4 && word.length() < 6)
                        .toList();
                return mediumWords.get(getRandomNumberBetweenZeroAnd(mediumWords.size()));
            }
            case HARD -> {
                List<String> hardWords =
                    _wordsRepository.getWords().stream().filter(word -> word.length() >= 6).toList();
                return hardWords.get(getRandomNumberBetweenZeroAnd(hardWords.size()));
            }
        }
        throw new UnimplementedLevelException("Unknown level in currencyWord generator");
    }

    public String getNextStage(State currentState) {
        return _stagesRepository.getStage(currentState.deathScore() + currentState.level().mistakeDeathScore());
    }

    public String getCurrStage(State currentState) {
        return _stagesRepository.getStage(currentState.deathScore());
    }

    public Integer getMaxScore() {
        return _stagesRepository.getCountStages();
    }

    public Boolean isLoss(State currentState) {
        return getMaxScore() > currentState.deathScore() + currentState.level().mistakeDeathScore();
    }

    private Integer getRandomNumberBetweenZeroAnd(Integer last) {
        return _randomizer.nextInt(last - 1);
    }
}