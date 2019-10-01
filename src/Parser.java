import java.util.HashMap;

public class Parser
{
    private enum Rule
    {
        NULL, Formal, Body, Expression;
    }
    private enum NextTerminal
    {
        NULL, leftParen, rightParen;
    }

    public Rule currentRule;
    public NextTerminal nextTerminal;
    private static Scanner scan;
    int table[][];
    private final int tableX = 7;
    private final int tableY = 7;



    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        currentRule = Rule.NULL;
        nextTerminal = NextTerminal.NULL;
        createTable();


    }

    //TODO: return the tree
    public boolean parse()
    {
        Token tok;
        //tmp. need default though
        currentRule = Rule.Expression;
        int nextFunc = 0;
        while (scan.peek().type != Token.Type.EOF && scan.peek().type != Token.Type.Error)
        {

            //function for each rule to apply. switch statement for which to call
            //need to peek next token VALUE to get column
            tok = scan.peek();
            //System.out.println(tok.type.toString() + tok.type.ordinal());


            //need to check return value.
                if(tok.value.equals("("))
                    System.out.println(table[currentRule.ordinal()][NextTerminal.leftParen.ordinal()]);

                //switch (nextRuleVal)
            //case 1: //func
            //put token in tree
            tok = scan.next();
        }

        return true;
    }

    private void createTable()
    {
        //Initialize table with all invalid states
        for(int i=0; i<tableX; i++)
        {
            for(int j=0; j<tableY; j++)
            {
                table[i][j] = -1;
            }
        }

        //valid states. Starts counting from 1. Zero is invalid
        table[3][1] = 6;
    }
}
