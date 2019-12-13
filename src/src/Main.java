package src;

import java.io.*;

public class Main
{
    //-1 production, 1 scanner, 3 parser,
    //even with debug prints, odd no prints
    //odd values are used for kleinf,kleinp, etc
    //**Do not change here. the override is below**
    static int debugStage =-1;

    /////CHANGE THIS ONE
    static int debugOverride = 10;

    //can use this line in the code and will mute the debug print
    //if (Main.debugStage>=2) {System.out.println("The stack is: " + stack.toString());}

    public static void main(String[] args)
    {
        if(args.length == 1)
        {
            debugStage = 7;
        }
        else if(args.length == 2)
        {
            if(args[1].toLowerCase().equals("scan"))
                debugStage = 1;
            else if(args[1].toLowerCase().equals("validate"))
                debugStage = 3;
            else if(args[1].toLowerCase().equals("parse"))
                debugStage = 5;
            else if(args[1].toLowerCase().equals("check"))
                debugStage = 7;
            else if(args[1].toLowerCase().equals("compile"))
                debugStage = 9;
            else if(args[1].toLowerCase().equals("debug"))
                debugStage = debugOverride;
            else
                debugStage = -1;
        }

        try
        {
            String fileName = args[0];
            //make sure there is an extension appended
            if (!args[0].contains(".kln"))
                fileName += ".kln";
            String fullFile = new java.util.Scanner(new File(fileName)).useDelimiter("\\Z").next();

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
                parse.printTree();
            }
            //Type Checker
            if(debugStage == 7 || debugStage == 8)
            {
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                TypeCheck checker = new TypeCheck();
                Node tree = parse.getTree();
                checker.check(tree);
                if(debugStage == 8)
                {
                    //checker.printSymbolTable();
                    checker.printAllTypes(tree, 0);
                }
            }
            //code gen
            if(debugStage == 9 || debugStage == 10)
            {
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                TypeCheck checker = new TypeCheck();
                Node tree = parse.getTree();
                checker.check(tree);
                Generator gen = new Generator();
                gen.gen(checker.getStable(), tree, fileName);
            }

            //production - full pipeline
            if(debugStage == -1)
            {
                //System.out.println("Started full pipeline");
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                TypeCheck checker = new TypeCheck();
                Node tree = parse.getTree();
                checker.check(tree);
                Generator gen = new Generator();
                gen.gen(checker.getStable(), tree, fileName);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("The specified file could not be found.");
        }
        System.out.println("Exiting...");
    }
}
