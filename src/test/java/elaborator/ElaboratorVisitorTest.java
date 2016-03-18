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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qc1iu on 3/4/16.
 */
public class ElaboratorVisitorTest
{
  String[] files = {
      "BinarySearch.java",
      "BinaryTree.java",
      "BubbleSort.java",
      "Factorial.java",
      "LinearSearch.java",
      "LinkedList.java",
      "QuickSort.java",
      "Sum.java",
      "TreeVisitor.java"
  };

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testNoError()
  {
    for (String fname : files) {
      InputStream in = null;
      try {
        in = new BufferedInputStream(
            new FileInputStream("src/test/resources/" + fname));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      Parser p = new Parser(in);
      Ast.Program.T pp = null;
      try {
        pp = p.parser();
      } catch (ParseException e) {
        System.err.println("in file: " + fname);
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
    assertEquals(ElabError.UndeclError.class,
        elab.errorStack.firstElement().getClass());
    ElabError.UndeclError err =
        (ElabError.UndeclError) elab.errorStack.firstElement();
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
    assertEquals(ElabError.MethodMissMatch.class,
        elab.errorStack.firstElement().getClass());
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
    assertEquals(ElabError.TypeMissMatchError.class,
        elab.errorStack.firstElement().getClass());
  }

  @Test
  public void testClassOverLoad()
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/ClassOverload.java"));
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
    assertEquals(0, elab.errorStack.size());
  }

  @Test
  public void testElabExtends()
  {
    InputStream in = null;
    try {
      in = new BufferedInputStream(
          new FileInputStream("src/test/resources/TestExtendsFrom.java"));
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
    assertEquals(0, elab.errorStack.size());
    Ast.Program.ProgramSingle ps = (Ast.Program.ProgramSingle) pp;
    Ast.MainClass.MainClassSingle ms =
        (Ast.MainClass.MainClassSingle) ps.mainClass;
    Ast.Stm.Print stm_print = (Ast.Stm.Print) ms.stm;
    Ast.Exp.Call exp_call = (Ast.Exp.Call) stm_print.exp;
    Ast.Class.ClassSingle class_base1 = null;
    for (Ast.Class.T c : ps.classes){
      Ast.Class.ClassSingle cs = (Ast.Class.ClassSingle) c;
      if (cs.id.equals("Base1")){
        class_base1 = cs;
      }
    }
    assertNotNull(class_base1);
    Ast.Method.MethodSingle doit_base1 = null;
    for (Ast.Method.T m : class_base1.methods){
      Ast.Method.MethodSingle mss = (Ast.Method.MethodSingle) m;
      if (mss.id.equals("doit_base1")){
        doit_base1 = mss;
      }
    }
    assertNotNull(doit_base1);
    Ast.Stm.Print _stm_print = (Ast.Stm.Print) doit_base1.stms.getFirst();
    Ast.Exp.Id id = (Ast.Exp.Id) _stm_print.exp;

  }
}
