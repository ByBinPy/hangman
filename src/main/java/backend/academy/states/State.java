package backend.academy.states;

public interface State {
    String getState();
    String imageOfHangman();
    String currencyWord();
    Integer deathScore();
    Level level();
}
