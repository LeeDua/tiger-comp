package elaborator;

import ast.Ast.Type;

import java.util.HashMap;
import java.util.Map;

public class ClassBinding {
  String extendss; // null for non-existing extends
  HashMap<String, Type.T> fields;
  HashMap<String, MethodType> methods;

  public ClassBinding(String extendss) {
    this.extendss = extendss;
    this.fields = new HashMap<>();
    this.methods = new HashMap<>();
  }

  public ClassBinding(String extendss,
                      HashMap<String, Type.T> fields,
                      HashMap<String, MethodType> methods) {
    this.extendss = extendss;
    this.fields = fields;
    this.methods = methods;
  }

  /**
   * put a field into classbindding.
   * duplicated not allow.
   *
   * @param fname field name
   * @param type  field type
   */
  public void put(String fname, Type.T type) throws ElabExpection {
    if (this.fields.get(fname) != null) {
      throw new ElabExpection("duplicated class field: " + fname);
    }
    this.fields.put(fname, type);
  }

  /**
   * put a method and corresponding methodType into classbinding.
   * Duplicated not allow.
   *
   * @param mid method name
   * @param mt  method type
   */
  public void put(String mid, MethodType mt) throws ElabExpection {
    if (this.methods.get(mid) != null) {
      throw new ElabExpection("duplicated class method: " + mid);
    }
    this.methods.put(mid, mt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (this.extendss != null) {
      sb.append("extends ").append(this.extendss);
    }
    sb.append("\n");
    sb.append(" Fields:\n");
    for (Map.Entry<String, Type.T> en : this.fields.entrySet()) {
      String k = en.getKey();
      Type.T t = en.getValue();
      sb.append("   " + k + " ");
      sb.append(t.toString());
      sb.append("\n");

    }
    sb.append("\n");
    sb.append(" Methods:\n");
    for (Map.Entry<String, MethodType> en : this.methods.entrySet()) {
      String k = en.getKey();
      MethodType t = en.getValue();
      sb.append("   " + k + " ");
      sb.append(t.toString());
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

}
