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
                case 'a': pos++; state5(); break;
                case 'b': pos++; state8(); break;
                case 'e': pos++; state15(); break;
                //case 'f': pos++; state33(); break;
                case 'm': pos++; state19(); break;
                case 'n': pos++; state23(); break;
                case '0': pos++; state26(); break;
                case 'p': pos++; state28(); break;
                //case 't': pos++; state38(); break;
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

    //recognize n in and
    private static void state5()
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
                case 'n': pos++; state6(); break;
                default: pos++; state0();
            }
    }

    //recognize d in and
    private static void state6()
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
                case 'd': pos++; state7(); break;
                default: pos++; state0();
            }
    }  

    //keyword and accepted
    private static void state7()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept and
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "and";
                return;
            }
            else
                state0();
    }
    
    //first o of boolean
    private static void state8()
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
                case 'o': pos++; state9(); break;
                default: pos++; state0();
            }
    }  

    //second 0 of boolean
    private static void state9()
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
                case 'o': pos++; state10(); break;
                default: pos++; state0();
            }
    }  

    //l of boolean
    private static void state10()
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
                case 'l': pos++; state11(); break;
                default: pos++; state0();
            }
    }  

    //e of boolean
    private static void state11()
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
                case 'e': pos++; state12(); break;
                default: pos++; state0();
            }
    }  

    //a of boolean
    private static void state12()
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
                case 'a': pos++; state13(); break;
                default: pos++; state0();
            }
    } 
    
    //n of boolean
    private static void state13()
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
                case 'n': pos++; state14(); break;
                default: pos++; state0();
            }
    } 

    //keyword boolean accepted
    private static void state14()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "boolean";
                return;
            }
            else
                state0();
    } 

    //l of else
    private static void state15()
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
                case 'l': pos++; state16(); break;
                default: pos++; state0();
            }
    }

    //s of else
    private static void state16()
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
                case 's': pos++; state17(); break;
                default: pos++; state0();
            }
    }

    //e of else
    private static void state17()
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
                case 'e': pos++; state18(); break;
                default: pos++; state0();
            }
    }

    //keyword else accepted
    private static void state18()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "else";
                return;
            }
            else
                state0();
    }

    //a of main
    private static void state19()
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
                case 'a': pos++; state20(); break;
                default: pos++; state0();
            }
    }

    //i of main
    private static void state20()
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
                case 'i': pos++; state21(); break;
                default: pos++; state0();
            }
    } 

    //n of main
    private static void state21()
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
                case 'n': pos++; state22(); break;
                default: pos++; state0();
            }
    } 

    //indentifier main accepted
    private static void state22()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Identifier;
                token.value = "main";
                return;
            }
            else
                state0();
    }

    //o of not
    private static void state23()
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
                case 'o': pos++; state24(); break;
                default: pos++; state0();
            }
    } 

    //t of not
    private static void state24()
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
                case 't': pos++; state25(); break;
                default: pos++; state0();
            }
    }
    
    //keyword not accepted
    private static void state25()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "not";
                return;
            }
            else
                state0();
    }

    //r of or
    private static void state26()
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
                case 'r': pos++; state27(); break;
                default: pos++; state0();
            }
    }
    
    //keyword or accepted
    private static void state27()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "or";
                return;
            }
            else
                state0();
    }

    //r of print
    private static void state28()
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
                case 'r': pos++; state29(); break;
                default: pos++; state0();
            }
    }

    //i of print
    private static void state29()
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
                case 'i': pos++; state30(); break;
                default: pos++; state0();
            }
    }

    //n of print
    private static void state30()
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
                case 'n': pos++; state31(); break;
                default: pos++; state0();
            }
    }

    //t of print
    private static void state31()
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
                case 't': pos++; state32(); break;
                default: pos++; state0();
            }
    }
    
    //identifier print accepted
    private static void state32()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            //if current char is whitespace accept boolean
            if(isDelineater())
            {
                token.type = Token.Type.Identifier;
                token.value = "print";
                return;
            }
            else
                state0();
    }         
}
