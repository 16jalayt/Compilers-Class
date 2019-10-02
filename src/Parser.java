import java.util.*;

public class Parser
{
    public enum Terminals
    {
        NULL(0), print(1), NUMBER(2), EOF(3);

        private final int value;
        private static Map map = new HashMap<>();

        Terminals(int number) {
            this.value = number;
        }
        static {
            for (Terminals term : Terminals.values()) {
                map.put(term.value, term);
            }
        }

        public static Terminals valueOf(int key) {
            return (Terminals) map.get(key);
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
    public enum Rule
    {
        NULL(0), PROGRAM(1), DEFINITIONS(2);

        private final int value;
        private static Map map = new HashMap<>();

        Rule(int number) {
            this.value = number;
        }
        static {
            for (Rule rule : Rule.values()) {
                map.put(rule.value, rule);
            }
        }

        public static Rule valueOf(int key) {
            return (Rule) map.get(key);
        }

        public static boolean contains(int obj)
        {
            if(map.containsValue(obj))
                return true;
            else
                return false;
        }
        public static boolean containsType(Token obj)
        {
            if(map.containsKey(obj.type))
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

    public int currentRule;
    //public Terminals terminals;
    private static Scanner scan;
    int table[][];
    Map<Rule,Terminals> hm =
            new HashMap< Rule,Terminals>();
    private final int tableX = 7;
    private final int tableY = 7;



    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        currentRule = -1;
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

                if(Parser.Terminals.valueOf(temp).toString().equals(next.type.toString()))
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
                currentRule = table[temp][next.type.ordinal()];
                if(currentRule != -1)
                {
                    stack.pop();
                    stack.push(currentRule);
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
