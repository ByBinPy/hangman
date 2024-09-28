package backend.academy.states;

public record StateBegin(Integer deathScore, String currencyWord, Level level, String imageOfHangman) implements State {
    @Override
    public String getState() {
        return String.format("""
            Текущий уровень сложности - %s
            Слово: %s
            %s""",
            level.levelName(),
            currencyWord,
            imageOfHangman);
    }

}
