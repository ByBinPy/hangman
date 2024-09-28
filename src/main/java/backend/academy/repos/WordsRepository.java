package backend.academy.repos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WordsRepository {
    private final Map<String, String> _words = new HashMap<>() {
        {
            {
                put("яблоко", "фрукт");
                put("машина", "транспорт");
                put("слон", "животное");
                put("компьютер", "технология");
                put("роза", "цветок");
                put("лев", "животное");
                put("банан", "фрукт");
                put("поезд", "транспорт");
                put("стул", "мебель");
                put("телефон", "технология");
                put("подсолнух", "цветок");
                put("собака", "животное");
                put("виноград", "фрукт");
                put("диван", "мебель");
                put("планшет", "технология");
                put("маргаритка", "цветок");
                put("кошка", "животное");
                put("апельсин", "фрукт");
                put("автобус", "транспорт");
                put("шкаф", "мебель");
                put("телевизор", "технология");
                put("лилия", "цветок");
                put("тигр", "животное");
                put("груша", "фрукт");
                put("мотоцикл", "транспорт");
                put("полка", "мебель");
                put("камера", "технология");
                put("тюльпан", "цветок");
                put("медведь", "животное");
                put("клубника", "фрукт");
                put("самолёт", "транспорт");
                put("купюра", "мебель");
                put("наушники", "технология");
                put("орхидея", "цветок");
                put("волк", "животное");
                put("ананас", "фрукт");
                put("корабль", "транспорт");
                put("гардероб", "мебель");
                put("смарт-часы", "технология");
                put("гвоздика", "цветок");
                put("кролик", "животное");
                put("киви", "фрукт");
                put("самокат", "транспорт");
                put("выдвижной ящик", "мебель");
                put("принтер", "связано с документами");
                put("пион", "цветок");
                put("лиса", "животное");
                put("персик", "фрукт");
                put("метро", "транспорт");
                put("книжный шкаф", "мебель");
                put("дрон", "технология");
                put("мак", "цветок");
                put("олень", "животное");
                put("арбуз", "фрукт");
                put("вертолёт", "транспорт");
                put("ночной столик", "мебель");
                put("монитор", "технология");
                put("голубика", "цветок");
                put("кенгуру", "животное");
                put("грейпфрут", "фрукт");
                put("грузовик", "транспорт");
                put("табурет", "мебель");
                put("клавиатура", "компьютер");
                put("лаванда", "цветок");
                put("панда", "животное");
                put("слива", "фрукт");
                put("велосипед", "транспорт");
                put("скамейка", "мебель");
                put("мышь", "технология");
                put("гладиолус", "цветок");
                put("зебра", "животное");
                put("малина", "фрукт");
                put("фургон", "транспорт");
                put("стол", "мебель");
                put("проектор", "технология");
                put("герань", "цветок");
                put("леопард", "животное");
                put("вишня", "фрукт");
                put("круиз", "транспорт");
                put("кресло", "мебель");
                put("роутер", "технология");
                put("ноготки", "женское");
                put("жираф", "животное");
                put("манго", "фрукт");
                put("яхта", "транспорт");
                put("оттоманка", "мебель");
                put("смартфон", "технология");
                put("гепард", "животное");
            }
        }
    };

    public Set<String> getWords() {
        return _words.keySet();
    }

    public void addWord(String word, String category) {
        _words.put(word, category);
    }
}