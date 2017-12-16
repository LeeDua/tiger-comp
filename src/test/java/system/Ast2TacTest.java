package system;

import ast.Ast;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;
import tac.Tac;
import tac.Ast2TacVisitor;
import util.StreamDrainer;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Ast2TacTest {
  @Test
  public void testAll() throws IOException {
    for (int i = 0; i < Result.R.length; i++) {
      Result.R r = Result.R[i];
      System.out.println("test " + r.fname);
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
            new FileInputStream("src/test/resources/" + r.fname + ".java"));
        Parser p = new Parser(in);
        Ast.Program.T prog = null;
        try {
          prog = p.parser();
        } catch (ParseException e) {
          System.err.println("Error in parser. Need debug the javacc.");
          e.printStackTrace();
        }
        // elab
        ElaboratorVisitor elab = new ElaboratorVisitor();
        prog.accept(elab);    // elab

        // ast2tac
        Ast2TacVisitor trans = new Ast2TacVisitor();
        prog.accept(trans);



        Tac.Program.T tac = trans.prog;

        if (elab.getErrorStack().isEmpty()) {

        }else {
          new util.Todo();
        }

        // print tac to file
        tac.PrettyPrintVisitor pp = new tac.PrettyPrintVisitor();
        tac.accept(pp);
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("build/tmp/t/" + r.fname + ".java")));
        w.write(pp.toString());
        w.flush();
        w.close();
        // compile
        Process compile = Runtime.getRuntime()
            .exec("javac build/tmp/t/" + r.fname + ".java");
        BufferedReader br = new BufferedReader(
            new InputStreamReader(compile.getInputStream()));
        assertNull(br.readLine());
        new Thread(new StreamDrainer(compile.getErrorStream())).start();
        compile.getOutputStream().close();
        try {
          compile.waitFor();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("  compile finished.");

        // run
        Process exec = Runtime.getRuntime()
            .exec("java -cp build/tmp/t " + r.fname);
        br = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
        for (String err = br.readLine(); err != null; err = br.readLine()) {
          System.out.println(err);
        }
        br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String[] rr = r.r;
        for (int j = 0; j < rr.length; j++) {
          assertEquals(rr[j], br.readLine());
        }
        exec.getOutputStream().close();
        try {
          exec.waitFor();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("  exec finished");
      } finally {
        Process p = Runtime.getRuntime().exec("rm -rf build/tmp/t");
        new Thread(new StreamDrainer(p.getInputStream())).start();
        new Thread(new StreamDrainer(p.getErrorStream())).start();
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
}
