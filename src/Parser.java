import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Parser
{   //TODO: on parole
    private enum Rule
    {
        NULL, Formal, Body, Expression;
    }
    //TODO:make all terminals
    //make into arraylist
    /*private enum Terminals
    {
        NULL, leftParen, rightParen;
    }*/
    ArrayList<String> terminal = new ArrayList<String>(Arrays.asList(new String[]
            {"print", "("}));
    ArrayList<String> nonterminal = new ArrayList<String>(Arrays.asList(new String[]
            {"program", "definitions"}));

    public Rule currentRule;
    //public Terminals terminals;
    private static Scanner scan;
    int table[][];
    private final int tableX = 7;
    private final int tableY = 7;



    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        currentRule = Rule.NULL;
        //terminals = Terminals.NULL;
        createTable();


    }

    //TODO: return the tree
    public boolean parse()
    {
        //Token tok;
        //tmp. need default though
        currentRule = Rule.Expression;
        int nextFunc = 0;
        //cant be type token because can be nonterminal
        Stack<Token> stack = new Stack<>();
        stack.push(new Token(Token.Type.EOF,"null"));
        Token next = new Token(Token.Type.Error,"");
        while (!stack.isEmpty())
        {
            Token temp = stack.peek();
            //if tmp is terminal
            if(terminal.contains(temp.value))
            {
                next = scan.next();
                if(temp.type == next.type)
                    stack.pop();
                else
                {
                    System.out.println("Token mismatch error: " + temp.type.toString() + " and: " + next.type.toString());
                    return false;
                }
            }
            else if (nonterminal.contains(temp.value))
            {
                next = scan.peek();
                int rule = table[temp][next.type];
                if(rule != -1)
                {
                    stack.pop();
                    stack.push(rule);
                }
                else
                {
                    System.out.println("Can not expand: " + temp.type.toString() + " on: " + next.type.toString());
                    return false;
                }

            }
            else
            {
                System.out.println("Invalid item on stack: " + temp.type.toString());
                return false;
            }
        }
        if(next.type != Token.Type.EOF)
        {
            System.out.println("Unexpected token at end: " + next.type.toString());
            return false;
        }
        return true;
    }

    private void createTable()
    {
        //Initialize table with all invalid states. could use null too
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
