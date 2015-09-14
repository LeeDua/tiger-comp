TIGER
=====
[![Build Status](https://drone.io/github.com/qc1iu/tiger-comp/status.png)](https://drone.io/github.com/qc1iu/tiger-comp/latest)

The Tiger compiler. Copyright (C) 2013-2015, SSE of USTC.

`Tiger` is a **MiniJava lanuage**(see [Tiger book](http://www.cs.princeton.edu/~appel/modern/java/) Appendix MiniJava Language
Reference Manual) compile. Just for fun, it has a garbage collector using copy collection algorithm.


##Getting Start


### compile to C programming language
use 

	java -jar tiger test/LinkedList.java -codegen C

### compile to jasmin programming language
use

	java -jar tiger test/LinkedList.java -codegen bytecode

and is will translate .java to `.j(jasmin)`.

##GC Support

In tiger, we use a gc named **Gimple garbage collector**, whitch means `gc is simple`. And we use the algorithm called [Cheney's algorithm](https://en.wikipedia.org/wiki/Cheney's_algorithm) which uses a breadth-first strategy.

###Object Model

An object model is the strategy that how an object can be laid out in memory (the heap). A good object model should support all kinds of possible operations efficiently on an object: virtual method dispatching, locking, garbage collection, etc., whereas at the same time be as compact as possible to save storage. 

There are two different forms of objects in MiniJava: normal objects and (integer) array objects. So, to support virtual method dispatching on normal objects, each object is represented as a sequence of its fields plus a unique virtual method table pointer at the zero offset as a header. And to support array length operations on array objects, arrays contain a length field as a header.


	// "new" a new object, do necessary initializations, and
	// return the pointer (reference).
	
		  ----------------
	      | vptr      ---|----> (points to the virtual method table)
	      |--------------|
	      | isObjOrArray | (0: for normal objects)
	      |--------------|
	      | length       | (this field should be empty for normal objects)
	      |--------------|
	      | forwarding   |
	      |--------------|\
	p---->| v_0          | \
	      |--------------|  s
	      | ...          |  i
	      |--------------|  z
	      | v_{size-1}   | /e
	      ----------------/


	// "new" an array of size "length", do necessary
	// initializations. And each array comes with an
	// extra "header" storing the array length and other information.
		  ----------------
		  | vptr         | (this field should be empty for an array)
		  |--------------|
		  | isObjOrArray | (1: for array)
		  |--------------|
		  | length       |
		  |--------------|
	      | forwarding   |
		  |--------------|\
	p---->| e_0          | \
      	  |--------------|  s
      	  | ...          |  i
      	  |--------------|  z
      	  | e_{length-1} | /e
      	  ----------------/

###Play In Runtime

Since the evaluation order of the argument list, when using `clang`, need add the compile options **`__CLANG__`**
	
	tiger-comp/runtime$ clang test/Test_GC3.java.c runtime.c -D__CLANG__

###command-line

	-heapSize <n>            set the Java heap size (in bytes)
	-verbose {0|1|2|3}       trace method execuated
   	-gcLog                   generate GClog
   	-help                    help


##Caveats & Limitations

Too many...


