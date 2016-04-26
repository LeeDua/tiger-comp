package cfg;

import java.util.HashMap;
import java.util.HashSet;

import cfg.Cfg.Block;
import cfg.Cfg.Block.BlockSingle;
import cfg.Cfg.Class.ClassSingle;
import cfg.Cfg.Dec.DecSingle;
import cfg.Cfg.MainMethod.MainMethodSingle;
import cfg.Cfg.Method.MethodSingle;
import cfg.Cfg.Operand;
import cfg.Cfg.Operand.Int;
import cfg.Cfg.Operand.Var;
import cfg.Cfg.Program.ProgramSingle;
import cfg.Cfg.Stm.Add;
import cfg.Cfg.Stm.InvokeVirtual;
import cfg.Cfg.Stm.Lt;
import cfg.Cfg.Stm.Move;
import cfg.Cfg.Stm.NewObject;
import cfg.Cfg.Stm.Print;
import cfg.Cfg.Stm.Sub;
import cfg.Cfg.Stm.Times;
import cfg.Cfg.Transfer;
import cfg.Cfg.Transfer.Goto;
import cfg.Cfg.Transfer.If;
import cfg.Cfg.Transfer.Return;
import cfg.Cfg.Type.ClassType;
import cfg.Cfg.Type.IntArrayType;
import cfg.Cfg.Type.IntType;
import cfg.Cfg.Vtable.VtableSingle;

public class VisualVisitor implements Visitor
{
  public StringBuffer strb;
  private HashSet<String> redecs;
  private int indentLevel;

  public VisualVisitor()
  {
    this.strb = new StringBuffer();
    this.redecs = new HashSet<>();
    this.indentLevel = 2;
  }

  // ///////////////////////////////////////////////////
  private void printSpaces()
  {
    int i = this.indentLevel;
    while (i-- != 0)
      this.say(" ");
  }

  private void sayln(String s)
  {
    say(s);
    this.strb.append("\n");
  }

  private void say(String s)
  {
    this.strb.append(s);
  }

  private void emit(String s)
  {
    strb.append(s);
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
    emit(Integer.toString(operand.i));
  }

  @Override
  public void visit(Var operand)
  {
    emit(operand.id);
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
  public void visit(Cfg.Stm.And s)
  {
    say(getVar(s.dst) + " = ");
    s.left.accept(this);
    say(" + ");
    s.right.accept(this);
    say(";");
  }

  @Override
  public void visit(Cfg.Stm.ArraySelect s)
  {
    this.say(s.dst + " = ");
    s.array.accept(this);
    this.say("[");
    s.index.accept(this);
    this.say("+ARRAYSELECT_OFFSET];");
  }

  @Override
  public void visit(Cfg.Stm.AssignArray s)
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
  public void visit(Cfg.Stm.Length s)
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
  public void visit(Cfg.Stm.NewIntArray s)
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
  public void visit(Cfg.Stm.Not s)
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
    this.say(b.label.toString() + ":\n");
    for (Cfg.Stm.T s : b.stms) {
      printSpaces();
      s.accept(this);
      this.say("\n");
    }
    printSpaces();
    b.transfer.accept(this);
  }

  // method
  @Override
  public void visit(MethodSingle m)
  {
    java.util.HashMap<util.Label, Block.T> map =
        new HashMap<>();
    for (Block.T block : m.blocks) {
      BlockSingle b = (BlockSingle) block;
      util.Label label = b.label;
      map.put(label, b);
    }

    util.Graph<Block.T> graph = new util.Graph<>(m.classId + "_"
        + m.id);

    // add node
    for (Block.T block : m.blocks) {
      graph.addNode(block);
    }
    // add edge
    for (Block.T block : m.blocks) {
      BlockSingle b = (BlockSingle) block;
      Transfer.T transfer = b.transfer;
      if (transfer instanceof Transfer.Goto) {
        Transfer.Goto gotoo = (Transfer.Goto) transfer;
        Block.T to = map.get(gotoo.label);
        graph.addEdge(block, to);
      } else if (transfer instanceof Transfer.If) {
        Transfer.If iff = (If) transfer;
        Block.T truee = map.get(iff.truee);
        graph.addEdge(block, truee);
        Block.T falsee = map.get(iff.falsee);
        graph.addEdge(block, falsee);
      }
    }
    graph.visualize();
  }

  @Override
  public void visit(MainMethodSingle m)
  {
    java.util.HashMap<util.Label, Block.T> map =
        new HashMap<>();
    for (Block.T block : m.blocks) {
      Block.BlockSingle b = (Block.BlockSingle) block;
      util.Label label = b.label;
      map.put(label, b);
    }

    util.Graph<Block.T> graph = new util.Graph<>("Tiger_main");

    for (Block.T block : m.blocks) {
      graph.addNode(block);
    }
    for (Block.T block : m.blocks) {
      BlockSingle b = (BlockSingle) block;
      Transfer.T transfer = b.transfer;
      if (transfer instanceof Goto) {
        Transfer.Goto gotoo = (Transfer.Goto) transfer;
        Block.T to = map.get(gotoo.label);
        graph.addEdge(block, to);
      } else if (transfer instanceof Transfer.If) {
        Transfer.If iff = (Transfer.If) transfer;
        Block.T truee = map.get(iff.truee);
        graph.addEdge(block, truee);
        Block.T falsee = map.get(iff.falsee);
        graph.addEdge(block, falsee);
      }
    }
    graph.visualize();
  }

  // vtables
  @Override
  public void visit(VtableSingle v)
  {
    //no need
  }

  // class
  @Override
  public void visit(ClassSingle c)
  {
    //no need
  }

  // program
  @Override
  public void visit(ProgramSingle p)
  {
    for (cfg.Cfg.Method.T m : p.methods) {
      m.accept(this);
    }
    p.mainMethod.accept(this);
  }
}
