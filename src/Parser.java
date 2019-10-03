import java.util.*;

public class Parser
{
    public enum Terminals
    {
        NULL(0), print(1), NUMBER(2), EOF(3), Error(4), Comment(5),
        BOOLEAN(6), not(7), and(8), or(9), IDENTIFIER(10), leftParen(11), rightParen(12),
        plus(13), minus(14), equals(15), lessThan(16), colon(17), comma(18), If(19),
        then(20), Else(21), integer(22), Boolean(23), function(24);

        private final int value;
        private static Map<Integer, Terminals> map = new HashMap<Integer, Terminals>();

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

        public static boolean contains(int key)
        {
            if(map.get(key) != null)
                return true;
            else
                return false;
        }

        public int getValue() {
            return value;
        }
        public static void print() {System.out.println(map.toString());}
    }
    public enum Rule
    {
        NULL(100), PROGRAM(101), DEFINITIONS(102), DEF(103), FORMALS(104), NONEMPTYFORMALS(105),
        NEFREST(106), FORMAL(107), BODY(108), PRINTBODY(109), TYPE(110), EXPR(111),
        EXPRREST(112), SIMPLEEXPR(113), SIMPLEEXPRREST(114), TERM(115), TERMREST(116), FACTOR(117),
        NOTFACTOR(118), NEGFACTOR(119), IDENTIFIERACTUALS(120), ACTUALS(121), NONEMPTYACTUALS(122),
        NEAREST(123), LITERAL(124), PRINTSTATEMENT(125);

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


    public int currentRule;
    //public Terminals terminals;
    private static Scanner scan;
    int table[][];
    Map<Rule,Terminals> hm =
            new HashMap< Rule,Terminals>();
    private final int tableX = 25;
    private final int tableY = 25;



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
        table[0][0]   = 1;
        table[0][24]  = 1;
        table[1][0]   = 2;
        table[1][24]  = 3;
        table[2][24]  = 4;
        table[3][8]   = 6;
        table[3][11]  = 5;
        table[4][8]   = 7;
        table[5][9]   = 8;
        table[5][11]  = 9;
        table[6][8]   = 10;
        table[7][1]   = 11;
        table[7][2]   = 12;
        table[7][3]   = 12;
        table[7][5]   = 12;
        table[7][8]   = 12;
        table[7][10]  = 12;
        table[7][19]  = 12;
        table[8][1]   = 13;
        table[9][22]  = 14;
        table[9][23]  = 15;
        table[10][2]  = 16;
        table[10][3]  = 16;
        table[10][5]  = 16;
        table[10][8]  = 16;
        table[10][10] = 16;
        table[10][19] = 16;
        table[11][6]  = 19;
        table[11][9]  = 19;
        table[11][11] = 19;
        table[11][14] = 19;
        table[11][15] = 19;
        table[11][16] = 18;
        table[11][17] = 17;
        table[11][20] = 19;
        table[11][21] = 19;
        table[11][24] = 19;
        table[12][2]  = 20;
        table[12][3]  = 20;
        table[12][5]  = 20;
        table[12][8]  = 20;
        table[12][10] = 20;
        table[12][19] = 20;
        table[13][7]  = 21;
        table[13][12] = 22;
        table[13][13] = 23;
        table[13][16] = 24;
        table[13][17] = 24;
        table[14][2]  = 25;
        table[14][3]  = 25;
        table[14][5]  = 25;
        table[14][8]  = 25;
        table[14][10] = 25;
        table[14][19] = 25;
        table[15][6]  = 26;
        table[15][7]  = 29;
        table[15][12] = 29;
        table[15][13] = 29;
        table[15][14] = 28;
        table[15][15] = 27;
        table[16][2]  = 33;
        table[16][3]  = 33;
        table[16][5]  = 31;
        table[16][8]  = 32;
        table[16][10] = 35;
        table[16][13] = 34;
        table[16][19] = 30;
        table[17][5]  = 36;
        table[18][13] = 37;
        table[19][8]  = 38;
        table[20][6]  = 40;
        table[20][10] = 39;
        table[20][14] = 40;
        table[20][15] = 40;
        table[21][2]  = 42;
        table[21][3]  = 42;
        table[21][5]  = 42;
        table[21][8]  = 42;
        table[21][10] = 42;
        table[21][11] = 41;
        table[21][19] = 42;
        table[22][2]  = 43;
        table[22][3]  = 43;
        table[22][5]  = 43;
        table[22][8]  = 43;
        table[22][10] = 43;
        table[22][19] = 43;
        table[23][9]  = 44;
        table[23][11] = 45;
        table[24][2]  = 46;
        table[24][3]  = 47;
        table[25][1]  = 48;
    }
}
