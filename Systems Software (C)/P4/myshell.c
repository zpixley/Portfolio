/*
 *  Zachary Pixley
 *  zap15
 *  cs449 Project 4
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/times.h>
#include <errno.h>

#define TRUE 0
#define FALSE 1
#define TPS sysconf(_SC_CLK_TCK)

//#define DEBUG

int main() {
    char input_string[500];
    const char delim[] = " \t\n()<>|&;";
    int i;

    do {
        FILE *fp;
        int temp_stdout = dup(1);
        int output_redirect = 0;
        int redirect = FALSE;
        int checking_time = FALSE;
        char *input = (char *)malloc(500);
        char *args[20];
        for (i = 0; i < 20; i++) {
            args[i] = (char *)malloc(25);
        }
        char *token;
        int num_args = 0;
        int status;
        static struct tms start;
        static struct tms end;
        double user_time;
        double sys_time;
        
        printf("myshell $ ");
        fgets(input, 500, stdin);
        strcpy(input_string, input);
        input_string[strlen(input_string) - 1] = '\0';
        
        for (i = 0; i < strlen(input_string); i++) {
            if (input_string[i] == '>') {
                redirect = TRUE;
                output_redirect++;
            }
        }
        
        //  Tokenize input
        token = strtok(input, delim);
        
        while (token != NULL) {
            strcpy(args[num_args], token);
            num_args++;
            token = strtok(NULL, delim);
        }

        //  Exit
        if (strcmp(args[0], "exit") == 0) {
            exit(EXIT_SUCCESS);
        }
        
        //  Time
        if (strcmp(args[0], "time") == 0) {
            checking_time = TRUE;
            //  remove time argument
            for (i = 0; i < num_args - 1; i++) {
                strcpy(args[i], args[i + 1]);
            }
            num_args--;

            if (times(&start) == -1) {
                printf("%s\n", strerror(errno));
            }
        }
        
        //  Change directory
        if (strcmp(args[0], "cd") == 0) {
            if (num_args == 1 || strcmp(args[1], "~") == 0) {
                chdir(getenv("HOME"));
            }
            else if (chdir(args[1]) == -1) {
                printf("%s\n", strerror(errno));
            }
        }
        
        //  Unix command
        else {
            //  IO Redirection
            if (redirect == TRUE) {
                if (output_redirect == 1) {
                    fp = freopen(args[num_args - 1], "w", stdout);
                }
                else if (output_redirect == 2) {
                    fp = freopen(args[num_args - 1], "a", stdout);
                }
                num_args--;
            }
            
            args[num_args] = NULL;
            
            if (fork() == 0) {
                execvp(args[0], args);
                printf("%s\n", strerror(errno));
            }
            else {
                wait(&status);
                if (redirect == TRUE) {
                    fclose(fp);
                    freopen("/dev/tty", "a", stdout);
                }
            }
        }
        
        if (checking_time == TRUE) {
            if (times(&end) == -1) {
                printf("%s\n", strerror(errno));
            }
            
            user_time = (end.tms_cutime - start.tms_cutime) / (double)TPS;
            sys_time = (end.tms_cstime - start.tms_cstime) / (double)TPS;
            
            printf("User:\t%fs\n", user_time);
            printf("System:\t%fs\n", sys_time);
        }
        
        free(input);
        for (i = 0; i < 10; i++) {
            free(args[i]);
        }
    } while (1);

    return 0;
}
