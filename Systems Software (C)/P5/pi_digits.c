/*
 * Zachary Pixley
 * zap15
 * CS1501 Project 5
 */

#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>

int main(int argc, char *argv[]) {
    int start = atoi(argv[1]);                  // start index of pi digits
    int end = atoi(argv[2]);                    // end index of pi digits
    int count = end - start + 1;                // number of digits being read (add 1 since 0-indexed)
    char *buffer = (char *)malloc(count + 1);   // buffer to hold values read form pi
    
    // Pi driver device is opened as read only
    int pi_file = open("/dev/pi", O_RDONLY);
    
    // move file pointer to the starting pi digit
    lseek(pi_file, start, SEEK_SET);
    
    // use pi device read function to get substring of digits
    read(pi_file, buffer, count);
    
    printf("%s\n", buffer);
    
    close(pi_file);
    
    return 0;
}