package ast.optimizations;

public class Main
{
  private ast.Ast.Program.T program;
  boolean changed;

  public Main(ast.Ast.Program.T p)
  {
    this.program = p;
    this.changed = false;
  }

  public ast.Ast.Program.T opt()
  {
    do {
      this.changed = false;
      DeadClass dc = new DeadClass();
      this.program.accept(dc);
      this.program = dc.program;
      this.changed = this.changed | dc.changed;

      DeadCode deadcode = new DeadCode();
      this.program.accept(deadcode);
      this.program = deadcode.program;
      this.changed = this.changed | deadcode.changed;

      AlgSimp as = new AlgSimp();
      this.program.accept(as);
      this.program = as.program;
      this.changed = this.changed | as.changed;

      ConstFold cf = new ConstFold();
      this.program.accept(cf);
      this.program = cf.program;
      this.changed = this.changed | cf.changed;
    } while (this.changed);

    return this.program;
  }
}
