import ast.Ast;
import elaborator.ElabError;
import elaborator.ElaboratorVisitor;
import javacc.ParseException;
import javacc.Parser;
import tac.Ast2TacVisitor;
import tac.PrettyPrintVisitor;
import tac.Tac;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Tiger {
  public static void main(String[] args)
      throws FileNotFoundException, ParseException {
    System.out.println("// tiger !");

    Parser p = new Parser(new BufferedInputStream(
        new FileInputStream("src/test/resources/BinarySearch.java")));

    Ast.Program.T prog = p.parser();


    //PrettyPrintVisitor pp = new PrettyPrintVisitor();
    //prog.accept(pp);
    //System.out.println(pp.toString());


    ElaboratorVisitor elab = new ElaboratorVisitor();
    prog.accept(elab);
    for (ElabError.T e :elab.getErrorStack()) {
      e.toString();
    }

    tac.Ast2TacVisitor trans = new Ast2TacVisitor();
    prog.accept(trans);

    Tac.Program.T tac = trans.prog;

    PrettyPrintVisitor tacPrinter = new PrettyPrintVisitor();
    tac.accept(tacPrinter);

    System.out.println(tacPrinter.toString());

  }
}