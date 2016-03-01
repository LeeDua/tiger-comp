package javacc;

import ast.Ast;
import ast.PrettyPrintVisitor;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by qc1iu on 2/27/16.
 */
public class ParserTest
{
  @Test
  public void testOneStatement() throws ParseException
  {
    System.out.println("test one statement");
    InputStream in = new BufferedInputStream(new ByteArrayInputStream(
        ("class LinkedList{\n" +
            "public static void main(String[] a){\n" +
            "System.out.println(new Person().foo(1, 2, 3));\n" +
            "}\n" +
            "}\n").getBytes()));

    Parser p = new Parser(in);
    Ast.Program.ProgramSingle prog = (Ast.Program.ProgramSingle) p.parser();
    Ast.MainClass.MainClassSingle main = (Ast.MainClass.MainClassSingle) prog.mainClass;
    assertEquals("ast.Ast$Stm$Print", main.stm.getClass().getName());
    Ast.Stm.Print stm = (Ast.Stm.Print) main.stm;
    assertEquals("ast.Ast$Exp$Call", stm.exp.getClass().getName());
    Ast.Exp.Call call = (Ast.Exp.Call) stm.exp;
    assertEquals("ast.Ast$Exp$NewObject", call.exp.getClass().getName());
    Ast.Exp.NewObject obj = (Ast.Exp.NewObject) call.exp;
    assertEquals("Person", obj.id);
    assertEquals("foo", call.id);
  }

  @Test
  public void testParser() throws ParseException, FileNotFoundException
  {
    System.out.println("test parse LinkedList.java");
    InputStream in = new BufferedInputStream(
        new FileInputStream("src/test/resources/LinkedList.java"));
    Parser p = new Parser(in);
    Ast.Program.ProgramSingle prog = (Ast.Program.ProgramSingle) p.parser();
    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    prog.accept(pp);
  }

}






