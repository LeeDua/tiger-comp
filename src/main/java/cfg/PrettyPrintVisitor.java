package cfg;

import cfg.Cfg.Block;
import cfg.Cfg.Block.BlockSingle;
import cfg.Cfg.Class;
import cfg.Cfg.Class.ClassSingle;
import cfg.Cfg.Dec;
import cfg.Cfg.Dec.DecSingle;
import cfg.Cfg.MainMethod.MainMethodSingle;
import cfg.Cfg.Method;
import cfg.Cfg.Method.MethodSingle;
import cfg.Cfg.Operand;
import cfg.Cfg.Operand.Int;
import cfg.Cfg.Operand.Var;
import cfg.Cfg.Program.ProgramSingle;
import cfg.Cfg.Stm;
import cfg.Cfg.Stm.Add;
import cfg.Cfg.Stm.InvokeVirtual;
import cfg.Cfg.Stm.Lt;
import cfg.Cfg.Stm.Move;
import cfg.Cfg.Stm.NewObject;
import cfg.Cfg.Stm.Print;
import cfg.Cfg.Stm.Sub;
import cfg.Cfg.Stm.Times;
import cfg.Cfg.Transfer.Goto;
import cfg.Cfg.Transfer.If;
import cfg.Cfg.Transfer.Return;
import cfg.Cfg.Type.ClassType;
import cfg.Cfg.Type.IntArrayType;
import cfg.Cfg.Type.IntType;
import cfg.Cfg.Vtable;
import cfg.Cfg.Vtable.VtableSingle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import static ast.Ast.Type.TYPE_INTARRAY;

public class PrettyPrintVisitor implements Visitor
{
  public StringBuilder sb;
  private int indentLevel;
  private HashMap<String, LinkedList<Tuple>> class_decs;
  private HashSet<String> redecs;

  public PrettyPrintVisitor()
  {
    this.sb = new StringBuilder();
    this.indentLevel = 2;
    this.class_decs = new HashMap<>();
    this.redecs = new HashSet<>();
  }

  private void indent()
  {
    this.indentLevel += 2;
  }

  private void unIndent()
  {
    this.indentLevel -= 2;
  }

  private void printSpaces()
  {
    int i = this.indentLevel;
    while (i-- != 0)
      this.say(" ");
  }

  private void sayln(String s)
  {
    say(s);
    this.sb.append("\n");
  }

  private void say(String s)
  {
    this.sb.append(s);
  }

  private String getVar(String dst)
  {
    if (this.redecs.contains(dst)) {
      return "frame." + dst;
    } else {
      return dst;
    }
  }

  // /////////////////////////////////////////////////////
  // operand
  @Override
  public void visit(Int operand)
  {
    this.say(Integer.toString(operand.i));
  }

  @Override
  public void visit(Var operand)
  {
    if (operand.isField) {
      this.say("this->" + operand.id);
    } else {
      if (this.redecs.contains(operand.id)) {
        this.say("frame." + operand.id);
      } else {
        this.say(operand.id);
      }
    }
  }

  // statements
  @Override
  public void visit(Add s)
  {
    say(getVar(s.dst) + " = ");
    s.left.accept(this);
    say(" + ");
    s.right.accept(this);
    say(";");
  }

  @Override
  public void visit(Stm.And s)
  {
    this.say(getVar(s.dst) + " = ");
    s.left.accept(this);
    this.say(" && ");
    s.right.accept(this);
    this.say(";");
  }

  @Override
  public void visit(Stm.ArraySelect s)
  {
    this.say(s.dst + " = ");
    s.array.accept(this);
    this.say("[");
    s.index.accept(this);
    this.say("+ARRAYSELECT_OFFSET];");
  }

  @Override
  public void visit(Stm.AssignArray s)
  {
    if (s.isField) {
      this.say("this->" + s.dst + "[");
    } else {
      this.say(getVar(s.dst) + "[");
    }
    s.index.accept(this);
    this.say("+ARRAYSELECT_OFFSET]=");
    s.exp.accept(this);
    this.say(";");
  }

  @Override
  public void visit(InvokeVirtual s)
  {
    this.say(getVar(s.dst) + " = " + getVar(s.obj));
    this.say("->vptr->" + s.f + "(" + getVar(s.obj));
    for (Operand.T x : s.args) {
      this.say(", ");
      x.accept(this);
    }
    this.say(");");
  }

