package control;

import org.apache.commons.cli.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by qc1iu on 01/03/16.
 */
public class CommandLineParserTest {
  @Test
  public void test() throws ParseException {
    String cmd = "-o aaa.c LinkedList.java -codegen Bytecode -v svg";
    String[] args = cmd.split(" ");
    CommandLineParser c = new CommandLineParser(args);
    c.scan();
    assertEquals(Control.ConCodeGen.Kind_t.Bytecode,
        Control.ConCodeGen.codegen);
    assertEquals("LinkedList.java", Control.ConCodeGen.fileName);
    assertEquals("aaa.c", Control.ConCodeGen.outputName);
    assertEquals("Svg", Control.visualize.name());
  }


  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void testMutilInputfile() throws ParseException {
    expectedEx.expect(ParseException.class);
    expectedEx.expectMessage("can only parse one file");
    String cmd = "-o aaa.c input1.java input2.java";
    String[] args = cmd.split(" ");
    CommandLineParser c = new CommandLineParser(args);
    c.scan();
  }

  @Test
  public void testNoInputfile() throws ParseException {
    expectedEx.expect(ParseException.class);
    expectedEx.expectMessage("no input file");
    String cmd = "-o aaa.c";
    String[] args = cmd.split(" ");
    CommandLineParser c = new CommandLineParser(args);
    c.scan();
  }

  @Test
  public void testIllegleGenerator() throws ParseException {
    expectedEx.expect(ParseException.class);
    expectedEx.expectMessage("expect {RuntimeC|Bytecode|Dalvik|X86}");
    String cmd = "-codegen abc LinkedList.java";
    String[] args = cmd.split(" ");
    CommandLineParser c = new CommandLineParser(args);
    c.scan();
  }

  @Test
  public void testIllegleVisualFormat() throws ParseException {
    expectedEx.expect(ParseException.class);
    expectedEx.expectMessage("expect {bmp|pdf|svg|jpg}");
    String cmd = "LinkedList.java -v jpeg";
    String[] args = cmd.split(" ");
    CommandLineParser c = new CommandLineParser(args);
    c.scan();
  }
}