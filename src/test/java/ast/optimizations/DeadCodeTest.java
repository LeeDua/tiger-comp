package ast.optimizations;

import ast.Ast;
import ast.PrettyPrintVisitor;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by qc1iu on 18/03/16.
 */
public class DeadCodeTest
{
  @Test
  public void testOptStmIfTrue()
  {
    System.out.println("test deadcode opt Stm.If true");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream(
            "if(true){System.out.println(1);}else{x=1;}"
                .getBytes()));
    Parser p = new Parser(in);
    Ast.Stm.T stm = null;
    try {
      stm = p.parseStatement();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(stm);
    assertEquals(Ast.Stm.If.class, stm.getClass());
    System.out.println("  before opt: OK");
    DeadCode dc = new DeadCode();
    stm.accept(dc);
    assertNotNull(dc._stm);
    assertEquals(Ast.Stm.Block.class, dc._stm.getClass());
    Ast.Stm.Block block = (Ast.Stm.Block) dc._stm;
    assertEquals(1, block.stms.size());
    assertEquals(Ast.Stm.Print.class, block.stms.getFirst().getClass());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptStmIfFalse()
  {
    System.out.println("test deadcode opt Stm.If false");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream(
            "if(false){System.out.println(1);}else{x=1;}"
                .getBytes()));
    Parser p = new Parser(in);
    Ast.Stm.T stm = null;
    try {
      stm = p.parseStatement();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(stm);
    assertEquals(Ast.Stm.If.class, stm.getClass());
    System.out.println("  before opt: OK");
    DeadCode dc = new DeadCode();
    stm.accept(dc);
    assertNotNull(dc._stm);
    assertEquals(Ast.Stm.Block.class, dc._stm.getClass());
    Ast.Stm.Block block = (Ast.Stm.Block) dc._stm;
    assertEquals(1, block.stms.size());
    assertEquals(Ast.Stm.Assign.class, block.stms.getFirst().getClass());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptStmWhileFalse()
  {
    System.out.println("test deadcode opt Stm.While false");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream(
            "while(false){System.out.println(1);}}".getBytes()));
    Parser p = new Parser(in);
    Ast.Stm.T stm = null;
    try {
      stm = p.parseStatement();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(stm);
    assertEquals(Ast.Stm.While.class, stm.getClass());
    System.out.println("  before opt: OK");
    DeadCode dc = new DeadCode();
    stm.accept(dc);
    assertNull(dc._stm);
    System.out.println("  after opt: OK");
  }
}