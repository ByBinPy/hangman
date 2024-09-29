package backend.academy.sevices;

import backend.academy.exceptions.InvalidInputGuessData;
import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.states.Level;
import backend.academy.states.StateEnd;
import java.util.HashMap;
import java.util.Map;

public class GameService {
    private final Map<String, GameSession> gameSessionsCache;
    private final GuessService guessService;

    public GameService(GuessService guessService) {
        this.guessService = guessService;
        this.gameSessionsCache = new HashMap<>();
    }

    public GameService(GuessService guessService, Map<String, GameSession> gameSessionsCache) {
        this.guessService = guessService;
        this.gameSessionsCache = gameSessionsCache;
    }

    public void createSession(String partyName) {
        GameSession session = new GameSession(guessService);
        gameSessionsCache.put(partyName, session);
    }

    public void startSession(String partyName, Integer level)
        throws UnimplementedLevelException {
        GameSession session =
            gameSessionsCache.computeIfAbsent(partyName, (key)
                -> gameSessionsCache.put(key, new GameSession(guessService)));
        if (level < 0 || level >= Level.values().length) {
            throw new UnimplementedLevelException(
                String.format("Не могу начать сессию в уровнем сложности %d", level));
        }
        if (session != null) {
            session.start(Level.values()[level]);
        }
    }

    public String getGameState(String partyName) {
        return gameSessionsCache.getOrDefault(partyName, new GameSession(guessService)).getCurrentState().getState();
    }

    public void tryGuess(String partyName, String chars) throws InvalidInputGuessData {
         String character =  chars.toLowerCase();
        if (character.length() > 1) {
            throw new InvalidInputGuessData("Length input greater then 1. Is nor character");
        }
        gameSessionsCache.getOrDefault(partyName, new GameSession(guessService)).guess(character.charAt(0)).getState();
    }

    public void getClue(String partyName) {
        String word = gameSessionsCache.getOrDefault(partyName, new GameSession(guessService)).getWordCategory();
        gameSessionsCache.getOrDefault(partyName, new GameSession(guessService)).sendMessage(word);
    }

    public boolean isGameWasEnd(String partyName) {
        return gameSessionsCache.get(partyName).getCurrentState() instanceof StateEnd;
    }
}
