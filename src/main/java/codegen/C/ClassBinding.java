package codegen.C;

import java.util.ArrayList;
import java.util.LinkedList;

import codegen.C.Ast.Dec;
import codegen.C.Ast.Type;

public class ClassBinding
{
  String extendss; // null for non-existing extends
  /**
   * the mark for inherit
   */
  boolean visited;
  LinkedList<Tuple> fields; // all fields
  ArrayList<Ftuple> methods; // all methods

  public ClassBinding(String extendss)
  {
    this.extendss = extendss;
    this.visited = false;
    this.fields = new LinkedList<>();
    this.methods = new ArrayList<>();
  }

  // put a single field
  void put(String c, Type.T type, String var)
  {
    this.fields.add(new Tuple(c, type, var));
  }

  void update(java.util.LinkedList<Tuple> fs)
  {
    this.fields = fs;
  }

  void update(java.util.ArrayList<Ftuple> ms)
  {
    this.methods = ms;
  }

  /**
   * Put the C method info into Classbinding.
   *
   * @param cname classname
   * @param ret   method return type.
   * @param args  the arguments of method
   * @param mthd  method name.
   */
  void putm(String cname, Type.T ret, java.util.LinkedList<Dec.T> args,
            String mthd)
  {
    Ftuple t = new Ftuple(cname, ret, args, mthd);
    this.methods.add(t);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("extends: ");
    if (this.extendss != null) {
      sb.append(this.extendss);
    } else {
      sb.append("<>");
    }
    sb.append("\nfields:\n  ");
    sb.append(fields.toString());
    sb.append("\nmethods:\n  ");
    sb.append(methods.toString());
    return sb.toString();
  }

}
