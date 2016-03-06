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
    class_field = num[0];
    //class_field = this.getArray(array)[0];
    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num -1));
    return num_aux;
  }

  public int[] getArray(int[] array) {
    return array;
  }
}