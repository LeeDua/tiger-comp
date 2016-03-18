class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}
class Fac {
  int class_field;
  public int ComputeFac(int num) {
    int num_aux;
    class_field = 10;
    if (undecl < 1) // undecl error
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num-1));
    return num_aux;
  }
}