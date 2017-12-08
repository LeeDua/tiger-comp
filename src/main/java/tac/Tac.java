package tac;

import java.util.LinkedList;

public class Tac {
  // ///////////////////////////////////////////////////////////
  // type
  public static class Type {
    public static abstract class T implements Acceptable {
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
      public boolean isField;

      public DecSingle(Type.T type, String id, boolean isField) {
        this.type = type;
        this.id = id;
        this.isField = isField;
      }

      public DecSingle(Type.T type, String id, boolean isField, int linenum) {
        this.type = type;
        this.id = id;
        this.isField = isField;
        this.lineNum = linenum;
      }

      @Override
      public void accept(Visitor v) {
      }
    }
  }


  ////////////////////////////////////////////////////////////
  public static class VarInfo {
    public static abstract class T implements Acceptable{
      Type.T type;
    }
    public static class Var extends T {
      String id;

      @Override
      public void accept(Visitor v) {
      }
    }
    public static class Int extends T {
      int num;

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

    public static class AssignArraySelect extends T {
      public String dst;
      public VarInfo.Var array;
      public VarInfo.T index;


      @Override
      public void accept(Visitor v) {

      }
    }

    public static class AssignArray extends T {
      public String dst;
      public VarInfo.T index;
      public VarInfo.T exp;
      @Override
      public void accept(Visitor v) {
      }
    }

    public static class InvokeVirtual extends T {
      public String dst;
      public String obj;
      public String f;
      public LinkedList<VarInfo.T> args;


      @Override
      public void accept(Visitor v) {
      }
    }

    public static class AssignArrayLength extends T {
      public VarInfo.Var dst;
      public VarInfo.Var array;
      @Override
      public void accept(Visitor v) {
      }
    }

    public static class AssignNewIntArray extends T {
      public VarInfo.Var dst;
      public VarInfo.T size;

      @Override
      public void accept(Visitor v) {
      }
    }

    public static class AssignNewObject extends T {
      public VarInfo.Var dst;
      public String c; // class id

      @Override
      public void accept(Visitor v) {
      }
    }

    public static class Print extends T {
      public VarInfo.T arg;

      @Override
      public void accept(Visitor v) {
      }
    }
    // if
    public static class If extends T {
      public VarInfo.T condition;
      public T xen;
      public T ilse;

      @Override
      public void accept(Visitor v) {

      }
    }

    // while
    public static class While extends T {
      public VarInfo.T condition;
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
      public LinkedList<Stm.T> stms;
      public VarInfo.T retExp;

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
      public java.util.LinkedList<Dec.T> decs;
      public java.util.LinkedList<Method.T> methods;


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
