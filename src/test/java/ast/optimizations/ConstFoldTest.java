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
 * Created by qc1iu on 21/03/16.
 */
public class ConstFoldTest
{
  @Test
  public void testOptTimes()
  {
    System.out.println("test consfold Exp.Times");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("3*4".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Times.class, exp.getClass());
    System.out.println("  before opt: OK");
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Num.class, constFold._exp.getClass());
    Ast.Exp.Num num = (Ast.Exp.Num) constFold._exp;
    assertEquals(12, num.num);
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiTimes()
  {
    System.out.println("test consfold multi Exp.Times");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("x*5*4*3".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Times.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(x * (5 * (4 * 3)))", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Times.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("(x * 60)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptAdd()
  {
    System.out.println("test consfold Exp.Add");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("1+5".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Add.class, exp.getClass());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Num.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("6", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiAdd()
  {
    System.out.println("test consfold multi Exp.Add");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("x+5+4+3".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Add.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(x + (5 + (4 + 3)))", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Add.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("(x + 12)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptSub()
  {
    System.out.println("test consfold Exp.Sub");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("1-2".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Sub.class, exp.getClass());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Num.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("-1", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testOptMultiSub()
  {
    System.out.println("test consfold multi Exp.Sub");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("x-1-2-3".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Sub.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(x - (1 + (2 + 3)))", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.Sub.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("(x - 6)", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testExpNot()
  {
    System.out.println("test consfold multi Exp.Not");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("!!!true".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.Not.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(!((!((!(true))))))", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.False.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("false", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testExpAnd()
  {
    System.out.println("test consfold Exp.And");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("true&&false".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.And.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(true && false)", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.False.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("false", pp_after.toString());
    System.out.println("  after opt: OK");
  }

  @Test
  public void testExpMultiAnd()
  {
    System.out.println("test consfold multi Exp.And");
    InputStream in = new BufferedInputStream(
        new ByteArrayInputStream("true&&false&&true&&!true".getBytes()));
    Parser p = new Parser(in);
    Ast.Exp.T exp = null;
    try {
      exp = p.parseExp();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assertNotNull(exp);
    assertEquals(Ast.Exp.And.class, exp.getClass());
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    exp.accept(pp);
    assertEquals("(true && (false && (true && (!(true)))))", pp.toString());
    System.out.println("  before opt: OK");
    PrettyPrintVisitor pp_after = new PrettyPrintVisitor();
    ConstFold constFold = new ConstFold();
    exp.accept(constFold);
    assertEquals(Ast.Exp.False.class, constFold._exp.getClass());
    constFold._exp.accept(pp_after);
    assertEquals("false", pp_after.toString());
    System.out.println("  after opt: OK");
  }
}