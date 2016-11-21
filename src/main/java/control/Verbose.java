package control;

public class Verbose {
  public static int level = 0;

  public static interface F {
    Object f();
  }

  public static final int SILENCE = 0;
  public static final int PASS = 1;
  public static final int SUBPASS = 2;
  public static final int DETAIL = 3;

  public static boolean order(int l) {
    if (l <= Verbose.level) {
      return true;
    } else {
      return false;
    }
  }

  public static Object trace(String s, F action, int level) {
    boolean exsit = order(level);
    if (exsit) {
      Trace.space();
      System.out.println(s + " starting...");
      Trace.indent();
    }
    // do
    Object p = action.f();

    if (exsit) {
      Trace.unindent();
      Trace.space();
      System.out.println(s + " finished...");
    }

    return p;
  }
}
