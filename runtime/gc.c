/*^_^*--------------------------------------------------------------*//*{{{*/
/* Copyright (C) SSE-USTC, 2014-2015                                */
/*                                                                  */
/*  FILE NAME             :  gc.c                                   */
/*  PRINCIPAL AUTHOR      :  qc1iu                                  */
/*  LANGUAGE              :  C                                      */
/*  TARGET ENVIRONMENT    :  ANY                                    */
/*  DATE OF FIRST RELEASE :  2014/10/05                             */
/*  DESCRIPTION           :  the tiger compiler 'gc                 */
/*------------------------------------------------------------------*/

/*
 * Revision log:
 * ---------------------
 * 2014/12/06
 * 1>add Exchange()
 * 2>add RewriteObj()
 * --------------------
 *
 * 2014/12/08
 * 1>add the copyCount
 *
 *
 */
/*}}}*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "assert.h"
#include "error.h"

#define O Object_t
#define F Frame_t
#define V Vtable_t

#define __CLANG__

#ifdef __CLANG__
#define GET_STACK_ARG_ADDRESS(base, index) (((char*)base)-(index)*sizeof(void*))
#else
#define GET_STACK_ARG_ADDRESS(base, index) (((char*)base)+(index)*sizeof(void*))
#endif

#define CAST_ADDR(p)        ((int*)(p))
#define GET_LOCAL_BASE_ADDR(frame)  (frame+1)
#define GET_LOCAL_ADDRESS(base, index)  (((char*)base)+(index)*WORLD)

#define GET_OBJECT_BASE_FIELD(obj)  (obj+1)
#define GET_OBJECT_FIELD_ADDR(obj, index)    ((char*)(obj+1)+index*(sizeof(int)))

#define GET_OBJECT(addr)    (*(O*)addr)

#define ADDRESS_COMPARE(add1, op, add2)     ((int*)(add1)op(int*)(add2))

typedef enum
{
    TYPE_OBJECT,
    TYPE_ARRAY
}OBJECT_TYPE;

typedef struct O *O;
typedef struct F *F;
typedef struct V *V;


struct V
{
    char* class_map;
};

struct O
{
    V vptr;
    OBJECT_TYPE isArray;
    int length;
    void* forwarding;
};


struct F
{
    F prev;
    char* arguments_gc_map;
    int* arguments_base_address;
    int locals_gc_map;
};




void Tiger_gc ();
extern int Log;
static int copyCount=0;//用于记录copy了几个对象
static int gcNum;
clock_t start,end;
float sec;
extern char* logname;

static const int WORLD = sizeof(int);

static const int OBJECT_HEADER_SIZE = sizeof(struct O);

static const int FRAME_HEADER_SIZE = sizeof(struct F);


// The Gimple Garbage Collector.


//===============================================================//
// The Java Heap data structure.

/*
   ----------------------------------------------------
   |                        |                         |
   ----------------------------------------------------
   ^\                      /^
   | \<~~~~~~~ size ~~~~~>/ |
   from                       to
   */
struct JavaHeap
{
    int size;         // in bytes, note that this is for semi-heap size
    char *from;       // the "from" space pointer
    char *fromFree;   // the next "free" space in the from space
    char *to;         // the "to" space pointer
    char *toStart;    // "start" address in the "to" space
    char *toNext;     // "next" free space pointer in the to space
};

// The Java heap, which is initialized by the following
// "heap_init" function.
struct JavaHeap heap;

// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init (int heapSize)
{
    // #1: allocate a chunk of memory of size "heapSize"
    char* jheap=(char*)malloc(heapSize);

    // #2: initialize the "size" field, note that "size" field
    // is for semi-heap, but "heapSize" is for the whole heap.
    heap.size=heapSize/2;
    // #3: initialize the "from" field.
    heap.from=(char*)jheap;
    // #4: initialize the "fromFree" field.
    heap.fromFree=heap.from;
    // #5: initialize the "to" field.
    heap.to=heap.fromFree+heap.size;
    // #6: initizlize the "toStart" field. 
    heap.toNext=(char*)heap.to+1;
    // #7: initialize the "toNext" field.
    heap.toStart=(char*)heap.to+1;

    return;
}

