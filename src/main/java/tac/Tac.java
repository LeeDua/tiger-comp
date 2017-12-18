package tac;

import ast.Ast;

import java.util.LinkedList;

public class Tac {
  // ///////////////////////////////////////////////////////////
  // type
  public static class Type {
    public static abstract class T implements Acceptable {
    }

    public static class Int extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Boolean extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class IntArray extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }
    public static class ClassType extends T {
      public String id;

      public ClassType(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }
  }

  // ///////////////////////////////////////////////////
  // dec
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
        v.visit(this);
      }
    }
  }


  ////////////////////////////////////////////////////////////
  public static class Operand {
    public static abstract class T implements Acceptable {
    }

    public static class Var extends T {
      public String id;
      public Var(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Int extends T {
      public int value;

      public Int(int value) {
        this.value = value;
      }
      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class True extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class False extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }

  public static class UnOp {
    public static abstract class T implements Acceptable {

    }

    public static class Not extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }
  }

  public static class BinOp {
    public static abstract class T implements Acceptable {
    }

    public static class Add extends T {
      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Sub extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

    public static class Times extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

    public static class Lt extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

    public static class And extends T {

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }
  }

  // /////////////////////////////////////////////////////////
  // statement
  public static class Stm {
    public static abstract class T implements Acceptable {
    }
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
        v.visit(this);

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
        v.visit(this);

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
        v.visit(this);

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
        v.visit(this);
      }
    }

    public static class AssignArray extends T {
      public Operand.Var dst;
      public Operand.T index;
      public Operand.T exp;

      public AssignArray(Operand.Var dst, Operand.T index, Operand.T exp) {
        this.dst = dst;
        this.index = index;
        this.exp = exp;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
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
        v.visit(this);
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
        v.visit(this);
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
        v.visit(this);
      }
    }

    public static class Print extends T {
      public Operand.T arg;
      public Print (Operand.T arg) {
        this.arg = arg;
      }
      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    // if
    public static class If extends T {
      public Operand.T cond;
      public T xen;
      public T ilse;

      public If(Operand.T cond, T xen, T ilse) {
        this.cond = cond;
        this.xen = xen;
        this.ilse = ilse;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    // while
    public static class While extends T {
      public Ast.Exp.T cond;
      public T body;

      public While(Ast.Exp.T cond, T body) {
        this.cond = cond;
        this.body = body;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

    public static class Block extends T {
      public LinkedList<T> stms;
      public Block(LinkedList<T> stms) {
        this.stms = stms;
      }
      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

    public static class AssignCall extends T {
      public Operand.T dst;
      public Operand.T caller;
      public Type.ClassType callerType;
      public String methodName;
      public LinkedList<Operand.T> args;

      public AssignCall(Operand.T dst, Operand.T caller,
                        Type.ClassType callerType, String methodName,
                        LinkedList<Operand.T> args) {
        this.dst = dst;
        this.caller = caller;
        this.callerType = callerType;
        this.methodName = methodName;
        this.args = args;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);

      }
    }

  }// end of statement

  // /////////////////////////////////////////////////////////
  // method
  public static class Method {
    public static abstract class T implements Acceptable {
    }

    public static class MethodSingle extends T {
      public Type.T retType;
      public String id;
      public LinkedList<Dec.T> formals;
      public LinkedList<Dec.T> locals;
      public LinkedList<Stm.T> stms;
      public Operand.T retExp;

      public MethodSingle(Type.T retType, String id,
                          LinkedList<Dec.T> formals,
                          LinkedList<Dec.T> locals,
                          LinkedList<Stm.T> stms, Operand.T retExp) {
        this.retType = retType;
        this.id = id;
        this.formals = formals;
        this.locals = locals;
        this.stms = stms;
        this.retExp = retExp;
      }



      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }

  // class
  public static class Class {
    public static abstract class T implements Acceptable {
    }

    public static class ClassSingle extends T {
      public String id;
      public String extendss; // null for non-existing "extends"
      public LinkedList<Dec.T> decs;
      public LinkedList<Method.T> methods;


      public ClassSingle(String id, String extendss, LinkedList<Dec.T> decls,
                         LinkedList<Method.T> methods) {
        this.id = id;
        this.extendss = extendss;
        this.decs = decls;
        this.methods = methods;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }

  // main class
  public static class MainClass {
    public static abstract class T implements Acceptable {
    }

    public static class MainClassSingle extends T {
      public String id;// Sum
      public String arg;// a
      public LinkedList<Dec.T> locals;
      public LinkedList<Stm.T> stms;

      public MainClassSingle(String id, String arg, LinkedList<Dec.T> locals, LinkedList<Stm.T> stms) {
        this.id = id;
        this.arg = arg;
        this.locals = locals;
        this.stms = stms;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }

  // whole program
  public static class Program {
    public static abstract class T implements Acceptable {
    }

    public static class ProgramSingle extends T {
      public MainClass.T mainClass;
      public LinkedList<Class.T> classes;

      public ProgramSingle(MainClass.T mainClass,
                           LinkedList<Class.T> classes) {
        this.mainClass = mainClass;
        this.classes = classes;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }


}
