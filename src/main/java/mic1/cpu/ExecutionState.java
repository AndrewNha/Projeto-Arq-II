package mic1.cpu;

public class ExecutionState {

    public final String ir;
    public final int    pc;
    public final int    a;
    public final int    b;
    public final int    s;
    public final int    carryOut;

    public ExecutionState(String ir, int pc, int a, int b, int s, int carryOut) {
        this.ir       = ir;
        this.pc       = pc;
        this.a        = a;
        this.b        = b;
        this.s        = s;
        this.carryOut = carryOut;
    }
}
