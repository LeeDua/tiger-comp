package parser;

import lexer.Token;

public enum Error {
    SYNTAX_ERROR() {
	public void error(Token.Kind expect, Token.Kind current, int linenum) {
	    System.err.print("syntax error> expect ");
	    System.err.print(expect.toString());
	    System.err.print(", but got ");
	    System.err.print(current.toString());
	    System.err.println(" at line: " + linenum);
	    System.exit(1);
	}

	@Override
	public void error(String info, int linenum) {
	    System.out.println("syntax error> " + info + " :line " + linenum);
	    System.exit(1);
	}
    };

    public abstract void error(Token.Kind expect, Token.Kind current,
	    int linenum);

    public abstract void error(String info, int linenum);

}
