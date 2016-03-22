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

// Constant folding optimizations on an AST.

public class ConstFold implements ast.Visitor
{
  Ast.Class.T _class;
  Ast.MainClass.T _main;
  Ast.Stm.T _stm;
  Ast.Method.T _method;
  Ast.Exp.T _exp;
  public Program.T program;

  public ConstFold()
  {
  }

  // expressions
  @Override
  public void visit(Add e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    if ((left instanceof Ast.Exp.Num) &&
        (right instanceof Ast.Exp.Num)) {
      Ast.Exp.Num l = (Num) left;
      Ast.Exp.Num r = (Num) right;
      this._exp = new Ast.Exp.Num(l.num + r.num, e.linenum);
    } else {
      this._exp = new Ast.Exp.Add(left, right, e.linenum);
    }
  }

  @Override
  public void visit(And e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    if ((left instanceof Ast.Exp.False) ||
        right instanceof Ast.Exp.False) {
      this._exp = new Ast.Exp.False(e.linenum);
    } else if ((left instanceof Ast.Exp.True) &&
        (right instanceof Ast.Exp.True)) {
      this._exp = new Ast.Exp.True(e.linenum);
    } else {
      this._exp = new Ast.Exp.Add(left, right, e.linenum);
    }
  }

  @Override
  public void visit(ArraySelect e)
  {
    e.array.accept(this);
    Ast.Exp.T array = this._exp;
    e.index.accept(this);
    Ast.Exp.T index = this._exp;
    this._exp = new Ast.Exp.ArraySelect(array, index, e.linenum);
  }

  @Override
  public void visit(Call e)
  {
    e.caller.accept(this);
    Ast.Exp.T caller = this._exp;
    LinkedList<Ast.Exp.T> args = new LinkedList<>();
    for (Ast.Exp.T arg : e.args) {
      arg.accept(this);
      args.add(this._exp);
    }
    this._exp = new Ast.Exp.Call(caller, e.id, args, e.type, e.at, e.retType,
        e.linenum);
  }

  @Override
  public void visit(False e)
  {
    this._exp = e;
  }

  @Override
  public void visit(Id e)
  {
    this._exp = e;
  }

  @Override
  public void visit(Length e)
  {
    e.array.accept(this);
    this._exp = new Ast.Exp.Length(this._exp, e.linenum);
  }

  @Override
  public void visit(Lt e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    if ((left instanceof Ast.Exp.Num) &&
        (right instanceof Ast.Exp.Num)) {
      Ast.Exp.Num l = (Num) left;
      Ast.Exp.Num r = (Num) right;
      if (l.num < r.num) {
        this._exp = new Ast.Exp.True(e.linenum);
      } else {
        this._exp = new Ast.Exp.False(e.linenum);
      }
    } else {
      this._exp = new Ast.Exp.Lt(left, right, e.linenum);
    }
  }

  @Override
  public void visit(NewIntArray e)
  {
    e.exp.accept(this);
    this._exp = new Ast.Exp.NewIntArray(this._exp, e.linenum);
  }

  @Override
  public void visit(NewObject e)
  {
    this._exp = e;
  }

  @Override
  public void visit(Not e)
  {
    e.exp.accept(this);
    Ast.Exp.T exp = this._exp;
    if (exp instanceof Ast.Exp.True) {
      this._exp = new Ast.Exp.False(e.linenum);
    } else if (exp instanceof Ast.Exp.False) {
      this._exp = new Ast.Exp.True(e.linenum);
    } else {
      this._exp = exp;
    }
  }

  @Override
  public void visit(Num e)
  {
    this._exp = e;
  }

  @Override
  public void visit(Sub e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    if ((left instanceof Ast.Exp.Num) &&
        (right instanceof Ast.Exp.Num)) {
      Ast.Exp.Num l = (Num) left;
      Ast.Exp.Num r = (Num) right;
      this._exp = new Ast.Exp.Num(l.num - r.num, e.linenum);
    } else {
      this._exp = new Ast.Exp.Sub(left, right, e.linenum);
    }
  }

  @Override
  public void visit(This e)
  {
    this._exp = e;
  }

  @Override
  public void visit(Times e)
  {
    e.left.accept(this);
    Ast.Exp.T left = this._exp;
    e.right.accept(this);
    Ast.Exp.T right = this._exp;
    if ((left instanceof Ast.Exp.Num) &&
        (right instanceof Ast.Exp.Num)) {
      Ast.Exp.Num l = (Num) left;
      Ast.Exp.Num r = (Num) right;
      this._exp = new Ast.Exp.Num(l.num * r.num, e.linenum);
    } else {
      this._exp = new Ast.Exp.Times(left, right, e.linenum);
    }
  }

  @Override
  public void visit(True e)
  {
    this._exp = e;
  }

  // statements
  @Override
  public void visit(Assign s)
  {
    s.exp.accept(this);
    Ast.Exp.T exp = this._exp;
    this._stm = new Ast.Stm.Assign(s.id, exp, s.type, s.isField, s.linenum);
  }

  @Override
  public void visit(AssignArray s)
  {
    s.index.accept(this);
    Ast.Exp.T index = this._exp;
    s.exp.accept(this);
    Ast.Exp.T exp = this._exp;
    this._stm =
        new Ast.Stm.AssignArray(s.id, index, exp, s.type, s.isField, s.linenum);
  }

  @Override
  public void visit(Block s)
  {
    LinkedList<Ast.Stm.T> stms = new LinkedList<>();
    for (Ast.Stm.T stm : s.stms) {
      stm.accept(this);
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
    this._method =
        new Ast.Method.MethodSingle(m.retType, m.id, m.formals, m.locals, stms,
            m.retExp);
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
