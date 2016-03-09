package ast;

import ast.Ast.*;
import ast.Ast.Exp.*;
import ast.Ast.MainClass.MainClassSingle;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Stm.Assign;
import ast.Ast.Stm.If;
import ast.Ast.Stm.Print;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by qc1iu on 02/03/16.
 */
public class PrettyPrintVisitorTest
{
  // main class: "Factorial"
  MainClass.T factorial = new MainClassSingle(
      "Factorial", "a", new Print(new Call(
      new NewObject("Fac", 1), "ComputeFac",
      new util.Flist<T>().list(new Num(10, 1)), 1), 1));

  // // class "Fac"
  Ast.Class.T fac = new Ast.Class.ClassSingle("Fac", null,
      new util.Flist<Dec.T>().list(),
      new util.Flist<Method.T>().list(new Method.MethodSingle(
          new Type.Int(), "ComputeFac", new util.Flist<Dec.T>()
          .list(new Dec.DecSingle(new Type.Int(), "num", false)),
          new util.Flist<Dec.T>().list(new Dec.DecSingle(
              new Type.Int(), "num_aux", false)), new util.Flist<Stm.T>()
          .list(new If(new Lt(new Id("num"),
              new Num(1)), new Assign("num_aux",
              new Num(1)), new Assign("num_aux",
              new Times(new Id("num"), new Call(
                  new This(), "ComputeFac",
                  new util.Flist<T>().list(new Sub(
                      new Id("num"), new Num(1), 1))))))),
          new Id("num_aux", 1))));

  // program
  ProgramSingle prog = new ProgramSingle(factorial,
      new util.Flist<Ast.Class.T>().list(fac));

  @Test
  public void testPrettyPrint()
  {
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    prog.accept(pp);
  }

  @Test
  public void testAddExp()
  {
    System.out.println("testAddExp");
    Id left = new Id("x");
    Num right = new Num(1);
    Add addExp = new Add(left, right);
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    addExp.accept(pp);
    assertEquals("x + 1", pp.toString());
  }

  @Test
  public void testCallExp()
  {
    System.out.println("testCallExp");
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    Call c = new Call(
        new NewObject("Fac", 1), "ComputeFac",
        new util.Flist<T>().list(new Num(10, 1)), 1);
    c.accept(pp);
    assertEquals("new Fac().ComputeFac(10)", pp.toString());
  }
}