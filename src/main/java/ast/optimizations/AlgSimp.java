package ast.optimizations;

import ast.Ast;
import ast.Ast.Class.ClassSingle;
import ast.Ast.Dec.DecSingle;
import ast.Ast.Exp.*;
import ast.Ast.MainClass.MainClassSingle;
import ast.Ast.Method.MethodSingle;
import ast.Ast.Program;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Stm.*;
import ast.Ast.Type.Boolean;
import ast.Ast.Type.ClassType;
import ast.Ast.Type.Int;
import ast.Ast.Type.IntArray;

import java.util.LinkedList;

// Algebraic simplification optimizations on an AST.

public class AlgSimp implements ast.Visitor
{
  boolean isZero;
  Ast.MainClass.T _main;
  Ast.Class.T _class;
  Ast.Method.T _method;
  Ast.Stm.T _stm;
  Ast.Exp.T _exp;
  boolean changed;
  public Program.T program;

  public AlgSimp()
  {
    this.program = null;
    this.changed = false;
  }

  // expressions
  @Override
  public void visit(Add e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    boolean left0 = this.isZero;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    boolean right0 = this.isZero;
    if (left0 && right0) {
      this._exp = new Ast.Exp.Num(0, e.linenum);
      this.isZero = true;
      this.changed = true;
    } else if (left0) {
      this._exp = right;
      this.isZero = false;
      this.changed = true;
    } else if (right0) {
      this._exp = left;
      this.isZero = false;
      this.changed = true;
    } else {
      this._exp = new Ast.Exp.Add(left, right, e.linenum);
      this.isZero = false;
    }
  }

