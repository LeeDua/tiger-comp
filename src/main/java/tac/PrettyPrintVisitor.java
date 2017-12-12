package tac;

import ast.Ast;

public class PrettyPrintVisitor implements Visitor {

  private StringBuilder sb;

  private int indentLevel;

  public PrettyPrintVisitor() {
    this.sb = new StringBuilder();
    this.indentLevel = 4;
  }

  private void indent() {
    this.indentLevel += 2;
  }

  private void unIndent() {
    this.indentLevel -= 2;
  }

  private void printSpaces() {
    int i = this.indentLevel;
    while (i-- != 0)
      this.say(" ");
  }

  private void sayln(String s) {
    sb.append(s);
    sb.append("\n");
  }

  private void say(String s) {
    sb.append(s);
  }

  @Override
  public void visit(Tac.Operand.Var e) {
    this.say(e.id);

  }

  @Override
  public void visit(Tac.Operand.Int e) {
    this.say(Integer.toString(e.value));

  }

  @Override
  public void visit(Tac.Operand.True e) {
    this.say("true");

  }

  @Override
  public void visit(Tac.Operand.False e) {
    this.say("false");

  }

  @Override
  public void visit(Tac.BinOp.Add e) {
    this.say(" + ");

  }

  @Override
  public void visit(Tac.BinOp.And e) {
    this.say(" && ");

  }

  @Override
  public void visit(Tac.BinOp.Sub e) {
    this.say(" - ");

  }

  @Override
  public void visit(Tac.BinOp.Times e) {
    this.say(" * ");

  }

  @Override
  public void visit(Tac.BinOp.Lt e) {
    this.say(" < ");

  }

  @Override
  public void visit(Tac.UnOp.Not e) {
    this.say("!");

  }

  @Override
  public void visit(Tac.Stm.Assign e) {
    e.dst.accept(this);
    this.say(" = ");
    e.src.accept(this);
  }

  @Override
  public void visit(Tac.Stm.AssignCall e) {
    e.dst.accept(this);
    this.say(" = ");
    e.caller.accept(this);
    this.say(".");
    this.say(e.methodName);
    this.say("(");
    int i = 0;
    for (Tac.Operand.T arg : e.args) {
      if (i == 0) {
        arg.accept(this);
      }else {
        this.say(", ");
        arg.accept(this);
      }
      i++;
    }
    this.say(")");
  }

  @Override
  public void visit(Tac.Stm.AssignArray e) {
    e.dst.accept(this);
    this.say("[");
    e.index.accept(this);
    this.say("] = ");
    e.exp.accept(this);
  }

  @Override
  public void visit(Tac.Stm.AssignArraySelect e) {
    e.dst.accept(this);
    this.say(" = ");
    e.array.accept(this);
    this.say("[");
    e.index.accept(this);
    this.say("]");
  }

  @Override
  public void visit(Tac.Stm.AssignBinOp e) {
    e.dst.accept(this);
    this.say(" = ");
    e.left.accept(this);
    e.op.accept(this);
    e.right.accept(this);
    }

  @Override
  public void visit(Tac.Stm.AssignUnOp e) {
    e.dst.accept(this);
    this.say(" = ");
    e.op.accept(this);
    e.exp.accept(this);

  }

  @Override
  public void visit(Tac.Stm.AssignArrayLength e) {
    e.dst.accept(this);
    this.say(" = ");
    e.array.accept(this);
    this.say(".length");
  }

  @Override
  public void visit(Tac.Stm.AssignNewIntArray e) {
    e.dst.accept(this);
    this.say(" = new int[");
    e.size.accept(this);
    this.say("]");
  }

  @Override
  public void visit(Tac.Stm.AssignNewObject e) {
    e.dst.accept(this);
    this.say(" = new ");
    this.say(e.c);
    this.say("()");
  }

  @Override
  public void visit(Tac.Stm.If e) {
    this.say("if (");
    e.cond.accept(this);
    this.say(")");
    e.xen.accept(this);
    this.say("else");
    e.ilse.accept(this);
  }

