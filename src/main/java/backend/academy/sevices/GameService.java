package backend.academy.sevices;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.sessions.GameSession;
import backend.academy.states.State;
import java.util.HashMap;
import java.util.Map;

public class GameService {
    private final Map<String, GameSession> gameSessionsCache = new HashMap<>();

    public void createSession(String partyName) {
        GameSession session = new GameSession();
        gameSessionsCache.put(partyName, session);
    }

    public void startSession(String partyName) throws UnimplementedLevelException {
        GameSession session =
            gameSessionsCache.computeIfAbsent(partyName, (key) -> gameSessionsCache.put(key, new GameSession()));
        session.start();
    }

    public State getGameState(String partyName) {
        return gameSessionsCache.getOrDefault(partyName, new GameSession()).getCurrentState();
    }
}
