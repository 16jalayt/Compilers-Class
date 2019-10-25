package src;

import java.io.*;

public class Main
{
    //-1 production, 1 scanner, 3 parser,
    //even with debug prints, odd no prints
    static int debugStage = 5;

    //can use this line in the code and will mute the debug print
    //if (Main.debugStage>=2) {System.out.println("The stack is: " + stack.toString());}

    //just type testing
    private static void test()
    {
        //testing
        Node.Binary bin = new Node.Binary('g',null);
        Node nd = bin;
        bin.value= 'h';
        System.out.println(bin.value);
        Node test = new Node(new Node[]{bin, nd});
    }

    public static void main(String[] args)
    {
        //test();
        if(args.length != 1)
        {
            System.out.println("Correct usage is: program filename.kln");
            return;
        }

        try
        {
            String fullFile = new java.util.Scanner(new File(args[0])).useDelimiter("\\Z").next();

            //scanner
            if(debugStage == 1 || debugStage == 2)
            {
                Scanner scan = new Scanner(fullFile);
                while (scan.peek().type != Token.Type.EOF && scan.peek().type != Token.Type.Error)
                    System.out.println(scan.next().toString());
                //one last call to display EOF
                System.out.println(scan.next().toString());
            }
            //parser tf
            if(debugStage == 3 || debugStage == 4)
            {
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                if (parse.parse())
                    System.out.println("The program is: Valid");
                else
                    System.out.println("The program is: Invalid");
            }
            //parser tree
            if(debugStage == 5 || debugStage == 6)
            {

                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                parse.getTree();
            }
            //production - full pipeline
            if(debugStage == -1)
            {
                //System.out.println("Started full pipeline");
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                parse.getTree();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("The specified file could not be found.");
        }
        System.out.println("Exiting...");
    }
}
