package src;

import java.util.*;
import src.Node;
public class Parser
{
    //TODO: generate tree as private variable. make instance method to access tree. This way it wont break the true/false tests

    ArrayList<String> Rules = new ArrayList<String>(Arrays.asList(
            "PROGRAM", "DEFINITIONS", "DEF", "FORMALS", "NONEMPTYFORMALS",
            "NEFREST", "FORMAL", "BODY", "PRINTBODY", "TYPE", "EXPR",
            "EXPRREST", "SIMPLEEXPR", "SIMPLEEXPRREST", "TERM", "TERMREST", "FACTOR",
            "NOTFACTOR", "NEGFACTOR", "IDENTIFIERMAIN", "IDENTIFIERREST",
            "ACTUALS", "NONEMPTYACTUALS", "NEAREST", "LITERAL", "PRINTSTATEMENT"));

    ArrayList<String> Terminals = new ArrayList<String>(Arrays.asList(
            "EOF", "print", "NUMBER", "BOOLEAN", "NULL", "not", "and",
            "or", "IDENTIFIER", "comma", "leftParen", "rightParen",
            "plus", "minus", "divide", "multiply", "equals", "lessThan",
            "colon", "if", "then", "else", "integer", "boolean",
            "function", "Error", "Comment"));

    ArrayList<String> RulesWhichCanNull = new ArrayList<String>(Arrays.asList(
            "DEFINITIONS", "FORMALS", "NEFREST", "EXPRREST", "SIMPLEEXPRREST",
            "TERMREST", "IDENTIFIERREST"));

    ArrayList<ArrayList<String>> ruleList= new ArrayList<ArrayList<String>>();

    ArrayList<String> semanticActionsList= new ArrayList<String>(Arrays.asList(
        "make-<PROGRAM>", "make-<DEF>", "make-<IDENTIFIER>", "make-<FORMALS>", 
        "make-integer", "make-boolean", "make-<BINARY>", 
        "make-<UNARY>", "make-<if-EXPR>", "make-<ACTUALS>",
        "make-<NUMBER>", "make-<BOOLEAN>", "make-Function-Call"));

    //TODO: if structure for all symantic atctions. node type hardcoded
    //move saction branch up a level?

    private static Scanner scan;
    int table[][];
    private final int tableX = 30;
    private final int tableY = 30;

    private Node tree = new Node.Program();

    public void getTree() {
        tree.Iterate(0);
    }

    public Parser(Scanner scan)
    {
        this.scan = scan;
        table = new int[tableX][tableY];
        createTable();
        createRuleList();
    }

