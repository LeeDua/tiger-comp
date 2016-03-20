package ast.optimizations;

import ast.Ast;
import ast.PrettyPrintVisitor;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by qc1iu on 3/19/16.
 */
public class AlgSimpTest
{
  @Test
  public void testOptTimes()
  {
    System.out.println("test algsimp Exp.Times");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("x*0+2".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Add.class, exp.getClass());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertEquals(Ast.Exp.Num.class, algSimp._exp.getClass());
    Ast.Exp.Num num = (Ast.Exp.Num) algSimp._exp;
    assertEquals(2, num.num);
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiTimes()
  {
    System.out.println("test algsimp multi Exp.Times");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("x*y*3*0".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals(Ast.Exp.Times.class, exp.getClass());
    assertEquals("(x * (y * (3 * 0)))", pp.toString());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.Num.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("0", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptAdd()
  {
    System.out.println("test algsimp Exp.Add");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("1+0".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(1 + 0)", pp.toString());
    assertEquals(Ast.Exp.Add.class, exp.getClass());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.Num.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("1", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiAdd()
  {
    System.out.println("test algsimp multi Exp.Add");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("1+0+3+0".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(1 + (0 + (3 + 0)))", pp.toString());
    assertEquals(Ast.Exp.Add.class, exp.getClass());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.Add.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("(1 + 3)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptSub()
  {
    System.out.println("test algsimp Exp.Sub");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("0-1-0".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(0 - (1 - 0))", pp.toString());
    assertEquals(Ast.Exp.Sub.class, exp.getClass());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.Sub.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("(0 - 1)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiSub()
  {
    System.out.println("test algsimp multi Exp.Sub");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("0-1-0-0-0".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals(Ast.Exp.Sub.class, exp.getClass());
    assertEquals("(0 - (1 - (0 - (0 - 0))))", pp.toString());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.Sub.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("(0 - 1)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testArraySelect()
  {
    System.out.println("test algsimp Exp.ArraySelect");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("array[5*0]".getBytes()));
    javacc.Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(array[(5 * 0)])", pp.toString());
    assertEquals(Ast.Exp.ArraySelect.class, exp.getClass());
    System.out.println("  before opt: OK");
    AlgSimp algSimp = new AlgSimp();
    exp.accept(algSimp);
    assertNotNull(algSimp._exp);
    assertEquals(Ast.Exp.ArraySelect.class, algSimp._exp.getClass());
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    algSimp._exp.accept(pp_after);
    assertEquals("(array[0])", pp_after.toString());
    System.out.println("  after opt: OK");
  }

}