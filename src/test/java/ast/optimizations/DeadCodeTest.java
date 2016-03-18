package ast.optimizations;

import ast.Ast;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by qc1iu on 18/03/16.
 */
public class DeadCodeTest
{
  @Test
  public void testDeadCode() throws IOException
  {
    try {
      InputStream in = new BufferedInputStream(
          new FileInputStream("src/test/resources/TestDeadCode.java"));
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

      Ast.Class.ClassSingle cs1 =
          (Ast.Class.ClassSingle) ((Ast.Program.ProgramSingle) prog).classes
              .getFirst();
      assertEquals(1, cs1.methods.size());
      Ast.Method.MethodSingle ms1 =
          (Ast.Method.MethodSingle) cs1.methods.getFirst();
      // 2 stm before deadcode opt.
      assertEquals(2, ms1.stms.size());
      assertEquals(Ast.Stm.If.class, ms1.stms.getFirst().getClass());
      assertEquals(Ast.Stm.While.class, ms1.stms.getLast().getClass());

      // DeadCode opt
      DeadCode dc = new DeadCode();
      prog.accept(dc);
      prog = dc.program;
      System.out.println("  DeadCode opt finished.");

      Ast.Class.ClassSingle cs =
          (Ast.Class.ClassSingle) ((Ast.Program.ProgramSingle) prog).classes
              .getFirst();
      assertEquals(1, cs.methods.size());
      Ast.Method.MethodSingle ms =
          (Ast.Method.MethodSingle) cs.methods.getFirst();
      // 1 stm after deadcode opt.
      assertEquals(1, ms.stms.size());
      Ast.Stm.T stm_print = ms.stms.getFirst();
      assertEquals(Ast.Stm.Print.class, stm_print.getClass());
    } finally {
    }

  }

}