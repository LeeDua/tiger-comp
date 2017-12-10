package tac;

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
  }

  // ///////////////////////////////////////////////////
  // dec
  public static class Dec {
    public static abstract class T implements Acceptable {
      int lineNum;
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


  ////////////////////////////////////////////////////////////
  public static class Operand {
    public static abstract class T implements Acceptable {
      Type.T type;
    }

    public static class Var extends T {
      String id;
      public Var(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {
      }
    }

    public static class Int extends T {
      int value;

      public Int(int value) {
        this.value = value;
      }
      @Override
      public void accept(Visitor v) {
      }
    }

  }

  public static class UnOp {
    public static abstract class T implements Acceptable {

    }

    public static class Not extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  public static class BinOp {
    public static abstract class T implements Acceptable {
    }

    public static class Add extends T {
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

    public static class And extends T {

      @Override
      public void accept(Visitor v) {

      }
    }
  }

  // /////////////////////////////////////////////////////////
  // statement
  public static class Stm {
    public static abstract class T implements Acceptable {
      public int linenum;
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
      }
    }

    public static class AssignArray extends T {
      public Operand.Var dst;
      public Operand.T index;
      public Operand.T exp;

      @Override
      public void accept(Visitor v) {
      }
    }

    public static class InvokeVirtual extends T {
      public String dst;
      public String obj;
      public String f;
      public LinkedList<Operand.T> args;


      @Override
      public void accept(Visitor v) {
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
      }
    }

    public static class Print extends T {
      public Operand.T arg;

      @Override
      public void accept(Visitor v) {
      }
    }

    // if
    public static class If extends T {
      public Operand.T condition;
      public T xen;
      public T ilse;

      @Override
      public void accept(Visitor v) {

      }
    }

    // while
    public static class While extends T {
      public Operand.T condition;
      public T body;

      @Override
      public void accept(Visitor v) {

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

      public LinkedList<Stm.T> stms;
      public Operand.T retExp;

      @Override
      public void accept(Visitor v) {
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
      public Stm.T stm;// system.out.println

      public MainClassSingle(String id, String arg, Stm.T stm) {
        this.id = id;
        this.arg = arg;
        this.stm = stm;
      }

      @Override
      public void accept(Visitor v) {
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
      }
    }

  }


}
