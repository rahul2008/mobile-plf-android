#include "fileio.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
   We assume sizeof(unsigned char) == 1, which indicates 1 byte
   this is the case in c99. 

   These functions have to be revisited when we deal with a non-C
   compliant compiler.

*/

int readfile (char *name, unsigned char **data, int *len)
{
  FILE *f = fopen(name,"r");
  if (f == 0)
    {
      return ERROR_CANNOT_OPEN_FILE;
    }

  fseek(f,0,SEEK_END);
  int size = ftell(f);
  fseek(f,0,SEEK_SET);

  unsigned char *buffer = (unsigned char *) malloc(8192);
  unsigned char *message = (unsigned char *) malloc(size);
  
  int current = 0;

  while (current < size)
    {
      size_t count = fread(buffer,1,8192,f);
      memcpy(message+current,buffer,count);
      current = current + count;
    }
  free(buffer);
  *data =message;
  *len = size;
  fclose(f);
  return 0;
}

int writefile(char *name, unsigned char *data, int len)
{
 FILE *f = fopen(name,"w");
  if (!f) {
    return ERROR_CANNOT_OPEN_FILE;
  }
  fwrite(data,1,len,f);
  fclose(f);
  return 0;
}
