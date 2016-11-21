package codegen.RuntimeC;

import codegen.RuntimeC.Ast.Class.ClassSingle;
import codegen.RuntimeC.Ast.Dec.DecSingle;
import codegen.RuntimeC.Ast.Exp.Add;
import codegen.RuntimeC.Ast.Exp.And;
import codegen.RuntimeC.Ast.Exp.ArraySelect;
import codegen.RuntimeC.Ast.Exp.Call;
import codegen.RuntimeC.Ast.Exp.Id;
import codegen.RuntimeC.Ast.Exp.Length;
import codegen.RuntimeC.Ast.Exp.Lt;
import codegen.RuntimeC.Ast.Exp.NewIntArray;
import codegen.RuntimeC.Ast.Exp.NewObject;
import codegen.RuntimeC.Ast.Exp.Not;
import codegen.RuntimeC.Ast.Exp.Num;
import codegen.RuntimeC.Ast.Exp.Sub;
import codegen.RuntimeC.Ast.Exp.This;
import codegen.RuntimeC.Ast.Exp.Times;
import codegen.RuntimeC.Ast.MainMethod.MainMethodSingle;
import codegen.RuntimeC.Ast.Method.MethodSingle;
import codegen.RuntimeC.Ast.Program.ProgramSingle;
import codegen.RuntimeC.Ast.Stm.Assign;
import codegen.RuntimeC.Ast.Stm.AssignArray;
import codegen.RuntimeC.Ast.Stm.Block;
import codegen.RuntimeC.Ast.Stm.If;
import codegen.RuntimeC.Ast.Stm.Print;
import codegen.RuntimeC.Ast.Stm.While;
import codegen.RuntimeC.Ast.Type.ClassType;
import codegen.RuntimeC.Ast.Type.Int;
import codegen.RuntimeC.Ast.Type.IntArray;
import codegen.RuntimeC.Ast.Vtable.VtableSingle;

public interface Visitor {
  // expressions
  void visit(Add e);

  void visit(And e);

  void visit(ArraySelect e);

  void visit(Call e);

  void visit(Id e);

  void visit(Length e);

  void visit(Lt e);

  void visit(NewIntArray e);

  void visit(NewObject e);

  void visit(Not e);

  void visit(Num e);

  void visit(Sub e);

  void visit(This e);

  void visit(Times e);

  // statements
  void visit(Assign s);

  void visit(AssignArray s);

  void visit(Block s);

  void visit(If s);

  void visit(Print s);

  void visit(While s);

  // type
  void visit(ClassType t);

  void visit(Int t);

  void visit(IntArray t);

  // dec
  void visit(DecSingle d);

  // method
  void visit(MethodSingle m);

  // main method
  void visit(MainMethodSingle m);

  // vtable
  void visit(VtableSingle v);

  // class
  void visit(ClassSingle c);

  // program
  void visit(ProgramSingle p);
}
