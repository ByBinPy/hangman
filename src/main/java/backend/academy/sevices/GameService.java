package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.states.Level;
import backend.academy.states.StateEnd;
import java.util.HashMap;
import java.util.Map;

public class GameService {
    private final Map<String, GameSession> _gameSessionsCache;
    private final GuessService _guessService;
    public GameService(GuessService guessService) {
        _guessService = guessService;
        _gameSessionsCache = new HashMap<>();
    }
    public GameService(GuessService guessService, Map<String, GameSession> gameSessionsCache) {
        _guessService = guessService;
        _gameSessionsCache = gameSessionsCache;
    }

    public void createSession(String partyName) {
        GameSession session = new GameSession(_guessService);
        _gameSessionsCache.put(partyName, session);
    }

    public void startSession(String partyName, Integer level) throws UnimplementedLevelException {
        GameSession session =
            _gameSessionsCache.computeIfAbsent(partyName, (key) -> _gameSessionsCache.put(key, new GameSession(_guessService)));
        if (level < 0 || level >= Level.values().length) throw new UnimplementedLevelException(String.format("Не могу начать сессию в уровнем сложности %d", level));
        if (session != null) session.start(Level.values()[level]);
    }
    public String getGameState(String partyName) {
        return _gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).getCurrentState().getState();
    }
    public void tryGuess(String partyName, Character character) {
        _gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).guess(character).getState();
    }
    public void getClue(String partyName) {
        String word = _gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).getWordCategory();
        _gameSessionsCache.getOrDefault(partyName, new GameSession(_guessService)).sendMessage(word);
    }
    public boolean isGameWasEnd(String partyName) {
        return _gameSessionsCache.get(partyName).getCurrentState() instanceof StateEnd;
    }
}
