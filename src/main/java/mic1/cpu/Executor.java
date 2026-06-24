package mic1.cpu;

import mic1.io.ProgramReader.ProgramLine;
import mic1.ula.ALU;
import mic1.ula.ALUResult;
import mic1.ula.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Executor {

    public List<ExecutionState> run(List<ProgramLine> program) {
        Registers            registers = new Registers();
        List<ExecutionState> log       = new ArrayList<>();

        for (ProgramLine line : program) {
            registers.setIR(line.bits());
            int pc = registers.getPC();

            Instruction instr  = new Instruction(line.bits());
            ALUResult   result = ALU.execute(instr, line.a(), line.b());

            log.add(new ExecutionState(
                line.bits(), pc, line.a(), line.b(),
                result.s, result.carryOut
            ));

            registers.incrementPC();
        }

        return log;
    }
}
