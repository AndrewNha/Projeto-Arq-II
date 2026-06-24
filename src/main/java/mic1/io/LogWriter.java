package mic1.io;

import mic1.cpu.ExecutionState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogWriter {

    private static final String HEADER =
        String.format("%-10s %-4s %-4s %-4s %-4s %-8s",
            "IR", "PC", "A", "B", "S", "VAI-UM");

    public static void write(String filePath, List<ExecutionState> states) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(HEADER);
            writer.newLine();
            writer.write("-".repeat(HEADER.length()));
            writer.newLine();

            for (ExecutionState state : states) {
                writer.write(formatLine(state));
                writer.newLine();
            }
        }
    }

    private static String formatLine(ExecutionState st) {
        return String.format("%-10s %-4d %-4d %-4d %-4d %-8d",
            st.ir, st.pc, st.a, st.b, st.s, st.carryOut);
    }
}
