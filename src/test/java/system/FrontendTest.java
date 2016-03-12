package system;

import ast.Ast;
import ast.PrettyPrintVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by qc1iu on 3/7/16.
 */
public class FrontendTest
{
  @Test
  public void testAll() throws IOException
  {
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
        PrettyPrintVisitor pp = new PrettyPrintVisitor();
        prog.accept(pp);
        BufferedWriter w = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream("build/tmp/t/" + r.fname + ".java")));
        w.write(pp.toString());
        w.flush();
        w.close();
        Process compile = Runtime.getRuntime()
            .exec("javac build/tmp/t/" + r.fname + ".java");
        BufferedReader br = new BufferedReader(
            new InputStreamReader(compile.getInputStream()));
        assertNull(br.readLine());
        System.out.println("  compile finished.");

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
        try {
          exec.waitFor();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("  exec finished");
      } finally {
        Runtime.getRuntime().exec("rm -rf build/tmp/t");
        System.out.println("  clean finished");
      }

    }
  }

}
