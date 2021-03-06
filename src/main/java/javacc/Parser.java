/* Parser.java */
/* Generated By:JavaCC: Do not edit this line. Parser.java */
package  javacc;
import ast.Ast.Class;
import ast.Ast.Class.ClassSingle;
import ast.Ast.MainClass;
import ast.Ast.Dec;
import ast.Ast.Dec.DecSingle;
import ast.Ast.Exp;
import ast.Ast.Method;
import ast.Ast.Method.MethodSingle;
import ast.Ast.Program;
import ast.Ast.Type;
import control.Verbose;
import ast.Ast.Exp.Call;
import ast.Ast.Exp.Id;
import ast.Ast.Exp.NewObject;
import ast.Ast.Exp.T;
import ast.Ast.Exp.ArraySelect;
import ast.Ast.MainClass.MainClassSingle;
import ast.Ast.Program.ProgramSingle;
import ast.Ast.Stm.If;
import ast.Ast.Stm.Print;
import ast.Ast.Stm;
import java.util.LinkedList;



public class Parser implements ParserConstants {
    Type.T currentType;

  final public LinkedList<Token> lexerTest() throws ParseException {Token t;
    LinkedList<Token> tokens = new LinkedList<Token>();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:
      case AND:
      case ASSIGN:
      case BOOLEAN:
      case CLASS:
      case COMMER:
      case DOT:
      case ELSE:
      case EXTENDS:
      case FALSE:
      case IF:
      case INT:
      case LBRACE:
      case LBRACK:
      case LENGTH:
      case LPAREN:
      case LT:
      case MAIN:
      case NEW:
      case NOT:
      case NUM:
      case OUT:
      case PRINTLN:
      case PUBLIC:
      case RBRACE:
      case RBRACK:
      case RETURN:
      case RPAREN:
      case SEMI:
      case STATIC:
      case STRING:
      case SUB:
      case SYSTEM:
      case THIS:
      case TIMES:
      case TRUE:
      case VOID:
      case WHILE:
      case ID:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:{
        t = jj_consume_token(ADD);
tokens.add(t);
        break;
        }
      case AND:{
        t = jj_consume_token(AND);
tokens.add(t);
        break;
        }
      case ASSIGN:{
        t = jj_consume_token(ASSIGN);
tokens.add(t);
        break;
        }
      case BOOLEAN:{
        t = jj_consume_token(BOOLEAN);
tokens.add(t);
        break;
        }
      case CLASS:{
        t = jj_consume_token(CLASS);
tokens.add(t);
        break;
        }
      case COMMER:{
        t = jj_consume_token(COMMER);
tokens.add(t);
        break;
        }
      case DOT:{
        t = jj_consume_token(DOT);
tokens.add(t);
        break;
        }
      case ELSE:{
        t = jj_consume_token(ELSE);
tokens.add(t);
        break;
        }
      case EXTENDS:{
        t = jj_consume_token(EXTENDS);
tokens.add(t);
        break;
        }
      case FALSE:{
        t = jj_consume_token(FALSE);
tokens.add(t);
        break;
        }
      case ID:{
        t = jj_consume_token(ID);
tokens.add(t);
        break;
        }
      case IF:{
        t = jj_consume_token(IF);
tokens.add(t);
        break;
        }
      case INT:{
        t = jj_consume_token(INT);
tokens.add(t);
        break;
        }
      case LBRACE:{
        t = jj_consume_token(LBRACE);
tokens.add(t);
        break;
        }
      case LBRACK:{
        t = jj_consume_token(LBRACK);
tokens.add(t);
        break;
        }
      case LPAREN:{
        t = jj_consume_token(LPAREN);
tokens.add(t);
        break;
        }
      case LENGTH:{
        t = jj_consume_token(LENGTH);
tokens.add(t);
        break;
        }
      case LT:{
        t = jj_consume_token(LT);
tokens.add(t);
        break;
        }
      case MAIN:{
        t = jj_consume_token(MAIN);
tokens.add(t);
        break;
        }
      case NEW:{
        t = jj_consume_token(NEW);
tokens.add(t);
        break;
        }
      case NOT:{
        t = jj_consume_token(NOT);
tokens.add(t);
        break;
        }
      case NUM:{
        t = jj_consume_token(NUM);
tokens.add(t);
        break;
        }
      case OUT:{
        t = jj_consume_token(OUT);
tokens.add(t);
        break;
        }
      case PRINTLN:{
        t = jj_consume_token(PRINTLN);
tokens.add(t);
        break;
        }
      case PUBLIC:{
        t = jj_consume_token(PUBLIC);
tokens.add(t);
        break;
        }
      case RBRACK:{
        t = jj_consume_token(RBRACK);
tokens.add(t);
        break;
        }
      case RBRACE:{
        t = jj_consume_token(RBRACE);
tokens.add(t);
        break;
        }
      case RPAREN:{
        t = jj_consume_token(RPAREN);
tokens.add(t);
        break;
        }
      case RETURN:{
        t = jj_consume_token(RETURN);
tokens.add(t);
        break;
        }
      case SEMI:{
        t = jj_consume_token(SEMI);
tokens.add(t);
        break;
        }
      case STATIC:{
        t = jj_consume_token(STATIC);
tokens.add(t);
        break;
        }
      case STRING:{
        t = jj_consume_token(STRING);
tokens.add(t);
        break;
        }
      case SUB:{
        t = jj_consume_token(SUB);
tokens.add(t);
        break;
        }
      case SYSTEM:{
        t = jj_consume_token(SYSTEM);
tokens.add(t);
        break;
        }
      case THIS:{
        t = jj_consume_token(THIS);
tokens.add(t);
        break;
        }
      case TIMES:{
        t = jj_consume_token(TIMES);
tokens.add(t);
        break;
        }
      case TRUE:{
        t = jj_consume_token(TRUE);
tokens.add(t);
        break;
        }
      case VOID:{
        t = jj_consume_token(VOID);
tokens.add(t);
        break;
        }
      case WHILE:{
        t = jj_consume_token(WHILE);
tokens.add(t);
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(0);
{if ("" != null) return tokens;}
    throw new Error("Missing return statement in function");
  }

// ExpList  -> Exp ExpRest*
//          ->
// ExpRest -> , Exp
  final public 
LinkedList<Exp.T> parseExpList() throws ParseException {LinkedList<Exp.T> args = new LinkedList<Exp.T>();
    Exp.T exp;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case FALSE:
    case LPAREN:
    case NEW:
    case NOT:
    case NUM:
    case THIS:
    case TRUE:
    case ID:{
      exp = parseExp();
args.add(exp);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMER:{
          ;
          break;
          }
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMER);
        exp = parseExp();
args.add(exp);
      }
      break;
      }
    default:
      jj_la1[3] = jj_gen;
      ;
    }
{if ("" != null) return args;}
    throw new Error("Missing return statement in function");
  }

