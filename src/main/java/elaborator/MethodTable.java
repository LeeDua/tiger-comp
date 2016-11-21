package elaborator;

import ast.Ast.Dec;
import ast.Ast.Type;

import java.util.HashMap;
import java.util.LinkedList;

public class MethodTable {
  /**
   * Map each locals name (a string), to the corresponding type.
   * The method locals include formals and locals variable.
   */
  HashMap<String, Type.T> locals;

  public MethodTable() {
    this.locals = new HashMap<>();
  }

  /**
   * Put all formals and locals into localsTable.
   * Duplication is not allowed
   *
   * @param formals the formal arguments of the method
   * @param locals  the local variables of the method
   * @throws ElabExpection
   */
  public void put(LinkedList<Dec.T> formals, LinkedList<Dec.T> locals) throws
      ElabExpection {
    for (Dec.T dec : formals) {
      Dec.DecSingle decc = (Dec.DecSingle) dec;
      if (this.locals.get(decc.id) != null) {
        throw new ElabExpection("duplicated parameter: " + decc.id);
      }
      this.locals.put(decc.id, decc.type);
    }

    for (Dec.T dec : locals) {
      Dec.DecSingle decc = (Dec.DecSingle) dec;
      if (this.locals.get(decc.id) != null) {
        throw new ElabExpection("duplicated variable: " + decc.id);
      }
      this.locals.put(decc.id, decc.type);
    }

  }

  /**
   * return null for non-existing keys
   *
   * @param id the key for locals
   * @return null if non-exeistiong
   */
  public Type.T get(String id) {
    return this.locals.get(id);
  }

  public void dump() {
    System.out.println("<id----Type>");
    System.out.println(this.locals);
  }

  @Override
  public String toString() {
    return this.locals.toString();
  }
}
