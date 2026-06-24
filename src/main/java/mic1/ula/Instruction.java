package mic1.ula;

public class Instruction {

    public final int    f0;
    public final int    f1;
    public final int    ena;
    public final int    enb;
    public final int    inva;
    public final int    inc;
    public final String raw;

    public Instruction(String bits) {
        if (bits == null || bits.length() != 6 || !bits.matches("[01]{6}")) {
            throw new IllegalArgumentException(
                "Instrucao invalida: '" + bits + "'. Esperado 6 bits binarios.");
        }
        this.raw  = bits;
        this.f0   = bits.charAt(0) - '0';
        this.f1   = bits.charAt(1) - '0';
        this.ena  = bits.charAt(2) - '0';
        this.enb  = bits.charAt(3) - '0';
        this.inva = bits.charAt(4) - '0';
        this.inc  = bits.charAt(5) - '0';
    }
}
