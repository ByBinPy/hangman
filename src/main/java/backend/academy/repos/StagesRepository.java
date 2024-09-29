package backend.academy.repos;

import java.util.ArrayList;
import java.util.List;

public class StagesRepository {
    @SuppressWarnings({"AnonInnerLength", "MultipleStringLiterals"})
    private final List<String> stages = new ArrayList<>() {
        {
            add("""

                |       |
                |
                |
                |
                |
                |
                """);

            add("""

                |       |
                |       O
                |
                |
                |
                |
                """);

            add("""

                |       |
                |       O
                |       |
                |
                |
                |
                """);

            add("""

                |       |
                |       O
                |      /|
                |
                |
                |
                """);

            add("""

                |       |
                |       O
                |      /|\\
                |
                |
                |
                """);

            add("""

                |       |
                |       O
                |      /|\\
                |      /
                |
                |
                """);

            add("""

                |       |
                |       O
                |      /|\\
                |      / \\
                |
                |
                """);

            add("""

                |       |
                |       O
                |     --|--
                |      / \\
                |
                |
                """);

            add("""

                |       |
                |       O
                |     --|--
                |      / \\
                |
                |
                """);

            add("""

                |       |
                |       O
                |     --|--
                |      / \\
                |
                """);

            add("""

                |       |
                |       |
                |       O
                |     --|--
                |      / \\
                |
                """);

            add("""

                |       |
                |       |
                |     (X X)
                |     --|--
                |      / \\
                |
                """);
        }
    };

    public String getStage(Integer numberOfStage) {
        if (numberOfStage >= stages.size()) {
            return stages.getLast();
        }
        return stages.get(numberOfStage);
    }

    public Integer getCountStages() {
        return stages.size();
    }
}
