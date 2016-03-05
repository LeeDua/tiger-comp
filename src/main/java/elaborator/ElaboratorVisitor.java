package elaborator;

import java.util.LinkedList;
import java.util.Vector;

import ast.Ast.Class;
import ast.Ast.Exp.False;
import ast.Ast.Exp.Id;
import ast.Ast.Exp.Length;
import ast.Ast.Exp.Lt;
import ast.Ast.Exp.NewIntArray;
import ast.Ast.Exp.NewObject;
import ast.Ast.Exp.Not;
import ast.Ast.Exp.Num;
import ast.Ast.Exp.Sub;
import ast.Ast.Exp.This;
import ast.Ast.Exp.Times;
import ast.Ast.Exp.True;
import ast.Ast.MainClass;
import ast.Ast.Class.ClassSingle;
import ast.Ast.Dec;
import ast.Ast.Exp;
import ast.Ast.Exp.Add;
import ast.Ast.Exp.And;
import ast.Ast.Exp.ArraySelect;
import ast.Ast.Exp.Call;
import ast.Ast.Method;
import ast.Ast.Method.MethodSingle;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Stm;
import ast.Ast.Stm.Assign;
import ast.Ast.Stm.AssignArray;
import ast.Ast.Stm.Block;
import ast.Ast.Stm.If;
import ast.Ast.Stm.Print;
import ast.Ast.Stm.While;
import ast.Ast.Type;
import ast.Ast.Type.ClassType;

public class ElaboratorVisitor implements ast.Visitor
{
  ClassTable classTable; // symbol table for class
  MethodTable methodTable; // symbol table for each method
  String currentClass; // the class name being elaborated
  Type.T type; // type of the expression being elaborated
  int linenum;
  Vector<Error> errorStack;

  public ElaboratorVisitor()
  {
    this.classTable = new ClassTable();
    this.methodTable = new MethodTable();
    this.currentClass = null;
    this.type = null;
    this.errorStack = new Vector<>();
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(Add e)
  {
    this.linenum = e.linenum;
    e.left.accept(this);
    Type.T t = this.type;
    e.right.accept(this);
    if (t.getType() != Type.TYPE_INT ||
        t.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, linenum);
      errorStack.add(Error.MISTYPE);
    }

  }

  @Override
  public void visit(And e)
  {
    this.linenum = e.linenum;
    e.left.accept(this);
    Type.T t = this.type;
    e.right.accept(this);
    if (t.getType() != Type.TYPE_BOOLEAN ||
        this.type.getType() != Type.TYPE_BOOLEAN) {
      Error.MISTYPE.error(this, linenum);
    }
  }

