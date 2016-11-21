package codegen.RuntimeC;

import codegen.RuntimeC.Ast.Dec;
import codegen.RuntimeC.Ast.Type;

public class ClassTable {
  private java.util.Hashtable<String, ClassBinding> table;

  public ClassTable() {
    this.table = new java.util.Hashtable<>();
  }

  /**
   * init the classtable.
   *
   * @param cname    classname
   * @param extendss null if the class has no base class.
   */
  void init(String cname, String extendss) {
    this.table.put(cname, new ClassBinding(extendss));
  }

  /**
   * put the RuntimeC decls into corresponding classbinding.
   *
   * @param cname classname
   * @param decs  the decleares of RuntimeC.
   */
  void initDecs(String cname, java.util.LinkedList<Dec.T> decs) {
    ClassBinding cb = this.table.get(cname);
    for (Dec.T dec : decs) {
      Dec.DecSingle decc = (Dec.DecSingle) dec;
      cb.put(cname, decc.type, decc.id);
    }
  }

  /**
   * @param cname
   * @param ret
   * @param args
   * @param mid
   */
  void initMethod(String cname, Type.T ret,
                  java.util.LinkedList<Dec.T> args, String mid) {
    ClassBinding cb = this.table.get(cname);
    cb.putm(cname, ret, args, mid);
  }

  /**
   * @param cname class name
   */
  void inherit(String cname) {
    ClassBinding cb = this.table.get(cname);
    if (cb.visited) {
      return;
    }
    if (cb.extendss == null) {
      cb.visited = true;
      return;
    }
    inherit(cb.extendss);
    ClassBinding pb = this.table.get(cb.extendss);
    // this tends to be very slow...
    // need a much fancier data structure.
    java.util.LinkedList<Tuple> newFields = new java.util.LinkedList<>();
    newFields.addAll(pb.fields);
    newFields.addAll(cb.fields);
    cb.update(newFields);
    // methods;
    java.util.ArrayList<Ftuple> newMethods = new java.util.ArrayList<>();
    newMethods.addAll(pb.methods);
    for (codegen.RuntimeC.Ftuple t : cb.methods) {
      int index = newMethods.indexOf(t);
      if (index == -1) {
        newMethods.add(t);
        continue;
      }
      newMethods.set(index, t);
    }
    cb.update(newMethods);
    // set the mark
    cb.visited = true;
  }

  // return null for non-existing keys
  ClassBinding get(String c) {
    return this.table.get(c);
  }

  @Override
  public String toString() {
    return this.table.toString();
  }
}
