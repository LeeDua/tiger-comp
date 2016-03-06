package elaborator;

import ast.Ast;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qc1iu on 3/4/16.
 */
public class ElaboratorVisitorTest
{
  String[] files = {
      //"BinarySearch.java",
      "BinaryTree.java",
      //"BubbleSort.java",
      "Factorial.java",
      //"LinearSearch.java",
      "LinkedList.java",
      //"QuickSort.java",
      "Sum.java",
      "TreeVisitor.java"
  };

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testNoError()
  {
    for (String fname : files){
      InputStream in = null;
      try {
        in = new BufferedInputStream(
            new FileInputStream("src/test/resources/"+fname));
      } catch (FileNotFoundException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }

      Parser p = new Parser(in);
      Ast.Program.T pp = null;
      try {
        pp = p.parser();
      } catch (ParseException e) {
        System.err.println("in file: "+fname);
        e.printStackTrace();
      }
      ElaboratorVisitor elab = new ElaboratorVisitor();
      pp.accept(elab);
      assertEquals(0, elab.errorStack.size());
    }
  }

  @Test
  public void testExtends() throws ParseException
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/TreeVisitor.java"));
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

    Parser p = new Parser(in);
    Ast.Program.T pp = p.parser();
    ElaboratorVisitor elab = new ElaboratorVisitor();
    pp.accept(elab);
    assertEquals(5, elab.classTable.table.size());
    ClassBinding cb = elab.classTable.table.get("MyVisitor");
    assertNotNull(cb);
    assertEquals("Visitor", cb.extendss);
  }

  @Test
  public void testUndeclError()
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/UndeclError.java"));
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    Parser p = new Parser(in);
    Ast.Program.T pp = null;
    try {
      pp = p.parser();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    ElaboratorVisitor elab = new ElaboratorVisitor();
    pp.accept(elab);
    assertEquals(1, elab.errorStack.size());
    assertEquals(ElabError.UndeclError.class, elab.errorStack.firstElement().getClass());
    ElabError.UndeclError err = (ElabError.UndeclError) elab.errorStack.firstElement();
    assertEquals(11, err.linenum);
    assertEquals("undecl", err.undecl);
  }

  @Test
  public void testMethodMissMatchError()
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/MethodMissMatchError.java"));
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    Parser p = new Parser(in);
    Ast.Program.T pp = null;
    try {
      pp = p.parser();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    ElaboratorVisitor elab = new ElaboratorVisitor();
    pp.accept(elab);
    assertEquals(1, elab.errorStack.size());
    assertEquals(ElabError.MethodMissMatch.class, elab.errorStack.firstElement().getClass());
  }

  @Test
  public void testTypeMissMatchError()
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/TypeMissMatchError.java"));
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    Parser p = new Parser(in);
    Ast.Program.T pp = null;
    try {
      pp = p.parser();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    ElaboratorVisitor elab = new ElaboratorVisitor();
    pp.accept(elab);
    assertEquals(1, elab.errorStack.size());
    assertEquals(ElabError.TypeMissMatchError.class, elab.errorStack.firstElement().getClass());
  }
}