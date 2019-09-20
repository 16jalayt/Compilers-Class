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
        while(prog.charAt(pos) == ' ')
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
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':pos++; state3(); break;
                //Check for punctuations.
                case '+': case '-': case '*': case '/': case '<': case '=': case '(': case ')': case ',': case ':': pos++; state59(); break;
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
                case 'c': case 'd': case 'g': case 'h':
                case 'j': case 'k': case 'l':
                case 'q': case 'r': case 's': case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z':
                case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                case 'Y': case 'Z':
                pos++; state58(); break;

                
                default: pos ++; state60();
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
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
    
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
        }
    
        //int accepted
        private static void state3()
        {
            
            //If true, then return the token.
            if (isDelineater()) {
                token.type = Token.Type.Integer;
                token.value = tokenSoFar;
            }
            else {
                tokenSoFar += prog.charAt(pos);
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
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state6(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60(); break;
    
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'd': pos++; state7(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'o': pos++; state9(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
                }
        }  
    
        //second o of boolean
        private static void state9()
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
                    case 'o': pos++; state10(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'l': pos++; state11(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state12(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'a': pos++; state13(); break;
                    //non explicit identifier
                    case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state14(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'l': pos++; state16(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
                    
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 's': pos++; state17(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state18(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'a': pos++; state20(); break;
                    //non explicit identifier
                    case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'i': pos++; state21(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state22(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'o': pos++; state24(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 't': pos++; state25(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'r': pos++; state27(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'r': pos++; state29(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'i': pos++; state30(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state31(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 't': pos++; state32(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'a': pos++; state34(); break;
                    case 'u': pos++; state51(); break;
                    //non explicit identifier
                    case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'l': pos++; state35(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 's': pos++; state36(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state37(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'h': pos++; state39(); break;
                    case 'r': pos++; state42(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state40(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state41(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'u': pos++; state43(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state44(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 't': pos++; state46(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state47(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'g': pos++; state48(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'e': pos++; state49(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'r': pos++; state50(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state52(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'c': pos++; state53(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 't': pos++; state54(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'i': pos++; state55(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'o': pos++; state56(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    case 'n': pos++; state57(); break;
                    //non explicit identifier
                    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
                    case 'i': case 'j': case 'k': case 'l': case 'm': case 'o': case 'p':
                    case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                    case 'y': case 'z':
                    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H':
                    case 'I': case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                    case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
                    case 'Y': case 'Z':
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_':
                    pos++; state58(); break;
                    default: pos++; state60();
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
                    tokenSoFar += prog.charAt(pos);
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
                        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                        case '_':
                        pos++; state58(); break;
                        default: pos++; state60();
                    }
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
                    token.type = Token.Type.Identifier;
                    token.value = tokenSoFar;
                    return;
                }
                tokenSoFar += prog.charAt(pos);
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
                    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    case '_': 
                    pos++; state58(); break;
                    default: pos++; state60();
                }
        }   
        
        
        //comments
        private static void state59()
        {
            if ( pos > prog.length() )
            {
                token.type = Token.Type.EOF;
                token.value = "";
                return;
            }
            else
            {
                if(prog.charAt(pos-1) == '(')
                {
                    if (prog.charAt(pos) == '*')
                    {
                        int i = 2;
                        while (true)
                        {
                            if (i > prog.length() -2)
                            {
                                //System.out.println("WTF Fail");
                                token.type = Token.Type.Error;
                                token.value = "Unmatched comment block at " + pos;
                                return;
                            }
                            
                            if(prog.charAt(i) == '*')
                            if(prog.charAt(i+1) == ')')
                            break;
                            i++;
                            
                        }
                        //System.out.println(i);
                        pos = i + 2;
                        token.type = Token.Type.Comment;
                        token.value = "";
                        
                    }
                    else
                    state4();
                }
                else
                state4();
            }
        }
        
        //Is not any type of recognizeable token
        private static void state60()
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
                    token.type = Token.Type.Error;
                    token.value = tokenSoFar;
                    return;
                }
                tokenSoFar += prog.charAt(pos);
                switch ( prog.charAt(pos) )
                {
                    default: pos++; state60();
                }
        }
    
}
