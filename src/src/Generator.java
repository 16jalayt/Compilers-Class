package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Generator
{
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();
    Node tree;
    static BufferedWriter writer;
    static int lineNumber = 0;
    private int top=1;

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
            iterate(tree);
            computeOffsets();

            writer.close();
        }
       catch(IOException e)
       {
           System.out.println("Unable to write to file");
       }
    }

    private void iterate(Node tree) throws IOException {
        for (Node child : tree.children) {
            iterate(child);
        }
    }

    private class WaitingType
    {
        public int lineNumber;
        public String tag;
        @Override
        public String toString()
        {
            return lineNumber + ":" + tag;
        }
    }

    //jumps awaiting an addr
    LinkedList<WaitingType> object = new LinkedList<WaitingType>();
    //function addresses as they are parsed
    HashMap<String, Integer> funcs = new HashMap<>();

    private void computeOffsets()
    {
        //use string label as dict key. SHould be able to pass string around?
        //function name gets inserted on creation.
        //use list as waiting for addr
        //line numbers. make struct as list type

        //itter through the structure and match tags, just print like normal
        //print("lda", 5,1,7,"jump to main");
    }

    private void bootStrap() throws IOException
    {
        //Needs to be first
        //lineNumber = 0;

        //Ignore: just get from func tree
        //LD  3,1(0)   ; read command-line arg into MAIN's arg slot
        //just copy paste known number of times
        //need to move to tmp because will be overwritten by stack frame, unless waste that space

        //is main always slot zero?
        //int numberArgs = tree.children.get(0);
        //hardcoded for now
        int numberArgs = 0;
        /////proj 5 just skip this
        //only wroks for 4 args or less dum into reg and put back in later
        //let make stackframe take care of it
        for(int i=1; i<numberArgs;i++)
        {
            print("ld", i, top + i,0);
        }


        //r5 used for ret addr
        // +2 = +1 for addr. -1 = -2

        //must be last thing called, so addr calculated right
        StackFrame.makeFrame(0);
        //jump immediately after

        //need to send to a seperate function (or hook from print?)
        //skip line and put into some variable to compute later
        //addr("lda", 5,1,7,"jump to main");
        Generator.print("ldc", 7,2,7,"jump to main by offseting by 2");


        print("out", 6,0,0,"print the return register");
        print("halt", 0,0,0);

        //isolate function from tree//need to start at main. itter to find
        ////NEW! skip over line then try and parse pain after bootstrap
        parseFunc(new Node());
    }



    private void parseFunc(Node func) throws IOException
    {
        //parse tree for main. should find exp and print

        ////////first thing store starting addr to func table

        //exp can just trigger a return for now, will have to get smarter

        //FIGURE OUT WHAT PRINT NODE LOOKS LIKE
        //print is func call. children (id print) (exp number 1)


        //if (node.name == print)
            //print("out", node.value);
        //else
            //System.out.println("Generator: Unknown node type " + node.name);

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


    //These functions are for programming convienience and
    //overload depending on what we pass. Would be nice if
    //java had default args insted of just overloading.

    private void addr(String instruction, int r1, int r2, int r3, String coment)
    {
        //add to some structure to compute once known
        //use labels?
    }

    //1 op instruction, ex halt
    public static void print(String instruction, String comment) throws IOException
    {
        print(instruction,0,0,0,comment);
    }
    public static void print(String instruction) throws IOException
    {
        print(instruction,0,0,0,"");
    }
    //instructions with 3 args, or with an offset
    public static void print(String instruction, int r1, int r2, int r3) throws IOException
    {
        print(instruction,r1,r2,r3,"");
    }
    public static void print(String instruction, int r1, int r2, int r3,String comment) throws IOException
    {
        //pretty print the indentations
        int spaces = 7-instruction.length();

        String lineOut = lineNumber + ":" + new String(new char[spaces]).replace("\0", " ") +
                instruction.toUpperCase() + "  " + r1 + "," + r2;

        switch(instruction.toLowerCase())
        {
            case "lda":
            case "ldc":
            case "ld":
            case "st":
            case "jeq":
            case "jne":
            case "jlt":
            case "jle":
            case "jgt":
            case "jge":
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
