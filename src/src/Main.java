package src;

import java.io.*;

public class Main
{
    //-1 production, 1 scanner, 3 parser,
    //even with debug prints, odd no prints
    //**Do not change here. the override is below the if statement**
    static int debugStage = -1;

    //can use this line in the code and will mute the debug print
    //if (Main.debugStage>=2) {System.out.println("The stack is: " + stack.toString());}

    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.out.println("Correct usage is: program filename.kln");
            return;
        }
        else if(args.length == 2)
        {
            if(args[1].toLowerCase().equals("scan"))
                debugStage = 1;
            if(args[1].toLowerCase().equals("validate"))
                debugStage = 3;
            if(args[1].toLowerCase().equals("parse"))
                debugStage = 5;
            if(args[1].toLowerCase().equals("check"))
                debugStage = 7;
            else
                debugStage = -1;
        }
        else
            debugStage = -1;

        //**override for debug**
        debugStage = 6;

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
                parse.printTree();
            }
            //Type Checker
            if(debugStage == 7 || debugStage == 8)
            {
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                TypeCheck checker = new TypeCheck();
                System.out.println("Type Checker returned: " + checker.check(parse.getTree()));
                if(debugStage == 8)
                    System.out.println(checker.symbolTable);
            }




            //production - full pipeline
            if(debugStage == -1)
            {
                //System.out.println("Started full pipeline");
                Scanner scan = new Scanner(fullFile);
                Parser parse = new Parser(scan);
                parse.parse();
                TypeCheck checker = new TypeCheck();
                checker.check(parse.getTree());
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("The specified file could not be found.");
        }
        System.out.println("Exiting...");
    }
}
