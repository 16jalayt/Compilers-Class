public class StateMachineAsSwitch
{
    public static void main( String[] args )
    {
        int START_STATE    = 0;
        int TERMINAL_STATE = 3;

        String candidate = args[0];
        char   next;

        int state = START_STATE;
        for (int i = 0; i < candidate.length(); i++)
        {
            next = candidate.charAt(i);
            switch (state)
            {
                case 0:
                    switch ( next )
                    {
                        case 'a': state = 1; break;
                        case 'b': state = 0;
                    }
                    break;

                case 1:
                    switch ( next )
                    {
                        case 'a': state = 1; break;
                        case 'b': state = 2;
                    }
                    break;

                case 2:
                    switch ( next )
                    {
                        case 'a': state = 1; break;
                        case 'b': state = 3;
                    }
                    break;

                case 3:
                    switch ( next )
                    {
                        case 'a': state = 1; break;
                        case 'b': state = 0;
                    }
                    break;
            }
        }

        if ( state == TERMINAL_STATE )
            System.out.println( "accept" );
        else
            System.out.println( "reject" );
    }
}
