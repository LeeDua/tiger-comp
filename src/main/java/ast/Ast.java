package ast;

import java.util.LinkedList;

public class Ast {
  // ///////////////////////////////////////////////////////////
  // type
  public static class Type {
    public static final int TYPE_BOOLEAN = -1;
    public static final int TYPE_INT = 0;
    public static final int TYPE_INTARRAY = 1;
    public static final int TYPE_CLASS = 2;

    public static abstract class T implements ast.Acceptable {
      public abstract int getType();
    }

    // boolean
    public static class Boolean extends T {
      public Boolean() {
      }

      @Override
      public String toString() {
        return "@boolean";
      }

      @Override
      public int getType()//
      {
        return TYPE_BOOLEAN;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    // class
    public static class ClassType extends T {
      public String id;

      public ClassType(String id) {
        this.id = id;
      }

      @Override
      public String toString() {
        // since Type.boolean toString() is "@boolean"
        // return "@" + this.id;
        return this.id;
      }

      @Override
      public int getType() {
        return TYPE_CLASS;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }

    // int
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
      public int getType() {
        return TYPE_INT;
      }
    }

    // int[]
    public static class IntArray extends T {
      // String id;
      public IntArray() {
        // this.id=id;
      }

      @Override
      public String toString() {
        return "@int[]";
      }

      @Override
      public int getType()

      {
        return TYPE_INTARRAY;
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
    public static abstract class T implements ast.Acceptable {
      int linenum;
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
        this.linenum = linenum;
      }

      @Override
      public void accept(Visitor v) {
        v.visit(this);
      }
    }
  }

  // /////////////////////////////////////////////////////////
  // expression
  public static class Exp {
    public static abstract class T implements ast.Acceptable {
      public int linenum;
    }

    // +
    public static class Add extends T {
      public T left;
      public T right;

      public Add(T left, T right, int linenum) {
        this.left = left;
        this.right = right;
        this.linenum = linenum;
      }

      public Add(T left, T right) {
        this.left = left;
        this.right = right;
        this.linenum = 0;

      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // and
    public static class And extends T {
      public T left;
      public T right;

      public And(T left, T right, int linenum) {
        this.left = left;
        this.right = right;
        this.linenum = linenum;
      }

      public And(T left, T right) {
        this.left = left;
        this.right = right;
        this.linenum = 0;

      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // ArraySelect
    public static class ArraySelect extends T {
      public T array;
      public T index;

      public ArraySelect(T array, T index, int linenum) {
        this.array = array;
        this.index = index;
        this.linenum = linenum;
      }

      public ArraySelect(T array, T index) {
        this.array = array;
        this.index = index;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // Call
    public static class Call extends T {
      public T caller;
      public String id; // method name that be invoked
      public java.util.LinkedList<T> args;
      public Type.ClassType type; // type of caller
      public java.util.LinkedList<Type.T> at; // args type list
      public Type.T retType;

      public Call(T caller, String id, java.util.LinkedList<T> args,
                  Type.ClassType type, java.util.LinkedList<Type.T> at, Type.T retType,
                  int linenum) {
        this.caller = caller;
        this.id = id;
        this.args = args;
        this.type = type;
        this.at = at;
        this.retType = retType;
        this.linenum = linenum;
      }

      public Call(T caller, String id, java.util.LinkedList<T> args,
                  int linenum) {
        this.caller = caller;
        this.id = id;
        this.args = args;
        this.type = null;
        this.linenum = linenum;
      }

      public Call(T caller, String id, java.util.LinkedList<T> args) {
        this.caller = caller;
        this.id = id;
        this.args = args;
        this.type = null;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // False
    public static class False extends T {

      public False(int linenum) {
        this.linenum = linenum;
      }

      public False() {
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // Id
    public static class Id extends T {
      public String id;
      public Type.T type;
      public boolean isField; // whether or not this is a class field

      public Id(String id, int linenum) {
        this.id = id;
        this.type = null;
        this.isField = false;
        this.linenum = linenum;
      }

      public Id(String id) {
        this.id = id;
        this.type = null;
        this.isField = false;
        this.linenum = 0;
      }

      public Id(String id, Type.T type, boolean isField, int linenum) {
        this.id = id;
        this.type = type;
        this.isField = isField;
        this.linenum = linenum;
      }

      public Id(String id, Type.T type, boolean isField) {
        this.id = id;
        this.type = type;
        this.isField = isField;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // length
    public static class Length extends T {
      public T array;

      public Length(T array, int linenum) {
        this.array = array;
        this.linenum = linenum;
      }

      public Length(T array) {
        this.array = array;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // <
    public static class Lt extends T {
      public T left;
      public T right;

      public Lt(T left, T right, int linenum) {
        this.left = left;
        this.right = right;
        this.linenum = linenum;
      }

      public Lt(T left, T right) {
        this.left = left;
        this.right = right;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // new int [e]
    public static class NewIntArray extends T {
      public T exp;

      public NewIntArray(T exp, int linenum) {
        this.exp = exp;
        this.linenum = linenum;
      }

      public NewIntArray(T exp) {
        this.exp = exp;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // new A();
    public static class NewObject extends T {
      public String id;

      public NewObject(String id, int linenum) {
        this.id = id;
        this.linenum = linenum;
      }

      public NewObject(String id) {
        this.id = id;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // !
    public static class Not extends T {
      public T exp;

      public Not(T exp, int linenum) {
        this.exp = exp;
        this.linenum = linenum;
      }

      public Not(T exp) {
        this.exp = exp;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // number
    public static class Num extends T {
      public int num;

      public Num(int num, int linenum) {
        this.num = num;
        this.linenum = linenum;
      }

      public Num(int num) {
        this.num = num;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // -
    public static class Sub extends T {
      public T left;
      public T right;

      public Sub(T left, T right, int linenum) {
        this.left = left;
        this.right = right;
        this.linenum = linenum;
      }

      public Sub(T left, T right) {
        this.left = left;
        this.right = right;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // this
    public static class This extends T {
      public This(int linenum) {
        this.linenum = linenum;
      }

      public This() {
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // *
    public static class Times extends T {
      public T left;
      public T right;

      public Times(T left, T right, int linenum) {
        this.left = left;
        this.right = right;
        this.linenum = linenum;
      }

      public Times(T left, T right) {
        this.left = left;
        this.right = right;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // True
    public static class True extends T {
      public True(int linenum) {
        this.linenum = linenum;
      }

      public True() {
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

  }// end of expression

  // /////////////////////////////////////////////////////////
  // statement
  public static class Stm {
    public static abstract class T implements ast.Acceptable {

      public int linenum;
    }

    // assign
    public static class Assign extends T {
      public String id;
      public Exp.T exp;
      public Type.T type; // type of the id.
      public boolean isField;// weather it's a classfield

      public Assign(String id, ast.Ast.Exp.T exp, ast.Ast.Type.T type,
                    boolean isField, int linenum) {
        this.id = id;
        this.exp = exp;
        this.type = type;
        this.isField = isField;
        this.linenum = linenum;
      }

      public Assign(String id, Exp.T exp, int linenum) {
        this.id = id;
        this.exp = exp;
        this.type = null;
        isField = false;
        this.linenum = linenum;
      }

      public Assign(String id, Exp.T exp) {
        this.id = id;
        this.exp = exp;
        this.type = null;
        isField = false;
        this.linenum = 0;
      }

      // public Assign(Type.T type)
      // {
      // this.type=type;
      // }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // assign-array number[10]=exp;
    public static class AssignArray extends T {
      public String id;
      public Exp.T index;
      public Exp.T exp;
      public Type.T type;// type of the id
      public boolean isField;

      public AssignArray(String id, ast.Ast.Exp.T index,
                         ast.Ast.Exp.T exp, ast.Ast.Type.T type,
                         boolean isField,
                         int linenum) {
        this.id = id;
        this.index = index;
        this.exp = exp;
        this.type = type;
        this.isField = isField;
        this.linenum = linenum;
      }

      public AssignArray(String id, Exp.T index, Exp.T exp, int linenum) {
        this.id = id;
        this.index = index;
        this.exp = exp;
        this.isField = false;
        this.linenum = linenum;
      }

      public AssignArray(String id, Exp.T index, Exp.T exp) {
        this.id = id;
        this.index = index;
        this.exp = exp;
        this.isField = false;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // block
    public static class Block extends T {
      public java.util.LinkedList<T> stms;

      public Block(java.util.LinkedList<T> stms, int linenum) {
        this.stms = stms;
        this.linenum = linenum;
      }

      public Block(java.util.LinkedList<T> stms) {
        this.stms = stms;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // if
    public static class If extends T {
      public Exp.T condition;
      public T thenn;
      public T elsee;

      public If(Exp.T condition, T thenn, T elsee, int linenum) {
        this.condition = condition;
        this.thenn = thenn;
        this.elsee = elsee;
        this.linenum = linenum;
      }

      public If(Exp.T condition, T thenn, T elsee) {
        this.condition = condition;
        this.thenn = thenn;
        this.elsee = elsee;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // Print
    public static class Print extends T {
      public Exp.T exp;

      public Print(Exp.T exp, int linenum) {
        this.exp = exp;
        this.linenum = linenum;
      }

      public Print(Exp.T exp) {
        this.exp = exp;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

    // while
    public static class While extends T {
      public Exp.T condition;
      public T body;

      public While(Exp.T condition, T body, int linenum) {
        this.condition = condition;
        this.body = body;
        this.linenum = linenum;
      }

      public While(Exp.T condition, T body) {
        this.condition = condition;
        this.body = body;
        this.linenum = 0;
      }

      @Override
      public void accept(ast.Visitor v) {
        v.visit(this);
      }
    }

  }// end of statement

  // /////////////////////////////////////////////////////////
  // method
  public static class Method {
    public static abstract class T implements ast.Acceptable {
    }

    public static class MethodSingle extends T {
      public Type.T retType;
      public String id;
      public LinkedList<Dec.T> formals;
      public LinkedList<Dec.T> locals;
      public LinkedList<Stm.T> stms;
      public Exp.T retExp;

      public MethodSingle(Type.T retType, String id,
                          LinkedList<Dec.T> formals, LinkedList<Dec.T> locals,
                          LinkedList<Stm.T> stms, Exp.T retExp) {
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
    public static abstract class T implements ast.Acceptable {
    }

    public static class ClassSingle extends T {
      public String id;
      public String extendss; // null for non-existing "extends"
      public java.util.LinkedList<Dec.T> decs;
      public java.util.LinkedList<ast.Ast.Method.T> methods;

      public ClassSingle(String id, String extendss,
                         java.util.LinkedList<Dec.T> decs,
                         java.util.LinkedList<ast.Ast.Method.T> methods) {
        this.id = id;
        this.extendss = extendss;
        this.decs = decs;
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
    public static abstract class T implements ast.Acceptable {
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
        v.visit(this);
      }
    }

  }

  // whole program
  public static class Program {
    public static abstract class T implements ast.Acceptable {
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
