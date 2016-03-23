package cfg;

import cfg.Cfg.*;
import cfg.Cfg.Block.BlockSingle;
import cfg.Cfg.Class;
import cfg.Cfg.Class.ClassSingle;
import cfg.Cfg.Dec.DecSingle;
import cfg.Cfg.MainMethod.MainMethodSingle;
import cfg.Cfg.Method.MethodSingle;
import cfg.Cfg.Operand.Int;
import cfg.Cfg.Operand.Var;
import cfg.Cfg.Program.ProgramSingle;
import cfg.Cfg.Stm.*;
import cfg.Cfg.Transfer.Goto;
import cfg.Cfg.Transfer.If;
import cfg.Cfg.Transfer.Return;
import cfg.Cfg.Type.ClassType;
import cfg.Cfg.Type.IntType;
import cfg.Cfg.Vtable.VtableSingle;

import java.util.LinkedList;
import java.util.Vector;

// Traverse the C AST, and generate
// a control-flow graph.
public class TranslateVisitor implements codegen.C.Visitor
{
  private Type.T _type; // type after translation
  private Operand.T _operand;
  private Dec.T _dec;
  private Vector<Object> stmOrTransfer;
  private LinkedList<Dec.T> _locals;
  private Method.T _method;
  private Class.T _classs;
  private Vtable.T _vtable;
  private MainMethod.T _mainMethod;
  public Cfg.Program.ProgramSingle program;

  public TranslateVisitor()
  {
    this._type = null;
    this._dec = null;
    this._locals = new LinkedList<>();
    this.stmOrTransfer = new Vector<>();
    this._method = null;
    this._classs = null;
    this._vtable = null;
    this._mainMethod = null;
    this.program = null;
  }

  // /////////////////////////////////////////////////////
  // utility functions
  private LinkedList<Block.T> cookBlocks()
  {
    LinkedList<Block.T> blocks = new LinkedList<>();
    int i = 0;
    int size = this.stmOrTransfer.size();
    while (i < size) {
      util.Label label;
      BlockSingle b;
      LinkedList<Stm.T> stms = new LinkedList<>();
      Transfer.T transfer;
      if (!(this.stmOrTransfer.get(i) instanceof util.Label)) {
        new util.Bug("block not start with Label:" + this.stmOrTransfer.get(i));
      }
      label = (util.Label) this.stmOrTransfer.get(i++);
      while (i < size && this.stmOrTransfer.get(i) instanceof Stm.T) {
        stms.add((Stm.T) this.stmOrTransfer.get(i++));
      }
      if (!(this.stmOrTransfer.get(i) instanceof Transfer.T)) {
        new util.Bug(
            "block not end with Transfer:" + this.stmOrTransfer.get(i));
      }
      transfer = (Transfer.T) this.stmOrTransfer.get(i++);
      b = new BlockSingle(label, stms, transfer);
      blocks.add(b);
    }
    return blocks;
  }

  private void emit(Object obj)
  {
    this.stmOrTransfer.add(obj);
  }

  private String genVar()
  {
    String fresh = util.Temp.next();
    DecSingle dec = new DecSingle(new IntType(), fresh);
    this._locals.add(dec);
    return fresh;
  }

