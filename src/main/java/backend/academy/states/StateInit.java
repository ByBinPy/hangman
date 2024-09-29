package backend.academy.states;

public record StateInit(Integer deathScore, String currencyWord, Level level, String imageOfHangman) implements State {
    @Override public String getState() {
        return String.format("""
            Привет!
            Это игра в виселицу X)
            Выбери уровень сложности и начинай играть

            Для получения подсказки введи слово help

            Уровни сложности
            1 - легкий 2- средний 3 - сложный

            Сделай выбор нажатием клавиши уровня сложности

            Текущий уровень сложности - %s
            """, Level.EASY.levelName());
    }
}
