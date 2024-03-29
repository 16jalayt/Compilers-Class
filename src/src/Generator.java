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
            {//TODO: just update r2 with new addr
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
/*
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
                    return tree;
                }
            }
        }
        System.out.println("Main function not found");
        return null;
    }*/

    private Node getFunc(Node tree, String name)
    {
        if(Main.debugStage == 8) System.out.println("looking in "+tree.name);
        //go down one level
        tree = tree.children.get(0);
        for (Node child : tree.children)
        {
            if(child.name.equals("Identifier"))
            {
                if (child.value.equals(name))
                {
                    return tree;
                }
            }
        }
        System.out.println(name+" function not found");
        return null;
    }

    /*
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
    }*/

    private void bootStrap() throws IOException
    {
        printComment("-----Begin bootstrap-----\n");

        Node main = getFunc(tree,"main");

        //troubleshooting guard
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
        //main is def node, hack to get down to body
        //main = main.children.get(3).children.get(0);
        main = main.children.get(3);
        parseFunc(main);

        printComment("-----Ending Program-----\n\n");
        //System.out.println("Type is: "+returnVal.name+":"+returnVal.returnReg);
        print("out", returnVal.returnReg,0,0,"print the return register");
        print("halt", 0,0,0);
    }



    private void parseFunc(Node defNode) throws IOException
    {//TODO: optimize by inlining short functions
        printComment("-----Begin function call-----\n");
        //print block at top of function

        ///////////first thing store starting addr to func table and the known label dict tag value.

        //Def
        //System.out.println("value of "+String.valueOf(defNode));
        //labels.put(defNode.children.get(0).value.toString(), lineNumber);

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

    private static Node.Returned returnVal;
    private void parseFuncHelper (Node tree) throws IOException {
        if(tree.children.isEmpty())
        {
            //System.out.println("Children are empty for object" + tree.toString());
            return;
        }
        for (int i=0; i<tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            switch (child.name) {
                case "FunctionCalls":
                    parseFuncHelper(child);
                    if (child.children.get(0).value.equals("print")) {
                        printTM(child);
                    }
                    //handle func call
                    else {
                        functionCallTM(child);
                    }

                    break;
                case "Expr":
                    parseFuncHelper(child);
                    Node exprnode = exprTM(child);
                    tree.children.set(i,exprnode);
                    break;
                case "Identifier":
                    Node idnode = identifierTM(child);
                    tree.children.set(i,idnode);
                    break;
                case "Number":
                    parseFuncHelper(child);
                    Node numNode = numberTM(child);
                    tree.children.set(i,numNode);

                    break;
                case "BooleanValue":
                    parseFuncHelper(child);
                    booleanTM(child);
                    Node node = exprTM(child);
                    tree.children.set(i,node);
                    break;
                case "Binary":
                    parseFuncHelper(child);
                    Node binaryNode = binaryTM(child);
                    tree.children.set(i,binaryNode);

                    break;
                case "Unary":
                    parseFuncHelper(child);
                    Node unaryNode = unaryTM(child);
                    tree.children.set(i,unaryNode);

                    break;
                case "If":
                    parseFuncHelper(child);
                    ifTM(child);

                    break;
                case "Actuals":
                    parseFuncHelper(child);
                    Node actnode = actualsTM(child);
                    tree.children.set(i,actnode);
                    break;
                default:
                    parseFuncHelper(child);
                    System.out.println("Unknown node type " + child.name);
                    break;
            }
        }
        //System.out.println("returnval = "+tree.children.get(0).name);
        returnVal = new Node.Returned(tree.children.get(0).returnReg);
    }

    //the following 12 functions write out TM statements for their specific node types

    //////////////////////////////////////////The child nodes no longer have types.
    // //it is assumed they are already the correct type, so just pull the returnreg

    //turn node into return type
    private static HashMap<String, Object> variables = new HashMap<>();

    private void printTM (Node tree) throws IOException {
        //System.out.println("Print work in progress");
        int reg = tree.children.get(0).returnReg;
        print("out" ,reg,0,0, "Printing value in reg "+reg);
    }

    private Node exprTM (Node tree) throws IOException {
        //System.out.println("Expression not implemented");
        //shouldnt need to do anything except pass back up
        return tree.children.get(0);
    }

    private Node identifierTM (Node tree) throws IOException {
        System.out.println("Identifier work in progress");
        int reg = getFreeRegister();
        if(tree.value.toString().equals("true"))
            print("ldc",reg,1,0,"Boolean true");
        else if(tree.value.toString().equals("false"))
            print("ldc",reg,0,0,"Boolean false");
        else
            System.out.println("Invalid ID expression");
        return new Node.Returned(reg);
    }

    private Node formalsTM (Node tree) throws IOException {
        //System.out.println("Formals not implemented");
        return tree.children.get(0);
    }

    private Node formalTM (Node tree) throws IOException {
        //System.out.println("Formal not implemented");
        return tree.children.get(0);
    }

    private Node numberTM (Node tree) throws IOException {
        //System.out.println("Number work in progress");
        int reg = getFreeRegister();
        print("ldc",reg,Integer.parseInt(tree.value.toString()),0,"Load number");
        Node rNode = new Node.Returned(reg);
        return rNode;
    }

    private Node booleanTM (Node tree) throws IOException {
        //bool stored as identifier, uses that func insted
        //System.out.println("Boolean work in progress");
        int reg = getFreeRegister();
        if(tree.value.toString().equals("true"))
            print("ldc",reg,1,0,"Boolean true");
        else if(tree.value.toString().equals("false"))
            print("ldc",reg,0,0,"Boolean false");
        else
            System.out.println("Invalid boolean expression");
        return new Node.Returned(reg);
    }

    //chage node to result node and check for
    private Node binaryTM (Node tree) throws IOException {
        //System.out.println("Binary Expression work in progrss");
        Node one = tree.children.get(0);
        Node two = tree.children.get(1);

        if (tree.value.toString().equals("+"))
            //result in the second reg because less likey to be overwritten
            print("add",two.returnReg,one.returnReg, two.returnReg);
        else if (tree.value.toString().equals("-"))
            print("sub",two.returnReg,one.returnReg, two.returnReg);
        else if (tree.value.toString().equals("*"))
            print("mul",two.returnReg,one.returnReg, two.returnReg);
        else if (tree.value.toString().equals("/"))
            print("div",two.returnReg,one.returnReg, two.returnReg);
        else if (tree.value.toString().equals("="))
            //return copy of same node and let if deal with the op
            return tree;
        else if (tree.value.toString().equals(">"))
            return tree;
        else if (tree.value.toString().equals("<"))
            return tree;
        else
            System.out.println("Unknown binary op: "+tree.value.toString());

        return new Node.Returned(two.returnReg);
    }

    private Node unaryTM (Node tree) throws IOException {
        System.out.println("Unary Expression partially Implemented");

        Node one = tree.children.get(0);

        //negate. subtrack value from zero
        if (tree.value.toString().equals("-"))
            print("sub",one.returnReg,0, one.returnReg);
            //not great way to flip bool. would take a few lines of tm
            //else if (tree.value.toString().equals("not"))

        else
            System.out.println("Unknown unary op: "+tree.value.toString());


        return new Node.Returned(one.returnReg);
    }

    private Node ifTM (Node tree) throws IOException {
        System.out.println("If work in progress");
        //if
        Node one = tree.children.get(0);
        //then
        Node two = tree.children.get(1);
        //else
        Node three = tree.children.get(2);

        //get bool node as child, have to parse and eval
        Node opOne = one.children.get(0);
        Node opTwo = one.children.get(1);
        String op = one.value.toString();
        int result = getFreeRegister();
        //sub 2 operands to make one zero. See if the other one is equal to zero
        print("sub",two.returnReg, two.returnReg, one.returnReg,"subtract operands to compare to 0");


        //TODO: setup label to match and get lebel working
        if (op.equals("="))
            //jump to abs addr
            addr("JEQ",result, -1, 0,"if");
        else if (op.equals(">"))
            addr("JGT",result, -1, 0,"if");
        else if (op.equals("<"))
            addr("JLT",result, -1, 0,"if");
        else
            System.out.println("Unknown operand: " + op);

        return new Node.Returned(three.returnReg);
    }

    private Node actualsTM (Node tree) throws IOException {
        System.out.println("Actuals not implemented");
        return tree.children.get(0);
    }

    private Node functionCallTM (Node tree) throws IOException {
        //check exists in symboltable - i think that is validated already
        //add to unknown list
        //call parsefunc
        System.out.println("Function call work in progress");
        //pull out func name
        String funcName = tree.name;
        System.out.println("trying to get: "+funcName);
        //labels.put(name);

        return null;
    }

    //set to zero because increments before return
    int current = 0;
    private int getFreeRegister()
    {//1-4 are general use.
        //can do map of vars in what registers
        //that way can tell if registers redundent

        //if full, write existing one to memory, not sure how to keep track and retrieve.
        //locality of reference/ next use info

        //or last ditch just keep looping and repeat, and hope no conflict

        //if all given out return null
        if(current >=4)
        {
            current = 1;
            return current;
        }
        else
        {
            current++;
            return current;
        }
    }

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
                lineOut += "," + r3+" ";
                lineNumber++;
                break;
        }
        lineOut += "    ;" + comment + "\n";
        writer.write(lineOut);
    }

}