  private String genVar(Type.T ty)
  {
    String fresh = util.Temp.next();
    DecSingle dec = new DecSingle(ty, fresh);
    this._locals.add(dec);
    return fresh;
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(codegen.C.Ast.Exp.Add e)
  {
    String dst = genVar();
    e.left.accept(this);
    Operand.T left = this._operand;
    e.right.accept(this);
    Operand.T right = this._operand;
    emit(new Stm.Add(dst, left, right));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.And e)
  {
    String dst = genVar();
    e.left.accept(this);
    Operand.T left = this._operand;
    e.right.accept(this);
    Operand.T right = this._operand;
    emit(new Stm.And(dst, left, right));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.ArraySelect e)
  {
    String dst = genVar();
    e.array.accept(this);
    Operand.T array = this._operand;
    e.index.accept(this);
    Operand.T index = this._operand;
    emit(new Stm.ArraySelect(dst, array, index));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Call e)
  {
    e.retType.accept(this);
    String dst = genVar(this._type);
    String obj = null;
    e.exp.accept(this);
    Operand.T objOp = this._operand;
    if (objOp instanceof Var) {
      Var var = (Var) objOp;
      if (var.isField) {
        obj = "this->" + var.id;
      } else {
        obj = var.id;
      }
    } else {
      new util.Bug();
    }

    LinkedList<Operand.T> newArgs = new LinkedList<>();
    for (codegen.C.Ast.Exp.T x : e.args) {
      x.accept(this);
      newArgs.add(this._operand);
    }
    emit(new InvokeVirtual(dst, obj, e.id, newArgs));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Id e)
  {
    this._operand = new Var(e.id, e.isField);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Length e)
  {
    String dst = genVar();
    e.array.accept(this);
    Operand.T array = this._operand;
    emit(new Stm.Length(dst, array));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Lt e)
  {
    String dst = genVar();
    e.left.accept(this);
    Operand.T left = this._operand;
    e.right.accept(this);
    emit(new Lt(dst, left, this._operand));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.NewIntArray e)
  {
    e.size.accept(this);
    Operand.T size = this._operand;
    String dst = genVar(new Type.IntArrayType());
    emit(new Stm.NewIntArray(dst, size));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.NewObject e)
  {
    String dst = genVar(new ClassType(e.id));
    emit(new NewObject(dst, e.id));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Not e)
  {
    String dst = genVar();
    e.exp.accept(this);
    Operand.T exp = this._operand;
    emit(new Stm.Not(dst, exp));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Num e)
  {
    this._operand = new Int(e.num);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Sub e)
  {
    String dst = genVar();
    e.left.accept(this);
    Operand.T left = this._operand;
    e.right.accept(this);
    emit(new Sub(dst, null, left, this._operand));
    this._operand = new Var(dst, false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.This e)
  {
    this._operand = new Var("this", false);
  }

  @Override
  public void visit(codegen.C.Ast.Exp.Times e)
  {
    String dst = genVar();
    e.left.accept(this);
    Operand.T left = this._operand;
    e.right.accept(this);
    emit(new Times(dst, null, left, this._operand));
    this._operand = new Var(dst, false);
  }

  // statements
  @Override
  public void visit(codegen.C.Ast.Stm.Assign s)
  {
    s.exp.accept(this);
    emit(new Move(s.id, this._operand, s.isField));
  }

  @Override
  public void visit(codegen.C.Ast.Stm.AssignArray s)
  {
    s.index.accept(this);
    Operand.T index = this._operand;
    s.exp.accept(this);
    Operand.T exp = this._operand;
    emit(new Stm.AssignArray(s.id, index, exp, s.isField));
  }

  @Override
  public void visit(codegen.C.Ast.Stm.Block s)
  {
    for (codegen.C.Ast.Stm.T stm : s.stms) {
      stm.accept(this);
    }
  }

  @Override
  public void visit(codegen.C.Ast.Stm.If s)
  {
    util.Label tl = new util.Label();
    util.Label fl = new util.Label();
    util.Label el = new util.Label();
    s.condition.accept(this);
    emit(new If(this._operand, tl, fl));
    emit(fl);
    s.elsee.accept(this);
    emit(new Goto(el));
    emit(tl);
    s.thenn.accept(this);
    emit(new Goto(el));
    emit(el);
  }

  @Override
  public void visit(codegen.C.Ast.Stm.Print s)
  {
    s.exp.accept(this);
    emit(new Print(this._operand));
  }

  @Override
  public void visit(codegen.C.Ast.Stm.While s)
  {
    util.Label start = new util.Label();
    util.Label end = new util.Label();
    util.Label body = new util.Label();
    emit(new Goto(start));
    emit(start);
    s.condition.accept(this);
    emit(new If(this._operand, body, end));
    emit(body);
    s.body.accept(this);
    emit(new Goto(start));
    emit(end);
  }

  // type
  @Override
  public void visit(codegen.C.Ast.Type.ClassType t)
  {
    this._type = new ClassType(t.id);
  }

  @Override
  public void visit(codegen.C.Ast.Type.Int t)
  {
    this._type = new IntType();
  }

  @Override
  public void visit(codegen.C.Ast.Type.IntArray t)
  {
    this._type = new Type.IntArrayType();
  }

  // dec
  @Override
  public void visit(codegen.C.Ast.Dec.DecSingle d)
  {
    d.type.accept(this);
    this._dec = new DecSingle(this._type, d.id);
  }

  // vtable
  @Override
  public void visit(codegen.C.Ast.Vtable.VtableSingle v)
  {
    LinkedList<Ftuple> newMethodDecs = new LinkedList<>();
    for (codegen.C.Ftuple t : v.ms) {
      t.ret.accept(this);
      Type.T ret = this._type;
      LinkedList<Dec.T> args = new LinkedList<>();
      for (codegen.C.Ast.Dec.T dec : t.args) {
        dec.accept(this);
        args.add(this._dec);
      }
      newMethodDecs.add(new Ftuple(t.classs, ret, args, t.id));
    }
    this._vtable = new VtableSingle(v.cname, newMethodDecs);
  }

  // class
  @Override
  public void visit(codegen.C.Ast.Class.ClassSingle c)
  {
    LinkedList<Tuple> newDecs = new LinkedList<>();
    for (codegen.C.Tuple t : c.decs) {
      t.type.accept(this);
      newDecs.add(new Tuple(t.classs, this._type, t.id));
    }
    this._classs = new ClassSingle(c.id, newDecs);
  }

  // method
  @Override
  public void visit(codegen.C.Ast.Method.MethodSingle m)
  {
    this.stmOrTransfer = new Vector<>();
    this._locals = new LinkedList<>();
    m.retType.accept(this);
    Type.T retType = this._type;
    LinkedList<Dec.T> newFormals = new LinkedList<>();
    for (codegen.C.Ast.Dec.T c : m.formals) {
      c.accept(this);
      newFormals.add(this._dec);
    }
    LinkedList<Dec.T> newLocals = new LinkedList<>();
    for (codegen.C.Ast.Dec.T c : m.locals) {
      c.accept(this);
      newLocals.add(this._dec);
    }
    // a junk label
    util.Label entry = new util.Label();
    emit(entry);
    for (codegen.C.Ast.Stm.T s : m.stms) {
      s.accept(this);
    }
    m.retExp.accept(this);
    emit(new Return(this._operand));
    LinkedList<Block.T> blocks = cookBlocks();
    newLocals.addAll(this._locals);
    this._method = new MethodSingle(retType, m.id, m.classId, newFormals,
        newLocals, blocks, entry, null, null);
  }

  // main method
  @Override
  public void visit(codegen.C.Ast.MainMethod.MainMethodSingle m)
  {
    this.stmOrTransfer = new Vector<>();
    this._locals = new LinkedList<>();
    LinkedList<Dec.T> locals = new LinkedList<>();
    for (codegen.C.Ast.Dec.T c : m.locals) {
      c.accept(this);
      locals.add(this._dec);
    }
    util.Label entry = new util.Label();
    emit(entry);
    m.stm.accept(this);
    emit(new Return(new Int(0)));
    LinkedList<Block.T> blocks = cookBlocks();
    locals.addAll(this._locals);
    this._mainMethod = new MainMethodSingle(locals, blocks);
  }

  // program
  @Override
  public void visit(codegen.C.Ast.Program.ProgramSingle p)
  {
    LinkedList<Class.T> newClasses = new LinkedList<>();
    for (codegen.C.Ast.Class.T c : p.classes) {
      c.accept(this);
      newClasses.add(this._classs);
    }
    LinkedList<Vtable.T> newVtable = new LinkedList<>();
    for (codegen.C.Ast.Vtable.T v : p.vtables) {
      v.accept(this);
      newVtable.add(this._vtable);
    }
    LinkedList<Method.T> newMethods = new LinkedList<>();
    for (codegen.C.Ast.Method.T m : p.methods) {
      m.accept(this);
      newMethods.add(this._method);
    }
    p.mainMethod.accept(this);
    MainMethod.T newMainMethod = this._mainMethod;

    this.program = new ProgramSingle(newClasses, newVtable, newMethods,
        newMainMethod);
  }
}
