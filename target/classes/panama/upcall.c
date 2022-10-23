#include <stdio.h>

// gcc -fPIC -shared -o ~/mylib.so upcall.c
void callback_function(void (*ptrToFunction)()) {

   puts("Foreign\t start of callback_function()");

   // invoke the function via the pointer passed as parameter
   (*ptrToFunction)();

   puts("Foreign\t end of callback_function()");

}