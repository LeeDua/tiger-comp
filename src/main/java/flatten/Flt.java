package flatten;

import ast.Ast;
import java.util.Vector;

public class Flt {
  public static class Type {
    public static abstract class T implements Acceptable {}

    public static class Int extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Boolean extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class IntArray extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class ClassType extends T {
      String id;

      public ClassType(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class ClassArray extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Dec {
    public static abstract class T implements Acceptable {
    }

    public static class DecSingle extends T {
      public Type.T type;
      public String id;

      public DecSingle(Type.T type, String id) {
        this.type = type;
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Operand {
    public static abstract class T implements Acceptable {}

    public static class Var extends T {
      public String id;

      public Var(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Int extends T {
      public int value;

      public Int(int value) {
        this.value = value;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class True extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class False extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

  }

  public static class Condition {
    public static abstract class T implements Acceptable {}
    public static class ConditionSingle extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Selector {
    public static abstract class T implements Acceptable {}

    public static class SelectorSingle extends T {
      String id;
      public Type.T elemType;
      public Vector<Condition.T> conds;

      public SelectorSingle(String id, Type.T elemType,
                            Vector<Condition.T> conds) {
        this.id = id;
        this.elemType = elemType;
        this.conds = conds;
      }
      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class BinOp {
    public static abstract class T implements Acceptable {}
    public static class Add extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class And extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Sub extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Times extends T {

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Lt extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class UnOp {
    public static abstract class T implements Acceptable {}

    public static class Not extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Stm {
    public static abstract class T implements Acceptable {}

    public static class AssignUnOp extends T {
      public Operand.T dst;
      public Operand.T exp;
      public UnOp.T op;

      public AssignUnOp(Operand.T dst, Operand.T exp, UnOp.T op) {
        this.dst = dst;
        this.exp = exp;
        this.op = op;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);

      }
    }
    public static class AssignBinOp extends T {
      public Operand.T dst;
      public Operand.T left;
      public Operand.T right;
      public BinOp.T op;

      public AssignBinOp(Operand.T dst, BinOp.T op, Operand.T left, Operand.T right) {
        this.dst = dst;
        this.op = op;
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);

      }
    }

    public static class Assign extends T {
      public Operand.T dst;
      public Operand.T src;

      public Assign(Operand.T dst, Operand.T src) {
        this.dst = dst;
        this.src = src;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);

      }
    }

    public static class AssignArraySelect extends T {
      public Operand.Var dst;
      public Operand.Var array;
      public Operand.T index;

      public AssignArraySelect(Operand.Var dst, Operand.Var array,
                               Operand.T index) {
        this.dst = dst;
        this.array = array;
        this.index = index;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);
      }
    }

    public static class AssignArray extends T {
      public Operand.T dst; // must be Var
      public Operand.T index;
      public Operand.T exp;

      public AssignArray(Operand.T dst, Operand.T index, Operand.T exp) {
        this.dst = dst;
        this.index = index;
        this.exp = exp;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);
      }
    }

    public static class AssignArrayLength extends T {
      public Operand.Var dst;
      public Operand.Var array;

      public AssignArrayLength(Operand.Var dst, Operand.Var array) {
        this.dst = dst;
        this.array = array;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);
      }
    }

    public static class AssignNewIntArray extends T {
      public Operand.T dst;
      public Operand.T size;

      public AssignNewIntArray(Operand.T dst, Operand.T size) {
        this.dst = dst;
        this.size = size;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);
      }
    }

    public static class AssignNewObject extends T {
      public Operand.T dst;
      public String c; // class id

      public AssignNewObject(Operand.T dst, String c) {
        this.dst = dst;
        this.c = c;
      }

      @Override
      public void accept(Visitor v) {
        //v.visit(this);
      }
    }

    public static class Print extends T {
      public Operand.T args;

      public Print(Operand.T args) {
        this.args = args;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignUnOp extends T {
      public Selector.T dst;
      public Operand.T exp;
      public UnOp.T op;

      public SelectAssignUnOp(Selector.T dst, Operand.T exp, UnOp.T op) {
        this.dst = dst;
        this.exp = exp;
        this.op = op;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignBinOp extends T {
      public Selector.T dst;
      public BinOp.T op;
      public Operand.T left;
      public Operand.T right;

      public SelectAssignBinOp(Selector.T dst, BinOp.T op, Operand.T left,
                         Operand.T right) {
        this.dst = dst;
        this.op = op;
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssign extends T {
      public Selector.T dst;
      public Operand.T src;

      public SelectAssign(Selector.T dst, Operand.T src) {
        this.dst = dst;
        this.src = src;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignArraySelect extends T {
      public Selector.T dst;
      public Operand.T array;
      public Operand.T index;

      public SelectAssignArraySelect(Selector.T dst, Operand.T array,
                               Operand.T index) {
        this.dst = dst;
        this.array = array;
        this.index = index;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignArray extends T {
      public Selector.T dst;
      public Operand.T index;
      public Operand.T exp;

      public SelectAssignArray(Selector.T dst, Operand.T index, Operand.T exp) {
        this.dst = dst;
        this.index = index;
        this.exp = exp;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignArrayLength extends T {
      public Selector.T dst;
      public Operand.T array;

      public SelectAssignArrayLength(Selector.T dst, Operand.T array) {
        this.dst = dst;
        this.array = array;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignNewIntArray extends T {
      public Selector.T dst;
      public Operand.T size;

      public SelectAssignNewIntArray(Selector.T dst, Operand.T size) {
        this.dst = dst;
        this.size = size;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class SelectAssignNewObject extends T {
      public Selector.T dst;
      public String c;

      public SelectAssignNewObject(Selector.T dst, String c) {
        this.dst = dst;
        this.c = c;
      }

      @Override
      public void accept(Visitor v) {

      }
    }



    public static class If extends T {
      // TODO: 12/17/17
      @Override
      public void accept(Visitor v) {

      }
    }

    public static class While extends T {
      public Ast.Exp.T cond;
      public T body;

      public While(Ast.Exp.T cond, T body) {
        this.cond = cond;
        this.body = body;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class Block extends T {
      public Vector<T> stms;

      public Block(Vector<T> stms) {
        this.stms = stms;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

    public static class AssignCall extends T {
      public Operand.T dst;
      public Operand.T caller;
      public Type.T callerTpye; // must be classType
      public String methodName;
      public Vector<Operand.T> args;

      public AssignCall(Operand.T dst, Operand.T caller,
                        Type.T callerTpye, String methodName,
                        Vector<Operand.T> args) {
        this.dst = dst;
        this.caller = caller;
        this.callerTpye = callerTpye;
        this.methodName = methodName;
        this.args = args;
      }

      @Override
      public void accept(Visitor v) {

      }
    }

  }

  public static class Method {
    public static abstract class T implements Acceptable {

    }
    public static class MethodSingle extends T {
      public Type.T retType;
      public String id;
      public Vector<Dec.T> formals;
      public Vector<Dec.T> locals;
      public Vector<Stm.T> stms;
      public Operand.T retExp;

      public MethodSingle(Type.T retType, String id,
                          Vector<Dec.T> formals,
                          Vector<Dec.T> locals,
                          Vector<Stm.T> stms, Operand.T retExp) {
        this.retType = retType;
        this.id = id;
        this.formals = formals;
        this.locals = locals;
        this.stms = stms;
        this.retExp = retExp;
      }

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Class {
    public static abstract class T implements Acceptable {

    }
    public static class ClassSingle extends T {
      public String id;
      public String extendss;
      public Vector<Dec.T> decs;
      public Vector<Method.T> methods;

      public ClassSingle(String id, String extendss,
                         Vector<Dec.T> decs,
                         Vector<Method.T> methods) {
        this.id = id;
        this.extendss = extendss;
        this.decs = decs;
        this.methods = methods;
      }

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class MainClass {
    public static abstract class T implements Acceptable {

    }
    public static class MainClassSingle extends T {
      public String id;
      public String arg;
      public Vector<Dec.T> locals;
      public Vector<Stm.T> stms;

      public MainClassSingle(String id, String arg,
                             Vector<Dec.T> locals,
                             Vector<Stm.T> stms) {
        this.id = id;
        this.arg = arg;
        this.locals = locals;
        this.stms = stms;
      }

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class Program {
    public static abstract class T implements Acceptable {

    }
    public static class ProgramSingle extends T {
      public MainClass.T mainClass;
      public Vector<Class.T> classes;

      public ProgramSingle(MainClass.T mainClass,
                           Vector<Class.T> classes) {
        this.mainClass = mainClass;
        this.classes = classes;
      }

      @Override
      public void accept(Visitor v) {

      }
    }
  }


}