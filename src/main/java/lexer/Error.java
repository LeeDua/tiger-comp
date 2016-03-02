package lexer;

@Deprecated
public enum Error
{
  LEXER() {
    @Override
    public void error(int linenum)
    {
      System.err.println("lexer error> line:" + linenum);
      System.exit(1);
    }

  },

  ILLEGAL_NUMBER() {
    @Override
    public void error(int linenum)
    {
      System.out.println("lexer error>illegal number" + linenum);

    }
  };

  public abstract void error(int linenum);
}
