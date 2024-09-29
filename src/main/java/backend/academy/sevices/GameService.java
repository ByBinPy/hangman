package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.repos.StagesRepository;
import backend.academy.repos.WordsRepository;
import backend.academy.sessions.GameSession;
import backend.academy.states.Level;
import backend.academy.states.State;
import backend.academy.states.StateEnd;
import java.util.HashMap;
import java.util.Map;
import java.util.random.RandomGenerator;

public class GameService {
    private final Map<String, GameSession> gameSessionsCache = new HashMap<>();
    private final WordsRepository _wordsRepository;
    private final GuessService _guessService;
    public GameService(WordsRepository wordsRepository, GuessService guessService) {
        _wordsRepository = wordsRepository;
        _guessService = guessService;
    }

    public GameService createSession(String partyName) {
        GameSession session = new GameSession();
        gameSessionsCache.put(partyName, session);
        return this;
    }

    public GameService startSession(String partyName, Integer level) throws UnimplementedLevelException {
        GameSession session =
            gameSessionsCache.computeIfAbsent(partyName, (key) -> gameSessionsCache.put(key, new GameSession()));
        if (level >= Level.values().length) throw new UnimplementedLevelException(String.format("Не могу начать сессию в уровнем сложности %d", level));
        session.start(Level.values()[level]);
        return this;
    }
    public String getGameState(String partyName) {
        return gameSessionsCache.getOrDefault(partyName, new GameSession()).getCurrentState().getState();
    }
    public String tryGuess(String partyName, Character character) {
        return gameSessionsCache.getOrDefault(partyName, new GameSession()).guess(character).getState();
    }
    public void getClue(String partyName) {
        String word = gameSessionsCache.getOrDefault(partyName, new GameSession()).getWordCategory();
        gameSessionsCache.getOrDefault(partyName, new GameSession()).sendMessage(word);
    }
    public boolean isGameWasEnd(String partyName) {
        return gameSessionsCache.get(partyName).getCurrentState() instanceof StateEnd;
    }
}
