package system;

import ast.optimizations.Main;
import cfg.Cfg;
import codegen.RuntimeC.TranslateVisitor;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qc1iu on 25/04/16.
 */
public class VisualizeTest {
  @Test
  public void testVisualize() throws IOException {
    // mkdir
    try {
      Process mkdir = Runtime.getRuntime().exec("mkdir build/tmp/t");
      BufferedReader mkdir_br = new BufferedReader(
          new InputStreamReader(mkdir.getInputStream()));
      while (mkdir_br.readLine() != null) {
      }
      mkdir_br = new BufferedReader(
          new InputStreamReader(mkdir.getErrorStream()));
      assertNull(mkdir_br.readLine());

      for (int i = 0; i < Result.R.length; i++) {
        Result.R r = Result.R[i];
        System.out.println("Test " + r.fname);
        InputStream in = new BufferedInputStream(
            new FileInputStream("src/test/resources/" + r.fname + ".java"));
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

        Main astOpt = new Main(prog);
        prog = astOpt.opt();
        assertNotNull(prog);
        System.out.println("  Ast opt finished.");

        TranslateVisitor tv = new TranslateVisitor();
        assertNotNull(prog);
        prog.accept(tv);
        codegen.RuntimeC.Ast.Program.T progc = tv.program;
        assertNotNull(progc);
        System.out.println("  Translate AST to RuntimeC finished.");

        cfg.TranslateVisitor cfgTrans = new cfg.TranslateVisitor();
        progc.accept(cfgTrans);
        Cfg.Program.ProgramSingle cfgProg = cfgTrans.program;
        assertNotNull(cfgProg);
        System.out.println("  Translate RuntimeC to CFG finished.");

        cfg.VisualVisitor v = new cfg.VisualVisitor();
        cfgProg.accept(v);
      }
      System.out.println("All test case passed.");
    } finally {
      Process p = Runtime.getRuntime().exec("rm -rf build/tmp/t");
      new Thread(new util.StreamDrainer(p.getInputStream())).start();
      new Thread(new util.StreamDrainer(p.getErrorStream())).start();
      p.getOutputStream().close();
      try {
        p.waitFor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println("  clean finished");
    }
  }
}
