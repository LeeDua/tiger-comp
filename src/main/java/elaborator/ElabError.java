package elaborator;

import ast.Ast;

import java.util.LinkedList;

/**
 * Created by qc1iu on 3/5/16.
 */
public class ElabError
{
  public static abstract class T
  {
    int linenum;
    String info;

    public abstract String toString();

  }

  public static class TypeMissMatchError extends T
  {
    Ast.Type.T expect;
    Ast.Type.T got;

    public TypeMissMatchError(Ast.Type.T expect, Ast.Type.T got, int linenum)
    {
      this.info = "";
      this.expect = expect;
      this.got = got;
      this.linenum = linenum;
    }

    public TypeMissMatchError(String info, Ast.Type.T expect,
                              Ast.Type.T got,
                              int linenum)
    {
      this.info = info;
      this.expect = expect;
      this.got = got;
      this.linenum = linenum;
    }

    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("TypeMissMatchError ");
      sb.append(this.info);
      sb.append(" line:" + linenum + ">");
      sb.append("expect " + expect.toString());
      sb.append(", ");
      sb.append("but got " + got.toString());
      sb.append("\n");
      return sb.toString();
    }
  }


  public static class UndeclError extends T
  {
    String undecl;

    public UndeclError(String undecl, int linenum)
    {
      this.undecl = undecl;
      this.linenum = linenum;
    }

    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("UndeclError line:" + linenum + ">");
      sb.append("\'" + this.undecl + "\'" + " used but not decl.");
      sb.append("\n");
      return sb.toString();
    }
  }

  public static class MethodMissMatch extends T
  {
    String id; // method id
    LinkedList<Ast.Type.T> expect;
    LinkedList<Ast.Type.T> got;

    public MethodMissMatch(String id,
                           LinkedList<Ast.Type.T> expect,
                           LinkedList<Ast.Type.T> got,
                           int linenum)
    {
      this.id = id;
      this.expect = expect;
      this.got = got;
      this.linenum = linenum;
    }

    @Override
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("MethodMissMatchError line:" + linenum + ">");
      sb.append("no matching method for "+id+got);
      sb.append("\n");
      return sb.toString();
    }
  }

}
