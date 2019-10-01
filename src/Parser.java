import java.util.HashMap;

public class Parser
{
    private static Scanner scan;
    int table[][];
    private final int tableX = 7;
    private final int tableY = 7;

    private enum rule
    {
        Punctuation, Keyword, Identifier, Integer, EOF, Error, Comment;
    }
    private enum nextTerminal
    {
        Punctuation, Keyword, Identifier, Integer, EOF, Error, Comment;
    }

    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        createTable();


    }

    //TODO: return the tree
    public void parse()
    {
        Token tok;
        while (scan.peek().type != Token.Type.EOF && scan.peek().type != Token.Type.Error)
        {
            tok = scan.next();
            System.out.println(tok.type.toString() + tok.type.ordinal());
            //not sure how to get first
            System.out.println(table[3][tok.type.ordinal()]);
            //put token in tree
            //parse.run(tok);
            //System.out.println(tok.toString());
        }

        //one last call to display EOF
        //System.out.println(scan.peek().toString());
    }

    private void createTable()
    {
        for(int i=0; i<tableX; i++)
        {
            for(int j=0; j<tableY; j++)
            {
                table[i][j] = -1;
            }
        }

        //actual states. Not zero indexed?
        table[3][1] = 6;
    }
}
