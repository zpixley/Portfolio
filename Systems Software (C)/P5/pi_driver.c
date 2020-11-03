/*
 * Zachary Pixley
 * zap15@pitt.edu
 * pi_driver program
 * CS1501 Project 5
 *
 */

#include <linux/fs.h>
#include <linux/init.h>
#include <linux/miscdevice.h>
#include <linux/module.h>

#include <asm/uaccess.h>

#define SCALE 10000
#define ARRINIT 2000
#define MALLOC(s) kmalloc(s, GFP_KERNEL)
#define FREE(s) kfree(s)


void pi(char *buffer, int m)
{
    int i, j;
    int carry = 0;
    //it seems we need 14 'iterations' to get 4 digits correctly.
    int max = (m/4 ) * 14;
    int *arr = (int *)MALLOC(sizeof(int)*(max+15));
    strcpy(buffer,"");

    //Here there be dragons...
    //I have no clue how this algorithm works
    for (i = 0; i <= max; ++i)
        arr[i] = ARRINIT;
    for (i = max; i>0; i -= 14)
    {
        int sum = 0;
        char temp[5];

	for (j = i; j > 0; --j)
        {
            sum = sum*j + SCALE*arr[j];
            arr[j] = sum % (j*2-1);
            sum /= (j*2-1);
        }
        //we seem to generate 4 digits at a time.
        sprintf(temp, "%04d", carry + sum/SCALE);
        strcat(buffer, temp);
	carry = sum % SCALE;
    }
    FREE(arr);
}

/*
 * pi_read is the function called when a process calls read() on
 * /dev/pi.  It writes the digits of pi from *ppos to *ppos+count to the 
 * buffer passed in the read() call.
 */

static ssize_t pi_read(struct file * file, char * buf, 
			  size_t count, loff_t *ppos)
{
    //  Read digits from ppos to ppos+count
    int start = (int) *ppos;
    int end = start + (int) count;
    
    /*
     * It looks like pi function returns values in multiples of 4?
     * pi_end is the m value for the pi function
     */ 
    int pi_end;
    
    // Adjust pi_end so it is a multiple of 4 for pi function
    if (end < 4) {
        pi_end = 4;
    }
    else if ((end % 4) != 0) {
        pi_end = end - (end % 4) + 4;
    }
    else {
        pi_end = end;
    }
    
    char pi_buffer[pi_end];
    
    pi(pi_buffer, pi_end);
    
    /*
     * Besides copying the string to the user provided buffer,
     * this function also checks that the user has permission to
     * write to the buffer, that it is mapped, etc.
     * 
     * Starts write at the "start" index of the pi_buffer returned
     * from pi()
     */
    if (copy_to_user(buf, pi_buffer+start, count))
            return -EINVAL;
    /*
     * Tell the user how much data we wrote.
     */
    *ppos += (int) count;

    return count;
}

/*
 * The only file operation we care about is read.
 */

static const struct file_operations pi_fops = {
	.owner		= THIS_MODULE,
	.read		= pi_read,
};

static struct miscdevice pi_driver = {
	/*
	 * We don't care what minor number we end up with, so tell the
	 * kernel to just pick one.
	 */
	MISC_DYNAMIC_MINOR,
	/*
	 * Name ourselves /dev/pi.
	 */
	"pi_driver",
	/*
	 * What functions to call when a program performs file
	 * operations on the device.
	 */
	&pi_fops
};

static int __init
pi_init(void)
{
	int ret;

	/*
	 * Create the "pi" device in the /sys/class/misc directory.
	 * Udev will automatically create the /dev/pi device using the 
         * default rules.
	 */
	ret = misc_register(&pi_driver);
	if (ret)
		printk(KERN_ERR
		       "Unable to register pi misc device\n");

	return ret;
}

module_init(pi_init);

static void __exit
pi_exit(void)
{
	misc_deregister(&pi_driver);
}

module_exit(pi_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Zachary Pixley <zap15@pitt.edu>");
MODULE_DESCRIPTION("pi module");
MODULE_VERSION("dev");
