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

    public void setIR(String ir) { this.ir = ir; }
    public void incrementPC()    { this.pc++;    }
}
