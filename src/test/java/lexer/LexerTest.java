package lexer;

import org.junit.Test;
import org.junit.Before;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;


/**
 * Created by qc1iu on 2/21/16.
 */
public class LexerTest {
  Lexer l;

  @Before
  public void createLexer() throws Exception
  {
    InputStream fstream = new BufferedInputStream(
        new FileInputStream("src/test/resources/LinkedList.java"));
    this.l = new Lexer("LinkedList.java", fstream);
  }

  @Test
  public void nextToken()
  {
    Token token = l.nextToken();
    assertEquals(1, token.lineNum.intValue());

    while (token.kind != Token.Kind.TOKEN_EOF) {
      System.out.println(token.toString());
      token = l.nextToken();
    }
  }
}