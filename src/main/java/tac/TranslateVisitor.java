package tac;

import ast.Ast;

import java.util.LinkedList;

public class TranslateVisitor implements ast.Visitor {

  private Tac.Operand.T operand;
  private Tac.Stm.T stm;
  private Tac.MainClass.T main;
  private Tac.Class.T clazz;
  public Tac.Program.T prog;
  private Tac.Dec.T dec;
  private Tac.Type.T type;
  private Tac.Method.T method;

  private LinkedList<Tac.Dec.T> genLocals;
  private LinkedList<Tac.Stm.T> stms;

  private void emit(Tac.Stm.T s) {
    stms.add(s);
  }

  private Tac.Operand.Var genVar(Tac.Type.T ty) {
    String fresh = util.Temp.next();
    Tac.Dec.DecSingle dec = new Tac.Dec.DecSingle(ty, fresh);
    this.genLocals.add(dec);
    return new Tac.Operand.Var(fresh);
  }

  public void visit(Ast.Exp.Add e) {
    Tac.Operand.T left;
    Tac.Operand.T right;
    e.left.accept(this);
    left = this.operand;
    e.right.accept(this);
    right = this.operand;

    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignBinOp((Tac.Operand.Var) this.operand,
        new Tac.BinOp.Add(),
        left, right));
  }

  @Override
  public void visit(Ast.Exp.And e) {
    Tac.Operand.T left;
    Tac.Operand.T right;
    e.left.accept(this);
    left = this.operand;
    e.right.accept(this);
    right = this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignBinOp((Tac.Operand.Var) this.operand,
        new Tac.BinOp.And(),
        left, right));
  }

  @Override
  public void visit(Ast.Exp.ArraySelect e) {
    e.array.accept(this);
    Tac.Operand.T array = this.operand;
    e.index.accept(this);
    Tac.Operand.T index = this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignArraySelect((Tac.Operand.Var) this.operand,
        (Tac.Operand.Var) array, index));
  }

  @Override
  public void visit(Ast.Exp.Call e) {
    e.caller.accept(this);
    Tac.Operand.T caller = this.operand;
    LinkedList<Tac.Operand.T> args = new LinkedList<>();
    for (Ast.Exp.T arg : e.args) {
      arg.accept(this);
      args.add(this.operand);
    }

    e.retType.accept(this);
    this.operand = genVar(this.type);

    emit(new Tac.Stm.AssignCall(this.operand, caller, e.id, args));


  }

  @Override
  public void visit(Ast.Exp.False e) {
    this.operand = new Tac.Operand.False();
  }

  @Override
  public void visit(Ast.Exp.Id e) {
    this.operand = new Tac.Operand.Var(e.id);
  }

  @Override
  public void visit(Ast.Exp.Length e) {
    e.array.accept(this);
    Tac.Operand.Var array = (Tac.Operand.Var) this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignArrayLength((Tac.Operand.Var) this.operand, array));
  }

  @Override
  public void visit(Ast.Exp.Lt e) {
    Tac.Operand.T left, right;
    e.left.accept(this);
    left = this.operand;
    e.right.accept(this);
    right = this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignBinOp((Tac.Operand.Var) this.operand,
        new Tac.BinOp.Lt(), left, right));

  }

  @Override
  public void visit(Ast.Exp.NewIntArray e) {
    e.exp.accept(this);
    Tac.Operand.T size = this.operand;
    this.type = new Tac.Type.IntArray();
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignNewIntArray(this.operand, size));
  }

  @Override
  public void visit(Ast.Exp.NewObject e) {
    this.type = new Tac.Type.ClassType(e.id);
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignNewObject(this.operand, e.id));
  }

  @Override
  public void visit(Ast.Exp.Not e) {
    e.exp.accept(this);
    Tac.Operand.T exp = this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignUnOp(this.operand, exp, new Tac.UnOp.Not()));
  }

  @Override
  public void visit(Ast.Exp.Num e) {
    this.operand = new Tac.Operand.Int(e.num);
    this.type = new Tac.Type.Int();
  }

  @Override
  public void visit(Ast.Exp.Sub e) {
    Tac.Operand.T left, right;
    e.left.accept(this);
    left = this.operand;
    e.right.accept(this);
    right = this.operand;
    this.operand = genVar(this.type);
    emit(new Tac.Stm.AssignBinOp(this.operand, new Tac.BinOp.Sub(), left,
        right));
  }

  @Override
  public void visit(Ast.Exp.This e) {
    // should have using corresponding class type.
    this.operand = new Tac.Operand.Var("this");
  }

  @Override
  public void visit(Ast.Exp.Times e) {
    Tac.Operand.T left, right;
    e.left.accept(this);
    left = this.operand;
    e.right.accept(this);
    right = this.operand;
    this.operand = genVar(this.type);

    emit(new Tac.Stm.AssignBinOp(this.operand, new Tac.BinOp.Times(), left,
        right));
  }

  @Override
  public void visit(Ast.Exp.True e) {
    this.operand = new Tac.Operand.True();
  }

  ///////////////////////////////////////////////////////////

  @Override
  public void visit(Ast.Stm.Assign s) {

    s.exp.accept(this);
    Tac.Operand.T src = this.operand;

    s.type.accept(this);
    this.operand = new Tac.Operand.Var(s.id);
    emit(new Tac.Stm.Assign(this.operand, src));

  }

  @Override
  public void visit(Ast.Stm.AssignArray s) {
    s.exp.accept(this);
    Tac.Operand.T exp = this.operand;
    s.index.accept(this);
    Tac.Operand.T index = this.operand;
    s.type.accept(this);
    this.operand = new Tac.Operand.Var(s.id);
    emit(new Tac.Stm.AssignArray((Tac.Operand.Var) this.operand,
        (Tac.Operand.Var) exp, index));

  }

  @Override
  public void visit(Ast.Stm.Block s) {
    LinkedList<Tac.Stm.T> tacStm = new LinkedList<>();
    for (Ast.Stm.T stm : s.stms) {
      stm.accept(this);
      tacStm.add(this.stm);
    }
    this.stm = new Tac.Stm.Block(tacStm);
  }

  @Override
  public void visit(Ast.Stm.If s) {
    s.condition.accept(this);
    Tac.Operand.T cond = this.operand;
    emit(new Tac.Stm.If(cond));
    s.thenn.accept(this);
    s.elsee.accept(this);
  }

  @Override
  public void visit(Ast.Stm.Print s) {
    s.exp.accept(this);
    Tac.Operand.T exp = this.operand;
    emit(new Tac.Stm.Print(exp));

  }

  @Override
  public void visit(Ast.Stm.While s) {
    s.condition.accept(this);
    emit(new Tac.Stm.While(this.operand));

    s.body.accept(this);
  }

  @Override
  public void visit(Ast.Type.Boolean t) {
    this.type = new Tac.Type.Boolean();
  }

  @Override
  public void visit(Ast.Type.ClassType t) {
    this.type = new Tac.Type.ClassType(t.id);
  }

  @Override
  public void visit(Ast.Type.Int t) {
    this.type = new Tac.Type.Int();
  }

  @Override
  public void visit(Ast.Type.IntArray t) {
    this.type = new Tac.Type.IntArray();
  }

  @Override
  public void visit(Ast.Dec.DecSingle d) {
    d.type.accept(this);
    this.dec = new Tac.Dec.DecSingle(this.type, d.id);
  }

  @Override
  public void visit(Ast.Method.MethodSingle m) {
    this.genLocals = new LinkedList<>();
    this.stms = new LinkedList<>();



    m.retType.accept(this);
    Tac.Type.T retType = this.type;
    LinkedList<Tac.Dec.T> formals = new LinkedList<>();
    for (Ast.Dec.T d : m.formals) {
      d.accept(this);
      formals.add(this.dec);
    }
    LinkedList<Tac.Dec.T> locals = new LinkedList<>();
    for (Ast.Dec.T d : m.locals) {
      d.accept(this);
      locals.add(this.dec);
    }
    for (Ast.Stm.T s : m.stms) {
      s.accept(this);
    }
    m.retExp.accept(this);
    locals.addAll(this.genLocals);
    this.method = new Tac.Method.MethodSingle(retType, m.id, formals, locals,
        this.stms, this.operand);

  }

  @Override
  public void visit(Ast.Class.ClassSingle c) {
    LinkedList<Tac.Dec.T> decls = new LinkedList<>();
    for (Ast.Dec.T d : c.decs) {
      d.accept(this);
      decls.add(this.dec);
    }
    LinkedList<Tac.Method.T> methods = new LinkedList<>();
    for (Ast.Method.T m : c.methods) {
      m.accept(this);
      methods.add(this.method);
    }
    this.clazz = new Tac.Class.ClassSingle(c.id, c.extendss, decls, methods);
  }

  @Override
  public void visit(Ast.MainClass.MainClassSingle c) {
    this.genLocals = new LinkedList<>();
    this.stms = new LinkedList<>();
    c.stm.accept(this);
    this.main = new Tac.MainClass.MainClassSingle(c.id, c.arg, this.genLocals, this.stms);
  }

  @Override
  public void visit(Ast.Program.ProgramSingle p) {
    p.mainClass.accept(this);
    LinkedList<Tac.Class.T> classes = new LinkedList<>();
    for (Ast.Class.T e : p.classes) {
      e.accept(this);
      classes.addLast(this.clazz);
    }
    this.prog = new Tac.Program.ProgramSingle(this.main, classes);
  }
}