// The "prev" pointer, pointing to the top frame on the GC stack.
// (see part A of Lab 4)
void *previous = 0;//Sum。java.c extern void* previous;




//===============================================================//
// Object Model And allocation


// "new" a new object, do necessary initializations, and
// return the pointer (reference).
/*    ----------------
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
      */
// Try to allocate an object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1;
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
static void* newObject(void* vtable, int size)
{
    O obj;

    obj  = (O)heap.fromFree;
    memset(obj, 0,  size);
    obj->vptr = vtable;
    obj->isArray = TYPE_OBJECT;
    obj->length = size;
    obj->forwarding = 0;

    heap.fromFree+=size;
    printf("new object: %d\n", size);
    return obj;
}

static void* newArray(int length)
{
    O obj;

    obj = (O)heap.fromFree;
    memset(obj ,0,length*sizeof(int));
    heap.fromFree+=(length*sizeof(int));
    obj->vptr = NULL;
    obj->isArray = TYPE_ARRAY;
    obj->length = length;
    obj->forwarding = 0;
    heap.fromFree += (length*sizeof(int));

    printf("new array: %d\n", length*sizeof(int));

    return obj;
}

void *Tiger_new (void *vtable, int size)
{
    if(heap.to-heap.fromFree<size)
    {
        int before_gc = heap.to-heap.fromFree;
        start = clock();
        Tiger_gc();
        end = clock();
        sec =  (double)(end-start)/CLOCKS_PER_SEC;
        int gcByte = heap.to-heap.fromFree-before_gc;
        gcNum++;

        if(Log)
        {
            FILE *fp;

            fp = fopen(logname, "at");
            if (!fp)
            {
                printf("File cannot be opened");
                exit(1);
            }
            fprintf(fp, "%d round of gc:%fs.%d byte reclaim\n",gcNum,sec,gcByte);
            fclose(fp);
        }

        if(heap.to-heap.fromFree<size)
        {
            printf("Tiger_gc can not collecte enough space...\n");
            printf("There is %d byte remained,but you need:%d\n", 
                        (int)(heap.to-heap.fromFree),size);
            exit(1);
        }
    }

    O obj = newObject(vtable, size);

    return obj;
}

// "new" an array of size "length", do necessary
// initializations. And each array comes with an
// extra "header" storing the array length and other information.
/*    ----------------
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
      */
// Try to allocate an array object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this array object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1;
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
void *Tiger_new_array (int length)
{
    if(heap.to-heap.fromFree<(length*sizeof(int))+16)
    {
        int before_gc =heap.to-heap.fromFree;
        start = clock();
        Tiger_gc();
        end = clock();
        int gcByte = heap.to-heap.fromFree-before_gc;
        sec =  (double)(end-start)/CLOCKS_PER_SEC;
        gcNum++;

        if(Log)
        {
            FILE *fp;
            fp = fopen(logname, "at");
            if (!fp)
            {
                printf("File cannot be opened");
                exit(1);
            }
            fprintf(fp,"%d round of gc :%fs.%d byte reclaim\n",gcNum,sec,gcByte);
            fclose(fp);
        }


        if(heap.to-heap.fromFree<(length*sizeof(int))+16)
        {
            exit(1);
        }
    }
    O array = newArray(length);

    return (array+1);
}



//===============================================================//
// The Gimple Garbage Collector

// A copying collector based-on Cheney's algorithm.
void Exchange()
{
    char* swap=heap.from;
    heap.from = heap.toStart;
    heap.to = (char*)heap.from+heap.size;

    heap.fromFree = heap.toNext;
    heap.toStart=swap;
    heap.toNext=swap;
}


