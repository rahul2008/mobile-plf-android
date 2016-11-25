#ifndef FILEIO_H
#define FILEIO_H

#define ERROR_CANNOT_OPEN_FILE 1


/*
   return 0 on success, other on failure
   read all bytes from file <name> and store in *data. len will hold
   number of bytes

   We assume sizeof(unsigned char) == 1
   this is the case in c99

*/

int readfile ( char *name , unsigned char ** data, int *len );

/* return 0 on success, other on failure
 */

int writefile( char *name, unsigned char *data , int len);



#endif
