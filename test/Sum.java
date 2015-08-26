class GC {
	public static void main(String[] a) {
        System.out.println(new Doit().doit(101));
    }
}

class Garbage {
	GC g1;
	GC g2;
}

class Doit {
    public int doit(int n) {
        int i;
        int j;
     Doit d1;
     Garbage g1;
     Garbage g2;
     Garbage g3;
     
      g1 = new Garbage();
 		g2 = new Garbage();
 	g3 = new Garbage();
        i=0;
        while (i<n){
        	System.out.println(i);
        	d1 =new Doit();
        	j = d1.doit2(g1, g2, g3);
        	i = i+1;
        }
        return i;
    }
    
    public int doit2(Garbage g1, Garbage g2, Garbage g3) {
    	int i;
    	
    	g1 = new Garbage();
    	g2 = new Garbage();
    	g3 = new Garbage();
    	
    	i=10;
    	return i;
    	
    }
}


