package ast.optimizations;

import ast.Ast;
import ast.Ast.Class.ClassSingle;
import ast.Ast.Dec.DecSingle;
import ast.Ast.MainClass.MainClassSingle;
import ast.Ast.Method.MethodSingle;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Type.Boolean;
import ast.Ast.Type.ClassType;
import ast.Ast.Type.Int;
import ast.Ast.Type.IntArray;
import ast.Ast.Exp.*;
import ast.Ast.Stm.*;

import java.util.LinkedList;

// Dead code elimination optimizations on an AST.

public class DeadCode implements ast.Visitor
{
  private Ast.Class.T _class;
  private Ast.MainClass.T _mainClass;
  private Ast.Stm.T _stm;
  private Ast.Method.T _method;
  private boolean isTrue;
  public ast.Ast.Program.T program;

  public DeadCode()
  {
    this._class = null;
    this._mainClass = null;
    this.program = null;
  }

  // expressions
  @Override
  public void visit(Add e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(And e)
  {
    e.left.accept(this);
    boolean left = this.isTrue;
    e.right.accept(this);
    boolean right = this.isTrue;
    this.isTrue = left && right;
  }

  @Override
  public void visit(ArraySelect e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Call e)
  {
    e.retType.accept(this);
  }

  @Override
  public void visit(False e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Id e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Length e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Lt e)
  {
    /*
     * Although we can do some magic in here to opt
		 * Exp like 1<2 -> true, but the real work is in
		 * const-fold.go
		 */
    this.isTrue = false;
  }

  @Override
  public void visit(NewIntArray e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(NewObject e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Not e)
  {
    boolean current = this.isTrue;
    e.exp.accept(this);
    this.isTrue = current && this.isTrue;
  }

  @Override
  public void visit(Num e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Sub e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(This e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(Times e)
  {
    this.isTrue = false;
  }

  @Override
  public void visit(True e)
  {
    this.isTrue = true;
  }

  // statements
  @Override
  public void visit(Assign s)
  {
    this._stm = s;
  }

  @Override
  public void visit(AssignArray s)
  {
    this._stm = s;
  }

  @Override
  public void visit(Block s)
  {
    LinkedList<Ast.Stm.T> stms = new LinkedList<>();
    for (Ast.Stm.T stm : s.stms) {
      stm.accept(this);
      if (this._stm != null) {
        stms.add(this._stm);
      }
    }
    this._stm = new Ast.Stm.Block(stms);
  }

  @Override
  public void visit(If s)
  {
    s.condition.accept(this);
    if (this.isTrue) {
      this._stm = s.thenn;
    } else {
      this._stm = s.elsee;
    }
  }

  @Override
  public void visit(Print s)
  {
    this._stm = s;
  }

  @Override
  public void visit(While s)
  {
    s.condition.accept(this);
    if (this.isTrue) {
      this._stm = s;
    } else {
      this._stm = null;
    }
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
      if (this._stm != null) {
        stms.add(this._stm);
      }
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
    this._class =
        new Ast.Class.ClassSingle(c.id, c.extendss, c.decs, methods);
  }

  // main class
  @Override
  public void visit(MainClassSingle c)
  {
    c.stm.accept(this);
    this._mainClass = new Ast.MainClass.MainClassSingle(c.id, c.arg, this._stm);
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
    this.program = new Ast.Program.ProgramSingle(this._mainClass, classes);
  }
}
