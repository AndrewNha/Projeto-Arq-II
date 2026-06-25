package mic1.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramReader {

    public record ProgramLine(String bits) {}

    public static List<ProgramLine> read(String filePath) throws IOException {
        List<ProgramLine> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String raw;
            int lineNumber = 1;

            while ((raw = reader.readLine()) != null) {
                String trimmed = raw.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    lineNumber++;
                    continue;
                }

                if (trimmed.length() != 6 || !trimmed.matches("[01]{6}")) {
                    throw new IllegalArgumentException(
                        "Linha " + lineNumber + ": instrucao invalida '" + trimmed + "'");
                }

                lines.add(new ProgramLine(trimmed));
                lineNumber++;
            }
        }

        return lines;
    }
}
