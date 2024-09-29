package backend.academy.sessions;

import backend.academy.exceptions.UnimplementedLevelException;
import backend.academy.states.Level;
import backend.academy.states.State;

public interface Session {
    State start() throws UnimplementedLevelException;
    State start(Level level) throws UnimplementedLevelException;
    State guess(Character letter);
    State sendMessage(String message);
}
