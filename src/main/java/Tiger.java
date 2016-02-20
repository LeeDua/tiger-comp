import static control.Control.ConAst.dumpAst;
import static control.Control.ConAst.testFac;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ast.Ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import control.CommandLine;
import control.Control;
import control.Verbose;

//-elab classTable -elab methodTable  -dump ast test/LinkedList.java -codegen C
public class Tiger {
    String fname = null;
    codegen.bytecode.Ast.Program.ProgramSingle tbytecodeAst = null;
    Program.T theAst = null;

    /**
     * it would be helpful to be able to test the lexer independently.
     */
    private void testLexer() {
	InputStream fstream;
	System.out.println("Testing the lexer. All tokens:");
	try {
	    fstream = new BufferedInputStream(new FileInputStream(this.fname));
	    Lexer lexer = new Lexer(this.fname, fstream);
	    Token token = lexer.nextToken();

	    while (token.kind != Token.Kind.TOKEN_EOF) {
		System.out.println(token.toString());
		token = lexer.nextToken();
	    }
	    fstream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.exit(1);
    }

    /**
     * parsing the file, get an AST.
     */
    private Object parser() {
	InputStream fstream;
	Parser parser;
	try {
	    fstream = new BufferedInputStream(new FileInputStream(this.fname));
	    parser = new Parser(this.fname, fstream);

	    this.theAst = parser.parse();

	    fstream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	return null;
    }

    /**
     * elaborate the AST, report all possible errors.
     */
    private Object elaborator() {
	elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
	this.theAst.accept(elab);
	return null;
    }

    private Object compileBytecode() {
	BufferedReader br = null;
	codegen.bytecode.Ast.MainClass.MainClassSingle mainClass = (codegen.bytecode.Ast.MainClass.MainClassSingle) this.tbytecodeAst.mainClass;
	String command2 = "java -jar jasmin.jar test/" + mainClass.id + ".j";
	String err2 = null;
	try {
	    Process pro2 = Runtime.getRuntime().exec(command2);
	    BufferedReader br2 = new BufferedReader(
		    new InputStreamReader(pro2.getErrorStream()));
	    while ((err2 = br2.readLine()) != null) {
		System.out.println(err2);
	    }
	    for (codegen.bytecode.Ast.Class.T c : tbytecodeAst.classes) {// jasmin
		codegen.bytecode.Ast.Class.ClassSingle cs = (codegen.bytecode.Ast.Class.ClassSingle) c;
		command2 = "java -jar jasmin.jar test\\" + cs.id + ".j";
		pro2 = Runtime.getRuntime().exec(command2);
		br = new BufferedReader(
			new InputStreamReader(pro2.getErrorStream()));
		while ((err2 = br.readLine()) != null) {
		    System.out.println(err2);
		}
	    }
	    System.out.println("Jasmin finished...\n");
	    command2 = "java " + mainClass.id;

	    System.out.println("Run " + mainClass.id + ".class");
	    pro2 = Runtime.getRuntime().exec(command2);
	    br = new BufferedReader(
		    new InputStreamReader(pro2.getInputStream()));
	    while ((err2 = br.readLine()) != null) {
		System.out.println(err2);
	    }
	    br = new BufferedReader(
		    new InputStreamReader(pro2.getErrorStream()));
	    while ((err2 = br.readLine()) != null) {
		System.err.println(err2);
	    }
	    System.out.println("Execute finished...");

	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	return null;
    }

    private Object codegenBytecode() {
	codegen.bytecode.TranslateVisitor trans = new codegen.bytecode.TranslateVisitor();
	this.theAst.accept(trans);
	codegen.bytecode.Ast.Program.T bytecodeAst = trans.program;
	codegen.bytecode.PrettyPrintVisitor ppbc = new codegen.bytecode.PrettyPrintVisitor();
	bytecodeAst.accept(ppbc);
	this.tbytecodeAst = (codegen.bytecode.Ast.Program.ProgramSingle) bytecodeAst;

	return null;
    }

    private Object codegenC() {
	codegen.C.TranslateVisitor transC = new codegen.C.TranslateVisitor();
	this.theAst.accept(transC);
	codegen.C.Ast.Program.T cAst = transC.program;
	codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
	cAst.accept(ppc);
	return null;
    }

    private void codegen() {
	switch (control.Control.ConCodeGen.codegen) {
	case Bytecode:
	    Verbose.trace("condegenBytecode", () -> {
		return codegenBytecode();
	    } , Verbose.PASS);
	    break;
	case C:
	    Verbose.trace("condegenBytecode", () -> {
		return codegenC();
	    } , Verbose.PASS);
	    break;
	case Dalvik:
	    new util.Todo();
	    break;
	case X86:
	    new util.Todo();
	    break;
	default:
	    break;
	}
    }

    private Object compileC() {
	BufferedReader br = null;
	System.out.println("start....");
	String command = "gcc " + this.fname + ".c" + " runtime/runtime.c -o "
		+ this.fname + ".out";
	System.out.println(command);
	br = null;
	String err = null;
	try {
	    Process proC = Runtime.getRuntime().exec(command);
	    br = new BufferedReader(
		    new InputStreamReader(proC.getErrorStream()));
	    while ((err = br.readLine()) != null) {
		System.out.println(err);
	    }
	    command = "./" + this.fname + ".out";
	    Process proC2 = Runtime.getRuntime().exec(command);
	    br = new BufferedReader(
		    new InputStreamReader(proC2.getInputStream()));
	    while ((err = br.readLine()) != null) {
		System.out.println(err);
	    }
	    System.out.println("GCC assemble finished...\n");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * some glue code to call gcc to compile the generated C or x86 file, or
     * call java to run the bytecode file, or dalvik to run the dalvik bytecode.
     */
    private void compile() {
	switch (control.Control.ConCodeGen.codegen) {
	case Bytecode:
	    Verbose.trace("execuate", () -> {
		return compileBytecode();
	    } , Verbose.PASS);
	    break;
	case C:
	    Verbose.trace("execuate", () -> {
		return this.compileC();
	    } , Verbose.PASS);
	    break;
	default:
	    System.exit(0);
	}
    }

    public static void main(String[] args) {
	Tiger tiger = new Tiger();

	// handle command line arguments
	CommandLine cmd = new CommandLine();
	tiger.fname = cmd.scan(args);
	if (tiger.fname == null) {
	    cmd.usage();
	    return;
	}
	Control.ConCodeGen.fileName = tiger.fname;
	if (Control.ConLexer.test) {
	    tiger.testLexer();
	}

	// normal compilation phases.
	Verbose.trace("parser", () -> {
	    return tiger.parser();
	} , Verbose.PASS);

	// pretty printing the AST, if necessary
	if (dumpAst) {
	    ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
	    tiger.theAst.accept(pp);
	}

	Verbose.trace("elaborator", () -> {
	    return tiger.elaborator();
	} , Verbose.PASS);
	// code generation
	tiger.codegen();

	/*
	 * Automaticlly compile. In v0.0.3, using gcc as the default compiler
	 * platform: 32bit Linux
	 */
	// tiger.compile();

    }
}