// AtomExp  -> (exp)
//          -> INTEGER_LITERAL
//          -> true
//          -> false
//          -> this
//          -> id
//          -> new int [exp]
//          -> new id ()
  final public Exp.T parseAtomExp() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LPAREN:{
Exp.T exp;
      jj_consume_token(LPAREN);
      exp = parseExp();
      jj_consume_token(RPAREN);
{if ("" != null) return exp;}
      break;
      }
    case NUM:{
int value;
      t = jj_consume_token(NUM);
value = Integer.parseInt(t.image);
{if ("" != null) return new Exp.Num(value, t.beginLine);}
      break;
      }
    case TRUE:{
      t = jj_consume_token(TRUE);
{if ("" != null) return new Exp.True(t.beginLine);}
      break;
      }
    case FALSE:{
      t = jj_consume_token(FALSE);
{if ("" != null) return new Exp.False(t.beginLine);}
      break;
      }
    case THIS:{
      t = jj_consume_token(THIS);
{if ("" != null) return new Exp.This(t.beginLine);}
      break;
      }
    case ID:{
      t = jj_consume_token(ID);
{if ("" != null) return new Exp.Id(t.image, currentType, false, t.beginLine);}
      break;
      }
    case NEW:{
      jj_consume_token(NEW);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INT:{
        jj_consume_token(INT);
        jj_consume_token(LBRACK);
Exp.T exp;
        exp = parseExp();
        t = jj_consume_token(RBRACK);
{if ("" != null) return new Exp.NewIntArray(exp, t.beginLine);}
        break;
        }
      case ID:{
        t = jj_consume_token(ID);
        jj_consume_token(LPAREN);
        jj_consume_token(RPAREN);
{if ("" != null) return new Exp.NewObject(t.image, t.beginLine);}
        break;
        }
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

// CallExp  -> AtomExp
//          -> AtomExp .id()
//          -> AtomExp .length
  final public Exp.T parseCallExp() throws ParseException {Token t;
    Exp.T exp;
    exp = parseAtomExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case DOT:{
      jj_consume_token(DOT);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LENGTH:{
        t = jj_consume_token(LENGTH);
{if ("" != null) return new Exp.Length(exp, t.beginLine);}
        break;
        }
      case ID:{
        t = jj_consume_token(ID);
        jj_consume_token(LPAREN);
LinkedList<Exp.T> args;
        args = parseExpList();
        jj_consume_token(RPAREN);
{if ("" != null) return new Exp.Call(exp, t.image, args, t.beginLine);}
        break;
        }
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[7] = jj_gen;
      ;
    }
{if ("" != null) return exp;}
    throw new Error("Missing return statement in function");
  }

// NotExp   -> CallExp
//          -> CallExp [exp]
  final public Exp.T parseNotExp() throws ParseException {Exp.T exp;
    exp = parseCallExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LBRACK:{
Exp.T index;
      jj_consume_token(LBRACK);
      index = parseExp();
      jj_consume_token(RBRACK);
{if ("" != null) return new Exp.ArraySelect(exp, index, index.linenum);}
      break;
      }
    default:
      jj_la1[8] = jj_gen;
      ;
    }
{if ("" != null) return exp;}
    throw new Error("Missing return statement in function");
  }

