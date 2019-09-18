public class Scanner
{
    private static String prog;
    private static int pos;
    private static String token;

    public Scanner(String file)
    {
        prog = file;
        pos = 0;
        token = "";
    }

    public String next()
    {
        //clear token
        token = "";
        state0();
        return token;
    }

    public String peek()
    {
        return "";
    }

    private static boolean isDelineater()
    { return true;
        /*this code doesn't work for some reason
        if(" ".equals(prog.charAt(pos)))
            return true;
        else
            return false;*/
    }


    private static void state0()
    {System.out.println("state0");
        if ( pos >= prog.length() )
            return;
        else
            switch ( prog.charAt(pos) )
            {
                case 'i': pos++; state1(); break;
                default: pos ++; state0();
            }
    }

    private static void state1()
    {System.out.println("state 1");
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
    {System.out.println("state2");
        if ( pos >= prog.length() )
            return;
        else
            //if current char is whitespace accept if, otherwise could be iforsometing. Drop back to generic identifier.
            if(isDelineater())
            {
                token = "if";
                System.out.println("if accepted");
                return;
            }
            else
                state0();

    }
    //token class. type, value  string not enum. can do diff obj per type
}
