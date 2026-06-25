package mic1;

import mic1.ula.ALU;
import mic1.ula.ALUResult;
import mic1.ula.Instruction;

public class TestALU {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testExemplo();
        testTabelaVerdadeSoma();
        testIncComF0Zero();
        testEnaEnbZero();
        testInvaComEnaZero();
        testInvaComF0Zero();
        testNotBComEnaUm();
        testAMenosB();
        testPassthroughAeB();
        testSmoke();
        testInstrucoesInvalidas();

        System.out.println("\n=============================");
        System.out.printf("  PASSED: %d  FAILED: %d%n", passed, failed);
        System.out.println("=============================");
        if (failed > 0) System.exit(1);
    }

    // ------------------------------------------------------------------
    static void testExemplo() {
        System.out.println("--- Exemplo da Professora ---");
        // F0=1,F1=1,ENA=1,ENB=1,INVA=0,INC=0: S = A XOR B, Vai-um = carry
        check("A=1,B=1 → S=0,Vai=1", "111100", 1, 1, 0, 1);
    }

    // ------------------------------------------------------------------
    static void testTabelaVerdadeSoma() {
        System.out.println("--- Tabela verdade: SOMA A+B (F0=1,F1=1,ENA=1,ENB=1,INVA=0,INC=0) ---");
        check("0+0=0,Vai=0", "111100", 0, 0, 0, 0);
        check("0+1=1,Vai=0", "111100", 0, 1, 1, 0);
        check("1+0=1,Vai=0", "111100", 1, 0, 1, 0);
        check("1+1=0,Vai=1", "111100", 1, 1, 0, 1);
    }

    // ------------------------------------------------------------------
    static void testIncComF0Zero() {
        System.out.println("--- INC=1 com F0=0 (bypass logico: INC ignorado, Vai=0 sempre) ---");
        // 001101: F0=0,F1=0,ENA=1,ENB=1,INVA=0,INC=1 → AND, INC ignorado
        check("AND(0,0)+INC=0,Vai=0", "001101", 0, 0, 0, 0);
        check("AND(1,1)+INC=1,Vai=0", "001101", 1, 1, 1, 0);
        // 011101: F0=0,F1=1,ENA=1,ENB=1,INVA=0,INC=1 → OR, INC ignorado
        check("OR(0,0)+INC=0,Vai=0",  "011101", 0, 0, 0, 0);
        check("OR(1,1)+INC=1,Vai=0",  "011101", 1, 1, 1, 0);
        check("OR(0,1)+INC=1,Vai=0",  "011101", 0, 1, 1, 0);
    }

    // ------------------------------------------------------------------
    static void testEnaEnbZero() {
        System.out.println("--- ENA=0 e/ou ENB=0 (entradas forcadas a 0) ---");
        // F0=1,F1=0,ENA=0,ENB=0: soma=0+~0+0=1,Vai=0   instr=100000
        check("0+~0+0=1,Vai=0",   "100000", 1, 1, 1, 0);
        // F0=1,F1=1,ENA=0,ENB=0: soma=0+0+0=0,Vai=0    instr=110000
        check("0+0+0=0,Vai=0",    "110000", 1, 1, 0, 0);
        // F0=0,F1=0,ENA=0,ENB=0: AND(0,0)=0             instr=000000
        check("AND(0,0)=0,Vai=0", "000000", 1, 1, 0, 0);
        // F0=0,F1=1,ENA=0,ENB=0: OR(0,0)=0              instr=010000
        check("OR(0,0)=0,Vai=0",  "010000", 1, 1, 0, 0);
        // F0=1,F1=1,ENA=1,ENB=0: soma=A+0+0=A           instr=111000
        check("Passa A=0",        "111000", 0, 1, 0, 0);
        check("Passa A=1",        "111000", 1, 1, 1, 0);
        // F0=1,F1=1,ENA=0,ENB=1: soma=0+B+0=B           instr=110100
        check("Passa B=0",        "110100", 1, 0, 0, 0);
        check("Passa B=1",        "110100", 1, 1, 1, 0);
    }

    // ------------------------------------------------------------------
    static void testInvaComEnaZero() {
        System.out.println("--- INVA=1 com ENA=0 (inverte 0 → aEff=1) ---");
        // 110010: F0=1,F1=1,ENA=0,ENB=0,INVA=1,INC=0
        // aEff=0→INVA→1; bEff=0; soma=1+0+0=1,Vai=0
        check("ENA=0,ENB=0,INVA=1: 1+0=1,Vai=0", "110010", 0, 0, 1, 0);
        check("ENA=0,ENB=0,INVA=1: 1+0=1,Vai=0", "110010", 1, 1, 1, 0);  // A,B ignorados
        // 110110: F0=1,F1=1,ENA=0,ENB=1,INVA=1,INC=0
        // aEff=0→INVA→1; bEff=B; soma=1+B
        check("ENA=0,ENB=1,INVA=1,B=0: 1+0=1,Vai=0", "110110", 0, 0, 1, 0);
        check("ENA=0,ENB=1,INVA=1,B=1: 1+1=0,Vai=1", "110110", 0, 1, 0, 1);
    }

    // ------------------------------------------------------------------
    static void testInvaComF0Zero() {
        System.out.println("--- INVA=1 com F0=0 (inverte A na unidade logica) ---");
        // 001110: F0=0,F1=0,ENA=1,ENB=1,INVA=1 → AND(~A,B)
        check("AND(~0,0)=AND(1,0)=0", "001110", 0, 0, 0, 0);
        check("AND(~0,1)=AND(1,1)=1", "001110", 0, 1, 1, 0);
        check("AND(~1,0)=AND(0,0)=0", "001110", 1, 0, 0, 0);
        check("AND(~1,1)=AND(0,1)=0", "001110", 1, 1, 0, 0);
        // 011110: F0=0,F1=1,ENA=1,ENB=1,INVA=1 → OR(~A,B)
        check("OR(~0,0)=OR(1,0)=1",  "011110", 0, 0, 1, 0);
        check("OR(~0,1)=OR(1,1)=1",  "011110", 0, 1, 1, 0);
        check("OR(~1,0)=OR(0,0)=0",  "011110", 1, 0, 0, 0);
        check("OR(~1,1)=OR(0,1)=1",  "011110", 1, 1, 1, 0);
    }

    // ------------------------------------------------------------------
    static void testNotBComEnaUm() {
        System.out.println("--- NOT B via somador (F0=1,F1=0,ENA=1,ENB=1,INVA=0,INC=0 = A+~B) ---");
        // 101100: soma = A + ~B + 0
        check("0+~0=0+1=1,Vai=0", "101100", 0, 0, 1, 0);
        check("0+~1=0+0=0,Vai=0", "101100", 0, 1, 0, 0);
        check("1+~0=1+1=0,Vai=1", "101100", 1, 0, 0, 1);
        check("1+~1=1+0=1,Vai=0", "101100", 1, 1, 1, 0);
    }

    // ------------------------------------------------------------------
    static void testAMenosB() {
        System.out.println("--- A - B = A + ~B + 1 (F0=1,F1=0,ENA=1,ENB=1,INVA=0,INC=1) ---");
        // 101101
        check("0-0=0+1+1=0,Vai=1", "101101", 0, 0, 0, 1);
        check("0-1=0+0+1=1,Vai=0", "101101", 0, 1, 1, 0);
        check("1-0=1+1+1=1,Vai=1", "101101", 1, 0, 1, 1);
        check("1-1=1+0+1=0,Vai=1", "101101", 1, 1, 0, 1);
    }

    // ------------------------------------------------------------------
    static void testPassthroughAeB() {
        System.out.println("--- Passthrough A e B via INC e sinais ---");
        // NOT A: F0=0,F1=1,ENA=1,ENB=0,INVA=1 = 011010 → OR(~A,0)=~A
        check("NOT A=0 → 1", "011010", 0, 0, 1, 0);
        check("NOT A=1 → 0", "011010", 1, 0, 0, 0);
        // -A = ~A+1: F0=1,F1=1,ENA=1,ENB=0,INVA=1,INC=1 = 111011
        check("-0=0,Vai=1",  "111011", 0, 0, 0, 1);
        check("-1=1,Vai=0",  "111011", 1, 0, 1, 0);
    }

    // ------------------------------------------------------------------
    static void testSmoke() {
        System.out.println("--- Smoke: todas as 64 instrucoes x 4 pares (A,B) ---");
        int erros = 0;
        for (int code = 0; code < 64; code++) {
            String bits = String.format("%6s", Integer.toBinaryString(code)).replace(' ', '0');
            Instruction ins = new Instruction(bits);
            for (int a : new int[]{0, 1}) {
                for (int b : new int[]{0, 1}) {
                    ALUResult r = ALU.execute(ins, a, b);
                    if (r.s < 0 || r.s > 1 || r.carryOut < 0 || r.carryOut > 1) {
                        System.out.printf("  FAIL instr=%s A=%d B=%d: S=%d,Vai=%d fora de {0,1}%n",
                            bits, a, b, r.s, r.carryOut);
                        erros++;
                    }
                }
            }
        }
        if (erros == 0) { System.out.println("  PASS 256/256 combinacoes em range"); passed++; }
        else { failed++; }
    }

    // ------------------------------------------------------------------
    static void testInstrucoesInvalidas() {
        System.out.println("--- Instrucoes invalidas devem lancar excecao ---");
        for (String s : new String[]{"", "0000", "0000000", "012345", "11110a", null}) {
            try {
                new Instruction(s);
                System.out.println("  FAIL '" + s + "' deveria ter lancado excecao");
                failed++;
            } catch (IllegalArgumentException e) {
                System.out.println("  PASS '" + s + "' → excecao correta");
                passed++;
            }
        }
    }

    // ------------------------------------------------------------------
    static void check(String label, String bits, int a, int b, int expS, int expC) {
        ALUResult r = ALU.execute(new Instruction(bits), a, b);
        boolean ok = r.s == expS && r.carryOut == expC;
        System.out.printf("  %s [%s] A=%d,B=%d → S=%d,Vai=%d (esp %d,%d)%n",
            ok ? "PASS" : "FAIL", label, a, b, r.s, r.carryOut, expS, expC);
        if (ok) passed++; else failed++;
    }
}
