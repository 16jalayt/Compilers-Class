package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Generator
{
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();
    Node tree;
    BufferedWriter writer;
    static int lineNumber = 0;
    private int top=0;

    public void gen(Map stable, Node tree, String fileName)
    {
        try
        {
            this.stable = stable;
            this.tree = tree;
            fileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".tm";
            writer =  new BufferedWriter(new FileWriter(new File(fileName)));

            //TEAM NOTE: doesnt matter how the instructions are typed
            //they will be converted to upper case
            //print("lda", 6,1,7,"jump to main");


            /////Make in reverse order so return addresses work out
            bootStrap();


            writer.close();
        }
       catch(IOException e)
       {
           System.out.println("Unable to write to file");
       }
    }

    private void bootStrap() throws IOException
    {
        //Ignore: just get from func tree
        //LD  3,1(0)   ; read command-line arg into MAIN's arg slot
        //needs to be in for loop

        //r5 used for ret addr
        // +2 = +1 for addr. -1 = -2
        print("lda", 5,1,7,"jump to main");

        //dont know where main is yet
        //print("lda", 7,x,0);

        ///////MABY keep outputs in linked list for out of order
        //should work without, if do reverse order

        print("out", 6,0,0,"print the return register");
        print("halt", 0,0,0);
    }
    private void main() throws IOException
    {
        //in mem not reg:
        //8:    ADD  5,0,3    ; store parameter into SQUARE's arg slot

        //in stack frame
        //9:    ST   6,2(0)   ; save return address in DMEM[ [r0]+2 ]

        //inc of 1 skips over the branch
        //10:    LDA  5,1(7)   ; store return address


        //11:    LDA  7,5(0)   ; branch to SQUARE, at [r0]+5

        //in stackframe
        //12:    ADD  2,0,4    ; copy SQUARE's return value into return slot

        //teardown jump
        //13:    LD   7,2(0)   ; return to address in DMEM[ [r0]+2 ]
    }

    //TODO
    int current = 1;
    private int getFreeRegister()
    {//1-4 are general use. would need to allocate and dealloc
        //I think we are getting algorithm for this

        //if all given out return null
        if(current >=5)
            return -1;
        else
        {
            current++;
            return 1;
        }
    }

    private void printStatement(int register) throws IOException
    {
        print("out", register,0,0,"print a value");
    }


    //These functions are for programming convienience and
    //overload depending on what we pass. Would be nice if
    //java had default args insted of just overloading.

    //1 op instruction, ex halt
    private void print(String instruction, String comment) throws IOException
    {
        print(instruction,0,0,0,comment);
    }
    private void print(String instruction) throws IOException
    {
        print(instruction,0,0,0,"");
    }
    //instructions with 3 args, or with an offset
    private void print(String instruction, int r1, int r2, int r3) throws IOException
    {
        print(instruction,r1,r2,r3,"");
    }
    private void print(String instruction, int r1, int r2, int r3,String comment) throws IOException
    {
        //pretty print the indentations
        int spaces = 7-instruction.length();

        String lineOut = lineNumber + ":" + new String(new char[spaces]).replace("\0", " ") +
                instruction.toUpperCase() + "  " + r1 + "," + r2;

        switch(instruction.toLowerCase())
        {
            case "ld":
            case "lda":
            case "st":
                lineOut += "(" + r3 + ")";
                lineNumber++;
                break;
            default:
                lineOut += "," + r3;
                lineNumber++;
                break;
        }
        lineOut += "    ;" + comment + "\n";
        writer.write(lineOut);
    }

}
