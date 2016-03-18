package system;
/**
 * Created by qc1iu on 18/03/16.
 */

import ast.optimizations.DeadClass;
import codegen.C.PrettyPrintVisitor;
import codegen.C.TranslateVisitor;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by qc1iu on 16/03/16.
 */
public class DeadClassTest
{
  @Test
  public void testDeadClass() throws IOException
  {
    // mkdir
    Process mkdir = Runtime.getRuntime().exec("mkdir build/tmp/t");
    BufferedReader mkdir_br = new BufferedReader(
        new InputStreamReader(mkdir.getInputStream()));
    while (mkdir_br.readLine() != null) {
    }
    mkdir_br = new BufferedReader(
        new InputStreamReader(mkdir.getErrorStream()));
    assertNull(mkdir_br.readLine());

    try {
      InputStream in = new BufferedInputStream(
          new FileInputStream("src/test/resources/TestDeadClass.java"));
      Parser p = new Parser(in);
      ast.Ast.Program.T prog = null;
      try {
        prog = p.parser();
      } catch (ParseException e) {
        e.printStackTrace();
      }
      System.out.println("  Parse finished.");
      ElaboratorVisitor ev = new ElaboratorVisitor();
      prog.accept(ev);
      assertEquals(0, ev.getErrorStack().size());
      System.out.println("  Elaborate finished.");
      // DeadClass opt
      DeadClass dc = new DeadClass();
      prog.accept(dc);
      System.out.println("  DeadClass opt finished.");
      prog = dc.program;
      TranslateVisitor tv = new TranslateVisitor();
      assertNotNull(prog);
      prog.accept(tv);
      codegen.C.Ast.Program.T progc = tv.program;
      PrettyPrintVisitor ppc = new PrettyPrintVisitor();
      progc.accept(ppc);
      // translate
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
          new FileOutputStream("build/tmp/t/TestDeadClass.java.c")));
      w.write(ppc.toString());
      w.flush();
      w.close();
      System.out.println("  Translate finished.");
      // compile
      Process compile = Runtime.getRuntime().exec(
          "gcc -g build/tmp/t/TestDeadClass.java.c src/main/runtime/runtime.c " +
              "-o build/tmp/t/TestDeadClass.out");
      BufferedReader br = new BufferedReader(
          new InputStreamReader(compile.getErrorStream()));
      for (String err = br.readLine(); err != null; err = br.readLine()) {
        System.out.println(err);
      }
      BufferedReader stdout = new BufferedReader(
          new InputStreamReader(compile.getInputStream()));
      for (String s = stdout.readLine(); s != null; s = stdout.readLine()) {
        System.out.println(s);
      }
      System.out.println("  Compile finished.");
      // run
      Process run = Runtime.getRuntime()
          .exec("./build/tmp/t/TestDeadClass.out -heapSize 4096");
      BufferedReader run_stdout = new BufferedReader(
          new InputStreamReader(run.getInputStream()));
      for (String s = run_stdout.readLine(); s != null;
           s = run_stdout.readLine()) {
        assertEquals("10", s);
      }
    } finally {
    }
    Process p = Runtime.getRuntime().exec("rm -rf build/tmp/t");
    BufferedReader rm_br = new BufferedReader(
        new InputStreamReader(p.getInputStream()));
    while (rm_br.readLine() != null) {
    }
    new BufferedReader(
        new InputStreamReader(p.getErrorStream()));
    while (rm_br.readLine() != null) {
    }
    System.out.println("  clean finished");
  }

}