// TimesExp -> ! TimesExp
//          -> NotExp
  final public Exp.T parseTimesExp() throws ParseException {Exp.T exp;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NOT:{
      jj_consume_token(NOT);
      exp = parseTimesExp();
{if ("" != null) return new Exp.Not(exp, exp.linenum);}
      break;
      }
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    exp = parseNotExp();
{if ("" != null) return exp;}
    throw new Error("Missing return statement in function");
  }

// AddSubExp    -> TimesExp * TimesExp
//              -> TimesExp
  final public Exp.T parseAddSubExp() throws ParseException {Exp.T left;
    Exp.T right;
    left = parseTimesExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case TIMES:{
      jj_consume_token(TIMES);
      right = parseAddSubExp();
{if ("" != null) return new Exp.Times(left, right, right.linenum);}
      break;
      }
    default:
      jj_la1[10] = jj_gen;
      ;
    }
{if ("" != null) return left;}
    throw new Error("Missing return statement in function");
  }

// LtExp    -> AddSubExp + AddSubExp
//          -> AddSubExp - AddSubExp
//          -> AddSubExp
  final public Exp.T parseLtExp() throws ParseException {Exp.T left;
    Exp.T right;
    left = parseAddSubExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ADD:
    case SUB:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ADD:{
        jj_consume_token(ADD);
        right = parseLtExp();
{if ("" != null) return new Exp.Add(left, right, right.linenum);}
        break;
        }
      case SUB:{
        jj_consume_token(SUB);
        right = parseLtExp();
if (right instanceof Exp.Sub){
                Exp.Sub e = (Exp.Sub)right;
                right = new Exp.Add(e.left, e.right, right.linenum);
            }
{if ("" != null) return new Exp.Sub(left, right, right.linenum);}
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      ;
    }
{if ("" != null) return left;}
    throw new Error("Missing return statement in function");
  }

// AndExp   -> LtExp < LtExp
//          -> LtExp
  final public Exp.T parseAndExp() throws ParseException {Exp.T left;
    Exp.T right;
    left = parseLtExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LT:{
      jj_consume_token(LT);
      right = parseLtExp();
{if ("" != null) return new Exp.Lt(left, right, right.linenum);}
      break;
      }
    default:
      jj_la1[13] = jj_gen;
      ;
    }
{if ("" != null) return left;}
    throw new Error("Missing return statement in function");
  }

