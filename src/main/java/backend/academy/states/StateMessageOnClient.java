package backend.academy.states;

public record StateMessageOnClient(
    Integer deathScore,
    String currencyWord,
    Level level,
    String imageOfHangman,
    String message) implements State {

    @Override
    public String getState() {
        return String.format(
            """
            %s
            Слово: %s
            %s
            """,
            message,
            currencyWord,
            imageOfHangman
            );
    }
}