  @Override
  public void visit(Stm.Length s)
  {
    this.say(getVar(s.dst) + " = ");
    s.array.accept(this);
    this.say("[LENGTH];");

  }

  @Override
  public void visit(Lt s)
  {
    this.say(getVar(s.dst) + " = ");
    s.left.accept(this);
    this.say(" < ");
    s.right.accept(this);
    this.say(";");
  }

  @Override
  public void visit(Move s)
  {
    if (s.isField) {
      this.say("this->" + s.dst + " = ");
    } else {
      this.say(getVar(s.dst) + " = ");
    }
    s.src.accept(this);
    this.say(";");
  }

  @Override
  public void visit(Stm.NewIntArray s)
  {
    this.say(getVar(s.dst) + " = (int*)Tiger_new_array(");
    s.size.accept(this);
    this.say(");");

  }

  @Override
  public void visit(NewObject s)
  {
    this.say(getVar(s.dst) + " = ((struct " + s.c + "*)(Tiger_new (&" + s.c
        + "_vtable_, sizeof(struct " + s.c + "))));");
  }

  @Override
  public void visit(Stm.Not s)
  {
    this.say(getVar(s.dst) + " = !(");
    s.exp.accept(this);
    this.say(");");
  }

  @Override
  public void visit(Print s)
  {
    this.say("System_out_println (");
    s.arg.accept(this);
    this.say(");");
  }

  @Override
  public void visit(Sub s)
  {
    this.say(getVar(s.dst) + " = ");
    s.left.accept(this);
    this.say(" - ");
    s.right.accept(this);
    this.say(";");
  }

  @Override
  public void visit(Times s)
  {
    this.say(getVar(s.dst) + " = ");
    s.left.accept(this);
    this.say(" * ");
    s.right.accept(this);
    this.say(";");
  }

  // transfer
  @Override
  public void visit(If s)
  {
    this.say("if (");
    s.operand.accept(this);
    this.sayln(")");
    this.printSpaces();
    this.sayln("  goto " + s.truee.toString() + ";");
    this.printSpaces();
    this.sayln("else");
    this.printSpaces();
    this.say("  goto " + s.falsee.toString() + ";");
  }

  @Override
  public void visit(Goto s)
  {
    this.say("goto " + s.label.toString() + ";");
  }

  @Override
  public void visit(Return s)
  {
    this.sayln("previous = frame.prev_;");
    printSpaces();
    this.say("return ");
    s.operand.accept(this);
    this.say(";");
  }

  // type
  @Override
  public void visit(ClassType t)
  {
    this.say("struct " + t.id + "*");
  }

  @Override
  public void visit(IntType t)
  {
    this.say("int");
  }

  @Override
  public void visit(IntArrayType t)
  {
    this.say("int*");
  }

  // dec
  @Override
  public void visit(DecSingle d)
  {
    d.type.accept(this);
    this.say(" " + d.id);
  }

  // dec
  @Override
  public void visit(BlockSingle b)
  {
    this.sayln("// block entry");
    this.say(b.label.toString() + ":\n");
    for (Stm.T s : b.stms) {
      printSpaces();
      s.accept(this);
      this.say("\n");
    }
    printSpaces();
    b.transfer.accept(this);
    this.sayln("\n//block exit");
  }

  // method
  @Override
  public void visit(MethodSingle m)
  {
    this.redecs = new HashSet<>();
    m.retType.accept(this);
    this.say(" " + m.classId + "_" + m.id + "(");
    int size = m.formals.size();
    for (Dec.T d : m.formals) {
      DecSingle dec = (DecSingle) d;
      size--;
      dec.type.accept(this);
      this.say(" " + dec.id);
      if (size > 0) {
        this.say(", ");
      }
    }
    this.sayln(")");

    this.sayln("{");
    printSpaces();
    sayln("struct " + m.classId + "_" + m.id + "_gc_frame frame;");
    printSpaces();
    sayln("frame.prev_ = previous;");
    printSpaces();
    sayln("previous = &frame;");
    printSpaces();
    sayln("frame.arguments_gc_map = " + m.classId + "_" + m.id +
        "_arguments_gc_map;");
    printSpaces();
    sayln("frame.arguments_base_address = (int*)&this;");
    printSpaces();
    sayln(
        "frame.locals_gc_map = " + m.classId + "_" + m.id + "_locals_gc_map;");

    for (Dec.T d : m.locals) {
      printSpaces();
      DecSingle dec = (DecSingle) d;
      if (dec.type instanceof ClassType ||
          dec.type instanceof IntArrayType) {
        this.redecs.add(dec.id);
        sayln("frame." + dec.id + "=0;");
      } else {
        dec.accept(this);
        this.sayln(";");
      }
    }

    this.sayln("");
    for (Block.T block : m.blocks) {
      BlockSingle b = (BlockSingle) block;
      b.accept(this);
      this.sayln("");
    }
    this.sayln("\n}");
  }

