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
    //symbol table
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();

    Node tree;
    static BufferedWriter writer;
    static int lineNumber = 0;
    private int top=1;

    public void gen(Map stable, Node tree, String fileName)
    {
        try
        {
            //set local vars
            this.stable = stable;
            this.tree = tree;

            //output file setup
            fileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".tm";
            writer =  new BufferedWriter(new FileWriter(new File(fileName)));

            bootStrap();

            //find main to start
            iterateMain(tree);

            //parse other functions not main
            iterateOthers(tree);

            computeOffsets();

            writer.close();
        }
       catch(IOException e)
       {
           System.out.println("Unable to write to file");
       }
    }

    //stores jumps awaiting an addr
    LinkedList<WaitingType> waiting = new LinkedList<WaitingType>();
    //function addresses as they are parsed
    HashMap<String, Integer> labels = new HashMap<>();

    private class WaitingType
    {
        public int lineNumber;
        public String tag;
        private String instruction;
        private int r1;
        private int r2;
        private int r3;
        private String comment;
        public WaitingType(String instruction, int r1, int r2, int r3, String comment,String tag)
        {
            this.lineNumber = Generator.lineNumber;
            this.tag = tag;

            this.instruction = instruction;
            this.r1 = r1;
            this.r2 = r2;
            this.r3 = r3;
            this.comment = comment;
        }
        @Override
        public String toString()
        {
            return lineNumber + ":" + tag;
        }
        public void out() throws IOException
        {
            int tmp = Generator.lineNumber;
            print(instruction, r1, r2, r3, comment);
            Generator.lineNumber = tmp;
        }
    }

    //Drop in for unknown addr jumps
    private void addr(String instruction, int r1, int r2, int r3, String tag)
    {
        waiting.add(new WaitingType(instruction, r1, r2, r3, "Jump to:"+tag, tag));
    }

    //compute all now known offsets
    private void computeOffsets() throws IOException
    {
        printComment("-----Begin compute offsets-----\n");

        //iterate through labels with unknown line numbers and print them
        for (int i = 0; i<waiting.size(); i++)
        {
            if(labels.containsKey(waiting.get(i).tag))
            {
                int addr = labels.get(waiting.get(i).tag);
                print(addr, waiting.get(i).instruction , waiting.get(i).r1 ,waiting.get(i).r2,
                        waiting.get(i).r3, waiting.get(i).comment);
                waiting.remove(i);
            }

            //labels left with unknown addresses
            if(waiting.size()>0)
            {
                System.out.println("Unknown labels list not empty..." + waiting);
            }
        }
        printComment("-----End compute offsets-----\n\n");
    }

    private void iterateMain(Node tree) throws IOException {
        for (Node child : tree.children)
        {
            if(child.name.equals("Identifier")) {
                if (child.value.equals("main")) {
                    System.out.println("Found main");
                    parseFunc(tree.children.get(3));
                }
            }
            else{
                iterateMain(child);
            }
        }
    }

    private void iterateOthers(Node tree) throws IOException {
        //check for all identifiers that are a function name, except for main and print
        for (Node child : tree.children)
        {
            if(child.name.equals("Identifier")) {
                if (stable.containsKey(child.value)) {
                    if (child.value.equals("main") || child.value.equals("print")){
                        continue;
                    }
                    else {
                        parseFunc(tree.children.get(3));
                    }
                }
            }
            else{
                iterateMain(child);
            }
        }
    }



    private void bootStrap() throws IOException
    {
        printComment("-----Begin bootstrap-----\n");
        //Ignore: just get from func tree
        //LD  3,1(0)   ; read command-line arg into MAIN's arg slot
        //just copy paste known number of times
        //need to move to tmp because will be overwritten by stack frame, unless waste that space

        //is main always slot zero? - no it is not. Have to compare names
        //int numberArgs = tree.children.get(0);
        //hardcoded for now
        int numberArgs = 0;
        /////proj 5 just skip this
        //only wroks for 4 args or less dum into reg and put back in later
        //let make stackframe take care of it
        for(int i=1; i<numberArgs;i++)
        {
            //////top doesnt work
            print("ld", i, top + i,0);
        }


        //r5 used for ret addr
        // +2 = +1 for addr. -1 = -2

        //must be last thing called, so addr calculated right
        ////////stackframes not quite ready yet
        StackFrame.makeFrame(0);
        //jump immediately after

        //need to send to a seperate function (addr)
        //skip printing line to compute later
        //addr("lda", 5,1,7,"jump to main");

        //ends up inserting main before end, so dont need for now
        //print("ldc", 7,2,7,"jump to main by offseting by 2");
        iterateMain(tree);
        print("out", 6,0,0,"print the return register");
        print("halt", 0,0,0);

        //isolate function from tree//need to start at main. itter to find
        ////NEW! skip over line then try and parse pain after bootstrap

        printComment("-----End bootstrap-----\n\n");
    }



    private void parseFunc(Node func) throws IOException
    {
        printComment("-----Begin function call-----\n");
        //print block at top of funtion

        ///////////first thing store starting addr to func table and the known label dict tag value.
        

        //exp can just trigger a return for now, will have to get smarter

        //FIGURE OUT WHAT PRINT NODE LOOKS LIKE
        //print is func call. children (id print) (exp number 1)

        //!!!!!I think it needs to dig into the tree deeper
        parseFuncHelper(func);

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
        Generator.printComment("-----End function call-----\n\n");
    }

    private void parseFuncHelper (Node tree) throws IOException {
        for (Node child : tree.children)
        {
            if(child.name.compareTo("FunctionCalls")==0)
                if(child.children.get(0).value.equals("print"))
                {
                    int reg = getFreeRegister();
                    int val = Integer.parseInt(child.children.get(1).children.get(0).value.toString());
                    print("ldc" ,reg,val,0);
                    print("out" ,reg,0,0, "Printing value:"+val);
                }
                else //handle func call
                    System.out.println("func call");
            else if(child.name.compareTo("Expr")==0)//handle return
                System.out.println("Handle return");
            else
                System.out.println("Unknown node type " + child.name);
        }
    }

    int current = 1;
    private int getFreeRegister()
    {//1-4 are general use.
        //can do map of vars in what registers
        //that way can tell if registers redundent

        //if full, write existing one to memory, not sure how to keep track and retrieve.
        //locality of reference/ next use info

        //or last ditch just keep looping and repeat, and hope no conflict

        //if all given out return null
        if(current >=5)
            return -1;
        else
        {
            current++;
            return 1;
        }
    }

    //NOTE binary op
    //ld arg1
    //ld arg2
    //op
    //st


    //These functions are for programming convenience and
    //overload depending on what we pass. Would be nice if
    //java had default args instead of just overloading.



    //prints out a line comment
    public static void printComment(String comment) throws IOException
    {
        writer.write("*" +comment);
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
    //specifying line number
    public static void print(int line, String instruction, int r1, int r2, int r3) throws IOException
    {
        print(line, instruction,r1,r2,r3,"");
    }
    public static void print(int line, String instruction, int r1, int r2, int r3,String comment) throws IOException
    {
        int tmp = lineNumber;
        lineNumber = line;
        print(instruction,r1,r2,r3,comment);
        lineNumber = tmp;
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
