package lexer;

import java.io.IOException;
import java.io.InputStream;

import lexer.Token.Kind;

public class Lexer {
  String fname; // the input file name to be compiled
  InputStream fstream; // input stream for the above file
  TokenMap tmap;

  public String s = "";
  int linenum = 1;

  public Lexer(String fname, InputStream fstream) {
    this.fname = fname;
    this.fstream = fstream;
    this.tmap = new TokenMap();
  }

  /**
   * @param c must be 0~9
   * @return
   * @throws IOException
   */
  private String dealNum(int c) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append((char) c);

    while (true) {
      this.fstream.mark(1);
      int next = this.fstream.read();
      if (next >= '0' && next <= '9') {
        sb.append((char) next);
        continue;
      }
      // 999aa is not legal
      if ((next == '_') || (next >= 'a' && next <= 'z')
          || (next >= 'A' && next <= 'Z'))
        Error.ILLEGAL_NUMBER.error(linenum);

      break;
    }

    this.fstream.reset();
    return sb.toString();
  }

  /**
   * @param c the char just read now
   * @return Token if s is keyword or s is Id, null if c == ' '&&s==""
   **/
  private Token expectIdOrKey(int c) throws Exception {
    Kind k = tmap.getKind(s);
    if (k != null) // keywords
    {
      Token tk = new Token(k, linenum, s);
      s = "";
      this.fstream.reset();// need reset to push back char
      return tk;
    } else if (s == "") // c must be key word
    {
      if (c != ' ') {
        Kind keyword_kind = tmap.getKind(String.valueOf((char) c));
        Token tk = new Token(keyword_kind, linenum,
            String.valueOf((char) c));
        return tk;
      } else
        return null;
    } else
    // s must be Id
    {
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
  private boolean expectKeyword(String expect) throws IOException {
    this.fstream.mark(expect.length());

    for (int i = 0; i < expect.length(); i++) {
      if (expect.charAt(i) == this.fstream.read())
        continue;

      this.fstream.reset();
      return false;
    }

    return true;
  }

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception {
    this.fstream.mark(1);
    int c = this.fstream.read();

    if (-1 == c)

      // The value for "lineNum" is now "null",
      // you should modify this to an appropriate
      // line number for the "EOF" token.
      return new Token(Kind.TOKEN_EOF, linenum);

    // skip all kinds of "blanks"
    while ('\t' == c || '\n' == c || '\r' == c) {
      this.fstream.mark(1);
      if ('\n' == c)
        linenum++;
      c = this.fstream.read();
    }
    if (-1 == c)
      return new Token(Kind.TOKEN_EOF, linenum);

    switch (c) {
      case '&':
        if (this.s == "") {
          if (expectKeyword("&"))
            return new Token(Kind.TOKEN_AND, linenum, "&&");
          else
            Error.LEXER.error(linenum);
          break;
        }
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
        if (s == "")
          return new Token(Kind.TOKEN_NUM, linenum, dealNum(c));
        s += (char) c;
        break;
      case '/':
        dealComments(c);
        break;
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
  private void dealComments(int c) throws IOException {
    int ex = this.fstream.read();
    if (ex == '/') {
      while (ex != '\n' && ex != -1) {
        this.fstream.mark(1);
        ex = this.fstream.read();
      }

      if (ex == -1) {
        this.fstream.reset();
        return;
      } else
        linenum++;
    } else if (ex == '*') {// confirm comment
      ex = this.fstream.read();
      while ((c != '*' || ex != '/') && (ex != -1)) {
        c = ex;
        ex = this.fstream.read();
      }
      if (ex == -1)
        Error.LEXER.error(linenum);
    } else
      Error.LEXER.error(linenum);
  }

  public Token nextToken() {
    Token t = null;

    try {
      while (t == null)
        t = this.nextTokenInternal();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return t;
  }

}
