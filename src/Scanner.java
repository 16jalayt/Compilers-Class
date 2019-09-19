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

    //put tokenSoFar += prog.charAt(pos); in any non final states and set the token value to toenSoFar when returning
    private static String tokenSoFar;

    public Scanner(String file)
    {
        prog = file;
        pos = 0;
        token = new Token();
        tokenSoFar = "";
    }

    public Token next()
    {
        if(lookAhead != null)
        {
            Token tmp = lookAhead;
            lookAhead = null;
            return tmp;
        }
        tokenSoFar = "";
        return getNext();
    }

    public Token peek()
    {
        if(lookAhead == null)
            lookAhead = getNext();
        tokenSoFar = "";
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
            tokenSoFar += prog.charAt(pos);
            switch ( prog.charAt(pos) )
            {
                case 'i': pos++; state1(); break;
                //Check for integers.
                case '0': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':pos++; state3(); break;
                //Check for punctuations.
                case '+': case '-': case '*': case '/': case '<': case '=': case '(': case ')': case ',': case ':': pos++; state4(); break;
                case 'a': pos++; state5(); break;
                case 'b': pos++; state8(); break;
                case 'e': pos++; state15(); break;
                case 'f': pos++; state33(); break;
                case 'm': pos++; state19(); break;
                case 'n': pos++; state23(); break;
                case 'o': pos++; state26(); break;
                case 'p': pos++; state28(); break;
                case 't': pos++; state38(); break;

                //non explicit identifiers

                
                default: pos ++; state0();
            }
    }
//f of if || n of integer
    private static void state1()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            tokenSoFar += prog.charAt(pos);
            switch ( prog.charAt(pos) )
            {
                case 'f': pos++; state2(); break;
                case 'n': pos++; state2(); break;
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
                token.value = tokenSoFar;
                return;
            }
            else
                state0();

    }

    //int accepted
    private static void state3()
    {
        tokenSoFar += prog.charAt(pos);

        //If true, then return the token.
        if (isDelineater()) {
            token.type = Token.Type.Integer;
            token.value = tokenSoFar;
        }
        else {
            switch(prog.charAt(pos)) {
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    pos++;
                    //If true, then this character was the last in the file, and is the end of the int token.
                    if (pos > prog.length()){
                        token.type = Token.Type.Integer;
                        token.value = tokenSoFar;
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
            if(isDelineater())
            {
                token.type = Token.Type.Identifier;
                token.value = "print";
                return;
            }
            else
                state0();
    }

    //a of false || u of function
    private static void state33()
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
                case 'a': pos++; state34(); break;
                case 'u': pos++; state51(); break;
                default: pos++; state0();
            }
    }

    //l of false
    private static void state34()
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
                case 'l': pos++; state35(); break;
                default: pos++; state0();
            }
    }

    //s of false
    private static void state35()
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
                case 's': pos++; state36(); break;
                default: pos++; state0();
            }
    }

    //e of false
    private static void state36()
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
                case 'e': pos++; state37(); break;
                default: pos++; state0();
            }
    }
    
    //identifier false accepted
    private static void state37()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            if(isDelineater())
            {
                token.type = Token.Type.Identifier;
                token.value = "false";
                return;
            }
            else
                state0();
    }

    //h of then || r of true
    private static void state38()
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
                case 'h': pos++; state39(); break;
                case 'r': pos++; state42(); break;
                default: pos++; state0();
            }
    }

    //e of then
    private static void state39()
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
                case 'e': pos++; state40(); break;
                default: pos++; state0();
            }
    }

    //n of then
    private static void state40()
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
                case 'n': pos++; state41(); break;
                default: pos++; state0();
            }
    }
    
    //keyword then accepted
    private static void state41()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "then";
                return;
            }
            else
                state0();
    } 

    //u of true
    private static void state42()
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
                case 'u': pos++; state43(); break;
                default: pos++; state0();
            }
    } 

    //e of true
    private static void state43()
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
                case 'e': pos++; state44(); break;
                default: pos++; state0();
            }
    }
    
    //Identifier true accepted
    private static void state44()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            if(isDelineater())
            {
                token.type = Token.Type.Identifier;
                token.value = "true";
                return;
            }
            else
                state0();
    } 

    //t of integer
    private static void state45()
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
                case 't': pos++; state46(); break;
                default: pos++; state0();
            }
    }

    //e of integer
    private static void state46()
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
                case 'e': pos++; state47(); break;
                default: pos++; state0();
            }
    }

    //g of integer
    private static void state47()
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
                case 'g': pos++; state48(); break;
                default: pos++; state0();
            }
    }

    //e of integer
    private static void state48()
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
                case 'e': pos++; state49(); break;
                default: pos++; state0();
            }
    }

    //r of integer
    private static void state49()
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
                case 'r': pos++; state50(); break;
                default: pos++; state0();
            }
    }
    
    //Keyword integer accepted
    private static void state50()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "integer";
                return;
            }
            else
                state0();
    }
    
    //first n of function
    private static void state51()
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
                case 'n': pos++; state52(); break;
                default: pos++; state0();
            }
    }
    
    //c of function
    private static void state52()
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
                case 'c': pos++; state53(); break;
                default: pos++; state0();
            }
    }
    
    //t of function
    private static void state53()
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
                case 't': pos++; state54(); break;
                default: pos++; state0();
            }
    }
    
    //i of function
    private static void state54()
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
                case 'i': pos++; state55(); break;
                default: pos++; state0();
            }
    }
    
    //o of function
    private static void state55()
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
                case 'o': pos++; state56(); break;
                default: pos++; state0();
            }
    }
    
    //last n of function
    private static void state56()
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
                case 'r': pos++; state57(); break;
                default: pos++; state0();
            }
    }
    
    //Keyword function accepted
    private static void state57()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
            if(isDelineater())
            {
                token.type = Token.Type.Keyword;
                token.value = "function";
                return;
            }
            else
                state0();
    }    
    
    //accept non explicit identifier || have additional chr added to non explicit identifier
    private static void state58()
    {
        if ( pos > prog.length() )
        {
            token.type = Token.Type.EOF;
            token.value = "";
            return;
        }
        else
         if(isDelineater())
         {
             //token.type = Token.Type.identifier;
             token.value = "placeholder text for non explicit identifier";
             return;
         }
            switch ( prog.charAt(pos) )
            {
                case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z':
                case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                case 'Y': case 'Z':
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9:
                case '_': 
                pos++; state58(); break;
                default: pos++; state0();
            }
    }
           
}
