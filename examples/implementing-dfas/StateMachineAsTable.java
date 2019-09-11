public class StateMachineAsTable
{
    public static void main( String[] args )
    {
        int START_STATE    = 0;
        int TERMINAL_STATE = 3;
        int[][] table = { {1, 0}, {1, 2}, {1, 3}, {1, 0} };

        String candidate = args[0];

        char next;
        int state = START_STATE;
        for (int i = 0; i < candidate.length(); i++)
        {
            next = candidate.charAt(i);
            state = table[state][next-'a'];
        }

        if ( state == TERMINAL_STATE )
            System.out.println( "accept" );
        else
            System.out.println( "reject" );
    }
}
