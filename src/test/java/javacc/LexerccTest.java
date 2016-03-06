package javacc;

import org.junit.Test;

import java.io.*;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by qc1iu on 2/26/16.
 */
public class LexerccTest
{

  @Test
  public void testLexerTest() throws ParseException
  {
    System.out.println("test lex LinkedList.java");
    InputStream fstream = null;
    try {
      fstream = new BufferedInputStream(
          new FileInputStream("src/test/resources/LinkedList.java"));
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
    Parser p = new Parser(fstream);
    LinkedList<Token> tokens = p.lexerTest();
    assertEquals("class", tokens.get(0).image);
    assertEquals("LinkedList", tokens.get(1).image);
    assertEquals("{", tokens.get(2).image);
    assertEquals(2, tokens.get(3).beginLine);
    assertEquals("Married", tokens.get(42).image);
    assertEquals(10, tokens.get(43).beginLine);
    assertEquals(13, tokens.get(44).beginLine);
    assertEquals("&&", tokens.get(473).image);
    assertEquals("{", tokens.get(559).image);
    assertEquals(147, tokens.get(566).beginLine);
    assertEquals("39", tokens.get(924).image);
    assertEquals(".", tokens.get(971).image);
    assertEquals(258, tokens.get(1055).beginLine);
    assertEquals(278, tokens.getLast().beginLine);
    assertEquals("}", tokens.getLast().image);
  }

  @Test
  public void testBrace() throws ParseException
  {
    System.out.println("test Brace");
    InputStream in = new BufferedInputStream(new ByteArrayInputStream(
        "[]".getBytes()));
    Parser lex = new Parser(in);
    LinkedList<Token> tokens = lex.lexerTest();
    assertEquals("[", tokens.get(0).image);
    assertEquals("]", tokens.get(1).image);
  }

  @Test
  public void testComment() throws ParseException
  {
    System.out.println("test Comment.");
    InputStream in = new BufferedInputStream(new ByteArrayInputStream(
        ("/*This is a MULTILINE_COMMENT\n" +
            "whitch has tow line.*/\n" +
            "//This is a LINE_COMMENT.\n" +
            "/***********MULTILINE_COMMENT************/\n" +
            "// In comment, we can input all charactors!@#$%^&*()(*&^%$##.\n").getBytes()));
    Parser lex = new Parser(in);
    LinkedList<Token> tokens = lex.lexerTest();
    assertEquals(0, tokens.size());
  }

  @Test
  public void testExpCall() throws ParseException
  {
    System.out.println("test lex Exp.call");
    InputStream in = new BufferedInputStream(new ByteArrayInputStream(
        "a= this.foo()[10];".getBytes()));
    Parser lex = new Parser(in);
    LinkedList<Token> tokens = lex.lexerTest();
    String[] ss = {"a", "=", "this", ".", "foo", "(", ")", "[", "10", "]", ";"};
    assertEquals(ss.length, tokens.size());
    for (int i=0; i<ss.length; i++) {
      assertEquals(ss[i], tokens.get(i).image);
    }
  }
}