static int objectSize(O obj)
{
    int size;

    switch (obj->isArray)
    {
        case TYPE_OBJECT:
            size = obj->length+OBJECT_HEADER_SIZE;
            break;
        case TYPE_ARRAY:
            size = obj->length*sizeof(int)+OBJECT_HEADER_SIZE;
            break;
        default:
            ERROR("wrong type");
    }

    return size;
}


static void copyCollection(int** addr_addr)
{
    O old_obj;
    O new_obj;
    int* obj_addr;

    obj_addr = *addr_addr;
    old_obj = GET_OBJECT(obj_addr);



    if (ADDRESS_COMPARE(old_obj, <, heap.from+heap.size) && 
                ADDRESS_COMPARE(old_obj, >=, heap.from))
    {
        void* forwarding = old_obj->forwarding;
        if (ADDRESS_COMPARE(forwarding, <, heap.to+heap.size) && 
                    ADDRESS_COMPARE(forwarding, >=, heap.to))
        {
            *addr_addr =(int*)forwarding;

            return;
        }
        else if ((ADDRESS_COMPARE(forwarding, <, heap.from+heap.size)&&
                        ADDRESS_COMPARE(forwarding, >=, heap.from))||
                    ADDRESS_COMPARE(forwarding, ==, 0))
        {

            copyCount++;
            new_obj = (O)heap.toNext;
            old_obj->forwarding = new_obj;

            int size = objectSize(old_obj);
            memcpy(new_obj, old_obj, size);
            heap.toNext+=size;

            return;
        }
        else
        {
            return;
        }
    }
    else
    {
        ;
    }
    return;
}

static void collectedField()
{
    char* toStart_temp;

    toStart_temp = heap.toStart;
    while(copyCount > 0)
    {
        O obj = (O)toStart_temp;

        switch (obj->isArray)
        {
            case TYPE_OBJECT:
                {
                    V vtable;
                    char* class_map;
                    int field_count;

                    vtable = obj->vptr;
                    class_map = vtable->class_map;
                    Assert_ASSERT(class_map);
                    field_count = strlen(class_map);
                    if (field_count > 0)
                    {
                        int i=0;
                        for (i=0; i<field_count; i++)
                        {
                            if (class_map[i] == '0')
                              continue;

                            int* class_field_addr = CAST_ADDR(GET_OBJECT_FIELD_ADDR(obj, i));
                            copyCollection((int**)GET_OBJECT_FIELD_ADDR(obj, i));
                        }
                    }
                    break;
                }
            case TYPE_ARRAY:
                {
                    break;
                }
            default:
                ERROR("impossible");
        }

        toStart_temp=(char*)toStart_temp+objectSize(obj);
        copyCount--;
    }
}

static void doArg(F frame)
{
    int len;
    char* arg_map;

    arg_map = frame->arguments_gc_map;

    if (arg_map == NULL)
      return;

    len = strlen(arg_map);
    int i = 0;
    for (i=0; i<len; i++)
    {
        if (arg_map[i] == '0')
          continue;

        int* arg_addr = GET_STACK_ARG_ADDRESS(frame->arguments_base_address, i);
        copyCollection((int**)arg_addr);
    }
}

static void doLocals(F frame)
{
    int locals_map = frame->locals_gc_map;
    Assert_ASSERT(locals_map>=0);

    int* local_base_addr;

    if (locals_map == 0)
      return;

    local_base_addr = CAST_ADDR(GET_LOCAL_BASE_ADDR(frame));
    int i=0;
    for (i=0; i<locals_map; i++)
    {
        int* local_addr = CAST_ADDR(GET_LOCAL_ADDRESS(local_base_addr, i));
        copyCollection((int**)GET_LOCAL_ADDRESS(local_base_addr, i));
    }
}

static void frameSingle(F frame)
{
    if (frame == NULL)
      return;

    doArg(frame);

    doLocals(frame);

    frameSingle(frame->prev);
}


void Tiger_gc ()
{
    printf("Tiger_gc start!\n");

    Assert_ASSERT(copyCount == 0);

    frameSingle(previous);

    collectedField();

    Exchange();

    return;
}


#undef O
#undef F
#undef V
