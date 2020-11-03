/*************************************************************************
 *  Compilation:  javac MyLZW.java
 *  Execution:    java MyLZW - {n,r,m} < input.txt > output.lzw  (compress)
 *  Execution:    java MyLZW + < input.lzw > output.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using MyLZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int MIN_W = 9;     // min codeword width
    private static final int MAX_W = 16;    // max codeword width
    private static final int MAX_L = 65536;
    private static final int R = 256;       // number of input chars

    private static int L = 512;             // number of codewords = 2^W
    private static int W = 9;               // codeword width
    private static char mode = 'n';         // compression mode defaults to 'do nothing' like LZW.java
    private static int oldSize = 0;
    private static int newSize = 0;
    private static double oldRatio = 0.0;
    private static double newRatio = 0.0;
    private static double comparison = 0.0;

    public static void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        //  Compression mode is first bytes of compressed file
        BinaryStdOut.write(mode);

        //  All compression takes place here
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);           // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);               // Print s's encoding.
            int t = s.length();

            oldSize += (t*8);
            newSize += W;

            if (t < input.length()) {
                //  There is room in codebook
                if (code < L) {
                    st.put(input.substring(0, t + 1), code++);  // Add s to symbol table.
                    oldRatio = ((double) oldSize) / ((double) newSize);
                }
                //  Codebook is full for current width
                else if (code >= L) {
                    //  Increase code width if less than MAX_W and recalculate number of codewords
                    if (W < MAX_W) {
                        W++;
                        L = (int) Math.pow(2, W);
                        st.put(input.substring(0, t + 1), code++);
                    }
                    //  Codebook is full
                    else if (W == MAX_W) {
                        //  Do Nothing Mode
                        if (mode == 'n') {

                        }
                        //  Reset Mode
                        else if (mode == 'r') {
                            st = new TST<Integer>();
                            for (int i = 0; i < R; i++)
                                st.put("" + (char) i, i);
                            code = R+1;  // R is codeword for EOF
                            W = MIN_W;
                            L = (int) Math.pow(2, W);
                            st.put(input.substring(0, t + 1), code++);
                        }
                        //  Monitor Mode
                        else if (mode == 'm') {
                            newRatio = ((double) oldSize) / ((double) newSize);
                            comparison = oldRatio / newRatio;
                            //  Reset if comparison exceeds 1.1
                            if (comparison > 1.1) {
                                st = new TST<Integer>();
                                for (int i = 0; i < R; i++)
                                    st.put("" + (char) i, i);
                                code = R+1;  // R is codeword for EOF
                                W = MIN_W;
                                L = (int) Math.pow(2, W);
                                st.put(input.substring(0, t + 1), code++);
                            }
                        }
                    }
                }
            }

            input = input.substring(t);                     // Scan past s in input.
        }

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }


    public static void expand() {
        String[] st = new String[MAX_L];
        int i;  //  next available codeword value
        int t;  //  used for length of value

        //  initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }
        st[i++] = "";   // (unused) lookahead for EOF
        //  Read compression mode (first byte) from file
        mode = BinaryStdIn.readChar();
        if (mode != 'n' && mode != 'r' && mode != 'm') {
            throw new IllegalArgumentException("Illegal compression mode");
        }

        //  Read first codeword
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) {
            return;           // expanded message is empty string
        }
        String val = st[codeword];

        while (true) {
            t = val.length();
            //  Track size of each file
            oldSize += W;
            newSize += (t*8);

            //  Write value to uncompressed file
            BinaryStdOut.write(val);

            if (i >= L) {
                //  Still filling codebook, expand codeword width
                if (W < MAX_W) {
                    W++;
                    L = (int) Math.pow(2, W);
                }
                //  Codebook was filled by this point during compression
                else if (W == MAX_W) {
                    //  Do nothing
                    if (mode == 'n') {

                    }
                    //  Reset
                    else if (mode == 'r') {
                        st = new String[MAX_L];
                        for (i = 0; i < R; i++) {
                            st[i] = "" + (char) i;
                        }
                        i = R+1;  // R is codeword for EOF
                        W = MIN_W;
                        L = (int) Math.pow(2, W);
                    }
                    //  Monitor
                    else if (mode == 'm') {
                        newRatio = ((double) newSize) / ((double) oldSize);
                        comparison = oldRatio / newRatio;
                        if (comparison > 1.1) {
                            st = new String[MAX_L];
                            for (i = 0; i < R; i++) {
                                st[i] = "" + (char) i;
                            }
                            i = R+1;  // R is codeword for EOF
                            W = MIN_W;
                            L = (int) Math.pow(2, W);
                            oldRatio = 0;
                        }
                    }
                }
            }

            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) {
                break;
            }
            String s = st[codeword];
            if (i == codeword) {
                s = val + val.charAt(0);   // special case hack
            }
            if (i < L) {
                st[i++] = val + s.charAt(0);
                //  Track ratio for monitor mode
                oldRatio = ((double) newSize) / ((double) oldSize);
            }
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        //  Compression
        if (args[0].equals("-")) {
            mode = args[1].toLowerCase().charAt(0);
            if (mode != 'n' && mode != 'r' && mode != 'm') {
                throw new IllegalArgumentException("Illegal compression mode");
            }
            else {
                compress();
            }
        }
        //  Expansion
        else if (args[0].equals("+")) {
            expand();
        }
        //  Illegal command line
        else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }

}
