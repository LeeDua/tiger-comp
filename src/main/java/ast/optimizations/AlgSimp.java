package ast.optimizations;

import ast.Ast.Class;
import ast.Ast.Class.ClassSingle;
import ast.Ast.Dec.DecSingle;
import ast.Ast.Exp.Add;
import ast.Ast.Exp.And;
import ast.Ast.Exp.ArraySelect;
import ast.Ast.Exp.Call;
import ast.Ast.Exp.False;
import ast.Ast.Exp.Id;
import ast.Ast.Exp.Length;
import ast.Ast.Exp.Lt;
import ast.Ast.Exp.NewIntArray;
import ast.Ast.Exp.NewObject;
import ast.Ast.Exp.Not;
import ast.Ast.Exp.Num;
import ast.Ast.Exp.Sub;
import ast.Ast.Exp.This;
import ast.Ast.Exp.Times;
import ast.Ast.Exp.True;
import ast.Ast.MainClass;
import ast.Ast.MainClass.MainClassSingle;
import ast.Ast.Method.MethodSingle;
import ast.Ast.Program;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Stm.Assign;
import ast.Ast.Stm.AssignArray;
import ast.Ast.Stm.Block;
import ast.Ast.Stm.If;
import ast.Ast.Stm.Print;
import ast.Ast.Stm.While;
import ast.Ast.Type.Boolean;
import ast.Ast.Type.ClassType;
import ast.Ast.Type.Int;
import ast.Ast.Type.IntArray;

// Algebraic simplification optimizations on an AST.

public class AlgSimp implements ast.Visitor
{
  private Class.T newClass;
  private MainClass.T mainClass;
  public Program.T program;

  public AlgSimp()
  {
    this.newClass = null;
    this.mainClass = null;
    this.program = null;
  }

  // //////////////////////////////////////////////////////
  // 
  public String genId()
  {
    return util.Temp.next();
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(Add e)
  {
  }

  @Override
  public void visit(And e)
  {
  }

  @Override
  public void visit(ArraySelect e)
  {
  }

  @Override
  public void visit(Call e)
  {
  }

  @Override
  public void visit(False e)
  {
  }

  @Override
  public void visit(Id e)
  {
  }

  @Override
  public void visit(Length e)
  {
  }

  @Override
  public void visit(Lt e)
  {
  }

  @Override
  public void visit(NewIntArray e)
  {
  }

  @Override
  public void visit(NewObject e)
  {
  }

  @Override
  public void visit(Not e)
  {
  }

  @Override
  public void visit(Num e)
  {
  }

  @Override
  public void visit(Sub e)
  {
  }

  @Override
  public void visit(This e)
  {
  }

  @Override
  public void visit(Times e)
  {

  }

  @Override
  public void visit(True e)
  {
  }

  /////////////////////////////////////////
  // statements
  @Override
  public void visit(Assign s)
  {

  }

  @Override
  public void visit(AssignArray s)
  {
  }

  @Override
  public void visit(Block s)
  {
  }

  @Override
  public void visit(If s)
  {

  }

  @Override
  public void visit(Print s)
  {
  }

  @Override
  public void visit(While s)
  {
  }

  // type
  @Override
  public void visit(Boolean t)
  {
  }

  @Override
  public void visit(ClassType t)
  {
  }

  @Override
  public void visit(Int t)
  {
  }

  @Override
  public void visit(IntArray t)
  {
  }

  // dec
  @Override
  public void visit(DecSingle d)
  {
  }

  // method
  @Override
  public void visit(MethodSingle m)
  {
  }

  // class
  @Override
  public void visit(ClassSingle c)
  {

  }

  // main class
  @Override
  public void visit(MainClassSingle c)
  {

  }

  // program
  @Override
  public void visit(ProgramSingle p)
  {

    this.program = p;

    System.out.println("before optimization:");
    ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
    p.accept(pp);
    System.out.println("after optimization:");
    this.program.accept(pp);
  }
}
