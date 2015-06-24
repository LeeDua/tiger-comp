Tiger
=====

The Tiger compiler. Copyright (C) 2013-2015, SSE of USTC.

Usage: java Tiger [options] <filename>


**Available options:**

1. -codegen {bytecode|C|dalvik|x86} which code generator to use
2. -dump {ast}                      dump information about the given ir
- -elab <arg>                      dump information about elaboration
- -help                            show this help information
- -lex                             dump the result of lexical analysis
- -slp {args|interp|compile}       run the SLP interpreter
- -output <outfile>                set the name of the output file
- -testFac                         whether or not to test the Tiger compiler on Fac.java
- -testlexer                       whether or not to test the lexer