// Exp  -> AndExp && AndExp
//      -> AndExp
  final public Exp.T parseExp() throws ParseException {Exp.T left;
    Exp.T right;
    left = parseAndExp();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case AND:{
      jj_consume_token(AND);
      right = parseExp();
{if ("" != null) return new Exp.And(left, right);}
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      ;
    }
{if ("" != null) return left;}
    throw new Error("Missing return statement in function");
  }

// Statements   -> Statement Statements
//              ->
  final public LinkedList<Stm.T> parseStatements() throws ParseException {LinkedList<Stm.T> stms = new LinkedList<Stm.T>();
    Stm.T stm;
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IF:
      case LBRACE:
      case SYSTEM:
      case WHILE:
      case ID:{
        ;
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        break label_3;
      }
      stm = parseStatement();
stms.add(stm);
    }
{if ("" != null) return stms;}
    throw new Error("Missing return statement in function");
  }

// Statement    -> { Statement* }
//              -> if ( Exp ) Statement else Statement
//              -> while ( Exp ) Statement
//              -> System.out.println ( Exp ) ;
//              -> id = Exp ;
//              -> id [ Exp ]= Exp ;
  final public Stm.T parseStatement() throws ParseException {Stm.T stm = null;
    Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LBRACE:{
      jj_consume_token(LBRACE);
LinkedList<Stm.T> block;
      block = parseStatements();
      t = jj_consume_token(RBRACE);
{if ("" != null) return new Stm.Block(block, t.beginLine);}
      break;
      }
    case IF:{
      t = jj_consume_token(IF);
Exp.T condition;
            Stm.T thenn;
            Stm.T elsee;
      jj_consume_token(LPAREN);
      condition = parseExp();
      jj_consume_token(RPAREN);
      thenn = parseStatement();
      jj_consume_token(ELSE);
      elsee = parseStatement();
{if ("" != null) return new Stm.If(condition, thenn, elsee, t.beginLine);}
      break;
      }
    case WHILE:{
      t = jj_consume_token(WHILE);
Exp.T condition;
            Stm.T body;
      jj_consume_token(LPAREN);
      condition = parseExp();
      jj_consume_token(RPAREN);
      body = parseStatement();
{if ("" != null) return new Stm.While(condition, body, t.beginLine);}
      break;
      }
    case SYSTEM:{
      t = jj_consume_token(SYSTEM);
      jj_consume_token(DOT);
      jj_consume_token(OUT);
      jj_consume_token(DOT);
      jj_consume_token(PRINTLN);
      jj_consume_token(LPAREN);
Exp.T exp;
      exp = parseExp();
      jj_consume_token(RPAREN);
      jj_consume_token(SEMI);
{if ("" != null) return new Print(exp, t.beginLine);}
      break;
      }
    case ID:{
      t = jj_consume_token(ID);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ASSIGN:{
        jj_consume_token(ASSIGN);
Exp.T exp;
        exp = parseExp();
        jj_consume_token(SEMI);
{if ("" != null) return new Stm.Assign(t.image, exp, t.beginLine);}
        break;
        }
      case LBRACK:{
        jj_consume_token(LBRACK);
Exp.T exp;
                Exp.T index;
        index = parseExp();
        jj_consume_token(RBRACK);
        jj_consume_token(ASSIGN);
        exp = parseExp();
        jj_consume_token(SEMI);
{if ("" != null) return new Stm.AssignArray(t.image, index, exp, t.beginLine);}
        break;
        }
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
{if ("" != null) return stm;}
      break;
      }
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

// Type -> int []
//      -> boolean
//      -> int
//      -> id
  final public Type.T parseType() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INT:{
      jj_consume_token(INT);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LBRACK:{
        jj_consume_token(LBRACK);
        jj_consume_token(RBRACK);
this.currentType = new Type.IntArray();
{if ("" != null) return this.currentType;}
        break;
        }
      default:
        jj_la1[18] = jj_gen;
this.currentType =  new Type.Int();
                {if ("" != null) return this.currentType;}
      }
      break;
      }
    case BOOLEAN:{
      jj_consume_token(BOOLEAN);
this.currentType = new Type.Boolean();
            {if ("" != null) return this.currentType;}
      break;
      }
    case ID:{
      t = jj_consume_token(ID);
this.currentType =  new Type.ClassType(t.image);
            {if ("" != null) return this.currentType;}
      break;
      }
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Dec.T parseVarDecl(boolean isField) throws ParseException {Token t;
    Type.T type;
    type = parseType();
    t = jj_consume_token(ID);
    jj_consume_token(SEMI);
{if ("" != null) return new DecSingle(type, t.image, isField);}
    throw new Error("Missing return statement in function");
  }

