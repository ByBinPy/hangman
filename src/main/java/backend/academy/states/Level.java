package backend.academy.states;

public enum Level {
    EASY("Легкий",1),
    MEDIUM("Средний", 5),
    HARD("Сложный", 11);
    private final Integer _mistakeDeathScore;
    private final String _levelName;
    Level(String levelName, Integer mistakeDeathScore) {
        this._mistakeDeathScore = mistakeDeathScore;
        this._levelName = levelName;
    }
    public Integer mistakeDeathScore() {
        return _mistakeDeathScore;
    }
    public String levelName() {
        return _levelName;
    }
}
