class DeadCode { 
  public static void main(String[] a) {
    System.out.println(new Doit().doit());
  }
}

class Doit {
  public int doit() {
    if (true)
      System.out.println(1);
    else 
      System.out.println(0);

    while (true && false) {
      System.out.println(2);
    }
    return 0;
  }
}
