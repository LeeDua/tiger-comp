package util;

public class Bug
{
	public Bug()
	{
		// throw new java.lang.Error("Compiler bug");
		System.err.println("error");
		System.exit(0);
	}

	public Bug(String info)
	{
		System.err.println(info);
		System.exit(0);
	}
}
