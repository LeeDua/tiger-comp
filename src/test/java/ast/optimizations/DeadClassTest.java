package ast.optimizations;

import ast.Ast;
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
      System.out.println("  before opt: classes size = [1]");
      assertEquals(1, ((Ast.Program.ProgramSingle)prog).classes.size());
      // DeadClass opt
      DeadClass dc = new DeadClass();
      prog.accept(dc);
      prog = dc.program;
      System.out.println("  DeadClass opt finished.");
      System.out.println("  after opt: classes size = [0]");
      assertEquals(0, ((Ast.Program.ProgramSingle)prog).classes.size());
    } finally {
    }

  }

}