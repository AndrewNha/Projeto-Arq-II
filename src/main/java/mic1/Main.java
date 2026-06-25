package mic1;

import mic1.cpu.ExecutionState;
import mic1.cpu.Executor;
import mic1.io.LogWriter;
import mic1.io.ProgramReader;
import mic1.io.ProgramReader.ProgramLine;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Uso: java mic1.Main <programa.txt> <saida.txt> <A> <B>");
            System.exit(1);
        }

        String inputFile  = args[0];
        String outputFile = args[1];
        int a = parseBit(args[2], "A");
        int b = parseBit(args[3], "B");

        try {
            List<ProgramLine>    program = ProgramReader.read(inputFile);
            List<ExecutionState> log     = new Executor(a, b).run(program);
            LogWriter.write(outputFile, log);

            System.out.println("Execucao concluida: " + log.size() + " instrucoes.");
            System.out.println("Log salvo em: " + outputFile);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }

    private static int parseBit(String value, String name) {
        try {
            int v = Integer.parseInt(value);
            if (v < 0 || v > 1) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException e) {
            System.err.println("Erro: " + name + " deve ser 0 ou 1.");
            System.exit(1);
            return 0;
        }
    }
}
