#ifndef ERROR_H
#define ERROR_H

#include <stdio.h>
#include <stdlib.h>

#define ERROR(s)                    \
    do {                            \
        fprintf(stderr,             \
                    "\e[31m\e[1m%s\e[0m >line: %d at %s\n",       \
                    s,              \
                    __LINE__,       \
                    __FILE__        \
               );                   \
        fflush(stderr);             \
        exit(1);                    \
    }while (0)




#endif
