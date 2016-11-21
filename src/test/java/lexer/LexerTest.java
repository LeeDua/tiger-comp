package lexer;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import static org.junit.Assert.assertEquals;


/**
 * Created by qc1iu on 2/21/16.
 */
public class LexerTest {
  Lexer l;
  int tmp = 0;


  private Vector<Token> getTokens(Vector<Token> tokens, Token current) {
    if (current.kind == Token.Kind.TOKEN_EOF) {
      return tokens;
    } else {
      System.out.println(tmp++ + " " + current.toString());
      tokens.add(current);
      return getTokens(tokens, l.nextToken());
    }
  }

  @Test
  public void testLinkedList() throws Exception {
    System.out.println("test lex LinkedList.java");
    InputStream fstream = new BufferedInputStream(
        new FileInputStream("src/test/resources/LinkedList.java"));
    this.l = new Lexer("LinkedList.java", fstream);

    Vector<Token> tokens = getTokens(new Vector<>(), l.nextToken());
    assertEquals(1, tokens.get(0).lineNum.intValue());
    assertEquals(Token.Kind.TOKEN_CLASS, tokens.get(0).kind);
    assertEquals("LinkedList", tokens.get(1).lexeme);
    assertEquals(Token.Kind.TOKEN_LBRACE, tokens.get(2).kind);
    assertEquals(2, tokens.get(3).lineNum.intValue());
    assertEquals("Married", tokens.get(42).lexeme);
    assertEquals(10, tokens.get(43).lineNum.intValue());
    assertEquals(13, tokens.get(44).lineNum.intValue());
    assertEquals(Token.Kind.TOKEN_AND, tokens.get(473).kind);
    assertEquals(Token.Kind.TOKEN_LBRACE, tokens.get(559).kind);
    assertEquals(147, tokens.get(566).lineNum.intValue());
    assertEquals("39", tokens.get(924).lexeme);
    assertEquals(Token.Kind.TOKEN_DOT, tokens.get(971).kind);
    assertEquals(258, tokens.get(1055).lineNum.intValue());
    assertEquals(278, tokens.lastElement().lineNum.intValue());

  }

  @Test
  public void testBubbleSort() throws Exception {
    System.out.println("test lex BubbleSort.java");
    InputStream fstream = new BufferedInputStream(
        new FileInputStream("src/test/resources/BubbleSort.java"));
    this.l = new Lexer("BubbleSort.java", fstream);

    Vector<Token> tokens = getTokens(new Vector<>(), l.nextToken());
    assertEquals(1, tokens.get(0).lineNum.intValue());
    assertEquals(Token.Kind.TOKEN_CLASS, tokens.get(0).kind);
    assertEquals("BubbleSort", tokens.get(1).lexeme);
    assertEquals(Token.Kind.TOKEN_LBRACK, tokens.get(177).kind);
    assertEquals(Token.Kind.TOKEN_LBRACK, tokens.get(219).kind);
  }

  @Test
  public void testPrint() throws Exception {
    System.out.println("testPrint");
    InputStream fstream = new BufferedInputStream(
        new ByteArrayInputStream("public static void main".getBytes()));
    this.l = new Lexer("a.java", fstream);

    Vector<Token> tokens = getTokens(new Vector<>(), l.nextToken());
    for (Token t : tokens
        ) {
      System.out.println(t.toString());
    }
  }
}