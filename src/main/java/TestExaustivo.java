import mic1.ula.ALU;
import mic1.ula.ALUResult;
import mic1.ula.Instruction;

public class TestExaustivo {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testJavaVsReferencia();
        testCarryChain();
        testNullInstruction();
        testIRSetGetConsistente();

        System.out.println("\n=============================");
        System.out.printf("  PASSED: %d  FAILED: %d%n", passed, failed);
        System.out.println("=============================");
        if (failed > 0) System.exit(1);
    }

    static void testJavaVsReferencia() {
        System.out.println("--- Java vs referencia independente: 256 combinacoes ---");
        int erros = 0;
        for (int code = 0; code < 64; code++) {
            int f0   = (code >> 5) & 1;
            int f1   = (code >> 4) & 1;
            int ena  = (code >> 3) & 1;
            int enb  = (code >> 2) & 1;
            int inva = (code >> 1) & 1;
            int inc  = (code >> 0) & 1;
            String bits = String.format("%6s", Integer.toBinaryString(code)).replace(' ','0');

            for (int a = 0; a <= 1; a++) {
                for (int b = 0; b <= 1; b++) {
                    int aEff = (ena == 1) ? a : 0;
                    if (inva == 1) aEff ^= 1;
                    int bEff = (enb == 1) ? b : 0;
                    int cin  = (inc == 1) ? 1 : 0;

                    int expS, expC;
                    if (f0 == 0) {
                        expS = (f1 == 0) ? (aEff & bEff) : (aEff | bEff);
                        expC = 0;
                    } else {
                        int bSec = (f1 == 0) ? (bEff ^ 1) : bEff;
                        expS = aEff ^ bSec ^ cin;
                        expC = (aEff & bSec) | (bSec & cin) | (aEff & cin);
                    }

                    ALUResult r = ALU.execute(new Instruction(bits), a, b);
                    if (r.s != expS || r.carryOut != expC) {
                        System.out.printf("  FAIL instr=%s A=%d B=%d: S=%d,Vai=%d (esp %d,%d)%n",
                            bits, a, b, r.s, r.carryOut, expS, expC);
                        erros++;
                    }
                }
            }
        }
        if (erros == 0) { System.out.println("  PASS 256/256"); passed++; }
        else failed++;
    }

    static void testCarryChain() {
        System.out.println("--- Encadeamento manual de carry ---");
        Instruction soma = new Instruction("111100");
        ALUResult r1 = ALU.execute(soma, 1, 1, 0);
        check("1+1+0=0,Vai=1", r1.s, r1.carryOut, 0, 1);
        ALUResult r2 = ALU.execute(soma, 0, 0, r1.carryOut);
        check("0+0+carry(1)=1,Vai=0", r2.s, r2.carryOut, 1, 0);
        ALUResult r3 = ALU.execute(soma, 1, 1, r2.carryOut);
        check("1+1+carry(0)=0,Vai=1", r3.s, r3.carryOut, 0, 1);
        Instruction somaInc = new Instruction("111101");
        ALUResult r4 = ALU.execute(somaInc, 0, 0, 0);
        check("INC=1 ignora carryIn: 0+0+1=1,Vai=0", r4.s, r4.carryOut, 1, 0);
    }

    static void testNullInstruction() {
        System.out.println("--- ALU.execute(null) lanca IllegalArgumentException ---");
        try {
            ALU.execute(null, 0, 0);
            System.out.println("  FAIL nenhuma excecao foi lancada");
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS IllegalArgumentException: " + e.getMessage());
            passed++;
        } catch (NullPointerException e) {
            System.out.println("  FAIL NullPointerException — falta validacao em ALU.execute");
            failed++;
        }
    }

    static void testIRSetGetConsistente() {
        System.out.println("--- Registers: setIR/getIR preservam string ---");
        mic1.cpu.Registers reg = new mic1.cpu.Registers();
        String original = "111100";
        reg.setIR(original);
        boolean ok = reg.getIR().equals(original);
        System.out.printf("  %s getIR() == setIR('%s')%n", ok ? "PASS" : "FAIL", original);
        if (ok) passed++; else failed++;
    }

    static void check(String label, int s, int c, int expS, int expC) {
        boolean ok = s == expS && c == expC;
        System.out.printf("  %s [%s]: S=%d,Vai=%d (esp %d,%d)%n",
            ok ? "PASS" : "FAIL", label, s, c, expS, expC);
        if (ok) passed++; else failed++;
    }
}
