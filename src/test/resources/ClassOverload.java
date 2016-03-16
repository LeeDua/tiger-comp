class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Base {
  public int foo(int a, int b) {
    return a + b;
  }
}
class Fac extends Base{
  public int ComputeFac(int num) {
    return this.foo(num);
  }

  public int foo(int a) {
    return a;
  }
}