    public boolean parse()
    {

        int nextFunc = 0;

        String EOFSymbol = "$";
        String starter = "PROGRAM";

        //cant be type token because can be nonterminal
        Stack<String> stack = new Stack<>();
        Stack<String> semanticStack = new Stack<>();
        Stack<Node> NodeStack = new Stack<>();

        // push the EOF symbol and the String PROGRAM onto the stack
        stack.push(EOFSymbol);
        stack.push(starter);
        Token next = new Token(Token.Type.Error,"");

        while (!stack.isEmpty())
        {
            while (scan.peek().getType().equals("Comment")){
                Token skip = scan.next();
            }
            String temp = stack.peek();
            //if tmp is terminal
            if(Terminals.contains(temp))
            {
                System.out.println("Terminals.contains(temp)" + Terminals.contains(temp));
                next = scan.next();

                if (temp.equals("NULL")){
                    stack.pop();
                }
                else if(temp.equals(getColumn(next))) {
                    semanticStack.push(next.value.toString());
                    stack.pop();
                }
                else
                {
                    System.out.println("Token mismatch error: " + temp + " and: " + getColumn(next));
                    return false;
                }
            }
            else if (Rules.contains(temp))
            {
                System.out.println("Rules.contains(temp)" + Rules.contains(temp));
                next = scan.peek();
                //Get the ordinal number for the rules and terminals, use that as table index values
                int row = Rules.indexOf(temp);
                String t = getColumn(next);
                int column = Terminals.indexOf(t);

                //really should redo table type
                int currentRule = table[row][column];
                if(currentRule != 0)
                {
                    stack.pop();
                    //Get the list of strings from ruleList, loop over them and push to stack.
                    //Strings in list should already be in reverse order.
                    ArrayList<String> tempArrayList = new ArrayList<String>();
                    //returning sublist not string. see if addAll works
                    tempArrayList.addAll(ruleList.get(currentRule));
                    for (String obj : ruleList.get(currentRule)) {
                        if (obj.equals("NULL")){
                            continue;
                        }
                        else {
                            stack.push(obj);
                        }
                    }
                }
                else if(RulesWhichCanNull.contains(temp)) {
                    stack.pop();
                }
                else
                {
                    System.out.println("Can not expand: " + temp + " on: " + getColumn(next));
                    return false;
                }
            }
            else if(semanticActionsList.contains(temp))
            {
                System.out.println("semanticActionsList.contains(temp)" + semanticActionsList.contains(temp));
                switch(temp) {
                    //there is static prog node at top, need to set that
                    case "make-<PROGRAM>":
                        tree.children.add(NodeStack.pop());
                        return true;
                    case "make-<DEF>":
                        NodeStack.push(new Node.Def(new Node[]{NodeStack.pop()}));
                        break;
                    case "make-<IDENTIFIER>":
                        NodeStack.push(new Node.Identifier(new Node[]{NodeStack.pop()}));
                        break;
                    case "make-<FORMALS>":
                        NodeStack.push(new Node.Formals(new Node[]{NodeStack.pop()}));
                        break;
                    case "make-integer":
                        semanticStack.pop();
                        NodeStack.push(new Node.Integer());
                        break;
                    case "make-boolean":
                        semanticStack.pop();
                        NodeStack.push(new Node.BooleanType());
                        break;
                    case "make-<BINARY>":
                        NodeStack.push(new Node.Binary(semanticStack.pop().charAt(0), new Node[]{NodeStack.pop(), NodeStack.pop()}));
                        break;
                    case "make-<UNARY>":
                        NodeStack.push(new Node.Unary(semanticStack.pop().charAt(0), new Node[]{NodeStack.pop()}));
                        break;
                    case "make-<if-EXPR>":
                        NodeStack.push(new Node.If(new Node[]{NodeStack.pop()}));
                        break;
                    case "make-<ACTUALS>":
                        NodeStack.push(new Node.Actuals(new Node[]{NodeStack.pop()}));
                        break;
                    case "make-<NUMBER>":
                        NodeStack.push(new Node.Number(Integer.parseInt(semanticStack.pop())));
                        break;
                    case "make-<BOOLEAN>":
                        if (semanticStack.pop().equals("true"))
                            NodeStack.push(new Node.BooleanValue(true));
                        else
                            NodeStack.push(new Node.BooleanValue(false));
                        break;
                    case "make-Function-Call":
                        NodeStack.push(new Node.FunctionCall(new Node[]{NodeStack.pop()}));
                        break;
                    default:
                        System.out.println("Unknown rule: " + temp);
                }
            }
            else if (temp.equals(EOFSymbol)  && next.type.equals(Token.Type.EOF))
            {
                return true;
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
        //valid states.
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
        table[7][13]  = 12;
        table[7][19]  = 12;
        table[8][1]   = 13;
        table[9][22]  = 14;
        table[9][23]  = 15;
        table[10][2]  = 16;
        table[10][3]  = 16;
        table[10][5]  = 16;
        table[10][8]  = 16;
        table[10][10] = 16;
        table[10][13] = 16;
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
        table[12][13] = 20;
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
        table[14][13] = 25;
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
        table[21][13] = 42;
        table[21][19] = 42;
        table[22][2]  = 43;
        table[22][3]  = 43;
        table[22][5]  = 43;
        table[22][8]  = 43;
        table[22][10] = 43;
        table[22][13] = 43;
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
        String h = tok.getValue();
        if (h.equals("print")){
            s = "print";
        }
        else if (t.equals("Identifier"))
        {
            s = "IDENTIFIER";
        }
        else if (t.equals("Integer"))
        {
            s = "NUMBER";
        }
        else if (t.equals("EOF"))
        {
            s = "EOF";
        }
        else if (t.equals("Punctuation")) {
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

    private void createRuleList()
    {
        //A list of lists, with each index of the first list corresponding to a rule number, and the nested list at
        //that index containing a list of rule strings in reverse order.

        ArrayList<String> tempList0 = new ArrayList<String>();
        ArrayList<String> tempList1 = new ArrayList<String>();
        ArrayList<String> tempList2 = new ArrayList<String>();
        ArrayList<String> tempList3 = new ArrayList<String>();
        ArrayList<String> tempList4 = new ArrayList<String>();
        ArrayList<String> tempList5 = new ArrayList<String>();
        ArrayList<String> tempList6 = new ArrayList<String>();
        ArrayList<String> tempList7 = new ArrayList<String>();
        ArrayList<String> tempList8 = new ArrayList<String>();
        ArrayList<String> tempList9 = new ArrayList<String>();
        ArrayList<String> tempList10 = new ArrayList<String>();
        ArrayList<String> tempList11 = new ArrayList<String>();
        ArrayList<String> tempList12 = new ArrayList<String>();
        ArrayList<String> tempList13 = new ArrayList<String>();
        ArrayList<String> tempList14 = new ArrayList<String>();
        ArrayList<String> tempList15 = new ArrayList<String>();
        ArrayList<String> tempList16 = new ArrayList<String>();
        ArrayList<String> tempList17 = new ArrayList<String>();
        ArrayList<String> tempList18 = new ArrayList<String>();
        ArrayList<String> tempList19 = new ArrayList<String>();
        ArrayList<String> tempList20 = new ArrayList<String>();
        ArrayList<String> tempList21 = new ArrayList<String>();
        ArrayList<String> tempList22 = new ArrayList<String>();
        ArrayList<String> tempList23 = new ArrayList<String>();
        ArrayList<String> tempList24 = new ArrayList<String>();
        ArrayList<String> tempList25 = new ArrayList<String>();
        ArrayList<String> tempList26 = new ArrayList<String>();
        ArrayList<String> tempList27 = new ArrayList<String>();
        ArrayList<String> tempList28 = new ArrayList<String>();
        ArrayList<String> tempList29 = new ArrayList<String>();
        ArrayList<String> tempList30 = new ArrayList<String>();
        ArrayList<String> tempList31 = new ArrayList<String>();
        ArrayList<String> tempList32 = new ArrayList<String>();
        ArrayList<String> tempList33 = new ArrayList<String>();
        ArrayList<String> tempList34 = new ArrayList<String>();
        ArrayList<String> tempList35 = new ArrayList<String>();
        ArrayList<String> tempList36 = new ArrayList<String>();
        ArrayList<String> tempList37 = new ArrayList<String>();
        ArrayList<String> tempList38 = new ArrayList<String>();
        ArrayList<String> tempList39 = new ArrayList<String>();
        ArrayList<String> tempList40 = new ArrayList<String>();
        ArrayList<String> tempList41 = new ArrayList<String>();
        ArrayList<String> tempList42 = new ArrayList<String>();
        ArrayList<String> tempList43 = new ArrayList<String>();
        ArrayList<String> tempList44 = new ArrayList<String>();
        ArrayList<String> tempList45 = new ArrayList<String>();
        ArrayList<String> tempList46 = new ArrayList<String>();
        ArrayList<String> tempList47 = new ArrayList<String>();
        ArrayList<String> tempList48 = new ArrayList<String>();

        //There is no rule 0, so index 0 is left blank
        tempList0.add("");
        ruleList.add(tempList0);
        //Create a list for each index position, then add it on to the overall list of lists
        tempList1.add("make-<PROGRAM>");
        tempList1.add("DEFINITIONS");
        ruleList.add(tempList1);
        tempList2.add("NULL");
        ruleList.add(tempList2);
        tempList3.add("DEFINITIONS");
        tempList3.add("DEF");
        ruleList.add(tempList3);
        tempList4.add("make-<DEF>");
        tempList4.add("BODY");
        tempList4.add("TYPE");
        tempList4.add("colon");
        tempList4.add("rightParen");
        tempList4.add("FORMALS");
        tempList4.add("leftParen");
        tempList4.add("make-<IDENTIFIER>");
        tempList4.add("IDENTIFIER");
        tempList4.add("function");
        ruleList.add(tempList4);
        tempList5.add("make-<FORMALS>");
        tempList5.add("NULL");
        ruleList.add(tempList5);
        tempList6.add("NONEMPTYFORMALS");
        ruleList.add(tempList6);
        tempList7.add("NEFREST");
        tempList7.add("FORMAL");
        ruleList.add(tempList7);
        tempList8.add("NONEMPTYFORMALS");
        tempList8.add("comma");
        ruleList.add(tempList8);
        tempList9.add("NULL");
        ruleList.add(tempList9);
        tempList10.add("make-<FORMAL>");
        tempList10.add("TYPE");
        tempList10.add("colon");
        tempList10.add("make-<IDENTIFIER>");
        tempList10.add("IDENTIFIER");
        ruleList.add(tempList10);
        tempList11.add("PRINTBODY");
        ruleList.add(tempList11);
        tempList12.add("make-<BODY>");
        tempList12.add("EXPR");
        ruleList.add(tempList12);
        tempList13.add("BODY");
        tempList13.add("PRINTSTATEMENT");
        ruleList.add(tempList13);
        tempList14.add("make-integer");
        tempList14.add("integer");
        ruleList.add(tempList14);
        tempList15.add("make-boolean");
        tempList15.add("boolean");
        ruleList.add(tempList15);
        tempList16.add("EXPRREST");
        tempList16.add("SIMPLEEXPR");
        ruleList.add(tempList16);
        tempList17.add("EXPRREST");
        tempList17.add("make-<BINARY>");
        tempList17.add("EXPR");
        tempList17.add("lessThan");
        ruleList.add(tempList17);
        tempList18.add("EXPRREST");
        tempList18.add("make-<BINARY>");
        tempList18.add("EXPR");
        tempList18.add("equals");
        ruleList.add(tempList18);
        tempList19.add("NULL");
        ruleList.add(tempList19);
        tempList20.add("SIMPLEEXPRREST");
        tempList20.add("TERM");
        ruleList.add(tempList20);
        tempList21.add("SIMPLEEXPRREST");
        tempList21.add("make-<BINARY>");
        tempList21.add("SIMPLEEXPR");
        tempList21.add("or");
        ruleList.add(tempList21);
        tempList22.add("SIMPLEEXPRREST");
        tempList22.add("make-<BINARY>");
        tempList22.add("SIMPLEEXPR");
        tempList22.add("plus");
        ruleList.add(tempList22);
        tempList23.add("SIMPLEEXPRREST");
        tempList23.add("make-<BINARY>");
        tempList23.add("SIMPLEEXPR");
        tempList23.add("minus");
        ruleList.add(tempList23);
        tempList24.add("NULL");
        ruleList.add(tempList24);
        tempList25.add("TERMREST");
        tempList25.add("FACTOR");
        ruleList.add(tempList25);
        tempList26.add("TERMREST");
        tempList26.add("make-<BINARY>");
        tempList26.add("TERM");
        tempList26.add("and");
        ruleList.add(tempList26);
        tempList27.add("TERMREST");
        tempList27.add("make-<BINARY>");
        tempList27.add("TERM");
        tempList27.add("multiply");
        ruleList.add(tempList27);
        tempList28.add("TERMREST");
        tempList28.add("make-<BINARY>");
        tempList28.add("TERM");
        tempList28.add("divide");
        ruleList.add(tempList28);
        tempList29.add("NULL");
        ruleList.add(tempList29);
        tempList30.add("make-<if-EXPR>");
        tempList30.add("EXPR");
        tempList30.add("else");
        tempList30.add("EXPR");
        tempList30.add("then");
        tempList30.add("EXPR");
        tempList30.add("if");
        ruleList.add(tempList30);
        tempList31.add("NOTFACTOR");
        ruleList.add(tempList31);
        tempList32.add("IDENTIFIERMAIN");
        ruleList.add(tempList32);
        tempList33.add("LITERAL");
        ruleList.add(tempList33);
        tempList34.add("NEGFACTOR");
        ruleList.add(tempList34);
        tempList35.add("rightParen");
        tempList35.add("EXPR");
        tempList35.add("leftParen");
        ruleList.add(tempList35);
        tempList36.add("make-<UNARY>");
        tempList36.add("FACTOR");
        tempList36.add("not");
        ruleList.add(tempList36);
        tempList37.add("make-<UNARY>");
        tempList37.add("FACTOR");
        tempList37.add("minus");
        ruleList.add(tempList37);
        tempList38.add("IDENTIFIERREST");
        tempList38.add("make-<IDENTIFIER>");
        tempList38.add("IDENTIFIER");
        ruleList.add(tempList38);
        tempList39.add("rightParen");
        tempList39.add("ACTUALS");
        tempList39.add("leftParen");
        ruleList.add(tempList39);
        tempList40.add("NULL");
        ruleList.add(tempList40);
        tempList41.add("NULL");
        ruleList.add(tempList41);
        tempList42.add("NONEMPTYACTUALS");
        ruleList.add(tempList42);
        tempList43.add("NEAREST");
        tempList43.add("EXPR");
        ruleList.add(tempList43);
        tempList44.add("NONEMPTYACTUALS");
        tempList44.add("comma");
        ruleList.add(tempList44);
        tempList45.add("make-<ACTUALS>");
        tempList45.add("NULL");
        ruleList.add(tempList45);
        tempList46.add("make-<NUMBER>");
        tempList46.add("NUMBER");
        ruleList.add(tempList46);
        tempList47.add("make-<BOOLEAN>");
        tempList47.add("BOOLEAN");
        ruleList.add(tempList47);
        tempList48.add("make-<Function-Call>");
        tempList48.add("rightParen");
        tempList48.add("EXPR");
        tempList48.add("leftParen");
        tempList48.add("print");
        ruleList.add(tempList48);
    }
}
