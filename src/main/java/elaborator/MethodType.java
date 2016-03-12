package elaborator;

import java.util.LinkedList;

import ast.Ast.Dec;
import ast.Ast.Type;

public class MethodType
{
  /**
   * Use return type and formals type to determin a unique method.
   */
  Type.T retType;
  LinkedList<Dec.T> argsType;

  public MethodType(Type.T retType, LinkedList<Dec.T> decs)
  {
    this.retType = retType;
    this.argsType = decs;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (Dec.T d : argsType) {
      Type.T t = ((Dec.DecSingle) d).type;
      sb.append(t.toString());
      sb.append(" ");
    }
    sb.append(")");
    sb.append(" -> ");
    sb.append(retType.toString());
    return sb.toString();
  }
}
