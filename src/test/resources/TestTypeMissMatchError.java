class Factorial {
  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}
class Fac {
  int class_field;
  int[] array;
  public int ComputeFac(int num) {
    int num_aux;
    class_field = num[0]; // type missmatch error
    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num -1));
    return num_aux;
  }
}