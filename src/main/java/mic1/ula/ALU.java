package mic1.ula;

/**
 * ULA da Mic-1 — opera sobre bits individuais (A, B ∈ {0,1}).
 *
 * Circuito (conforme figura do projeto):
 *   - ENA/ENB: habilitam A e B (0 → força a entrada a 0)
 *   - INVA: inverte A antes de entrar no somador/lógica
 *   - Unidade lógica: AND ou OR (saída usada quando F0=0)
 *   - Somador completo de 1 bit: usa aEff e bSecond (F1=0→~bEff, F1=1→bEff)
 *   - INC: força carry-in = 1 (vem-um)
 *   - F0=0 → saída da unidade lógica (bypass do somador)
 *   - F0=1 → saída do somador completo
 */
public class ALU {

    private ALU() {}

    public static ALUResult execute(Instruction instr, int a, int b) {
        return execute(instr, a, b, 0);
    }

    public static ALUResult execute(Instruction instr, int a, int b, int carryIn) {
        int aEff = (instr.ena  == 1) ? a : 0;
        if (instr.inva == 1) aEff = aEff ^ 1;
        int bEff = (instr.enb  == 1) ? b : 0;
        int cin  = (instr.inc  == 1) ? 1 : carryIn;

        if (instr.f0 == 0) {
            int s = (instr.f1 == 0) ? (aEff & bEff) : (aEff | bEff);
            return new ALUResult(s, 0);
        }

        int bSecond = (instr.f1 == 0) ? (bEff ^ 1) : bEff;
        int s       = aEff ^ bSecond ^ cin;
        int carry   = (aEff & bSecond) | (bSecond & cin) | (aEff & cin);
        return new ALUResult(s, carry);
    }
}
