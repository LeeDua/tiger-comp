package lexer;

import java.io.IOException;
import java.io.InputStream;

import lexer.Token.Kind;

public class Lexer {
  private String fname; // the input file name to be compiled
  private InputStream fstream; // input stream for the above file
  private TokenMap tmap;

  private String s = "";
  private int linenum = 1;

  public Lexer(String fname, InputStream fstream)
  {
    this.fname = fname;
    this.fstream = fstream;
    this.tmap = new TokenMap();
  }

  /**
   * @param c must be 0~9
   * @return the corrected num
   * @throws IOException
   */
  private String dealNum(int c) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    sb.append((char) c);

    while (true) {
      int next = carefulRead();
      if (next >= '0' && next <= '9') {
        sb.append((char) next);
        continue;
      }
      // 999aa is not legal
      if ((next == '_') || (next >= 'a' && next <= 'z')
          || (next >= 'A' && next <= 'Z')) {
        Error.ILLEGAL_NUMBER.error(linenum);
      }

      break;
    }

    this.fstream.reset();
    return sb.toString();
  }

  /**
   * @param c the char just read now
   * @return Token if s is keyword or s is Id, null if c == ' '&&s==""
   **/
  private Token expectIdOrKey(int c) throws Exception
  {
    Kind k = tmap.getKind(s);
    if (k != null) {
      Token tk = new Token(k, linenum, s);
      s = "";
      this.fstream.reset();// need reset to push back char
      return tk;
    } else if (s.equals("")) {
      if (c != ' ') {
        Kind keyword_kind = tmap.getKind(String.valueOf((char) c));
        return new Token(keyword_kind, linenum,
            String.valueOf((char) c));
      } else {
        return null;
      }
    } else {
      // s must be Id
      Token tk = new Token(Kind.TOKEN_ID, linenum, s);
      s = "";
      this.fstream.reset();
      return tk;
    }
  }

  /**
   * for the Binary Operator like &&, ||, ++, --
   *
   * @param expect expect keyword except the first alphabet e.g. &&'s expect is
   *               '&'
   * @return true if match the keyword, false if not
   **/
  private boolean expectKeyword(String expect) throws IOException
  {
    this.fstream.mark(expect.length());

    for (int i = 0; i < expect.length(); i++) {
      if (expect.charAt(i) == this.fstream.read()) {
        continue;
      }

      this.fstream.reset();
      return false;
    }

    return true;
  }

  private int carefulRead() throws IOException
  {
    this.fstream.mark(1);
    int c = this.fstream.read();
    if (c == '\n')
      linenum++;
    return c;
  }

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception
  {
    int c = carefulRead();
    if (-1 == c)
    {
      // The value for "lineNum" is now "null",
      // you should modify this to an appropriate
      // line number for the "EOF" token.
      return new Token(Kind.TOKEN_EOF, linenum);
    }
    // skip all kinds of "blanks"
    while ('\t' == c || '\n' == c || '\r' == c) {
      c = carefulRead();
    }
    if (-1 == c) {
      return new Token(Kind.TOKEN_EOF, linenum);
    }

    switch (c) {
      case '&':
        if (this.s.equals("")) {
          if (expectKeyword("&")) {
            return new Token(Kind.TOKEN_AND, linenum, "&&");
          } else {
            Error.LEXER.error(linenum);
          }
          break;
        }
      case -1:
      case ' ':
      case '+':
      case '=':
      case ',':
      case '.':
      case '{':
      case '[':
      case '(':
      case '<':
      case '!':
      case '}':
      case ']':
      case ')':
      case ';':
      case '-':
      case '*':
        return expectIdOrKey(c);
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        if (s == "") {
          return new Token(Kind.TOKEN_NUM, linenum, dealNum(c));
        }
        s += (char) c;
        break;
      case '/':
        return dealComments(c);
      default:
        s += (char) c;
        break;
    }
    return null;
  }

  /**
   * skip comments
   *
   * @param c must be '/'
   */
  private Token dealComments(int c) throws Exception
  {
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

  private Token expectComm() throws Exception
  {
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

  private Token expectComm2() throws Exception
  {
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

  public Token nextToken()
  {
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
