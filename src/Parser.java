import java.util.*;

public class Parser
{/*
    public enum Terminals
    {
        EOF(0), print(1), NUMBER(2), BOOLEAN(3), NULL(4), not(5), and(6),
        or(7), IDENTIFIER(8), comma(9), leftParen(10), rightParen(11),
        plus(12), minus(13), divide( 14), multiply(15), equals(16), lessThan(17),
        colon(18), If(19), then(20), Else(21), integer(22), Boolean(23),
        function(24), Error(25), Comment(26);

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

        public static boolean contains(String key)
        {
            if(map.containsKey(key))
                return true;
            else
                return false;

        }

        public static int getValue(String name) {
            return name.value;
        }
        public static void print() {System.out.println(map.toString());}
    }
    public enum Rule
    {
        NULL(100), PROGRAM(0), DEFINITIONS(1), DEF(2), FORMALS(3), NONEMPTYFORMALS(4),
        NEFREST(5), FORMAL(6), BODY(7), PRINTBODY(8), TYPE(9), EXPR(10),
        EXPRREST(11), SIMPLEEXPR(12), SIMPLEEXPRREST(13), TERM(14), TERMREST(15), FACTOR(16),
        NOTFACTOR(17), NEGFACTOR(18), IDENTIFIERMAIN(19), IDENTIFIERREST(20),
        ACTUALS(21), NONEMPTYACTUALS(22), NEAREST(23), LITERAL(24), PRINTSTATEMENT(25);

        private final String value;
        private static Map map = new HashMap<>();

        Rule(String st) {
            this.value = st;
        }
        static {
            for (Rule rule : Rule.values()) {
                map.put(rule.value, rule);
            }
        }

        public static Rule valueOf(int key) {
            return (Rule) map.get(key);
        }

        public static boolean contains(String key)
        {
            if(map.containsKey(key))
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


        public static int getValue(String name) {
            return name.value;
        }
    }*/

    ArrayList<String> Rules = new ArrayList<String>(Arrays.asList(
            "NULL", "PROGRAM", "DEFINITIONS", "DEF", "FORMALS", "NONEMPTYFORMALS",
            "NEFREST", "FORMAL", "BODY", "PRINTBODY", "TYPE", "EXPR",
            "EXPRREST", "SIMPLEEXPR", "SIMPLEEXPRREST", "TERM", "TERMREST", "FACTOR",
            "NOTFACTOR", "NEGFACTOR", "IDENTIFIERMAIN", "IDENTIFIERREST",
            "ACTUALS", "NONEMPTYACTUALS", "NEAREST", "LITERAL", "PRINTSTATEMENT"));

    ArrayList<String> Terminals = new ArrayList<String>(Arrays.asList(
            "EOF", "print", "NUMBER", "BOOLEAN", "NULL", "not", "and",
            "or", "IDENTIFIER", "comma", "leftParen", "rightParen",
            "plus", "minus", "divide", "multiply", "equals", "lessThan",
            "colon", "If", "then", "Else", "integer", "Boolean",
            "function", "Error", "Comment"));


    //public int currentRule;
    //public Terminals terminals;
    private static Scanner scan;
    int table[][];
    private final int tableX = 25;
    private final int tableY = 25;



    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        //currentRule = -1;
        //terminals = Terminals.NULL;
        createTable();
        createRuleList();
    }

    //TODO: return the tree
    public boolean parse()
    {
        //Token tok;
        //tmp. need default though
        //currentRule = Rule.Expression;
        int nextFunc = 0;

        String EOFSymbol = "$";
        String starter = "PROGRAM";

        //cant be type token because can be nonterminal
        Stack<String> stack = new Stack<>();

        // push the EOF symbol and the String PROGRAM onto the stack
        stack.push(EOFSymbol);
        stack.push(starter);
        Token next = new Token(Token.Type.Error,"");

        while (!stack.isEmpty())
        {
            String temp = stack.peek();
            //if tmp is terminal
            if(Terminals.contains(temp))
            {
                next = scan.next();

                if(temp.equals(next.type.toString()))
                    stack.pop();
                else
                {
                    System.out.println("Token mismatch error: " + temp + " and: " + next.type.toString());
                    return false;
                }
            }
            else if (Rules.contains(temp))
            {
                next = scan.peek();
                //Get the ordinal number for the rules and terminals, use that as table index values
                int row = Rules.indexOf(temp);
                String t = getColumn(next);
                int column = Terminals.indexOf(t);

                //really should redo table type
                int currentRule = table[row][column];
                if(currentRule != -1)
                {
                    stack.pop();
                    //Get the list of strings from ruleList, loop over them and push to stack.
                    //Strings in list should already be in reverse order.
                    ArrayList<String> tempArrayList = new ArrayList<String>();
                    tempArrayList.add(Rules.get(currentRule));
                    for (String obj : tempArrayList) {
                        stack.push(obj);
                    }
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

    private String getColumn(Token tok)
    {
        String t = tok.getType();
        String s = "";
        if (t.equals("Identifier"))
        {
            s = "IDENTIFIER";
        }
        else if (t.equals("Integer"))
        {
            s = "NUMBER";
        }
        else if (t.equals("Punctuation")) {
            String h = tok.getValue();
            if (h.equals(",")) {
                s = "comma";
            }
            else if (h.equals("(")) {
                s = "leftParen";
            }
            else if (h.equals(")")) {
                s = "rightParen";
            }
            else if (h.equals("+")) {
                s = "plus";
            }
            else if (h.equals("-")) {
                s = "minus";
            }
            else if (h.equals("/")) {
                s = "divide";
            }
            else if (h.equals("*")) {
                s = "multiply";
            }
            else if (h.equals("=")) {
                s = "equals";
            }
            else if (h.equals("<")) {
                s = "lessThan";
            }
            else {
                s = "colon";
            }
        }
        else if (t.equals("Keyword")) {
            s = tok.getValue();
            if (s.equals("true") || s.equals("false")) {
                s = "BOOLEAN";
            }
        }
        // add in else for f token.type() is EOF error or comment
        return s;

    }

    private void createRuleList(){
        //A list of lists, with each index of the first list corresponding to a rule number, and the nested list at
        //that index containing a list of rule strings in reverse order.
        ArrayList<ArrayList<String>> ruleList= new ArrayList<ArrayList<String>>();
        ArrayList<String> tempList0 = new ArrayList<String>();
        ArrayList<String> tempList1 = new ArrayList<String>();
        ArrayList<String> tempList2 = new ArrayList<String>();
        ArrayList<String> tempList3 = new ArrayList<String>();
        ArrayList<String> tempList4 = new ArrayList<String>();
        ArrayList<String> tempList5 = new ArrayList<String>();

        //There is no rule 0, so index 0 is left blank
        tempList0.add("");
        ruleList.add(tempList0);
        //Create a list for each index position, then add it on to the overall list of lists
        tempList1.add("DEFINITIONS");
        ruleList.add(tempList1);
        tempList2.add("NULL");
        ruleList.add(tempList2);
        tempList3.add("DEFINITIONS");
        tempList3.add("DEF");
        ruleList.add(tempList3);
        tempList4.add("BODY");
        tempList4.add("TYPE");
        tempList4.add("colon");
        tempList4.add("rightParen");
        tempList4.add("FORMALS");
        tempList4.add("leftParen");
        tempList4.add("IDENTIFIER");
        tempList4.add("function");
        ruleList.add(tempList4);
        tempList5.add("NULL");
        ruleList.add(tempList5);

    }
}
