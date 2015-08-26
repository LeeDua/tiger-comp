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

#define __CLANG__

#ifdef __CLANG__
#define GET_STACK_ARG_ADDRESS(base, index) (((char*)base)-(index)*sizeof(void*))
#else
#define GET_STACK_ARG_ADDRESS(base, index) (((char*)base)+(index)*sizeof(void*))
#endif

#define CAST_ADDR(p)        ((int*)(p))
#define GET_ARG_MAP(frame)      ((char*)*((int*)((char*)frame+WORLD)))
#define GET_BASE_ADDR(frame)    CAST_ADDR((*(int*)((char*)frame+WORLD*2)))
#define GET_LOCAL_MAP(frame)    (*(int*)((char*)frame+WORLD*3))
#define GET_LOCAL_BASE_ADDR(frame)  CAST_ADDR(((int*)((char*)frame+WORLD*4)))
#define GET_LOCAL_ADDRESS(base, index)  CAST_ADDR((((char*)base)+(index)*WORLD))

#define GET_OBJECT_BASE_FIELD(obj)  CAST_ADDR(obj+1)
#define GET_OBJECT_FIELD(obj, index)    CAST_ADDR(((char*)(obj+1)+index*(sizeof(int))))

#define GET_ARG(arg_addr)   CAST_ADDR((*(int*)arg_addr))
#define GET_OBJECT(addr)    (*(int*)addr)
#define GET_PREV_FRAME(frame)   (frame = (char*)GET_ARG(frame))


typedef enum
{
    TYPE_OBJECT,
    TYPE_ARRAY
}OBJECT_TYPE;

typedef struct O *O;
typedef struct F *F;

struct O
{
    void* vptr;
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

// Lab 4, exercise 10:
// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init (int heapSize)
{
    // You should write 7 statement here:
    // #1: allocate a chunk of memory of size "heapSize" using "malloc"
    char* jheap=(char*)malloc(heapSize);

    // #2: initialize the "size" field, note that "size" field
    // is for semi-heap, but "heapSize" is for the whole heap.
    heap.size=heapSize/2;
    // #3: initialize the "from" field (with what value?)
    heap.from=(char*)jheap;
    // #4: initialize the "fromFree" field (with what value?)
    heap.fromFree=heap.from;
    // #5: initialize the "to" field (with what value?)
    heap.to=heap.fromFree+heap.size;
    // #6: initizlize the "toStart" field with NULL;
    heap.toNext=(char*)heap.to+1;
    // #7: initialize the "toNext" field with NULL;
    heap.toStart=(char*)heap.to+1;

    return;
}

// The "prev" pointer, pointing to the top frame on the GC stack.
// (see part A of Lab 4)
void *previous = 0;//Sum。java.c extern void* previous;




//===============================================================//
// Object Model And allocation


// Lab 4: exercise 11:
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

        //printf("There is %d byte remained,but you need:%d\n",heap.to-heap.fromFree,size);
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
                        (int*)(heap.to-heap.fromFree),size);
            exit(1);
        }
    }
    //printf("\nthis is Tiger_new--------------\n");
    //printf("malloc size:%d\n",size);
    O obj = newObject(vtable, size);
    //printf("vtable is=%d\n",*(int*)(temp));
    //printf("isObj=%d,address=:%d\n",*(temp+4),temp+4);
    //printf("length=%d,address=:%d\n",*(temp+8),(temp+8));
    //printf("forward=%d,address=:%d\n",*(temp+12),(temp+12));
    //printf("malloc finished....------------------\n");
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
        //printf("There is %d byte remained,but you need:%d\n",
        //heap.to-heap.fromFree,length*(sizeof(int))+16);

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
            //printf("Tiger_gc can not collecte enough space...\n");
            //printf("There is %d byte remained,but you need:%d\n",
            //(int*)(heap.to-heap.fromFree),length*(sizeof(int))+16);
            exit(1);
        }
    }
    //printf("\nthis is Tiger_new_array-----------------------\n");
    //printf("malloc size:%d\n",length*(sizeof(int))+16);
    O array = newArray(length);

    //printf("isObj=%d,address=:%d\n",*(temp+4),temp+4);
    //printf("length=%d,address=:%d\n",*(temp+8),(temp+8));
    //printf("forward=%d,address=:%d\n",*(temp+12),(temp+12));
    //printf("malloc finished....-------------------------\n");
    return (array+1);
}







