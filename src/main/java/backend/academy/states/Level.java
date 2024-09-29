package backend.academy.states;

public enum Level {
    EASY("Легкий", 1),
    MEDIUM("Средний", 5),
    HARD("Сложный", 11);
    private final Integer mistakeDeathScore;
    private final String levelName;

    Level(String levelName, Integer mistakeDeathScore) {
        this.mistakeDeathScore = mistakeDeathScore;
        this.levelName = levelName;
    }

    public Integer mistakeDeathScore() {
        return mistakeDeathScore;
    }

    public String levelName() {
        return levelName;
    }
}
