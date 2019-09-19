import java.util.ArrayList;

public class Scanner
{
    private static String prog;
    private static int pos;
    private static Token token;
    private static Token lookAhead;
    private static ArrayList<Character> delim = new ArrayList<Character>()
    {{
        add(' '); add('+'); add('-'); add('*'); add('/');
        add('<'); add('='); add('('); add(')'); add(','); add(';');
    }};
    private String intString = "";

    public Scanner(String file)
    {
        prog = file;
        pos = 0;
        token = new Token();
    }

    public Token next()
    {
        if(lookAhead != null)
        {
            Token tmp = lookAhead;
            lookAhead = null;
            return tmp;
        }
       return getNext();
    }

    public Token peek()
    {
        if(lookAhead == null)
            lookAhead = getNext();
        return lookAhead;
    }

    private Token getNext()
    {
        //up front EOF check
        if ( pos >= prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return token;
        }
        while(prog.charAt(pos) != ' ')
        pos++;

        //clear token
        token = new Token();
        //go to start state
        state0();
        return token;
    }

    private static boolean isDelineater()
    {
        if ( pos >= prog.length() )
            return true;
        else if (delim.contains(prog.charAt(pos)))
            return true;
        else
            return false;
    }
//0 is default state. Check positive criteria, then invalid tokens, then known valid. return identifier.
    private static void state0()
    {
        //purely to stop an exception from happening at end of file
        if ( pos >= prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            switch ( prog.charAt(pos) )
            {
                case 'i': pos++; state1(); break;
                //Check for integers.
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: pos++; state3(); break;
                //Check for punctuations.
                case '+': case '-': case '*': case '/': case '<': case '=': case '(': case ')': case ',': case ':': pos++; state4(); break;
                default: pos ++; state0();
            }
    }
//i detected
    private static void state1()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            switch ( prog.charAt(pos) )
            {
                case 'f': pos++; state2(); break;
                default: pos++; state0();
            }
    }
    //if accepted
    private static void state2()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept if, otherwise could be iforsometing. Drop back to generic identifier.
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "if";
                return;
            }
            else
                state0();

    }

    //int accepted
    private static void state3()
    {
        //Adds the previous character to a string of integers
        String tempString = intString.concat(prog.charAt(pos - 1));
        intString = tempString;

        //If true, then return the token.
        if (isDelineater()) {
            token.type = Token.Type.Integer;
            token.value = inString.toString();
            inString = "";
        }
        else {
            switch(prog.charAt(pos)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    pos++;
                    //If true, then this character was the last in the file, and is the end of the int token.
                    if (pos > prog.length()){
                        token.type = Token.Type.Integer;
                        token.value = inString.toString();
                        inString = "";
                        break;
                    }
                    state3();
                    break;
                //This should call some sort of error, as no identifier can start with an int.
                //For now, I just sent it back to state0().
                default: pos++; state0();
            }
        }
        return;
    }


    //punctuation
    private static void state4()
    {
        token.type = Token.Type.Punctuation;
        token.value = String.valueOf(prog.charAt(pos-1));
        return;
    }

}
