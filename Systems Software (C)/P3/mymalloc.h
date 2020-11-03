/**
*	Zachary Pixley
*	zap15
*	cs449 - Project 3
*/

#ifndef MY_MALLOC_H
#define MY_MALLOC_H

typedef struct node {
	int size;
	int free;
	struct node *next;
	struct node *previous;
} Node;

void *my_worstfit_malloc(int size);

Node *find_worst_fit(int size);

Node *append_node(int size);

void my_free(void *ptr);

Node *merge_next(Node *ptr);
#endif