// VarDecls -> VarDecl VarDecls
//          ->
  final public LinkedList<Dec.T> parseVarDecls(boolean isField) throws ParseException {LinkedList<Dec.T> decls = new LinkedList<Dec.T>();
    Dec.T decl;
    label_4:
    while (true) {
      if (jj_2_1(2147483647)) {
        ;
      } else {
        break label_4;
      }
      decl = parseVarDecl(isField);
decls.add(decl);
    }
{if ("" != null) return decls;}
    throw new Error("Missing return statement in function");
  }

// FormalList -> Type id FormalRest*
//            ->
// FormalRest -> , Type id
  final public LinkedList<Dec.T> parseFormalList() throws ParseException {LinkedList<Dec.T> formals = new LinkedList<Dec.T>();
    Token t;
    Type.T type;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case BOOLEAN:
    case INT:
    case ID:{
      type = parseType();
      t = jj_consume_token(ID);
formals.add(new DecSingle(type, t.image, false));
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMER:{
          ;
          break;
          }
        default:
          jj_la1[20] = jj_gen;
          break label_5;
        }
        jj_consume_token(COMMER);
        type = parseType();
        t = jj_consume_token(ID);
formals.add(new DecSingle(type, t.image, false));
      }
      break;
      }
    default:
      jj_la1[21] = jj_gen;
      ;
    }
{if ("" != null) return formals;}
    throw new Error("Missing return statement in function");
  }

// Method -> public Type id ( FormalList )
//           { VarDecl* Statement* return Exp ;}
  final public Method.T parseMethodDecl() throws ParseException {Token t;
    Type.T retType;
    String id;
    LinkedList<Dec.T> formals;
    LinkedList<Dec.T> locals;
    LinkedList<Stm.T> stms;
    Exp.T retExp;
    jj_consume_token(PUBLIC);
    retType = parseType();
    t = jj_consume_token(ID);
id = t.image;
    jj_consume_token(LPAREN);
    formals = parseFormalList();
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    locals = parseVarDecls(false);
    stms = parseStatements();
    jj_consume_token(RETURN);
    retExp = parseExp();
    jj_consume_token(SEMI);
    jj_consume_token(RBRACE);
{if ("" != null) return new MethodSingle(retType, id, formals, locals, stms, retExp);}
    throw new Error("Missing return statement in function");
  }

// MethodDecls  -> MethodDecl MethodDecls
//              ->
  final public LinkedList<Method.T> parseMethodDecls() throws ParseException {LinkedList<Method.T> methods = new LinkedList<Method.T>();
    Method.T m;
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case PUBLIC:{
        ;
        break;
        }
      default:
        jj_la1[22] = jj_gen;
        break label_6;
      }
      m = parseMethodDecl();
methods.add(m);
    }
{if ("" != null) return methods;}
    throw new Error("Missing return statement in function");
  }

// ClassDecl -> class id { VarDecl* MethodDecl* }
//           -> class id extends id { VarDecl* MethodDecl* }
  final public Class.T parseClassDecl() throws ParseException {String e = null;
    String id = null;
    Token t;
    LinkedList<Dec.T> decls;
    LinkedList<Method.T> methods;
    jj_consume_token(CLASS);
    t = jj_consume_token(ID);
id = t.image;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case EXTENDS:{
      jj_consume_token(EXTENDS);
      t = jj_consume_token(ID);
e = t.image;
      break;
      }
    default:
      jj_la1[23] = jj_gen;
      ;
    }
    jj_consume_token(LBRACE);
    decls = parseVarDecls(true);
    methods = parseMethodDecls();
    jj_consume_token(RBRACE);
{if ("" != null) return new ClassSingle(id, e, decls, methods);}
    throw new Error("Missing return statement in function");
  }