//===============================================================//
// The Gimple Garbage Collector

// Lab 4, exercise 12:
// A copying collector based-on Cheney's algorithm.
void Exchange()
{
    char* swap=heap.from;
    heap.from = heap.toStart;
    heap.to = (char*)heap.from+heap.size;

    heap.fromFree = heap.toNext;
    heap.toStart=swap;
    heap.toNext=swap;
    //printf("\nTiger_gc finished!!!---------------------new  heap info-----------\n");
    //printf("heap.from is:0x%d\n",(int*)heap.from);
    //printf("heap.to is:0x%d\n",(int*)heap.to);
    //printf("heap.fromFree is:0x%d\n",(int*)heap.fromFree);
    //printf("heap.toStart is:0x%d\n",(int*)heap.toStart);
    //printf("heap.toNext is:0x%d\n",(int*)heap.toNext);
    //printf("\n");
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

/*
int calculateSize(void* temp)
{
    int size=0;
    int* objAdd=*(int*)temp;
    //printf("\ncalculateSize -----------------------\n");
    //printf("\n objorarray add is:0x%d\n",(char*)objAdd+4);
    int isArray=*((char*)objAdd+4);
    //printf("isAarray is:%d\n",isArray);
    if(isArray)
    {
        //Array
        size=(*((char*)objAdd+8))*sizeof(int)+16;
        //printf("return size is:%d\n",size);
    }
    else
    {
        //Obj
        int len=0;
        int* vtable=*(int*)objAdd;
        char* classMap=*vtable;
        //printf("----------------%s\n",classMap);
        len=strlen(classMap);
        //printf("class arguments is:%d\n",len);
        size=len*4+16;
        //printf("return size is:%d\n",size);
    }
    //printf("calculateSize finished....--------------------------\n");
    return size;
}

*/


void* Copy(void *obj_addr)
{

    O old_obj;
    O new_obj;

    old_obj = (O)GET_OBJECT(obj_addr);
    new_obj = obj_addr;
    //printf("copy start---------------\n");
    //printf("heap to:%d\n",heap.from+heap.size);
    //printf("heap from:%d\n",heap.from);


    if(((old_obj)<(heap.from+heap.size))&&(((old_obj)>=heap.from)))
    {//当目的地址在from区间里
        void* forwarding = old_obj->forwarding;
        //printf("forwarding is:%d\n",*(int*)forwarding);
        if((((int*)forwarding)<(heap.to+heap.size))&&((((int*)forwarding)>=heap.to)))
        {//forwarding在to区间里。说明已经copy。
            //printf("forwarding is already in tospace!!!!!!!!!!!!!!!!:%d\n",*(int*)forwarding);
            return forwarding;

        }
        else if(((((int*)forwarding)<(heap.from+heap.size))&&((((int*)forwarding)>=heap.from)))
                    ||((int*)forwarding)==0)
        {//forwarding在from区间里面或者forwarding为0

            //在这里才是真正的copy！！！
            copyCount++;
            //printf("forwarding is in from or forwarding is 0\n");
            //printf("forwarding is :%d\n",*(int*)forwarding);
            //因为没有给forwarding赋值，所以先用char*好输出0，不然是一大长串数。
            //printf("heap.from+heap.size=0x%d ",heap.from+heap.size);
            //printf("heap.from=0x%d   \n",heap.from);
            new_obj = (O)heap.toNext;
            old_obj->forwarding = new_obj;

            //printf("Copy!!!!\n");
            //得到size。有obj的size与array的size两种情况。
            int size = objectSize(old_obj);
            //int size=calculateSize(temp);
            //开始copy
            //按字节copy
            memcpy(new_obj, old_obj, size);
            heap.toNext+=size;

            return new_obj;
        }
        //copy finished...
        else
        {
            //printf("obj not exit!!!\n");
            return 0;
        }
    }
    else
    {
        //printf("no need copy!!!!!!!!!!!!!!!\n");
    }
    return obj_addr;
}

static void RewriteObj()
{
    char* toStart_temp = heap.toStart;
    O obj = (O)heap.toStart;
    while(copyCount>0)
    {
        //int* obj=(int*)toStart_temp;
        O obj = (O)toStart_temp;
        //判断对象是什么类型。
        //int isObj=(int)*((char*)obj+4);
        OBJECT_TYPE isObj = obj->isArray;
        int size = obj->length;

        switch (obj->isArray)
        {
            case TYPE_OBJECT:
                break;
            case TYPE_ARRAY:
                break;
            default:
                ERROR("impossible");
        }


        if(isObj==1)//is Array
        {
            //printf("in toSpace is an Array\n");
            toStart_temp=(char*)toStart_temp+size;
            copyCount--;
        }
        else
        {
            //printf("in toSpace is a Obj\n");
            //是Obj的话需要处理一下。
            void* vptr_arg=*(int*)toStart_temp;
            char* class_gcMap=*(int*)vptr_arg;
            //printf("map is :%s\n",(char*)class_gcMap);
            int classLocalCount=strlen(class_gcMap);
            if(classLocalCount>0)
            {
                //int* localAddress=(int*)((char*)toStart_temp+OBJECT_HEADER_SIZE);
                //int* localAddress = GET_LOCAL_ADDRESS(toStart_temp);
                int i=0;
                for(i=0;i<classLocalCount;i++)
                {
                    if(class_gcMap[i]=='1')
                    {
                        int* class_field = GET_OBJECT_FIELD(obj, i);
                        Copy(class_field);
                    }
                    //localAddress=(char*)localAddress+4;
                }
            }
            toStart_temp=(char*)toStart_temp+objectSize(obj);
            copyCount--;
        }
    }
}

static void doArg(void* frame, char* arg_map, int* arg_base_addr)
{
    if (arg_map == NULL)
      return;

    int len;

    len = strlen(arg_map);
    int i = 0;
    for (i=0; i<len; i++)
    {
        if (arg_map[i] == '0')
          continue;

        int* arg_addr = GET_STACK_ARG_ADDRESS(arg_base_addr, i);
        void* collected_arg = GET_ARG(arg_addr);
        Copy(collected_arg);
    }
}

static void doLocals(void* frame, int locals_map)
{
    Assert_ASSERT(locals_map>=0);

    int* local_base_addr;

    if (locals_map == 0)
      return;

    local_base_addr = GET_LOCAL_BASE_ADDR(frame);
    int i=0;
    for (i=0; i<locals_map; i++)
    {
        int* local_addr = GET_LOCAL_ADDRESS(local_base_addr, i);
        Copy(local_addr);
    }
}

static void frameSingle(F frame)
{
    if (frame == NULL)
      return;

    char* arg_map;
    int* arg_base_addr;
    int locals_map;

    arg_map = GET_ARG_MAP(frame);
    //arg_map = frame->arguments_gc_map;
    arg_base_addr = GET_BASE_ADDR(frame);
    //arg_base_addr = frame->arguments_base_address;
    locals_map = GET_LOCAL_MAP(frame);
    //locals_map = frame->locals_gc_map;

    doArg(frame, arg_map, arg_base_addr);

    doLocals(frame, locals_map);

    GET_PREV_FRAME(frame);

    frameSingle(frame);
}


void Tiger_gc ()
{
    printf("Tiger_gc start!\n");

    copyCount = 0;

    frameSingle(previous);
    
    RewriteObj();

    Exchange();

    return;
}


#undef O
