package flatten;

import tac.*;

import java.util.Vector;

public class Tac2FltVisitor implements tac.Visitor {
  Flt.Program.T prog;
  Flt.MainClass.MainClassSingle main;
  Vector<Flt.Class.T> classes;
  Vector<Flt.Dec.T> decs;
  Vector<Flt.Stm.T> stms;
  Vector<Flt.Method.T> methods;
  Flt.Type.T type;
  Flt.Operand.T operand;
  Flt.BinOp.T binop;
  Flt.UnOp.T unop;

  public Tac2FltVisitor() {
    this.classes = new Vector<>();
    this.stms = new Vector<>();
    this.methods = new Vector<>();
  }


  private void emit(Flt.Dec.T d) {
    this.decs.add(d);
  }

  private void emit(Flt.Method.T m) {
    this.methods.add(m);
  }

  private void emit(Flt.Class.T c) {
    this.classes.add(c);
  }

  private void emit(Flt.Stm.T s) {
    this.stms.add(s);
  }

  @Override
  public void visit(Tac.Operand.Var e) {
    this.operand = new Flt.Operand.Var(e.id);

  }

  @Override
  public void visit(Tac.Operand.Int e) {
    this.operand = new Flt.Operand.Int(e.value);
  }

  @Override
  public void visit(Tac.Operand.True e) {
    this.operand = new Flt.Operand.True();

  }

  @Override
  public void visit(Tac.Operand.False e) {
    this.operand = new Flt.Operand.False();

  }

  @Override
  public void visit(Tac.BinOp.Add e) {
    this.binop = new Flt.BinOp.Add();
  }

  @Override
  public void visit(Tac.BinOp.And e) {
    this.binop = new Flt.BinOp.And();
  }

  @Override
  public void visit(Tac.BinOp.Sub e) {
    this.binop = new Flt.BinOp.Sub();
  }

  @Override
  public void visit(Tac.BinOp.Times e) {
    this.binop = new Flt.BinOp.Times();
  }

  @Override
  public void visit(Tac.BinOp.Lt e) {
    this.binop = new Flt.BinOp.Lt();
  }

  @Override
  public void visit(Tac.UnOp.Not e) {
    this.unop = new Flt.UnOp.Not();
  }

  @Override
  public void visit(Tac.Stm.Assign e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.src.accept(this);
    emit(new Flt.Stm.Assign(dst, this.operand));
  }

  @Override
  public void visit(Tac.Stm.AssignCall e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.caller.accept(this);
    Flt.Operand.T caller = this.operand;
    e.callerType.accept(this);
    Flt.Type.T callerType = this.type;
    Vector<Flt.Operand.T> args = new Vector<>();
    for (Tac.Operand.T x : e.args) {
      x.accept(this);
      args.add(this.operand);
    }

    emit(new Flt.Stm.AssignCall(dst, caller, callerType, e.methodName, args));

  }

  @Override
  public void visit(Tac.Stm.AssignArray e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.index.accept(this);
    Flt.Operand.T index = this.operand;
    e.exp.accept(this);
    emit(new Flt.Stm.AssignArray(dst, index, this.operand));

  }

  @Override
  public void visit(Tac.Stm.AssignArraySelect e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.array.accept(this);
    Flt.Operand.T array = this.operand;
    e.index.accept(this);
    Flt.Operand.T index = this.operand;

    emit(new Flt.Stm.AssignArraySelect((Flt.Operand.Var) dst,
        (Flt.Operand.Var) array, index));

  }

  @Override
  public void visit(Tac.Stm.AssignBinOp e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.left.accept(this);
    Flt.Operand.T left = this.operand;
    e.right.accept(this);

    e.op.accept(this);
    Flt.Operand.T right = this.operand;
    emit(new Flt.Stm.AssignBinOp(dst, this.binop, left, right));


  }

  @Override
  public void visit(Tac.Stm.AssignUnOp e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.exp.accept(this);
    Flt.Operand.T exp = this.operand;
    e.op.accept(this);
    emit(new Flt.Stm.AssignUnOp(dst, exp, this.unop));
  }

  @Override
  public void visit(Tac.Stm.AssignArrayLength e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.array.accept(this);
    emit(new Flt.Stm.AssignArrayLength((Flt.Operand.Var) dst,
        (Flt.Operand.Var) this.operand));

  }

  @Override
  public void visit(Tac.Stm.AssignNewIntArray e) {
    e.dst.accept(this);
    Flt.Operand.T dst = this.operand;
    e.size.accept(this);
    emit(new Flt.Stm.AssignNewIntArray(dst, this.operand));
  }

