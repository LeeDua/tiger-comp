package lexer;

import lexer.Token.Kind;

import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class Lexer {
  private String fname; // the input file name to be compiled
  private InputStream fstream; // input stream for the above file
  private TokenMap tmap;
  private TerminalSet terminals;
  private int linenum = 1;

  public Lexer(String fname, InputStream fstream) {
    this.fname = fname;
    this.fstream = fstream;
    this.tmap = new TokenMap();
    this.terminals = new TerminalSet();
  }


  private int carefulRead() throws IOException {
    this.fstream.mark(1);
    int c = this.fstream.read();
    if (c == '\n') {
      linenum++;
    }
    return c;
  }

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception {
    int c = carefulRead();
    // skip all kinds of "blanks"
    while ('\t' == c || '\n' == c || '\r' == c || c == ' ') {
      c = carefulRead();
    }
    if (-1 == c) {
      return new Token(Kind.TOKEN_EOF, linenum);
    }

    return expect(c, "");

  }

  private Token expectIdOrNum(String s) throws IOException {
    char[] buf = s.toCharArray();
    for (char c : buf) {
      if (c >= '0' && c <= '9') {
        continue;
      } else {
        this.fstream.reset();
        return new Token(Kind.TOKEN_ID, linenum, s);
      }
    }
    this.fstream.reset();
    return new Token(Kind.TOKEN_NUM, linenum, s);

  }


  private Token expectIdOrNumOrKey(int c, String s) throws IOException {
    if (s.equals("") && this.tmap.getKind(String.valueOf((char) c)) != null) {
      return new Token(tmap.getKind(String.valueOf((char) c)), linenum, s);
    } else if (this.tmap.getKind(s) != null) {
      fstream.reset();
      return new Token(tmap.getKind(s), linenum, s);
    }
    return expectIdOrNum(s);
  }

  private Token expectIdOrKeyOrNumOrComment(int c, String s) throws Exception {
    if (s.equals("") && c == '&') {
      if ('&' == carefulRead()) {
        return new Token(Kind.TOKEN_AND, linenum, "&&");
      } else {
        Error.LEXER.error(linenum);
      }
    } else if (c == '/') {
      return dealComments();
    }

    return expectIdOrNumOrKey(c, s);


  }

  private Token expect(int c, String s) throws Exception {
    if (terminals.isTerminal(c)) {
      if (c == '\n') {
        this.linenum--;
      }
      return expectIdOrKeyOrNumOrComment(c, s);
    } else {
      return expect(carefulRead(), s + (char) c);
    }
  }

  /**
   * skip comments
   */
  private Token dealComments() throws Exception {
    int ex = this.fstream.read();
    switch (ex) {
      case '/':
        return expectComm();
      case '*':
        return expectComm2();
      default:
        Error.LEXER.error(linenum);
    }
    return null;
  }

  private enum commKind {
    IN_COMM,
    EXIT
  }

  private Token expectComm() throws Exception {
    boolean flag = true;
    while (flag) {
      int c = carefulRead();
      if (c == '\n') {
        flag = false;
      } else if (c == -1) {
        fstream.reset();
        flag = false;
      }
    }
    return new Token(Kind.TOKEN_COMMENT, linenum, "comment");
  }

  private Token expectComm2() throws Exception {
    commKind state = commKind.IN_COMM;
    boolean flag = true;
    while (flag) {
      int c = carefulRead();
      switch (state) {
        case IN_COMM:
          if (c == '*') {
            state = commKind.EXIT;
          } else if (c == -1) {
            Error.LEXER.error(linenum);
          } else {
            break;
          }
          break;
        case EXIT:
          if (c == '/') {
            flag = false;
          } else {
            state = commKind.IN_COMM;
          }
          break;
        default:
          Error.LEXER.error(linenum);
      }
    }
    return new Token(Kind.TOKEN_COMMENT, linenum, "comment");
  }

  public Token nextToken() {
    Token t = null;
    try {
      while (t == null ||
          t.kind == Kind.TOKEN_COMMENT)
        t = this.nextTokenInternal();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return t;
  }

}
