/**
 *	Zachary Pixley
 *	PS: 4039308
 *	Project 1: Image Transformations
 */

#pragma pack(1)
#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
	FILE *in_file;
	char option[100];
	int inv = 0;
	int gre = 0;
	int pad;
	int col;
	int row;
	int i;
	int j;
	struct bmp_header {
		char id[2];
		int file_size:32;
		int reserve1:16;
		int reserve2:16;
		int offset:32;
	} header_1;
	struct dib_header {
		int header_size:32;
		int width:32;
		int height:32;
		int color_planes:16;
		int bits_per_pixel:16;
		int scheme:32;
		int image_size:32;
		int h_res:32;
		int v_res:32;
		int num_colors:32;
		int num_important:32;
	} header_2;
	struct pixel {
		unsigned int blue:8;
		unsigned int green:8;
		unsigned int red:8;
	};
	struct pixel old;
	struct pixel new;
	
	/* Invert or greyscale */
	if (strcmp(argv[1],"-invert") == 0) {
		inv = 1;
		gre = 0;
	}
	else if (strcmp(argv[1],"-greyscale") == 0) {
		inv = 0;
		gre = 1;
	}

	/* Open file */
	in_file = fopen(argv[2],"r+b");
	/* Read headers and validate filetype */
	fread((char *)&header_1,1,sizeof(header_1),in_file);
	if (!(header_1.id[0] == 'B' && header_1.id[1] == 'M')) {
		printf("This file format is not supported (invalid format identifier).\n");
		return 0;
	}
	
	fread((char *)&header_2,1,sizeof(header_2),in_file);

	if (header_2.header_size != 40) {
		printf("This file format is not supported (invalid DIB header size).\n");
		return 0;
	}

	if (header_2.bits_per_pixel != 24) {		
		printf("This file format is not supported (must be 24-bit .bmp file).\n");
		return 0;
	}
	
	/* Find # of padded bytes in each row */
	if (header_2.width % 4 != 0) {
		pad = ((header_2.width * 3) / 4) + 1 - (header_2.width * 3);
	}
	/* File is valid, jump to pixel array */
	fseek(in_file,header_1.offset,SEEK_SET);
	for (i = 0; i < header_2.height; i++) {
		for (j = 0; j < header_2.width; j++) {
			fread((char *)&old,1,sizeof(old),in_file);
			fseek(in_file,-3,SEEK_CUR);
			/* Alter pixel */
			if (inv) {
				new.blue = ~(old.blue);
				new.green = ~(old.green);
				new.red = ~(old.red);
			}
			else if (gre) {

			}
			fwrite(&new,sizeof(new),1,in_file);
		}
		fseek(in_file,pad,SEEK_CUR);
	}
	
	fclose(in_file);
	
	return 0;
}
