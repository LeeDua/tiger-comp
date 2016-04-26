package util;

public class Bug
{
  public Bug()
  {
     throw new java.lang.Error("Compiler bug");
  }

  public Bug(String info)
  {
    throw new java.lang.Error(info);
  }
}
