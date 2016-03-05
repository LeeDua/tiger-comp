package elaborator;

import ast.Ast.Type;

import java.util.HashMap;
import java.util.Map;

public class ClassTable
{
  // map each class name (a string), to the class bindings.
  HashMap<String, ClassBinding> table;

  public ClassTable()
  {
    this.table = new HashMap<>();
  }

  /**
   * Duplication is not allowed
   *
   * @param cname class name
   * @param cb    the class bindings corresponding to the class
   */
  void put(String cname, ClassBinding cb) throws ElabExpection
  {
    if (this.table.get(cname) != null) {
      throw new ElabExpection("duplicated class: " + cname);
    }
    this.table.put(cname, cb);
  }

  /**
   * put a field into this table
   * Duplication is not allowed
   *
   * @param cname class name
   * @param fname field name
   * @param type  field type
   */
  void put(String cname, String fname, Type.T type) throws ElabExpection
  {
    ClassBinding cb = this.table.get(cname);
    cb.put(fname, type);
  }

  /**
   * put a method into this table
   * Duplication is not allowed.
   * Also note that MiniJava does NOT allow overloading.
   *
   * @param cname class name
   * @param mname method name
   * @parm type return type of the method
   */
  void put(String cname, String mname, MethodType type) throws ElabExpection
  {
    ClassBinding cb = this.table.get(cname);
    cb.put(mname, type);
  }

  // return null for non-existing class
  ClassBinding get(String className)
  {
    return this.table.get(className);
  }

  // get type of some field
  // return null for non-existing field.
  Type.T get(String className, String xid)
  {
    ClassBinding cb = this.table.get(className);
    Type.T type = cb.fields.get(xid);
    while (type == null) { // search all parent classes until found or fail
      if (cb.extendss == null) {
        return type;
      }

      cb = this.table.get(cb.extendss);
      type = cb.fields.get(xid);
    }
    return type;
  }

  /**
   * get type of some method
   *
   * @param className
   * @param mid
   * @return null for non-existing method
   */
  MethodType getMethodType(String className, String mid)
  {
    if (className == null) {
      return null;
    }
    ClassBinding cb = this.table.get(className);
    MethodType type = cb.methods.get(mid);
    if (cb.extendss == null) {
      return type;
    } else {
      return getMethodType(cb.extendss, mid);
    }
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, ClassBinding> en : table.entrySet()){
      String k = en.getKey();
      ClassBinding v = en.getValue();
      sb.append("class: "+k);
      sb.append(" ");
      sb.append(v.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
}
