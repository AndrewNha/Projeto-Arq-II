package mic1.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramReader {

    public record ProgramLine(String bits, int a, int b) {}

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

                String[] parts = trimmed.split("\\s+");
                String instrBits = parts[0];

                if (instrBits.length() != 6 || !instrBits.matches("[01]{6}")) {
                    throw new IllegalArgumentException(
                        "Linha " + lineNumber + ": instrucao invalida '" + instrBits + "'");
                }

                int a = (parts.length > 1) ? parseValue(parts[1], lineNumber) : 0;
                int b = (parts.length > 2) ? parseValue(parts[2], lineNumber) : 0;

                if (a < 0 || a > 1 || b < 0 || b > 1) {
                    throw new IllegalArgumentException(
                        "Linha " + lineNumber + ": A e B devem ser bits (0 ou 1)");
                }

                lines.add(new ProgramLine(instrBits, a, b));
                lineNumber++;
            }
        }

        return lines;
    }

    private static int parseValue(String token, int lineNumber) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Linha " + lineNumber + ": valor nao numerico '" + token + "'");
        }
    }
}
