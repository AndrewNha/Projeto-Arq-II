package mic1;

import mic1.cpu.ExecutionState;
import mic1.cpu.Executor;
import mic1.io.LogWriter;
import mic1.io.ProgramReader;
import mic1.io.ProgramReader.ProgramLine;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java mic1.Main <programa.txt> <saida.txt>");
            System.exit(1);
        }

        String inputFile  = args[0];
        String outputFile = args[1];

        try {
            List<ProgramLine>    program = ProgramReader.read(inputFile);
            List<ExecutionState> log     = new Executor().run(program);
            LogWriter.write(outputFile, log);

            System.out.println("Execucao concluida: " + log.size() + " instrucoes.");
            System.out.println("Log salvo em: " + outputFile);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }
}
