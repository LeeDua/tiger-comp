package tac;

interface  Visitor {
  // operand
  void visit(Tac.Operand.Var e);
  void visit(Tac.Operand.Int e);
  void visit(Tac.Operand.True e);
  void visit(Tac.Operand.False e);

  // binop
  void visit(Tac.BinOp.Add e);
  void visit(Tac.BinOp.And e);
  void visit(Tac.BinOp.Sub e);
  void visit(Tac.BinOp.Times e);
  void visit(Tac.BinOp.Lt e);


  // unop
  void visit(Tac.UnOp.Not e);


  // stm
  void visit(Tac.Stm.Assign e);
  void visit(Tac.Stm.AssignCall e);
  void visit(Tac.Stm.AssignArray e);
  void visit(Tac.Stm.AssignArraySelect e);
  void visit(Tac.Stm.AssignBinOp e);
  void visit(Tac.Stm.AssignUnOp e);
  void visit(Tac.Stm.AssignArrayLength e);
  void visit(Tac.Stm.AssignNewIntArray e);
  void visit(Tac.Stm.AssignNewObject e);
  void visit(Tac.Stm.If e);
  void visit(Tac.Stm.While e);
  void visit(Tac.Stm.Block e);
  void visit(Tac.Stm.Print e);


  // type
  void visit(Tac.Type.Int e);
  void visit(Tac.Type.IntArray e);
  void visit(Tac.Type.ClassType e);
  void visit(Tac.Type.Boolean e);

  void visit(Tac.Dec.DecSingle e);

  void visit(Tac.Method.MethodSingle e);

  void visit(Tac.MainClass.MainClassSingle e);

  void visit(Tac.Class.ClassSingle e);

  void visit(Tac.Program.ProgramSingle e);
}
