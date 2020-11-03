/**
*	Zachary Pixley
*	zap15
*	cs449 - Project 3
*/

#include <stdio.h>
#include <stdlib.h>
#include "mymalloc.h"

#define TRUE 1
#define FALSE 0

//#define DRIVER
//#define DEBUG

static Node *head = NULL;
static Node *tail = NULL;

void *my_worstfit_malloc(int size) {
	Node *new_node = NULL;
	Node *split_node = NULL;
	
	// heap is empty
	if (head == NULL) {
		#ifdef DEBUG
		printf("Creating first node at: %p\n", (void *) sbrk(0));
		#endif
		head = (Node *) sbrk(sizeof(Node) + size);
		head->size = size;
		head->free = FALSE;
		head->next = NULL;
		head->previous = NULL;
		tail = head;
		return (void *) head + sizeof(Node);
	}
	else {
		new_node = find_worst_fit(size);
		if (new_node == NULL) {
			#ifdef DEBUG
				printf("Appending node at: %p\n", (void *) sbrk(0));
			#endif
			new_node = append_node(size);
		}
		else {
			split_node = (Node *) (new_node + sizeof(Node) + size);
			split_node->size = new_node->size - sizeof(Node) - size;
			split_node->free = TRUE;
			split_node->next = new_node->next;
			split_node->next->previous = split_node;
			split_node->previous = new_node;
			
			new_node->size = size;
			new_node->free = FALSE;
			new_node->next = split_node;
		}
		
		return (void *) new_node + sizeof(Node);
	}
}

Node *find_worst_fit(int size) {
	int worst_size = -1;
	Node *worst_node = NULL;
	Node *curr_node = head;
	while (curr_node != NULL) {
		if (curr_node->free == TRUE && (curr_node->size - size > worst_size)) {
			worst_size = curr_node->size - size;
			worst_node = curr_node;
		}
		curr_node = curr_node->next;
	}

	if (worst_size == -1) {
		return NULL;
	}

	return worst_node;
}

Node *append_node(int size) {
	tail->next = (Node *) sbrk(sizeof(Node) + size);
	tail->next->size = size;
	tail->next->free = FALSE;
	tail->next->next = NULL;
	tail->next->previous = tail;
	tail = tail->next;
	return tail;
}

void my_free(void *ptr) {
	Node *free_node = (Node *) (ptr - sizeof(Node));
	#ifdef DEBUG
	printf("Freeing node at: %p\n", (void *) free_node);
	#endif
	Node *temp_node = NULL;

	if (free_node->free == TRUE) {
		#ifdef DEBUG
		printf("Node already free\n");
		#endif
		return;
	}
	
	// node to free is an interior node
	if (free_node != tail && free_node != head) {
		#ifdef DEBUG
		printf("Freeing interior node...\n");
		#endif
		
		if (free_node->next->free == TRUE) {
			#ifdef DEBUG
				printf("Merging with next...\n");
			#endif
			free_node = merge_next(free_node);
		}
		
		if (free_node->previous->free == TRUE) {
			#ifdef DEBUG
				printf("Merging with previous...\n");
			#endif
			free_node = free_node->previous;
			free_node = merge_next(free_node);
		}

		free_node->free = TRUE;

		return;
	}
	// freeing tail and shrinking heap
	else if (free_node == tail) {
		#ifdef DEBUG
			printf("Freeing tail...\n");
		#endif
		tail->free = TRUE;
		
		/*while (tail->free == TRUE) {
			if (tail == head) {
				#ifdef DEBUG
					printf("Freeing last node...\n");
				#endif
				sbrk(0 - sizeof(Node) - tail->size);
				head = NULL;
				tail = NULL;
				return;
			}
			tail = tail->previous;
			tail->next = NULL;
			sbrk(0 - sizeof(Node) - free_node->size);
		}*/
		
		if (tail == head) {
			#ifdef DEBUG
				printf("Freeing last node...\n");
			#endif
			sbrk(0 - sizeof(Node) - tail->size);
			head = NULL;
			tail = NULL;
			return;
		}
		
		if (tail->previous->free == TRUE) {
			free_node = free_node->previous;
			free_node = merge_next(free_node);
		}
		tail = free_node->previous;
		tail->next = NULL;
		sbrk(0 - sizeof(Node) - free_node->size);

		return;
	}
	// freeing head
	else if (free_node == head) {
		#ifdef DEBUG
			printf("Freeing head...\n");
		#endif
		if (head->next->free == TRUE) {
			merge_next(head);
		}
		head->free = TRUE;

		return;
	}
	
	#ifdef DEBUG
		printf("Free failed\n");
	#endif
	return;
}

Node *merge_next(Node *n) {
	Node *temp_node = n->next;
	n->size += sizeof(Node) + temp_node->size;
	n->next = temp_node->next;
	if (temp_node->next != NULL) {
		temp_node->next->previous = n;
	}
	return n;
}

#ifdef DRIVER
void print_list() {
	Node *curr_node = head;
	
	printf("brk at: %p\n", (void *) sbrk(0));
	
	while (curr_node != NULL) {
		if (curr_node->free == TRUE) {
			printf("free[%d]", curr_node->size);
		}
		else if (curr_node->free == FALSE) {
			printf("used[%d]", curr_node->size);
		}
		printf("-->");
		curr_node = curr_node->next;
	}
	printf("NULL\n");
	
	curr_node = tail;
	while (curr_node != NULL) {
		if (curr_node->free == TRUE) {
			printf("free[%d]", curr_node->size);
		}
		else {
			printf("used[%d]", curr_node->size);
		}
		if (curr_node->previous != NULL) printf("-->");
		curr_node = curr_node->previous;
	}
	printf("\n\n");
}

int main() {
	void *ptr1;
	void *ptr2;
	void *ptr3;
	void *ptr4;
	void *ptr5;
	void *ptr6;
	
	printf("Beginning...\n");
	print_list();
	
	printf("Adding 1st...\n");
	ptr1 = my_worstfit_malloc(2);
	printf("Address: %p\n", ptr1);
	print_list();
	
	printf("Adding 2nd...\n");
	ptr2 = my_worstfit_malloc(4);
	printf("Address: %p\n", ptr2);
	print_list();
	
	printf("Adding 3rd...\n");
	ptr3 = my_worstfit_malloc(6);
	printf("Address: %p\n", ptr3);
	print_list();
	
	printf("Adding 4th...\n");
	ptr4 = my_worstfit_malloc(8);
	printf("Address: %p\n", ptr4);
	print_list();
	
	printf("Adding 5th...\n");
	ptr5 = my_worstfit_malloc(10);
	printf("Address: %p\n", ptr5);
	print_list();
	
	printf("\nFreeing 2nd...\n");
	my_free(ptr2);
	print_list();
	
	printf("Freeing 3rd...\n");
	my_free(ptr3);
	print_list();
	
	printf("Freeing 1st...\n");
	my_free(ptr1);
	print_list();
	
	printf("\nAdding 6th...\n");
	ptr6 = my_worstfit_malloc(4);
	printf("Address: %p\n", ptr6);
	print_list();
	
	printf("\nFreeing 5th...\n");
	my_free(ptr5);
	print_list();
	
	printf("Freeing 4th...\n");
	my_free(ptr4);
	print_list();
	
	printf("Freeing 6th...\n");
	my_free(ptr6);
	print_list();
}
#endif
