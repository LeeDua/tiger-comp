package flatten;

interface Visitor {
  // type
  void visit(Flt.Type.Int t);
  void visit(Flt.Type.Boolean t);
  void visit(Flt.Type.IntArray t);
  void visit(Flt.Type.ClassType t);
  void visit(Flt.Type.ClassArray t);

  // dec
  void visit(Flt.Dec.DecSingle e);

  // operand
  void visit(Flt.Operand.Var e);

  // condition
  void visit(Flt.Condition.ConditionSingle e);


  // selector
  void visit(Flt.Selector.SelectorSingle e);

  // binop
  void visit(Flt.BinOp.Add e);

  // unop
  void visit(Flt.UnOp.Not e);

  // statement
  void visit(Flt.Stm.AssignUnOp s);
  void visit(Flt.Stm.AssignBinOp s);
  void visit(Flt.Stm.Assign s);
  void visit(Flt.Stm.AssignArraySelect s);
  void visit(Flt.Stm.AssignArray s);
  void visit(Flt.Stm.AssignArrayLength s);
  void visit(Flt.Stm.AssignNewIntArray s);
  void visit(Flt.Stm.AssignNewObject s);
  void visit(Flt.Stm.Print s);

  void visit(Flt.Stm.SelectAssignUnOp s);
  void visit(Flt.Stm.SelectAssignBinOp s);
  void visit(Flt.Stm.SelectAssign s);
  void visit(Flt.Stm.SelectAssignArraySelect s);
  void visit(Flt.Stm.SelectAssignArray s);
  void visit(Flt.Stm.SelectAssignArrayLength s);
  void visit(Flt.Stm.SelectAssignNewIntArray s);
  void visit(Flt.Stm.SelectAssignNewObject s);
  void visit(Flt.Stm.If s);
  void visit(Flt.Stm.While s);
  void visit(Flt.Stm.Block s);
  void visit(Flt.Stm.AssignCall s);

  // method
  void visit(Flt.Method.MethodSingle m);

  // class
  void visit(Flt.Class.ClassSingle c);

  void visit(Flt.MainClass.MainClassSingle c);

  void visit(Flt.Program.ProgramSingle p);
}