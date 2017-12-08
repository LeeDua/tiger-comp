import ast.Ast;
import ast.PrettyPrintVisitor;
import javacc.ParseException;
import javacc.Parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Tiger {
  public static void main(String[] args)
      throws FileNotFoundException, ParseException {
    System.out.println("tiger !");

    Parser p = new Parser(new BufferedInputStream(
        new FileInputStream("src/test/resources/LinkedList.java")));

    Ast.Program.T prog = p.parser();


    PrettyPrintVisitor pp = new PrettyPrintVisitor();
    prog.accept(pp);
    System.out.println(pp.toString());

  }
}