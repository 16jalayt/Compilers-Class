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
    //TODO: top will overwrite variables in mem

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

            ////////////////Needed, because should parse other funcs if called?
            //parse other functions not main
            ////////focus on everything in main
            //iterateOthers(tree);

            computeOffsets();

            writer.close();
        }
       catch(IOException e)
       {
           System.out.println("Unable to write to file");
       }
    }

    //stores jumps awaiting an addr
    private static LinkedList<WaitingType> waiting = new LinkedList<WaitingType>();
    //function addresses as they are parsed
    private static HashMap<String, Integer> labels = new HashMap<>();

    private static class WaitingType
    {
        public int lineNumber = 0;
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
    public static void addr(String instruction, int r1, int r2, int r3, String tag)
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
            {/////////Not actually setting addr?
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

    private Node getMain(Node tree)
    {
        //go down one level
        tree = tree.children.get(0);
        for (Node child : tree.children)
        {
            if(child.name.equals("Identifier"))
            {
                if (child.value.equals("main"))
                {
                    if(Main.debugStage == 10) System.out.println("Found main");
                    //Hack for out of scope. Should only be set once anyway.
                    /*main.value = tree.value;
                    main.children = tree.children;
                    main.type = tree.type;
                    main.name = tree.name;*/
                    return tree;
                }
            }
        }
        System.out.println("Main function not found");
        return null;
    }

    private void iterateOthers(Node tree) throws IOException {
        //sends all def nodes (except for main) to parseFunc
        for (Node child : tree.children) {
            if (child.name.equals("Def")) {
                if (child.children.get(0).value.equals("Main")) {
                    continue;
                } else {
                    parseFunc(child);
                }
            }
            else {
                iterateOthers(child);
            }
        }
    }
                /*if(child.name.equals("Identifier")) {
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
                iterateOthers(child);
            }*/

    private void bootStrap() throws IOException
    {
        printComment("-----Begin bootstrap-----\n");

        Node main = getMain(tree);
        if (main==null)
        {
            System.out.println("Could not find the main function.");
            System.exit(-1);
        }
        //Ignore: just get from func tree
        //LD  3,1(0)   ; read command-line arg into MAIN's arg slot
        //just copy paste known number of times
        //need to move to tmp because will be overwritten by stack frame, unless waste that space

        int numberArgs = 0;
        //numberArgs = main.children.length;

        //leave in dmem numberargs+1
        //top = 1;
        /*for(int i=1; i<numberArgs;i++)
        {
            //////top doesnt work
            print("ld", i, top + i,0);
            print("ldc", 6,1,6,"inc top by 1");
        }*/

        //top should be end of args

        // +2 = +1 for addr. -1 = -2

        //must be last thing called, so addr calculated right
        ///////StackFrame.makeFrame(new int[]{},"main");
        //jump immediately after

        //need to send to a seperate function (addr)
        //skip printing line to compute later
        //addr("lda", 5,1,7,"jump to main");

        //ends up inserting main before end, so dont need for now
        //print("ldc", 7,2,7,"jump to main by offseting by 2");
        printComment("-----End bootstrap-----\n\n");
        parseFunc(main);

        printComment("-----Ending Program-----\n\n");
        print("out", 5,0,0,"print the return register");
        print("halt", 0,0,0);
    }



    private void parseFunc(Node defNode) throws IOException
    {//TODO: optimize by inlining short functions
        printComment("-----Begin function call-----\n");
        //print block at top of function

        ///////////first thing store starting addr to func table and the known label dict tag value.

        System.out.println(String.valueOf(defNode));
        labels.put(defNode.children.get(0).value.toString(), lineNumber);

        //exp can just trigger a return for now, will have to get smarter

        //FIGURE OUT WHAT PRINT NODE LOOKS LIKE
        //print is func call. children (id print) (exp number 1)

        //!!!!!I think it needs to dig into the tree deeper
        //parseFuncHelper(defNode.child);
        parseFuncHelper(defNode);

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
        if(tree.children.isEmpty())
        {
            System.out.println("Children are empty for object" + tree.toString());
            return;
        }
        //////////Andrew, change this to for loop so we can do tree.children.get(x). The tm functions will have to return
        //the node and parsefunchelper will have to overwrite the node
        for (int i=0; i<tree.children.size(); i++)
        //for (Node child : tree.children)
        {
            Node child = tree.children.get(i);
            switch (child.name) {
                case "FunctionCalls":
                    if (child.children.get(0).value.equals("print")) {
                        printTM(child);
                    }
                    //handle func call
                    else {
                        System.out.println("func call");
                        functionCallTM(child);
                    }
                    parseFuncHelper(child);
                    break;
                case "Expr":
                    System.out.println("Handle return");
                    exprTM(child);
                    parseFuncHelper(child);
                    break;
				case "Identifier":
                    identifierTM(child);
                    parseFuncHelper(child);
                    break;
                case "Number":
                    Node numNode = numberTM(child);
                    parseFuncHelper(child);
                    break;
                case "BooleanValue":
                    booleanTM(child);
                    parseFuncHelper(child);
                    break;
                case "Binary":
                    Node binaryNode = binaryTM(child);
                    //binaryNode.addChildren(child.children.get());
                    child = binaryNode;
                    parseFuncHelper(child);
                    break;
                case "Unary":
                    Node unaryNode = unaryTM(child);
                    parseFuncHelper(child);
                    break;
                case "If":
                    ifTM(child);
                    parseFuncHelper(child);
                    break;
                case "Actuals":
                    actualsTM(child);
                    parseFuncHelper(child);
                    break;
                default:
                    parseFuncHelper(child);
                    //System.out.println("Unknown node type " + child.name);
                    break;
            }
        }
    }

    //the following 12 functions write out TM statements for their specific node types

    //turn node into return type
    private static HashMap<String, Object> variables = new HashMap<>();

    private void printTM (Node tree) throws IOException {
        int reg = getFreeRegister();
        int val = Integer.parseInt(tree.children.get(1).children.get(0).value.toString());
        print("ldc" ,reg,val,0);
        print("out" ,reg,0,0, "Printing value:"+val);
    }

    private void exprTM (Node tree) throws IOException {
        System.out.println("Expression not implemented");
    }

    private void identifierTM (Node tree) throws IOException {
        System.out.println("Identifier not implemented");
    }

    private void formalsTM (Node tree) throws IOException {
        System.out.println("Formals not implemented");
    }

    private void formalTM (Node tree) throws IOException {
        System.out.println("Formal not implemented");
    }

    private Node numberTM (Node tree) throws IOException {
        System.out.println("Number work in progress");
        tree = new Node.Returned(Integer.parseInt(tree.value.toString()));
        return tree;
    }

    private void booleanTM (Node tree) throws IOException {
        System.out.println("Boolean not implemented");
    }

    //chage node to result node and check for
    private Node binaryTM (Node tree) throws IOException {
        System.out.println("Binary Expression work in progrss");
        //if(identifier) look up symbol table
        //if(interger) parse int
        //stable.get(identifier.value).formals.get(0) gives linked list of formals
        Node one = tree.children.get(0);
        Node two = tree.children.get(1);
        int oneInt = 0;
        int twoInt = 0;
        int reg = getFreeRegister();
        if (tree.value.toString() == "+")
        {
            if(one.type.equals("Integer") && two.type.equals("Integer"))
                print("add",reg,Integer.parseInt(one.value.toString()), Integer.parseInt(two.value.toString()));

            else if(one.type.equals("Identifier") && two.type.equals("Integer"))
            {
                for(String id : stable.get("Main").formals.get(0))
                    if(id.equals(one.value))
                    {
                        System.out.println(one.value);
                        oneInt = Integer.parseInt(id);
                    }

                print("add",reg,oneInt, Integer.parseInt(two.value.toString()));
            }
        }
        Node rNode = new Node.Returned(reg);
        return rNode;
    }

    private Node unaryTM (Node tree) throws IOException {
        System.out.println("Unary Expression partially");
        int freeReg = getFreeRegister();

        if (tree.value.equals("-")){
            if (tree.children.get(0).name.equals("Number")){
                int nodeVal = Integer.parseInt(tree.children.get(0).value.toString());
                print("ldc", freeReg, nodeVal, 0);
                print("sub", freeReg, 0, freeReg);
            }
            else if(tree.children.get(0).name.equals("Identifier")){
                for (String id : stable.get("Main").formals.get(0)){

                }
            }
        }
        Node rNode = new Node.Returned(freeReg);
        return rNode;
    }

    private void ifTM (Node tree) throws IOException {
        System.out.println("If not implemented");
        //if(child0 child1 child2)
    }

    private void actualsTM (Node tree) throws IOException {
        System.out.println("Actuals not implemented");
    }

    private void functionCallTM (Node tree) throws IOException {
        //check exists in symboltable
        //add to unknown list
        //call parsefunc
        System.out.println("Functions not implemented");
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
