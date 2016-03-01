package control;

import java.util.HashSet;
import java.util.Set;

public class Trace
{
  private static final int STEP = 2;
  private static int INDENT = 0;
  private static Set<String> traceSet = new HashSet<String>();

  public static void indent()
  {
    INDENT += STEP;
  }

  public static void unindent()
  {
    INDENT -= STEP;
  }

  public static void space()
  {
    int i = INDENT;
    while (i-- > 0)
      System.out.print(" ");
  }

  public static boolean contains(String name)
  {
    boolean exist;

    exist = traceSet.contains(name);
    return exist;
  }

  public static void add(String name)
  {
    traceSet.add(name);
  }
}
