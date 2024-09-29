package backend.academy.states;

public record StateEnd(Integer deathScore,
                       String imageOfHangman,
                       String currencyWord,
                       Level level,
                       String result) implements State {

    @Override
    public String getState() {
        return String.format(
            """
                %s
                Это было слово: %s
                %s
                """,
            result,
            currencyWord,
            imageOfHangman
        );
    }
}
