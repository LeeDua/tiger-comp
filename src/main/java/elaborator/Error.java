package elaborator;

public enum Error {
  MISTYPE() {
    @Override
    public void error(ElaboratorVisitor v, int linenum) {
      System.err.println("error:type mismatch at line " + linenum);
      System.err.println("need type:" + v.type.toString());
      System.exit(1);
    }
  },
  UNDECL() {
    @Override
    public void error(ElaboratorVisitor v, int linenum) {
      System.err.println("error:un decl var at line " + linenum);
      System.exit(1);

    }
  },
  RET() {
    @Override
    public void error(ElaboratorVisitor v, int linenum) {
      System.err.println("error:return val mis at line " + linenum);
      System.err.println("return type must be " + v.type.toString());
      System.exit(1);

    }

  };

  public abstract void error(ElaboratorVisitor v, int linenum);
}
