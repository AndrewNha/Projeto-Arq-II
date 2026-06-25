package mic1.cpu;

import mic1.io.ProgramReader.ProgramLine;
import mic1.ula.ALU;
import mic1.ula.ALUResult;
import mic1.ula.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Executor {

    private final int a;
    private final int b;

    public Executor(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public List<ExecutionState> run(List<ProgramLine> program) {
        Registers            registers = new Registers();
        List<ExecutionState> log       = new ArrayList<>();

        for (ProgramLine line : program) {
            registers.setIR(line.bits());
            int pc = registers.getPC();

            Instruction instr  = new Instruction(registers.getIR());
            ALUResult   result = ALU.execute(instr, a, b);

            log.add(new ExecutionState(
                registers.getIR(), pc, a, b,
                result.s, result.carryOut
            ));

            registers.incrementPC();
        }

        return log;
    }
}
