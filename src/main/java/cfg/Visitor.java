package cfg;

public interface Visitor
{
  // operand
  void visit(Cfg.Operand.Int o);

  void visit(Cfg.Operand.Var o);

  // type
  void visit(Cfg.Type.ClassType t);

  void visit(Cfg.Type.IntType t);

  void visit(Cfg.Type.IntArrayType t);

  // dec
  void visit(Cfg.Dec.DecSingle d);

  // transfer
  void visit(Cfg.Transfer.If t);

  void visit(Cfg.Transfer.Goto t);

  void visit(Cfg.Transfer.Return t);

  // statement:
  void visit(Cfg.Stm.Add s);

  void visit(Cfg.Stm.And s);

  void visit(Cfg.Stm.ArraySelect s);

  void visit(Cfg.Stm.AssignArray s);

  void visit(Cfg.Stm.InvokeVirtual s);

  void visit(Cfg.Stm.Length s);

  void visit(Cfg.Stm.Lt s);

  void visit(Cfg.Stm.Move s);

  void visit(Cfg.Stm.NewIntArray s);

  void visit(Cfg.Stm.NewObject s);

  void visit(Cfg.Stm.Not s);

  void visit(Cfg.Stm.Print s);

  void visit(Cfg.Stm.Sub s);

  void visit(Cfg.Stm.Times s);

  // block
  void visit(Cfg.Block.BlockSingle b);

  // method
  void visit(Cfg.Method.MethodSingle m);

  // vtable
  void visit(Cfg.Vtable.VtableSingle v);

  // class
  void visit(Cfg.Class.ClassSingle c);

  // main method
  void visit(Cfg.MainMethod.MainMethodSingle c);

  // program
  void visit(Cfg.Program.ProgramSingle p);
}