// ClassDecls   -> ClassDecl ClassDecls
//              ->
  final public LinkedList<Class.T> parseClassDecls() throws ParseException {LinkedList<Class.T> classes = new LinkedList<Class.T>();
    Class.T c;
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case CLASS:{
        ;
        break;
        }
      default:
        jj_la1[24] = jj_gen;
        break label_7;
      }
      c = parseClassDecl();
classes.add(c);
    }
{if ("" != null) return classes;}
    throw new Error("Missing return statement in function");
  }

  final public MainClassSingle parseMain() throws ParseException {Token id;
    Token arg;
    Stm.T stm;
    jj_consume_token(CLASS);
    id = jj_consume_token(ID);
    jj_consume_token(LBRACE);
    jj_consume_token(PUBLIC);
    jj_consume_token(STATIC);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(LPAREN);
    jj_consume_token(STRING);
    jj_consume_token(LBRACK);
    jj_consume_token(RBRACK);
    arg = jj_consume_token(ID);
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    stm = parseStatement();
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
{if ("" != null) return new MainClassSingle(id.image, arg.image, stm);}
    throw new Error("Missing return statement in function");
  }

  final public Program.T parser() throws ParseException {Program.T p;
    MainClassSingle main;
    LinkedList<Class.T> classes;
    main = parseMain();
    classes = parseClassDecls();
    jj_consume_token(0);
{if ("" != null) return new ProgramSingle(main, classes);}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla)
 {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_3R_12()
 {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3R_8()
 {
    if (jj_3R_9()) return true;
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(SEMI)) return true;
    return false;
  }

  private boolean jj_3R_13()
 {
    if (jj_scan_token(LBRACK)) return true;
    if (jj_scan_token(RBRACK)) return true;
    return false;
  }

  private boolean jj_3_1()
 {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_11()
 {
    if (jj_scan_token(BOOLEAN)) return true;
    return false;
  }

  private boolean jj_3R_14()
 {
    return false;
  }

  private boolean jj_3R_10()
 {
    if (jj_scan_token(INT)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_13()) {
    jj_scanpos = xsp;
    if (jj_3R_14()) return true;
    }
    return false;
  }

  private boolean jj_3R_9()
 {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) return true;
    }
    }
    return false;
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[25];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xffffffe0,0xffffffe0,0x400,0x3904000,0x10000,0x2904000,0x80000,0x800,0x40000,0x1000000,0x0,0x20,0x20,0x200000,0x40,0x28000,0x40080,0x28000,0x40000,0x10100,0x400,0x10100,0x10000000,0x2000,0x200,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0xfff,0xfff,0x0,0x940,0x800,0x940,0x800,0x0,0x0,0x0,0x80,0x10,0x10,0x0,0x0,0xc20,0x0,0xc20,0x0,0x800,0x0,0x800,0x0,0x0,0x0,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[1];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
	if (jj_input_stream == null) {
      jj_input_stream = new SimpleCharStream(stream, 1, 1);
   } else {
      jj_input_stream.ReInit(stream, 1, 1);
   }
   if (token_source == null) {
      token_source = new ParserTokenManager(jj_input_stream);
   }

    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 25; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  @SuppressWarnings("serial")
  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) {
       return;
    }

    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];

      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }

      for (int[] oldentry : jj_expentries) {
        if (oldentry.length == jj_expentry.length) {
          boolean isMatched = true;

          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              isMatched = false;
              break;
            }

          }
          if (isMatched) {
            jj_expentries.add(jj_expentry);
            break;
          }
        }
      }

      if (pos != 0) {
        jj_lasttokens[(jj_endpos = pos) - 1] = kind;
      }
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[48];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 25; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 48; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
      try {
        JJCalls p = jj_2_rtns[i];

        do {
          if (p.gen > jj_gen) {
            jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
            switch (i) {
              case 0: jj_3_1(); break;
            }
          }
          p = p.next;
        } while (p != null);

        } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }

    p.gen = jj_gen + xla - jj_la; 
    p.first = token;
    p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
