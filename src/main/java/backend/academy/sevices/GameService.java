package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.states.Level;
import backend.academy.states.StateEnd;
import java.util.HashMap;
import java.util.Map;

public class GameService {
    private final Map<String, GameSession> gameSessionsCache = new HashMap<>();
    private final GuessService _guessService;
    public GameService(GuessService guessService) {
        _guessService = guessService;
    }

    public void createSession(String partyName) {
        GameSession session = new GameSession(_guessService);
        gameSessionsCache.put(partyName, session);
    }

    public void startSession(String partyName, Integer level) throws UnimplementedLevelException {
        GameSession session =
            gameSessionsCache.computeIfAbsent(partyName, (key) -> gameSessionsCache.put(key, new GameSession(_guessService)));
        if (level >= Level.values().length) throw new UnimplementedLevelException(String.format("Не могу начать сессию в уровнем сложности %d", level));
        if (session != null) session.start(Level.values()[level]);
    }
    public String getGameState(String partyName) {
        return gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).getCurrentState().getState();
    }
    public void tryGuess(String partyName, Character character) {
        gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).guess(character).getState();
    }
    public void getClue(String partyName) {
        String word = gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).getWordCategory();
        gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).sendMessage(word);
    }
    public boolean isGameWasEnd(String partyName) {
        return gameSessionsCache.get(partyName).getCurrentState() instanceof StateEnd;
    }
}
