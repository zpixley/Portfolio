/**
 *	Zachary Pixley
 *	PS: 4039308
 *	Project 1: RPS
 */

#include <stdio.h>
#include <string.h>

int main() {
	/* Declare variables */
	char answer[10];
	char player_choice[10];
	int cpu_choice;
	int player_score = 0;
	int cpu_score = 0;
	char cpu_rock[100] = "The computer chooses Rock. ";
	char cpu_paper[100] = "The computer chooses Paper. ";
	char cpu_scissors[100] = "The computer chooses Scissors. ";
	char win[100] = "You win this game!\n\n";
	char lose[100] = "You lose this game!\n\n";
	char tie[100] = "You tied!\n\n";
	srand((unsigned int)time(NULL));
	printf("Welcome to Rock, Paper, Scissors\n\nWould you like to play? ");
	fgets(answer, sizeof(answer), stdin);
	answer[strlen(answer)-1] = '\0';
	if (strcmp(answer, "yes") == 0) {
		/* Main loop of program */
		while (strcmp(answer, "yes") == 0) {
			player_score = 0;
			cpu_score = 0;
			/* Best of 5 loop (breaks if there is a winner) */
			while (1) {
				/* Get player choice */
				printf("\nWhat is your choice? ");
				fgets(player_choice, sizeof(player_choice), stdin);
				player_choice[strlen(player_choice)-1] = '\0';
				/* Get computer choice (0=>rock,1=>paper,2=>scissors) */
				cpu_choice = rand() % 3;
				/* Determine result */
				if (strcmp(player_choice, "rock") == 0) {
					
					if (cpu_choice == 0) {
						printf("%s", cpu_rock);
						printf("%s", tie);
						continue;
					}
					else if (cpu_choice == 1) {
						printf("%s", cpu_paper);
						printf("%s", lose);
						cpu_score++;
					}
					else if (cpu_choice == 2) {
						printf("%s", cpu_scissors);
						printf("%s", win);
						player_score++;
					}
				}
				else if (strcmp(player_choice, "paper") == 0) {
					if (cpu_choice == 0) {		
						printf("%s", cpu_rock);
						printf("%s", win);
						player_score++;
					}
					else if (cpu_choice == 1) {
						printf("%s", cpu_paper);
						printf("%s", tie);
						continue;
					}
					else if (cpu_choice == 2) {
						printf("%s", cpu_scissors);
						printf("%s", lose);
						cpu_score++;
					}
				}
				else if (strcmp(player_choice, "scissors") == 0) {	
					if (cpu_choice == 0) {
						printf("%s", cpu_rock);
						printf("%s", lose);
						cpu_score++;
					}
					else if (cpu_choice == 1) {
						printf("%s", cpu_paper);
						printf("%s", win);
						player_score++;
					}
					else if (cpu_choice == 2) {	
						printf("%s", cpu_scissors);
						printf("%s", tie);
						continue;
					}
				}
				/* Print score */
				printf("The score is now you: %d computer: %d\n", player_score, cpu_score);
				/* Check for winner */
				if (player_score == 3) {
					printf("You are the winner!\n\n");
					break;
				}
				else if (cpu_score == 3) {
					printf("The computer won.\n\n");
					break;
				}
			}
			/* Check if the player wants another game */
			printf("Would you like to play again? ");
			fgets(answer, sizeof(answer), stdin);
			answer[strlen(answer)-1] = '\0';
		}
	}
	if (strcmp(answer, "no") == 0) {
		printf("\nThanks for playing!\n");
	}
	return 0;
}
