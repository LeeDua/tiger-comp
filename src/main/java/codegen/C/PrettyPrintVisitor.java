/*------------------------------------------------------------------*/
/* Copyright (C) SSE-USTC, 2014-2015                                */
/*                                                                  */
/*  FILE NAME             :  PrettyPrintVisitor.java                */
/*  PRINCIPAL AUTHOR      :  qcLiu                                  */
/*  LANGUAGE              :  Java                                   */
/*  TARGET ENVIRONMENT    :  ANY                                    */
/*  DATE OF FIRST RELEASE :  2014/10/05                             */
/*  DESCRIPTION           :  the tiger compiler                     */
/*------------------------------------------------------------------*/

/*
 * Revision log:
 *
 * 
 *
 */
package codegen.C;

import codegen.C.Ast.Class.ClassSingle;
import codegen.C.Ast.*;
import codegen.C.Ast.Dec.DecSingle;
import codegen.C.Ast.Exp.*;
import codegen.C.Ast.MainMethod.MainMethodSingle;
import codegen.C.Ast.Method.MethodSingle;
import codegen.C.Ast.Program.ProgramSingle;
import codegen.C.Ast.Stm.*;
import codegen.C.Ast.Type.ClassType;
import codegen.C.Ast.Type.Int;
import codegen.C.Ast.Type.IntArray;
import codegen.C.Ast.Vtable.VtableSingle;
import control.Control;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class PrettyPrintVisitor implements Visitor
{
  private int indentLevel;
  private StringBuilder sb;
  /**
   * When visit the MethodSingle, record the reference declaration for
   * further visit.
   * NOTE: rebinding when visit a new MethodSignle.
   */
  private HashSet<String> referenceDecls = new HashSet<>();
  private HashMap<String, LinkedList<Tuple>> classLocal = new HashMap<>();

  public PrettyPrintVisitor()
  {
    sb = new StringBuilder();
    this.indentLevel = 2;
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
    this.say(s);
    this.sb.append("\n");
  }

  private void say(String s)
  {
    this.sb.append(s);
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(Add e)
  {
    e.left.accept(this);
    this.say(" + ");
    e.right.accept(this);
  }

  @Override
  public void visit(And e)
  {
    e.left.accept(this);
    this.say("&&");
    e.right.accept(this);
  }

  @Override
  public void visit(ArraySelect e)
  {
    e.array.accept(this);
    this.say("[");
    e.index.accept(this);
    this.say("+4]");
  }

  @Override
  public void visit(Call e)
  {
    if (this.referenceDecls.contains(e.assign)) {
      this.say("(frame." + e.assign + "=");
    } else {
      this.say("(" + e.assign + "=");
    }
    e.exp.accept(this);// n=this->r
    this.say(", ");
    if (this.referenceDecls.contains(e.assign)) {
      this.say("frame." + e.assign + "->vptr->" + e.id + "(frame." + e.assign);
    } else {
      this.say(e.assign + "->vptr->" + e.id + "(" + e.assign);
    }
    int size = e.args.size();
    if (size == 0) {
      this.say("))");
      return;
    }
    for (Exp.T x : e.args) {
      this.say(", ");
      x.accept(this);
    }
    this.say("))");
  }

  @Override
  public void visit(Id e)
  {
    if (e.isField) {
      this.say("this->" + e.id);
    } else {
      if (this.referenceDecls.contains(e.id)) {
        this.say("frame." + e.id);
      } else {
        this.say(e.id);
      }
    }
  }

  @Override
  public void visit(Length e)
  {
    e.array.accept(this);
    this.say("[2]");
  }

  @Override
  public void visit(Lt e)
  {
    e.left.accept(this);
    this.say(" < ");
    e.right.accept(this);
  }

  @Override
  public void visit(NewIntArray e)
  {
    this.say("(int*)Tiger_new_array(");
    e.exp.accept(this);
    this.say(")");
  }

  @Override
  public void visit(NewObject e)
  {
    this.say("((struct " + e.id + "*)(Tiger_new (&" + e.id +
        "_vtable_, sizeof(struct " + e.id + "))))");
  }

  @Override
  public void visit(Not e)
  {
    this.say("!(");
    e.exp.accept(this);
    this.say(")");
  }

  @Override
  public void visit(Num e)
  {
    this.say(Integer.toString(e.num));
  }

  @Override
  public void visit(Sub e)
  {
    e.left.accept(this);
    this.say(" - ");
    e.right.accept(this);
  }

  @Override
  public void visit(This e)
  {
    this.say("this");
  }

  @Override
  public void visit(Times e)
  {
    e.left.accept(this);
    this.say(" * ");
    e.right.accept(this);
  }

  // statements
  @Override
  public void visit(Assign s)
  {
    this.printSpaces();
    if (s.isField) {
      this.say("this->" + s.id + " = ");
    } else {
      if (this.referenceDecls.contains(s.id)) {
        this.say("frame." + s.id + " = ");
      } else {
        this.say(s.id + " = ");
      }
    }
    s.exp.accept(this);
    this.sayln(";");
  }

  @Override
  public void visit(AssignArray s)
  {
    this.printSpaces();
    if (s.isField) {
      this.say("this->" + s.id + "[");
    } else {
      if (this.referenceDecls.contains(s.id)) {
        this.say("frame." + s.id + "[");
      } else {
        this.say(s.id + "[");
      }
    }
    s.index.accept(this);
    this.say("+4]");
    this.say(" = ");
    s.exp.accept(this);
    this.sayln(";");
  }

  @Override
  public void visit(Block s)
  {
    this.printSpaces();
    this.sayln("{");
    this.indent();
    for (Stm.T b : s.stms)
      b.accept(this);
    this.unIndent();
    this.printSpaces();
    this.sayln("}");
  }

  @Override
  public void visit(If s)
  {
    this.printSpaces();
    this.say("if (");
    s.condition.accept(this);
    this.sayln(")");
    this.indent();
    s.thenn.accept(this);
    this.unIndent();
    this.sayln("");
    this.printSpaces();
    this.sayln("else");
    this.indent();
    s.elsee.accept(this);
    this.sayln("");
    this.unIndent();
  }

  @Override
  public void visit(Print s)
  {
    this.printSpaces();
    this.say("System_out_println (");
    s.exp.accept(this);
    this.sayln(");");
  }

  @Override
  public void visit(While s)
  {
    this.printSpaces();
    this.say("while (");
    s.condition.accept(this);
    this.sayln(")");
    this.indent();
    s.body.accept(this);
    this.unIndent();
    this.printSpaces();
  }

  // type
  @Override
  public void visit(ClassType t)
  {
    this.say("struct " + t.id + " *");
  }

  @Override
  public void visit(Int t)
  {
    this.say("int");
  }

  @Override
  public void visit(IntArray t)
  {
    this.say("int* ");// 貌似没用
  }

  // dec
  @Override
  public void visit(DecSingle d)
  {
    d.type.accept(this);
    this.say("");
  }

  // method
  @Override
  public void visit(MethodSingle m)
  {
    this.referenceDecls.clear();
    m.retType.accept(this);
    this.say(" " + m.classId + "_" + m.id + "(");// Fac_ComputeFac
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
    this.sayln("{");// 局部变量声明
    this.printSpaces();
    // decl the frame
    this.sayln("struct " + m.classId + "_" + m.id + "_gc_frame frame;");
    this.printSpaces();
    this.sayln("frame.prev_=previous;");
    this.printSpaces();
    this.sayln("previous=&frame;");
    this.printSpaces();
    this.sayln("frame.arguments_gc_map = " + m.classId + "_" + m.id
        + "_arguments_gc_map;");
    this.printSpaces();
    this.sayln("frame.arguments_base_address = (int*)&this;");
    this.printSpaces();
    this.sayln("frame.locals_gc_map = " + m.classId + "_" + m.id
        + "_locals_gc_map;");

    for (Dec.T d : m.locals) {
      DecSingle dec = (DecSingle) d;
      if (dec.type.getType() > 0) {
        /**
         * If a local variable is a reference, put it into the frame and
         * record this info for further use.
         */
        this.referenceDecls.add(dec.id);
        this.say("  frame." + dec.id + "=0;\n");
      } else {
        this.say("  ");
        dec.type.accept(this); // type
        this.say(" " + dec.id + ";\n");// id
      }
    }
    this.sayln("");
    for (Stm.T s : m.stms)
      s.accept(this);
    this.sayln("");
    this.printSpaces();
    this.sayln("previous=frame.prev_;");
    this.say("  return ");
    m.retExp.accept(this);
    this.sayln(";");
    this.sayln("}");
  }

  @Override
  public void visit(MainMethodSingle m)
  {
    this.referenceDecls.clear();
    this.sayln("int Tiger_main ()");
    this.sayln("{");
    this.indent();
    this.printSpaces();
    this.sayln("struct Tiger_main_gc_frame frame;");
    // initial frame
    this.printSpaces();
    this.sayln("frame.prev_=previous;");
    this.printSpaces();
    this.sayln("previous=&frame;");
    this.printSpaces();
    this.sayln("frame.arguments_gc_map = 0;");
    this.printSpaces();
    this.sayln("frame.arguments_base_address = 0;");
    this.printSpaces();
    this.sayln("frame.locals_gc_map = Tiger_main_locals_gc_map;");
    this.unIndent();

    for (Dec.T dec : m.locals) {
      this.say("  ");
      DecSingle d = (DecSingle) dec;
      if (d.type.getType() > 0) {
        this.referenceDecls.add(d.id);
        this.say("  frame." + d.id + "=0;\n");
      } else {
        d.type.accept(this);
        this.say(" ");
        this.sayln(d.id + ";");
      }
    }
    this.indent();
    m.stm.accept(this);
    this.printSpaces();
    this.sayln("return 0;\n}\n");
  }

  /**
   * Output the declaration of vtable.
   *
   * @param v
   */
  @Override
  public void visit(VtableSingle v)
  {
    this.sayln("struct " + v.cname + "_vtable");
    this.sayln("{");
    this.printSpaces();
    this.sayln("char* " + v.cname + "_gc_map;");
    for (codegen.C.Ftuple t : v.ms) {
      this.say("  ");
      t.ret.accept(this);
      this.say(" (*" + t.id + ")(");
      int size = t.args.size();
      for (Dec.T d : t.args) {
        DecSingle dd = (DecSingle) d;
        dd.type.accept(this);
        this.say(" " + dd.id);
        size--;
        if (size > 0) {
          this.say(",");
        }
      }
      this.sayln(");");
    }
    this.sayln("};\n");
  }

  /**
   * Output the defination of virtual method table.
   * Use classname_methodname to repect a method in C.
   *
   * @param v
   */
  private void outputVtable(VtableSingle v)
  {
    this.sayln("struct " + v.cname + "_vtable " + v.cname + "_vtable_ = ");
    this.sayln("{");
    LinkedList<Tuple> locals = this.classLocal.get(v.cname);
    this.printSpaces();
    this.say("\"");
    for (Tuple t : locals) {
      if (t.type.getType() > 0) {
        this.say("1");
      } else {
        this.say("0");
      }
    }
    this.sayln("\",");
    for (codegen.C.Ftuple t : v.ms) {
      this.say("  ");
      this.sayln(t.classs + "_" + t.id + ",");
    }
    this.sayln("};\n");
  }

  // outputGCstack
  private void outputGCstack(
      codegen.C.Ast.MainMethod.MainMethodSingle mainMethod)
  {
    this.sayln("struct Tiger_main_gc_frame");
    this.sayln("{");
    this.indent();
    this.printSpaces();
    this.sayln("void *prev_;");
    this.printSpaces();
    this.sayln("char *arguments_gc_map;");
    this.printSpaces();
    this.sayln("int *arguments_base_address;");
    this.printSpaces();
    this.sayln("int locals_gc_map;");
    for (codegen.C.Ast.Dec.T d : mainMethod.locals) {
      DecSingle dd = (DecSingle) d;
      if (dd.type.getType() > 0) {
        this.printSpaces();
        dd.accept(this);
        this.say(" ");
        this.sayln(dd.id + ";");
      }
    }
    this.unIndent();
    this.sayln("};\n");
  }

  private void outputGCstack(codegen.C.Ast.Method.MethodSingle m)
  {
    this.sayln("struct " + m.classId + "_" + m.id + "_gc_frame");
    this.sayln("{");
    this.indent();
    this.printSpaces();
    this.sayln("void *prev_;");
    this.printSpaces();
    this.sayln("char *arguments_gc_map;");
    this.printSpaces();
    this.sayln("int *arguments_base_address;");
    this.printSpaces();
    this.sayln("int locals_gc_map;");
    for (codegen.C.Ast.Dec.T d : m.locals) {
      DecSingle dd = (DecSingle) d;
      if (dd.type.getType() > 0) {
        this.printSpaces();
        dd.accept(this);
        this.say(" ");
        this.sayln(dd.id + ";");
      }
    }
    this.unIndent();
    this.sayln("};\n");
  }

  // outputGcmap
  private void outputGCmap(MainMethodSingle m)
  {

    this.sayln("int Tiger_main_locals_gc_map = 1;");
    this.sayln("");
  }

  private void outputGCmap(MethodSingle m)
  {
    int i = 0;
    this.say("char* " + m.classId + "_" + m.id + "_arguments_gc_map=");
    this.say("\"");
    for (codegen.C.Ast.Dec.T d : m.formals) {
      DecSingle dd = (DecSingle) d;
      if (dd.type instanceof Type.ClassType
          || dd.type instanceof Type.IntArray) {
        this.say("1");
      } else {
        this.say("0");
      }
    }
    this.sayln("\";");
    // locals_gc_map
    for (codegen.C.Ast.Dec.T d : m.locals) {
      DecSingle dd = (DecSingle) d;
      if (dd.type instanceof Type.ClassType
          || dd.type instanceof Type.IntArray) {
        i++;
      }
    }
    this.sayln(
        "int " + m.classId + "_" + m.id + "_locals_gc_map=" + i + ";");
    this.sayln("");
  }

  // class
  @Override
  public void visit(ClassSingle c)
  {
    LinkedList<Tuple> locals = new LinkedList<>();
    this.sayln("struct " + c.id);
    this.sayln("{");
    this.sayln("  struct " + c.id + "_vtable *vptr;");
    this.printSpaces();
    this.sayln("int isObjOrArray;");
    this.printSpaces();
    this.sayln("int length;");
    this.printSpaces();
    this.sayln("void* forwarding;");
    for (codegen.C.Tuple t : c.decs) {
      this.say("  ");
      t.type.accept(this);
      this.say(" ");
      this.sayln(t.id + ";");
      locals.add(t);
    }
    this.classLocal.put(c.id, locals);
    this.sayln("};");
  }

  // program
  @Override
  public void visit(ProgramSingle p)
  {
    // we'd like to output to a file, rather than the "stdout".
    this.sayln("// This is automatically generated by the Tiger compiler.");
    this.sayln("// Do NOT modify!\n");
    this.sayln("extern void *previous;");
    this.sayln("extern void *Tiger_new_array (int);");
    this.sayln("extern void *Tiger_new (void *, int);");
    this.sayln("extern int System_out_println (int);");
    this.sayln("// structures");
    for (codegen.C.Ast.Class.T c : p.classes) {
      c.accept(this);
    }
    this.sayln("// vtables structures");
    for (Vtable.T v : p.vtables) {
      v.accept(this);
    }
    this.sayln("");
    /**
     * Decl befor use.
     */
    this.sayln("//methods decl");
    for (Method.T mm : p.methods) {
      MethodSingle m = (MethodSingle) mm;
      m.retType.accept(this);
      this.say(" " + m.classId + "_" + m.id + "(");// Fac_ComputeFac
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
      this.sayln(");");
    }
    this.sayln("// vtables");
    for (Vtable.T v : p.vtables) {
      outputVtable((VtableSingle) v);
    }
    this.sayln("");
    // GC stack frames
    this.sayln("//GC stack frames");
    outputGCstack((codegen.C.Ast.MainMethod.MainMethodSingle) p.mainMethod);
    for (codegen.C.Ast.Method.T m : p.methods) {
      outputGCstack((MethodSingle) m);
    }
    // memory GC maps
    this.sayln("// memory GC maps");
    outputGCmap((codegen.C.Ast.MainMethod.MainMethodSingle) p.mainMethod);
    for (codegen.C.Ast.Method.T m : p.methods) {
      outputGCmap((codegen.C.Ast.Method.MethodSingle) m);
    }
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

  @Override
  public String toString()
  {
    return this.sb.toString();
  }
}