package backend.academy.states;

public record StateContinue(Integer deathScore, String imageOfHangman, String currencyWord, Level level,
                            Integer maxScore)
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
            deathScore == 0 ? Math.ceilDiv(maxScore, level.mistakeDeathScore())
                : Math.ceilDiv(maxScore, level.mistakeDeathScore())
                    - Math.ceilDiv(deathScore, level().mistakeDeathScore()),
            imageOfHangman
        );
    }

}
