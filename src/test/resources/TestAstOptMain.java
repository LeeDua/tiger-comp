class AstOptMain { 
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}

class Junk{
  public int foo() {
    return 1;
  }
}

class Fac {
  public int ComputeFac(int num) {
    while ((100 + 2) < 10) {
      System.out.println(new Junk().foo());
    }

    return 0;
  }
}
