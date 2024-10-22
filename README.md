# Описание
Разработайте консольную версию игры "Виселица", в которой игрок пытается угадать загаданное слово, вводя буквы по одной за раз. Слово выбирается по уровню сложности, случайно из предварительно заданного списка слов и категории. Количество попыток ограничено, и за каждую неверную догадку визуализируется часть виселицы и фигурки висельника.

# Функциональные требования
* Программа должна выбрать случайную категорию слов, если этого не сделал пользователь.
* Программа должна выбрать уровень сложности, если этого не сделал пользователь.
* Программа должна выбирать случайное слово из заранее определенного списка слов.
* Игрок вводит одну букву за раз, чтобы угадать слово.
* Экран должен обновляться после каждого ввода, показывая уже угаданные буквы и прочерки на местах неуказанных букв.
* Визуализация виселицы должна добавляться постепенно с каждой неправильной попыткой.
* Игра завершается, когда слово угадано полностью или когда висельник полностью нарисован.
* Количество попыток ограничено и должно быть указано в начале игры.

# Нефункциональные требования
* Код должен быть написан ясно и структурировано в соответствии с требованиями, указанными в разделе "Требования к ДЗ" информационного блока.
* Программа должна иметь текстовый интерфейс.

# Описание входных и выходных данных
## Ввод
* Ввод буквы осуществляется через стандартную консоль ввода.
* Ввод не должен быть чувствительным к регистру.
## Вывод
* На каждом этапе игры должно отображаться текущее состояние угадываемого слова и виселицы.
* При успешном угадывании буквы нужно обновить состояние слова на экране.
* При неправильной букве нужно обновить изображение виселицы и указать кол-во оставшихся попыток.
