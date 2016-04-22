package codegen.C;

import java.util.LinkedList;

import codegen.C.Ast.Class;
import codegen.C.Ast.Class.ClassSingle;
import codegen.C.Ast.Dec;
import codegen.C.Ast.Exp;
import codegen.C.Ast.Exp.Call;
import codegen.C.Ast.Exp.Id;
import codegen.C.Ast.Exp.Lt;
import codegen.C.Ast.Exp.NewObject;
import codegen.C.Ast.Exp.Num;
import codegen.C.Ast.Exp.Sub;
import codegen.C.Ast.Exp.Add;
import codegen.C.Ast.Exp.And;
import codegen.C.Ast.Exp.This;
import codegen.C.Ast.Exp.Times;
import codegen.C.Ast.MainMethod;
import codegen.C.Ast.MainMethod.MainMethodSingle;
import codegen.C.Ast.Method;
import codegen.C.Ast.Method.MethodSingle;
import codegen.C.Ast.Program;
import codegen.C.Ast.Program.ProgramSingle;
import codegen.C.Ast.Stm;
import codegen.C.Ast.Stm.Assign;
import codegen.C.Ast.Stm.If;
import codegen.C.Ast.Stm.Print;
import codegen.C.Ast.Type;
import codegen.C.Ast.Type.ClassType;
import codegen.C.Ast.Vtable;
import codegen.C.Ast.Vtable.VtableSingle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Given a Java AST, translate it into a C AST and outputs it.

public class TranslateVisitor implements ast.Visitor
{
  private static Logger logger = LogManager.getLogger("TransC");
  private ClassTable table;

  private String classId;
  private Type.T type; // type after translation
  private Dec.T dec; // the return of trans dec
  private Stm.T stm; // the return of trans stm
  private Exp.T exp; // the return of trans exp
  private Method.T method; // the return of trans method
  /**
   * The java method always takes an implicit argument which is the invoker
   * object, in order to deal with that the C function call return a object,
   * there should be declear the return value first, so every `Call` generated
   * a new DeclSingle.
   */
  private LinkedList<Dec.T> tmpVars;

  private LinkedList<Class.T> classes;
  private LinkedList<Vtable.T> vtables;
  private LinkedList<Method.T> methods;
  private MainMethod.T mainMethod;

  public Program.T program;

  public TranslateVisitor()
  {
    this.table = new ClassTable();
    this.classId = null;
    this.type = null;
    this.dec = null;
    this.stm = null;
    this.exp = null;
    this.method = null;
    this.classes = new LinkedList<>();
    this.vtables = new LinkedList<>();
    this.methods = new LinkedList<>();
    this.mainMethod = null;
    this.program = null;
  }