  @Override
  public void visit(ArraySelect e)
  {
    this.linenum = e.linenum;
    e.index.accept(this);
    if (this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, linenum);
    }
    e.array.accept(this);
    this.type = new ast.Ast.Type.Int();
  }

  /**
   * @param subName
   * @param baseName
   * @return
   */
  private boolean findBase(String subName, String baseName)
  {
    if (subName == null) {
      return false;
    }
    ClassBinding cb = this.classTable.get(subName);
    if (cb == null) {
      return false;
    }
    if (cb.extendss == null) {
      return false;
    } else if (cb.extendss.equals(baseName)) {
      return true;
    } else {
      return findBase(cb.extendss, baseName);
    }
  }

  /**
   * @param args
   * @param proto
   * @return If matched, return a type list which has the most generic type.
   * Otherwise, return null.
   */
  private LinkedList<Type.T> elabArgsList(LinkedList<Type.T> args, LinkedList<Type.T> proto)
  {
    if (args.size() != proto.size()) {
      Error.MISTYPE.error(this, linenum);
    }
    for (int i = 0; i < args.size(); i++) {
      Type.T argType = args.get(i);
      Type.T protoType = proto.get(i);
      if (argType.toString().equals(protoType.toString())) {
        continue;
      } else {
        String subName = argType.toString();
        String baseName = protoType.toString();
        if (findBase(subName, baseName)) {
          args.set(i, protoType);
        } else {
          Error.MISTYPE.error(this, linenum);
        }
      }
    }
    return args;
  }

  @Override
  public void visit(Call e)
  {
    this.linenum = e.linenum;
    e.caller.accept(this);
    Type.T callerType = this.type;
    if (callerType.getType() != Type.TYPE_CLASS) {
      Error.MISTYPE.error(this, linenum);
    }

    Type.ClassType ty = (ClassType) callerType;
    e.type = ty.id;
    MethodType methodType = this.classTable.getMethodType(ty.id, e.id);
    if (methodType == null) {
      Error.UNDECL.error(this, linenum);
    }
    LinkedList<Type.T> argsType = new LinkedList<>();
    for (Exp.T a : e.args) {
      a.accept(this);
      argsType.addLast(this.type);
    }
    LinkedList<Type.T> protoType = new LinkedList<>();
    for (Dec.T dec : methodType.argsType) {
      Dec.DecSingle d = (Dec.DecSingle) dec;
      protoType.add(d.type);
    }
    e.at = elabArgsList(argsType, protoType);
    this.type = methodType.retType;
    e.retType = this.type;
  }

  @Override
  public void visit(False e)
  {
    this.linenum = e.linenum;
    this.type = new Type.Boolean();
  }

  @Override
  public void visit(Id e)
  {
    this.linenum = e.linenum;
    // first look up the id in method table
    Type.T type = this.methodTable.get(e.id);
    // if search failed, then s.id must be a class field.
    if (type == null) {
      type = this.classTable.get(this.currentClass, e.id);
      // mark this id as a field id, this fact will be
      // useful in later phase.
      e.isField = true;
    }
    if (type == null) {
      Error.UNDECL.error(this, e.linenum);
    }
    this.type = type;
    // record this type on this node for future use.
    e.type = type;
  }

  @Override
  public void visit(Length e)
  {
    this.linenum = e.linenum;
    e.array.accept(this);
    this.type = new Type.Int();
  }

  @Override
  public void visit(Lt e)
  {
    this.linenum = e.linenum;
    e.left.accept(this);
    Type.T ty = this.type;
    e.right.accept(this);
    if (ty.getType() != Type.TYPE_INT ||
        this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, linenum);
    }
    this.type = new Type.Boolean();
  }

  @Override
  public void visit(NewIntArray e)
  {
    this.linenum = e.linenum;
    e.exp.accept(this);
    if (this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, linenum);
    }
    this.type = new Type.IntArray();
  }

  @Override
  public void visit(NewObject e)
  {
    this.linenum = e.linenum;
    this.type = new Type.ClassType(e.id);
  }

  @Override
  public void visit(Not e)
  {
    this.linenum = e.linenum;
    e.exp.accept(this);
    this.type = new Type.Boolean();
  }

  @Override
  public void visit(Num e)
  {
    this.linenum = e.linenum;
    this.type = new Type.Int();
  }

  @Override
  public void visit(Sub e)
  {
    this.linenum = e.linenum;
    e.left.accept(this);
    Type.T t = this.type;
    e.right.accept(this);
    if (t.getType() != Type.TYPE_INT ||
        this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, e.linenum);
    }
    this.type = new Type.Int();
  }

  @Override
  public void visit(This e)
  {
    this.linenum = e.linenum;
    this.type = new Type.ClassType(this.currentClass);
  }

  @Override
  public void visit(Times e)
  {
    this.linenum = e.linenum;
    e.left.accept(this);
    Type.T t = this.type;
    e.right.accept(this);
    if (t.getType() != Type.TYPE_INT ||
        this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, linenum);
    }
    this.type = new Type.Int();
  }

  @Override
  public void visit(True e)
  {
    this.linenum = e.linenum;
    this.type = new Type.Boolean();
  }

  // statements
  @Override
  public void visit(Assign s)
  {
    // first look up the id in method table
    Type.T type = this.methodTable.get(s.id);
    // if search failed, then s.id must
    if (type == null) {
      type = this.classTable.get(this.currentClass, s.id);
      s.isField = true;
    }

    if (type == null) {
      Error.UNDECL.error(this, s.linenum);
    }

    s.type = type;
    s.exp.accept(this);
  }

  @Override
  public void visit(AssignArray s)
  {
    //
    Type.T type = this.methodTable.get(s.id);
    //
    if (type == null) {
      type = this.classTable.get(this.currentClass, s.id);
      s.isField = true;

    }
    if (type == null) {
      Error.UNDECL.error(this, s.linenum);
    }
    s.tyep = type;
    s.index.accept(this);
    if (this.type.getType() != Type.TYPE_INT) {
      Error.UNDECL.error(this, s.linenum);
    }
    s.exp.accept(this);
    if (!s.exp.getClass().getName().equals("ast.Ast$Exp$ArraySelect")) {
      if (!this.type.toString().equals("@int")) {
        Error.MISTYPE.error(this, s.linenum);
      }
    } else {
      if (!type.toString().equals("@int[]")) {
        Error.MISTYPE.error(this, s.linenum);
      }
    }
  }

  @Override
  public void visit(Block s)
  {
    for (Stm.T t : s.stms)
      t.accept(this);
  }

  @Override
  public void visit(If s)
  {
    s.condition.accept(this);
    if (this.type.getType() != Type.TYPE_BOOLEAN) {
      Error.MISTYPE.error(this, s.linenum);
    }
    s.thenn.accept(this);
    s.elsee.accept(this);
  }

  @Override
  public void visit(Print s)
  {
    s.exp.accept(this);

    if (this.type.getType() != Type.TYPE_INT) {
      Error.MISTYPE.error(this, s.linenum);
    }

  }

  @Override
  public void visit(While s)
  {
    s.condition.accept(this);
    if (this.type.getType() != Type.TYPE_BOOLEAN) {
      Error.MISTYPE.error(this, s.linenum);
    }
    s.body.accept(this);
  }

  // type
  @Override
  public void visit(Type.Boolean t)
  {
    new util.Bug("The Ast is wrong!");
  }

  @Override
  public void visit(Type.ClassType t)
  {
    new util.Bug("The Ast is wrong!");
  }

  @Override
  public void visit(Type.Int t)
  {
    new util.Bug("The Ast is wrong!");
  }

  @Override
  public void visit(Type.IntArray t)
  {
    new util.Bug("The Ast is wrong!");
  }

  // dec
  @Override
  public void visit(Dec.DecSingle d)
  {
    new util.Bug("The Ast is wrong!");
  }

  // method
  @Override
  public void visit(Method.MethodSingle m)
  {
    // construct the method table
    this.methodTable = new MethodTable();
    try {
      this.methodTable.put(m.formals, m.locals);
    } catch (ElabExpection e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

    for (Stm.T s : m.stms) {
      s.accept(this);
    }
    ClassBinding cb = this.classTable.get(currentClass);
    MethodType methodtype = cb.methods.get(m.id);

    m.retExp.accept(this);
    linenum = m.retExp.linenum;
    if (!methodtype.retType.toString().equals(this.type.toString())) {
      Error.RET.error(this, linenum);
    }
  }

  // class
  @Override
  public void visit(Class.ClassSingle c)
  {
    this.currentClass = c.id;
    for (Method.T m : c.methods) {
      m.accept(this);
    }
  }

  // main class
  @Override
  public void visit(MainClass.MainClassSingle c)
  {
    this.currentClass = c.id;
    // "main" has an argument "arg" of type "String[]", but
    // one has no chance to use it. So it's safe to skip it...

    c.stm.accept(this);

  }

  // ////////////////////////////////////////////////////////
  // step 1: build class table
  // class table for Main class
  private void buildMainClass(MainClass.MainClassSingle main)
  {
    try {
      this.classTable.put(main.id, new ClassBinding(null));
    } catch (ElabExpection e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  // class table for normal classes
  private void buildClass(ClassSingle c)
  {
    try {
      this.classTable.put(c.id, new ClassBinding(c.extendss));
      // VarDecls
      for (Dec.T dec : c.decs) {
        Dec.DecSingle d = (Dec.DecSingle) dec;
        this.classTable.put(c.id, d.id, d.type);
      }
      // Method
      for (Method.T method : c.methods) {
        MethodSingle m = (MethodSingle) method;
        this.classTable.put(c.id, m.id,
            new MethodType(m.retType, m.formals));
      }
    } catch (ElabExpection e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Build a symbol table for class (the class table)
   * a class table is a mapping from class names to class bindings
   * classTable: className -> ClassBinding{extends, fields, methods}
   */
  void buildSymbleTable(ProgramSingle p)
  {
    buildMainClass((MainClass.MainClassSingle) p.mainClass);
    for (Class.T c : p.classes) {
      buildClass((ClassSingle) c);
    }
  }

  // program
  @Override
  public void visit(ProgramSingle p)
  {

    // setp 1
    buildSymbleTable(p);

    // step 2: elaborate each class in turn, under the class table
    // built above.
    p.mainClass.accept(this);
    for (Class.T c : p.classes) {
      c.accept(this);
    }

  }
}
