package backend.academy.repos;

import java.util.ArrayList;
import java.util.List;

public class StagesRepository {
    private final List<String> _stages = new ArrayList<>() {{
        add("""
            _________
            |       |
            |
            |
            |
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |
            |
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |       |
            |
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |      /|
            |
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |      /|\\
            |
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |      /|\\
            |      /
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |      /|\\
            |      / \\
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |     --|--
            |      / \\
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |     --|--
            |      / \\
            |
            |_________
            """);

        add("""
            _________
            |       |
            |       O
            |     --|--
            |      / \\
            |_________
            """);

        add("""
            _________
            |       |
            |       |
            |       O
            |     --|--
            |      / \\
            |_________
            """);

        add("""
            _________
            |       |
            |       |
            |     (X X)
            |     --|--
            |      / \\
            |_________
            """);
    }};

    public String getStage(Integer numberOfStage) {
        if (numberOfStage >= _stages.size()) {
            return _stages.get(_stages.size() - 1);  // исправлено для получения последней сцены
        }
        return _stages.get(numberOfStage);
    }

    public Integer getCountStages() {
        return _stages.size();
    }
}
