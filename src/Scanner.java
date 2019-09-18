public class Scanner
{
    private static String prog;
    private static int pos;
    private static Token token;
    private static Token lookAhead;
//TODO: get ranges for types of states, so more seperated
    public Scanner(String file)
    {
        prog = file;
        pos = 0;
        token = new Token();
    }

    public Token next()
    {//TODO: put white space remover here too
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
        //clear token
        token = new Token();
        state0();
        return token;
    }

    //TODO: just create list to check?
    private static boolean isDelineater()
    {
        //this code doesn't work for some reason
        if(prog.charAt(pos) == ' ')
            return true;
        else
            return false;
    }


    private static void state0()
    {
        //purely to stop an exception from happening at end of file
        if ( pos >= prog.length() )
            return;
        else
            switch ( prog.charAt(pos) )
            {
                case 'i': pos++; state1(); break;
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: pos++; state3(); break;
                default: pos ++; state0();
            }
    }

    private static void state1()
    {
        if ( pos >= prog.length() )
            return;
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
        if ( pos >= prog.length() )
            return;
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
        if ( pos >= prog.length() )
            return;
        else
            //TODO: make string builder here appening numbers until end
            if(isDelineater())
            {
                //token.type = Token.Type.Intager;
                //token.value = sb.toString();
                return;
            }
    }

}