  @Override
  public void visit(MainMethodSingle m)
  {
    this.redecs = new HashSet<>();
    this.sayln("int Tiger_main ()");
    this.sayln("{");
    printSpaces();
    sayln("struct Tiger_main_gc_frame frame;");
    printSpaces();
    sayln("frame.prev_ = previous;");
    printSpaces();
    sayln("previous = &frame;");
    printSpaces();
    sayln("frame.arguments_gc_map = 0;");
    printSpaces();
    sayln("frame.arguments_base_address = 0;");
    printSpaces();
    sayln("frame.locals_gc_map = Tiger_main_locals_gc_map;");

    for (Dec.T d : m.locals) {
      DecSingle dec = (DecSingle) d;
      if (dec.type instanceof ClassType ||
          dec.type instanceof IntArrayType) {
        this.redecs.add(dec.id);
        sayln("frame." + dec.id + "=0;");
      } else {
        dec.accept(this);
        this.sayln(";");
      }
    }

    this.sayln("");
    for (Block.T block : m.blocks) {
      BlockSingle b = (BlockSingle) block;
      b.accept(this);
    }
    this.sayln("\n}\n");
  }

  // vtables
  @Override
  public void visit(VtableSingle v)
  {
    this.sayln("struct " + v.id + "_vtable");
    this.sayln("{");
    printSpaces();
    this.sayln("char* " + v.id + "_gc_map;");
    for (Ftuple t : v.ms) {
      this.say("  ");
      t.ret.accept(this);
      this.say(" (*" + t.id + ")(");
      for (int i = 0; i < t.args.size(); i++) {
        DecSingle ds = (DecSingle) t.args.get(i);
        if (i != 0) {
          this.say(", ");
        }
        ds.accept(this);
      }
      this.sayln(");");
    }
    this.sayln("};\n");
  }

  private void outputVtable(VtableSingle v)
  {
    this.sayln("struct " + v.id + "_vtable " + v.id + "_vtable_ = ");
    this.sayln("{");
    printSpaces();
    this.say("\"");
    LinkedList<Tuple> locals = this.class_decs.get(v.id);
    for (Tuple t : locals) {
      if (t.type instanceof ClassType ||
          t.type instanceof IntArrayType) {
        this.say("1");
      } else {
        this.say("0");
      }
    }
    this.say("\",");
    this.sayln("  // bit map");
    for (Ftuple t : v.ms) {
      printSpaces();
      this.sayln(t.classs + "_" + t.id + ",");
    }
    this.sayln("};\n");
  }

  // class
  @Override
  public void visit(ClassSingle c)
  {
    LinkedList<Tuple> locals = new LinkedList<>();
    this.sayln("struct " + c.id);
    this.sayln("{");
    printSpaces();
    this.sayln("struct " + c.id + "_vtable *vptr;");
    printSpaces();
    this.sayln("int isObjOrArray;");
    printSpaces();
    this.sayln("int length;");
    printSpaces();
    this.sayln("void *forwarding;");
    for (Tuple t : c.decs) {
      printSpaces();
      t.type.accept(this);
      this.say(" ");
      this.sayln(t.id + ";");
      locals.add(t);
    }
    this.class_decs.put(c.id, locals);
    this.sayln("};");
  }