  @Override
  public void visit(Tac.Stm.AssignNewObject e) {
    e.dst.accept(this);
    emit(new Flt.Stm.AssignNewObject(this.operand, e.c));
  }


  private void transIfStms(Tac.Stm.T s) {
    new util.Todo();
    if (s instanceof Tac.Stm.AssignUnOp) {

    } else if (s instanceof Tac.Stm.AssignBinOp) {

    } else if (s instanceof Tac.Stm.Assign) {

    } else if (s instanceof Tac.Stm.AssignArraySelect) {

    } else if (s instanceof Tac.Stm.AssignArray) {

    } else if (s instanceof Tac.Stm.AssignArrayLength) {

    } else if (s instanceof Tac.Stm.AssignNewIntArray) {

    } else if (s instanceof Tac.Stm.AssignNewObject) {

    } else if (s instanceof Tac.Stm.Print) {

    } else if (s instanceof Tac.Stm.If) {

    } else if (s instanceof Tac.Stm.While) {

    } else if (s instanceof Tac.Stm.Block) {

    } else if (s instanceof Tac.Stm.AssignCall) {

    } else {
      new util.Bug();
    }
  }

  @Override
  public void visit(Tac.Stm.If e) {

    // TODO: 12/18/17
    new util.Todo();

  }

  @Override
  public void visit(Tac.Stm.While e) {
    Vector<Flt.Stm.T> old = this.stms;
    e.body.accept(this);
    Vector<Flt.Stm.T> stms = this.stms;
    this.stms = old;
    emit(new Flt.Stm.While(e.cond, new Flt.Stm.Block(stms)));

  }

  @Override
  public void visit(Tac.Stm.Block e) {
    Vector<Flt.Stm.T> old = this.stms;
    this.stms = new Vector<>();
    for (Tac.Stm.T s : e.stms) {
      s.accept(this);
    }
    Vector<Flt.Stm.T> stms = this.stms;
    this.stms = old;
    emit(new Flt.Stm.Block(stms));
  }

  @Override
  public void visit(Tac.Stm.Print e) {
    e.arg.accept(this);
    emit(new Flt.Stm.Print(this.operand));
  }

  @Override
  public void visit(Tac.Type.Int e) {
    this.type = new Flt.Type.Int();
  }

  @Override
  public void visit(Tac.Type.IntArray e) {
    this.type = new Flt.Type.IntArray();
  }

  @Override
  public void visit(Tac.Type.ClassType e) {
    this.type = new Flt.Type.ClassType(e.id);
  }

  @Override
  public void visit(Tac.Type.Boolean e) {
    this.type = new Flt.Type.Boolean();
  }

  @Override
  public void visit(Tac.Dec.DecSingle e) {
    e.type.accept(this);
    emit(new Flt.Dec.DecSingle(this.type, e.id));

  }

  @Override
  public void visit(Tac.Method.MethodSingle e) {
    this.decs = new Vector<>();
    this.stms = new Vector<>();

    e.retType.accept(this);
    Flt.Type.T retType = this.type;
    e.retExp.accept(this);
    Flt.Operand.T retExp = this.operand;


    for (Tac.Dec.T d : e.formals) {
      d.accept(this);
    }
    Vector<Flt.Dec.T> formals = this.decs;
    this.decs = new Vector<>();

    for (Tac.Dec.T d : e.locals) {
      d.accept(this);
    }

    for (Tac.Stm.T s : e.stms) {
      s.accept(this);
    }

    emit(new Flt.Method.MethodSingle(retType, e.id, formals, this.decs,
        this.stms, retExp));


  }

  @Override
  public void visit(Tac.MainClass.MainClassSingle e) {
    this.decs = new Vector<>();
    this.stms = new Vector<>();
    for (Tac.Dec.T d : e.locals) {
      d.accept(this);
    }

    for (Tac.Stm.T s : e.stms) {
      s.accept(this);
    }
    this.main = new Flt.MainClass.MainClassSingle(e.id, e.arg, this.decs,
        this.stms);
  }

  @Override
  public void visit(Tac.Class.ClassSingle e) {
    this.decs = new Vector<>();
    this.methods = new Vector<>();
    for (Tac.Dec.T d : e.decs) {
      d.accept(this);
    }
    Vector<Flt.Dec.T> fields = this.decs;

    for (Tac.Method.T m : e.methods) {
      m.accept(this);
    }

    emit(
        new Flt.Class.ClassSingle(e.id, e.extendss, fields, this.methods));

  }

  @Override
  public void visit(Tac.Program.ProgramSingle e) {
    e.mainClass.accept(this);
    for (Tac.Class.T c : e.classes) {
      c.accept(this);
    }

    this.prog = new Flt.Program.ProgramSingle(this.main, this.classes);


  }
}