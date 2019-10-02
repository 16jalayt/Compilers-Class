import java.util.*;

public class Parser
{   //https://codingexplained.com/coding/java/enum-to-integer-and-integer-to-enum
    private enum Rule
    {
        NULL, PROGRAM, DEFINITIONS, DEF, FORMALS, NONEMPTYFORMALS, NEFREST, FORMAL, BODY, PRINTBODY,
        TYPE, EXPR, EXPRREST, SIMPLEEXPR, SIMPLEEXPRREST, TERM, TERMREST, FACTOR, NOTFACTOR, NEGFACTOR,
        IDENTIFIERACTUALS, ACTUALS, NONEMPTYACTUALS, NEAREST, LITERAL, PRINTSTATEMENT;

        private int value;
        private int count = 100;
        private static Map map = new HashMap<>();

        private Rule() {
            this.value = count;
            count++;
        }

        static
        {
            for (Rule rule : Rule.values())
            {
                map.put(rule.value, rule);
            }
        }

        public static Rule valueOf(int rule)
        {
            return (Rule) map.get(rule);
        }
        public static int length()
        {
            return map.size();
        }
        public static boolean contains(int obj)
        {
            if(map.containsValue(obj))
                return true;
            else
                return false;
        }
        public int getValue() {
            return value;
        }
    }
    //TODO:make all terminals
    //make into arraylist
    private enum Terminals
    {
        NULL, print, NUMBER, BOOLEAN, not, and, or, IDENTIFIER, leftParen, rightParen, plus, minus,
        equals, lessThan, colon, comma, If, then, Else, integer, Boolean, function, EOF;

        private int value;
        private int count = 200;
        private static Map map = new HashMap<>();

        private Terminals() {
            this.value = count;
            count++;
        }

        static
        {
            for (Terminals terminals : Terminals.values())
            {
                map.put(terminals.value, terminals);
            }
        }

        public static Terminals valueOf(int terminals)
        {
            return (Terminals) map.get(terminals);
        }
        public static int length()
        {
            return map.size();
        }
        public static boolean contains(int obj)
        {
            if(map.containsValue(obj))
                return true;
            else
                return false;
        }
        public int getValue() {
            return value;
        }
    }
    //ArrayList<String> terminal = new ArrayList<String>(Arrays.asList(new String[]
            //{"print", "("}));
    //same as enum Rule above
    //ArrayList<String> nonterminal = new ArrayList<String>(Arrays.asList(new String[]
            //{"program", "definitions"}));

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
        //currentRule = Rule.Expression;
        int nextFunc = 0;
        //cant be type token because can be nonterminal
        Stack<Integer> stack = new Stack<>();
        stack.push(Terminals.EOF.getValue());
        Token next = new Token(Token.Type.Error,"");
        while (!stack.isEmpty())
        {
            int temp = stack.peek();
            //if tmp is terminal
            if(Terminals.contains(temp))
            {
                next = scan.next();
                //if(temp == next.type.ordinal())
                //////////////////line where i get confused
                if(temp == Terminals.contains(next.type.ordinal()))
                    stack.pop();
                else
                {
                    System.out.println("Token mismatch error: " + temp + " and: " + next.type.toString());
                    return false;
                }
            }
            //else if (nonterminal.contains(temp))
            else if (Rule.contains(temp))
            {
                next = scan.peek();
                currentRule = Rule.valueOf(table[temp][next.type.ordinal()]);
                if(currentRule.ordinal() != -1)
                {
                    stack.pop();
                    stack.push(currentRule.ordinal());
                }
                else
                {
                    System.out.println("Can not expand: " + temp + " on: " + next.type.toString());
                    return false;
                }

            }
            else
            {
                System.out.println("Invalid item on stack: " + temp);
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