  /**
   * Every `Call` can generat a new DecSingle
   *
   * @return a unique id.
   */
  private String genId()
  {
    return util.Temp.next();
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(ast.Ast.Exp.Add e)
  {
    e.left.accept(this);
    Exp.T left = this.exp;
    e.right.accept(this);
    Exp.T right = this.exp;
    this.exp = new Add(left, right);
  }

  @Override
  public void visit(ast.Ast.Exp.And e)
  {
    e.left.accept(this);
    Exp.T left = this.exp;
    e.right.accept(this);
    Exp.T right = this.exp;
    this.exp = new And(left, right);
  }

  @Override
  public void visit(ast.Ast.Exp.ArraySelect e)
  {
    e.array.accept(this);
    Exp.T array = this.exp;
    e.index.accept(this);
    Exp.T index = this.exp;
    this.exp = new codegen.C.Ast.Exp.ArraySelect(array, index);
  }

  @Override
  public void visit(ast.Ast.Exp.Call e)
  {
    e.caller.accept(this);
    String newid = this.genId();
    this.tmpVars.add(new Dec.DecSingle(new Type.ClassType(e.type), newid));
    Exp.T exp = this.exp;
    LinkedList<Exp.T> args = new LinkedList<>();
    for (ast.Ast.Exp.T x : e.args) {
      x.accept(this);
      args.add(this.exp);
    }
    e.retType.accept(this);
    this.exp = new Call(newid, exp, e.id, args, this.type);
  }

  @Override
  public void visit(ast.Ast.Exp.False e)
  {
    this.exp = new Num(0);
  }

  @Override
  public void visit(ast.Ast.Exp.Id e)
  {
    this.exp = new Id(e.id, e.isField);
  }

  @Override
  public void visit(ast.Ast.Exp.Length e)
  {
    this.type = new Type.IntArray();
    e.array.accept(this);
    Exp.T array = this.exp;
    this.exp = new codegen.C.Ast.Exp.Length(array);
  }

  @Override
  public void visit(ast.Ast.Exp.Lt e)
  {
    e.left.accept(this);
    Exp.T left = this.exp;
    e.right.accept(this);
    Exp.T right = this.exp;
    this.exp = new Lt(left, right);
  }

  @Override
  public void visit(ast.Ast.Exp.NewIntArray e)
  {
    e.exp.accept(this);
    Exp.T t = this.exp;
    this.exp = new codegen.C.Ast.Exp.NewIntArray(t);
  }

  @Override
  public void visit(ast.Ast.Exp.NewObject e)
  {
    this.exp = new NewObject(e.id);
  }

  @Override
  public void visit(ast.Ast.Exp.Not e)
  {
    e.exp.accept(this);
    Exp.T t = this.exp;
    this.exp = new codegen.C.Ast.Exp.Not(t);
  }

  @Override
  public void visit(ast.Ast.Exp.Num e)
  {
    this.exp = new Num(e.num);
  }

  @Override
  public void visit(ast.Ast.Exp.Sub e)
  {
    e.left.accept(this);
    Exp.T left = this.exp;
    e.right.accept(this);
    Exp.T right = this.exp;
    this.exp = new Sub(left, right);
  }

  @Override
  public void visit(ast.Ast.Exp.This e)
  {
    this.exp = new This();
  }

  @Override
  public void visit(ast.Ast.Exp.Times e)
  {
    e.left.accept(this);
    Exp.T left = this.exp;
    e.right.accept(this);
    Exp.T right = this.exp;
    this.exp = new Times(left, right);
  }

  @Override
  public void visit(ast.Ast.Exp.True e)
  {
    this.exp = new Num(1);

  }

  // //////////////////////////////////////////////
  // statements
  @Override
  public void visit(ast.Ast.Stm.Assign s)
  {
    s.exp.accept(this);
    this.stm = new Assign(s.id, this.exp, s.isField);
  }

  @Override
  public void visit(ast.Ast.Stm.AssignArray s)
  {
    s.index.accept(this);
    Exp.T index = this.exp;
    s.exp.accept(this);
    this.stm = new codegen.C.Ast.Stm.AssignArray(s.id, index, this.exp,
        s.isField);
  }

  @Override
  public void visit(ast.Ast.Stm.Block s)
  {
    LinkedList<codegen.C.Ast.Stm.T> stms = new java.util.LinkedList<>();
    for (ast.Ast.Stm.T t : s.stms) {
      t.accept(this);
      stms.add(this.stm);
    }
    this.stm = new codegen.C.Ast.Stm.Block(stms);
  }

  @Override
  public void visit(ast.Ast.Stm.If s)
  {
    s.condition.accept(this);
    Exp.T condition = this.exp;
    s.thenn.accept(this);
    Stm.T thenn = this.stm;
    s.elsee.accept(this);
    Stm.T elsee = this.stm;
    this.stm = new If(condition, thenn, elsee);
  }

  @Override
  public void visit(ast.Ast.Stm.Print s)
  {
    s.exp.accept(this);
    this.stm = new Print(this.exp);
  }

  @Override
  public void visit(ast.Ast.Stm.While s)
  {
    s.condition.accept(this);
    Exp.T condition = this.exp;
    s.body.accept(this);
    Stm.T body = this.stm;
    this.stm = new codegen.C.Ast.Stm.While(condition, body);
  }

  // ///////////////////////////////////////////
  // type
  @Override
  /**
   * the boolean in java be treated as int in C
   */
  public void visit(ast.Ast.Type.Boolean t)
  {
    this.type = new Type.Int();
  }

  @Override
  public void visit(ast.Ast.Type.ClassType t)
  {
    this.type = new Type.ClassType(t.id);
  }

  @Override
  public void visit(ast.Ast.Type.Int t)
  {
    this.type = new Type.Int();
  }

  @Override
  public void visit(ast.Ast.Type.IntArray t)
  {
    this.type = new Type.IntArray();
  }

  // ////////////////////////////////////////////////
  // dec
  @Override
  public void visit(ast.Ast.Dec.DecSingle d)
  {
    d.type.accept(this);
    this.dec = new codegen.C.Ast.Dec.DecSingle(this.type, d.id);
  }

  // method
  @Override
  public void visit(ast.Ast.Method.MethodSingle m)
  {
    this.tmpVars = new LinkedList<>();
    m.retType.accept(this);
    Type.T c_retType = this.type;
    LinkedList<Dec.T> c_formals = new LinkedList<>();
    // every method has a implicited first argument "this".
    c_formals.add(new Dec.DecSingle(new ClassType(this.classId), "this"));
    for (ast.Ast.Dec.T d : m.formals) {
      d.accept(this);
      c_formals.add(this.dec);
    }
    LinkedList<Dec.T> c_locals = new LinkedList<>();
    for (ast.Ast.Dec.T d : m.locals) {
      d.accept(this);
      c_locals.add(this.dec);
    }
    LinkedList<Stm.T> c_stms = new LinkedList<>();
    for (ast.Ast.Stm.T s : m.stms) {
      s.accept(this);
      c_stms.add(this.stm);
    }
    m.retExp.accept(this);
    Exp.T c_retExp = this.exp;
    // add the additional decls generated by function call.
    for (Dec.T dec : this.tmpVars) {
      c_locals.add(dec);
    }
    this.method = new MethodSingle(c_retType, this.classId, m.id,
        c_formals, c_locals, c_stms, c_retExp);
  }

  // class
  @Override
  public void visit(ast.Ast.Class.ClassSingle c)
  {
    ClassBinding cb = this.table.get(c.id);
    this.classes.add(new ClassSingle(c.id, cb.fields));
    this.vtables.add(new VtableSingle(c.id, cb.methods));
    this.classId = c.id;
    for (ast.Ast.Method.T m : c.methods) {
      m.accept(this);
      this.methods.add(this.method);
    }
  }

  // main class
  @Override
  public void visit(ast.Ast.MainClass.MainClassSingle c)
  {
    ClassBinding cb = this.table.get(c.id);
    this.classes.add(new ClassSingle(c.id, cb.fields));
    this.vtables.add(new VtableSingle(c.id, cb.methods));
    this.tmpVars = new LinkedList<>();
    c.stm.accept(this);
    MainMethod.T mthd = new MainMethodSingle(this.tmpVars, this.stm);
    this.mainMethod = mthd;
  }

  // /////////////////////////////////////////////////////
  // the first pass
  private void scanMain(ast.Ast.MainClass.T m)
  {
    this.table.init(((ast.Ast.MainClass.MainClassSingle) m).id, null);
    // this is a special hacking in that we don't want to
    // enter "main" into the table.
  }

  private void scanClasses(java.util.LinkedList<ast.Ast.Class.T> cs)
  {
    // put empty chuncks into the table
    for (ast.Ast.Class.T c : cs) {
      ast.Ast.Class.ClassSingle cc = (ast.Ast.Class.ClassSingle) c;
      this.table.init(cc.id, cc.extendss);
    }
    // put class fields and methods into the table
    for (ast.Ast.Class.T c : cs) {
      ast.Ast.Class.ClassSingle cc = (ast.Ast.Class.ClassSingle) c;
      LinkedList<Dec.T> cDecs = new LinkedList<>();
      for (ast.Ast.Dec.T dec : cc.decs) {
        dec.accept(this);
        cDecs.add(this.dec);
      }
      this.table.initDecs(cc.id, cDecs);
      // all methods
      java.util.LinkedList<ast.Ast.Method.T> methods = cc.methods;
      for (ast.Ast.Method.T mthd : methods) {
        ast.Ast.Method.MethodSingle m = (ast.Ast.Method.MethodSingle) mthd;
        LinkedList<Dec.T> cArgs = new LinkedList<>();
        // XXX every method has a implicit first argument "this"
        cArgs.add(new Dec.DecSingle(new ClassType(cc.id), "this"));
        for (ast.Ast.Dec.T arg : m.formals) {
          arg.accept(this);
          cArgs.add(this.dec);
        }
        m.retType.accept(this);
        Type.T cRet = this.type;
        this.table.initMethod(cc.id, cRet, cArgs, m.id);
      }
    }
    // calculate all inheritance information
    for (ast.Ast.Class.T c : cs) {
      ast.Ast.Class.ClassSingle cc = (ast.Ast.Class.ClassSingle) c;
      this.table.inherit(cc.id);
    }
  }

  private void scanProgram(ast.Ast.Program.T p)
  {
    // use stacktrace to get method name
    logger.debug(new Throwable().getStackTrace()[1].getMethodName()+" start");
    ast.Ast.Program.ProgramSingle pp = (ast.Ast.Program.ProgramSingle) p;
    scanMain(pp.mainClass);
    scanClasses(pp.classes);
    logger.debug(new Throwable().getStackTrace()[1].getMethodName()+" end");
  }
  // end of the first pass
  // ////////////////////////////////////////////////////

  // program
  @Override
  public void visit(ast.Ast.Program.ProgramSingle p)
  {
    logger.info("translate to C start");
    // The first pass is to scan the whole program "p", and
    // to collect all information of inheritance.
    scanProgram(p);
    // do translations
    p.mainClass.accept(this);
    for (ast.Ast.Class.T classs : p.classes) {
      classs.accept(this);
    }
    this.program = new ProgramSingle(this.classes, this.vtables,
        this.methods, this.mainMethod);
    logger.info("translate to C end");
  }
}