  @Override
  public void visit(And e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(ArraySelect e)
  {
    e.array.accept(this);
    Ast.Exp.T array = this._exp;
    e.index.accept(this);
    Ast.Exp.T index = this._exp;
    this._exp = new Ast.Exp.ArraySelect(array, index, e.linenum);
    this.isZero = false;
  }

  @Override
  public void visit(Call e)
  {
    LinkedList<Ast.Exp.T> args = new LinkedList<>();
    e.caller.accept(this);
    Ast.Exp.T caller = this._exp;
    for (Ast.Exp.T arg : e.args) {
      arg.accept(this);
      args.add(this._exp);
    }
    this._exp = new Ast.Exp.Call(caller, e.id, args, e.type, e.at, e.retType,
        e.linenum);
    this.isZero = false;
  }

  @Override
  public void visit(False e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(Id e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(Length e)
  {
    e.array.accept(this);
    this._exp = new Ast.Exp.Length(this._exp, e.linenum);
    this.isZero = false;
  }

  @Override
  public void visit(Lt e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    this._exp = new Ast.Exp.Lt(left, right, e.linenum);
    this.isZero = false;
  }

  @Override
  public void visit(NewIntArray e)
  {
    e.exp.accept(this);
    this._exp = new Ast.Exp.NewIntArray(this._exp, e.linenum);
    this.isZero = false;
  }

  @Override
  public void visit(NewObject e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(Not e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(Num e)
  {
    this._exp = e;
    this.isZero = (e.num == 0);
  }

  @Override
  public void visit(Sub e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    boolean left0 = this.isZero;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    boolean right0 = this.isZero;
    if (right0 && left0) {
      this._exp = new Ast.Exp.Num(0, e.linenum);
      this.isZero = true;
      this.changed = true;
    } else if (right0) {
      this._exp = left;
      this.isZero = false;
      this.changed = true;
    } else {
      this._exp = new Ast.Exp.Sub(left, right, e.linenum);
      this.isZero = false;
    }
  }

  @Override
  public void visit(This e)
  {
    this._exp = e;
    this.isZero = false;
  }

  @Override
  public void visit(Times e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    boolean left0 = this.isZero;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    boolean right0 = this.isZero;
    if (left0 || right0) {
      this._exp = new Ast.Exp.Num(0, e.linenum);
      this.isZero = true;
      this.changed = true;
    } else {
      this._exp = new Ast.Exp.Times(left, right, e.linenum);
      this.isZero = false;
    }
  }

  @Override
  public void visit(True e)
  {
    this.isZero = false;
    this._exp = e;
  }

  /////////////////////////////////////////
  // statements
  @Override
  public void visit(Assign s)
  {
    s.exp.accept(this);
    this._stm = new Ast.Stm.Assign(s.id, this._exp, s.type, s.isField,
        s.linenum);
  }

  @Override
  public void visit(AssignArray s)
  {
    s.index.accept(this);
    Ast.Exp.T index = this._exp;
    s.exp.accept(this);
    Ast.Exp.T e = this._exp;
    this._stm = new Ast.Stm.AssignArray(s.id, index, e, s.type, s.isField,
        s.linenum);
  }

  @Override
  public void visit(Block s)
  {
    LinkedList<Ast.Stm.T> stms = new LinkedList<>();
    for (Ast.Stm.T ss : s.stms) {
      ss.accept(this);
      stms.add(this._stm);
    }
    this._stm = new Ast.Stm.Block(stms, s.linenum);
  }

  @Override
  public void visit(If s)
  {
    s.condition.accept(this);
    Ast.Exp.T cond = this._exp;
    s.thenn.accept(this);
    Ast.Stm.T thenn = this._stm;
    s.elsee.accept(this);
    Ast.Stm.T elsee = this._stm;
    this._stm = new Ast.Stm.If(cond, thenn, elsee, s.linenum);

  }

  @Override
  public void visit(Print s)
  {
    s.exp.accept(this);
    this._stm = new Ast.Stm.Print(this._exp, s.linenum);
  }

  @Override
  public void visit(While s)
  {
    s.condition.accept(this);
    Ast.Exp.T cond = this._exp;
    s.body.accept(this);
    Ast.Stm.T body = this._stm;
    this._stm = new Ast.Stm.While(cond, body, s.linenum);
  }

  // type
  @Override
  public void visit(Boolean t)
  {
    new util.Bug("impossible");
  }

  @Override
  public void visit(ClassType t)
  {
    new util.Bug("impossible");
  }

  @Override
  public void visit(Int t)
  {
    new util.Bug("impossible");
  }

  @Override
  public void visit(IntArray t)
  {
    new util.Bug("impossible");
  }

  // dec
  @Override
  public void visit(DecSingle d)
  {
    new util.Bug("impossible");
  }

  // method
  @Override
  public void visit(MethodSingle m)
  {
    LinkedList<Ast.Stm.T> stms = new LinkedList<>();
    for (Ast.Stm.T s : m.stms) {
      s.accept(this);
      stms.add(this._stm);
    }
    m.retExp.accept(this);
    this._method = new Ast.Method.MethodSingle(m.retType, m.id, m.formals,
        m.locals, stms, this._exp);
  }

  // class
  @Override
  public void visit(ClassSingle c)
  {
    LinkedList<Ast.Method.T> methods = new LinkedList<>();
    for (Ast.Method.T m : c.methods) {
      m.accept(this);
      methods.add(this._method);
    }
    this._class = new Ast.Class.ClassSingle(c.id, c.extendss, c.decs, methods);
  }

  // main class
  @Override
  public void visit(MainClassSingle c)
  {
    c.stm.accept(this);
    this._main = new Ast.MainClass.MainClassSingle(c.id, c.arg, this._stm);
  }

  // program
  @Override
  public void visit(ProgramSingle p)
  {
    p.mainClass.accept(this);
    LinkedList<Ast.Class.T> classes = new LinkedList<>();
    for (Ast.Class.T c : p.classes) {
      c.accept(this);
      classes.add(this._class);
    }
    this.program = new Ast.Program.ProgramSingle(this._main, classes);
  }
}
