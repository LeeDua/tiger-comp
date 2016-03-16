#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern void Tiger_main();
extern void Tiger_heap_init (int);
extern void CommandLine_doarg (int argc, char **argv);
extern void dump_status();
int main (int argc, char **argv)
{
  CommandLine_doarg(argc,argv);
  Tiger_heap_init (Control_heapSize);
  // enter Java code...
  Tiger_main ();
  if (Log) {
    dump_status();
  }
}
