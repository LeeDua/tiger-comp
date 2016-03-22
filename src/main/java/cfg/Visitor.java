package cfg;

import cfg.Cfg.Block.BlockSingle;
import cfg.Cfg.Class.ClassSingle;
import cfg.Cfg.Dec.DecSingle;
import cfg.Cfg.MainMethod.MainMethodSingle;
import cfg.Cfg.Method.MethodSingle;
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
import cfg.Cfg.Transfer.Goto;
import cfg.Cfg.Transfer.If;
import cfg.Cfg.Transfer.Return;
import cfg.Cfg.Type.ClassType;
import cfg.Cfg.Type.IntArrayType;
import cfg.Cfg.Type.IntType;
import cfg.Cfg.Vtable.VtableSingle;

public interface Visitor
{
  // operand
  void visit(Int o);

  void visit(Var o);

  // type
  void visit(ClassType t);

  void visit(IntType t);

  void visit(IntArrayType t);

  // dec
  void visit(DecSingle d);

  // transfer
  void visit(If t);

  void visit(Goto t);

  void visit(Return t);

  // statement:
  void visit(Add m);

  void visit(InvokeVirtual m);

  void visit(Lt m);

  void visit(Move m);

  void visit(NewObject m);

  void visit(Print m);

  void visit(Sub m);

  void visit(Times m);

  // block
  void visit(BlockSingle b);

  // method
  void visit(MethodSingle m);

  // vtable
  void visit(VtableSingle v);

  // class
  void visit(ClassSingle c);

  // main method
  void visit(MainMethodSingle c);

  // program
  void visit(ProgramSingle p);
}
