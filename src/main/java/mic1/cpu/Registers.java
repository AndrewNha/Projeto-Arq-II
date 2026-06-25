package mic1.cpu;

public class Registers {

    private String ir;
    private int    pc;

    public Registers() {
        this.ir = "000000";
        this.pc = 0;
    }

    public String getIR() { return ir; }
    public int    getPC() { return pc; }

    public void setIR(String ir) {
        if (ir == null || ir.length() != 6 || !ir.matches("[01]{6}")) {
            throw new IllegalArgumentException(
                "IR invalido: '" + ir + "'. Esperado 6 bits binarios.");
        }
        this.ir = ir;
    }
    public void incrementPC()    { this.pc++;    }
}