  private void outputMainGcStack(Cfg.MainMethod.MainMethodSingle m)
  {
    sayln("struct Tiger_main_gc_frame");
    sayln("{");
    indent();
    printSpaces();
    sayln("void *prev_;");
    printSpaces();
    sayln("char *arguments_gc_map;");
    printSpaces();
    sayln("int *arguments_base_address;");
    printSpaces();
    sayln("int locals_gc_map;");
    for (Dec.T dec : m.locals) {
      DecSingle ds = (DecSingle) dec;
      if (ds.type instanceof ClassType ||
          ds.type instanceof IntArrayType) {
        printSpaces();
        ds.accept(this);
        this.sayln(";");
      }
    }
    unIndent();
    this.sayln("};");
  }

  private void outputGcStack(Method.MethodSingle m)
  {
    sayln("struct " + m.classId + "_" + m.id + "_gc_frame");
    sayln("{");
    indent();
    printSpaces();
    sayln("void *prev_;");
    printSpaces();
    sayln("char *arguments_gc_map;");
    printSpaces();
    sayln("int *arguments_base_address;");
    printSpaces();
    sayln("int locals_gc_map;");
    for (Dec.T dec : m.locals) {
      DecSingle ds = (DecSingle) dec;
      if (ds.type instanceof ClassType ||
          ds.type instanceof IntArrayType) {
        printSpaces();
        ds.accept(this);
        this.sayln(";");
      }
    }
    unIndent();
    this.sayln("};");
  }

  private void outputGcMap(MethodSingle m)
  {
    this.say("char* " + m.classId + "_" + m.id + "_arguments_gc_map = \"");
    for (Dec.T dec : m.formals) {
      DecSingle ds = (DecSingle) dec;
      if (ds.type instanceof ClassType ||
          ds.type instanceof IntArrayType) {
        this.say("1");
      } else {
        this.say("0"); // hack for 64bit system
      }
    }
    this.sayln("\";");
    //locals map
    int i = 0;
    for (Dec.T dec : m.locals) {
      DecSingle ds = (DecSingle) dec;
      if (ds.type instanceof ClassType ||
          ds.type instanceof IntArrayType) {
        i++;
      }
    }
    sayln("int " + m.classId + "_" + m.id + "_locals_gc_map= " + i + ";");
  }


  // program
  @Override
  public void visit(ProgramSingle p)
  {
    this.sayln("// This is automatically generated by the Tiger compiler.");
    this.sayln("// Do NOT modify!\n");
    this.sayln("// Control-flow Graph\n");
    this.sayln("#ifdef __M32__");
    this.sayln("#define LENGTH 2");
    this.sayln("#define ARRAYSELECT_OFFSET 4");
    this.sayln("#else");
    this.sayln("#define LENGTH 3");
    this.sayln("#define ARRAYSELECT_OFFSET 6");
    this.sayln("#endif");

    sayln("extern void *previous;");
    sayln("extern void *Tiger_new_array(int);");
    sayln("extern void *Tiger_new(void*, int);");
    sayln("extern int System_out_println(int);");


    this.sayln("// structures");
    for (Class.T c : p.classes) {
      c.accept(this);
    }

    this.sayln("// vtable structures");
    for (Vtable.T v : p.vtables) {
      v.accept(this);
    }
    this.sayln("");
    this.sayln("// method decls");
    for (Method.T m : p.methods) {
      MethodSingle ms = (MethodSingle) m;
      ms.retType.accept(this);
      this.say(" " + ms.classId + "_" + ms.id + "(");
      for (int i = 0; i < ms.formals.size(); i++) {
        if (i != 0) {
          this.say(", ");
        }
        ms.formals.get(i).accept(this);
      }
      this.sayln(");");
    }

    this.sayln("// vtables");
    for (Vtable.T v : p.vtables) {
      outputVtable((VtableSingle) v);
    }
    this.sayln("");

    this.sayln("// GC stack frame");
    outputMainGcStack((MainMethodSingle) p.mainMethod);
    for (Method.T m : p.methods) {
      outputGcStack((MethodSingle) m);
    }
    this.sayln("");

    this.sayln("// memery GC map");
    this.sayln("int Tiger_main_locals_gc_map = 1;\n");
    for (Method.T m : p.methods) {
      outputGcMap((MethodSingle) m);
      this.sayln("");
    }
    this.sayln("");

    this.sayln("// methods");
    for (Method.T m : p.methods) {
      m.accept(this);
    }
    this.sayln("");

    this.sayln("// main method");
    p.mainMethod.accept(this);
    this.sayln("");

    this.say("\n\n");
  }
}
