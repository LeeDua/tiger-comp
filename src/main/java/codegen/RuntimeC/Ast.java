package codegen.RuntimeC;

import java.util.ArrayList;
import java.util.LinkedList;

public class Ast {
  // /////////////////////////////////////////////
  // type
  public static class Type {
    public static final int TYPE_INT = 0;
    public static final int TYPE_INTARRAY = 1;
    public static final int TYPE_CLASS = 2;

    public static abstract class T implements codegen.RuntimeC.Acceptable {
      abstract int getType();
    }

    public static class ClassType extends T {
      public String id;

      public ClassType(String id) {
        this.id = id;
      }

      @Override
      public String toString() {
        return this.id;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }

      @Override
      int getType() {
        return TYPE_CLASS;
      }
    }

    public static class Int extends T {
      public Int() {
      }

      @Override
      public String toString() {
        return "@int";
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }

      @Override
      int getType() {
        return TYPE_INT;
      }
    }

    public static class IntArray extends T {
      public IntArray() {
      }

      @Override
      public String toString() {
        return "@int[]";
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }

      @Override
      int getType() {
        return TYPE_INTARRAY;
      }
    }

  }// end of type

  // /////////////////////////////////////////////
  // dec
  public static class Dec {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
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

  }// end of dec

  // /////////////////////////////////////////////
  // expression
  public static class Exp {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class Add extends T {
      public T left;
      public T right;

      public Add(T left, T right) {
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class And extends T {
      public T left;
      public T right;

      public And(T left, T right) {
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class ArraySelect extends T {
      public T array;
      public T index;

      public ArraySelect(T array, T index) {
        this.array = array;
        this.index = index;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Call extends T {
      public String dst;
      public T exp;
      public String id;
      public LinkedList<T> args;
      public Type.T retType;

      public Call(String dst, T exp, String id,
                  java.util.LinkedList<T> args, Type.T retType) {
        this.dst = dst;
        this.exp = exp;
        this.id = id;
        this.args = args;
        this.retType = retType;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Id extends T {
      public String id;
      public boolean isField;

      // public Type.T type;

      public Id(String id) {
        this.id = id;
      }

      public Id(String id, boolean isField) {
        this.id = id;
        this.isField = isField;
        // this.type=type;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Length extends T {
      public T array;

      public Length(T array) {
        this.array = array;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Lt extends T {
      public T left;
      public T right;

      public Lt(T left, T right) {
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class NewIntArray extends T {
      public T size;
      public String name; // This field is used to name the allocation.

      public NewIntArray(T size) {
        this.size = size;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class NewObject extends T {
      public String id;
      // This field is used to name the allocation.
      public String name;

      public NewObject(String id) {
        this.id = id;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Not extends T {
      public T exp;

      public Not(T exp) {
        this.exp = exp;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Num extends T {
      public int num;

      public Num(int num) {
        this.num = num;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Sub extends T {
      public T left;
      public T right;

      public Sub(T left, T right) {
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class This extends T {
      public This() {
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Times extends T {
      public T left;
      public T right;

      public Times(T left, T right) {
        this.left = left;
        this.right = right;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }// end of expression

  // /////////////////////////////////////////////
  // statement
  public static class Stm {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class Assign extends T {
      public String id;
      public Exp.T exp;
      public boolean isField;

      public Assign(String id, Exp.T exp, boolean isField) {
        this.id = id;
        this.exp = exp;
        this.isField = isField;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class AssignArray extends T {
      public String id;
      public Exp.T index;
      public Exp.T exp;
      public boolean isField;

      public AssignArray(String id, Exp.T index, Exp.T exp,
                         boolean isField) {
        this.id = id;
        this.index = index;
        this.exp = exp;
        this.isField = isField;
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

    public static class If extends T {
      public Exp.T condition;
      public T thenn;
      public T elsee;

      public If(Exp.T condition, T thenn, T elsee) {
        this.condition = condition;
        this.thenn = thenn;
        this.elsee = elsee;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class Print extends T {
      public Exp.T exp;

      public Print(Exp.T exp) {
        this.exp = exp;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    public static class While extends T {
      public Exp.T condition;
      public T body;

      public While(Exp.T condition, T body) {
        this.condition = condition;
        this.body = body;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }// end of statement

  // /////////////////////////////////////////////
  // class
  public static class Class {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class ClassSingle extends T {
      public String id;
      public LinkedList<codegen.RuntimeC.Tuple> decs;

      public ClassSingle(String id, LinkedList<codegen.RuntimeC.Tuple> decs) {
        this.id = id;
        this.decs = decs;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }// end of class

  // /////////////////////////////////////////////
  // vtable
  public static class Vtable {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    /**
     * Every class has a corresponding Vtable. In java, a class has its field
     * and methods. In RuntimeC, there has no concept of `Class`, so the class in java
     * is just a structure in RuntimeC, and the methods in java translated to the
     * point to specific function.
     * <p>
     * VtableSingle also has the class_gc_map, which signed the reference of
     * class fields.
     */
    public static class VtableSingle extends T {
      public String cname; // name of the class
      public java.util.ArrayList<codegen.RuntimeC.Ftuple> ms; // all methods

      public VtableSingle(String cname, ArrayList<codegen.RuntimeC.Ftuple> ms) {
        this.cname = cname;
        this.ms = ms;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

  }// end of vtable

  // /////////////////////////////////////////////
  // method
  public static class Method {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class MethodSingle extends T {
      public Type.T retType; // return type.
      public String classId; // the class name that the method belong to.
      public String id; // method name
      public LinkedList<Dec.T> formals;
      public LinkedList<Dec.T> locals;
      public LinkedList<Stm.T> stms;
      public Exp.T retExp;

      public MethodSingle(Type.T retType, String classId, String id,
                          LinkedList<Dec.T> formals, LinkedList<Dec.T> locals,
                          LinkedList<Stm.T> stms, Exp.T retExp) {
        this.retType = retType;
        this.classId = classId;
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

  // /////////////////////////////////////////////
  // main method
  public static class MainMethod {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class MainMethodSingle extends T {
      public LinkedList<Dec.T> locals;
      public Stm.T stm;

      public MainMethodSingle(LinkedList<Dec.T> locals, Stm.T stm) {
        this.locals = locals;
        this.stm = stm;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }// end of main method

  // ////////////////////////////////////////////
  // program
  public static class Program {
    public static abstract class T implements codegen.RuntimeC.Acceptable {
    }

    public static class ProgramSingle extends T {
      public LinkedList<Class.T> classes;
      public LinkedList<Vtable.T> vtables;
      public LinkedList<Method.T> methods;
      public MainMethod.T mainMethod;

      public ProgramSingle(LinkedList<Class.T> classes,
                           LinkedList<Vtable.T> vtables,
                           LinkedList<Method.T> methods,
                           MainMethod.T mainMethod) {
        this.classes = classes;
        this.vtables = vtables;
        this.methods = methods;
        this.mainMethod = mainMethod;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }// end of program
}