  @Override
  public void visit(Tac.Stm.While e) {
    this.say("while(");
    ast.PrettyPrintVisitor p = new ast.PrettyPrintVisitor();
    e.cond.accept(p);
    this.say(p.toString());
    //e.cond.accept(this);
    this.say(")");
    e.body.accept(this);
  }

  @Override
  public void visit(Tac.Stm.Block e) {
    this.sayln("{");
    this.indent();
    for (Tac.Stm.T s : e.stms) {
      this.printSpaces();
      s.accept(this);
      this.sayln(";");
    }
    this.unIndent();
    this.printSpaces();
    this.say("}");
  }

  @Override
  public void visit(Tac.Stm.Print e) {
    this.say("System.out.println(");
    e.arg.accept(this);
    this.say(")");
  }

  @Override
  public void visit(Tac.Type.Int e) {
    this.say("int");

  }

  @Override
  public void visit(Tac.Type.IntArray e) {
    this.say("int[]");

  }

  @Override
  public void visit(Tac.Type.ClassType e) {
    this.say(e.id);
  }

  @Override
  public void visit(Tac.Type.Boolean e) {
    this.say("boolean");

  }

  @Override
  public void visit(Tac.Dec.DecSingle e) {
    e.type.accept(this);
    this.say(" ");
    this.say(e.id);
  }

  @Override
  public void visit(Tac.Method.MethodSingle m) {
    this.say("  public ");
    m.retType.accept(this);
    this.say(" " + m.id + "(");
    int i = 0;
    for (Tac.Dec.T d : m.formals) {
      i++;
      d.accept(this);
      if (i != m.formals.size()) {
        this.say(", ");
      }
    }
    this.sayln(")");
    this.sayln("  {");
    for (Tac.Dec.T d : m.locals) {
      this.indent();
      this.printSpaces();
      d.accept(this);
      this.say(";\n");
      this.unIndent();
    }
    this.sayln("");
    for (Tac.Stm.T s : m.stms){
      this.indent();
      this.printSpaces();
      s.accept(this);
      this.sayln(";");
      this.unIndent();
    }

    this.indent();
    this.printSpaces();
    this.say("return ");
    m.retExp.accept(this);
    this.sayln(";");
    this.unIndent();
    this.sayln("  }");

  }

  @Override
  public void visit(Tac.MainClass.MainClassSingle c) {
    this.sayln("class " + c.id);
    this.sayln("{");
    this.sayln("  public static void main (String[] " + c.arg + ")");
    this.sayln("  {");
    for (Tac.Dec.T d : c.locals) {
      this.indent();
      this.printSpaces();
      d.accept(this);
      this.say(";\n");
      this.unIndent();
    }
    for (Tac.Stm.T s : c.stms) {
      this.indent();
      this.printSpaces();
      s.accept(this);
      this.sayln(";");
      this.unIndent();
    }

    this.sayln("  }");
    this.sayln("}");
  }

  @Override
  public void visit(Tac.Class.ClassSingle c) {
    this.say("class " + c.id);
    if (c.extendss != null) {
      this.sayln(" extends " + c.extendss);
    } else {
      this.sayln("");
    }
    this.sayln("{");
    for (Tac.Dec.T d : c.decs) {
      Tac.Dec.DecSingle dec = (Tac.Dec.DecSingle)d;
      this.say("  ");
      dec.type.accept(this);
      this.say(" ");
      this.sayln(dec.id + ";");
    }
    for (Tac.Method.T mthd : c.methods)
      mthd.accept(this);
    this.sayln("}");

  }

  @Override
  public void visit(Tac.Program.ProgramSingle p) {
     p.mainClass.accept(this);
    this.sayln("");
    for (Tac.Class.T classs : p.classes) {
      classs.accept(this);
    }
    this.say("\n\n");

  }

  @Override
  public String toString() {
    return this.sb.toString();
  }
}
