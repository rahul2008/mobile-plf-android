#include "client_sha256.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "fileio.h"
#include "ps_hmac.h"

/* A client that uses precomputed state to compute an hmac **/

int main ( int argc , char *argv[] )
{
  if (argc != 3)
    {
      printf("client <keyfile> <inputfile>\n");
      return 1;
    }
  unsigned char *keys;
  int keys_len;

  readfile(argv[1],&keys,&keys_len);
  if (keys_len != 64)
    {
      printf("Not enough bytes in key\n");
      return 1;
    }

  unsigned char *message;
  int message_len;
  if (readfile(argv[2],&message,&message_len))
    {
      printf("Could not read file: %s\n", argv[2]);
      return -1;
    }

  unsigned char *result = malloc(32);
  
  if (ps_hmac(keys,message,message_len,result))
    {
      printf("Unable to compute hmac\n");
      return 1;
    }
  
  for (int i = 0 ; i < 32; i ++)
    {
      printf("%02x",result[i]);
    }
  printf("\n");

}
