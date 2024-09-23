package backend.academy.states;

public record StateContinue(Integer deathScore, String imageOfHangman, String currencyWord, Level level)
    implements State {
    @Override
    public String getState() {
        return String.format(
            """
                 Слово: %s
                 Осталось попыток: %d
                 %s
                """,
            currencyWord,
            deathScore,
            imageOfHangman
        );
    }

}
