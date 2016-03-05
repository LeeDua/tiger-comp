package elaborator;

import ast.Ast;
import javacc.ParseException;
import javacc.Parser;
import org.junit.Test;

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
}