public class StateMachineAsSwitch
{
    public static void main( String[] args )
    {
        String candidate = args[0];
        state0( candidate, 0 );
    }

    public static void state0( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "reject" );
        else
            switch ( word.charAt(pos) )
            {
                case 'a': state1( word, pos+1 ); break;
                case 'b': state0( word, pos+1 );
            }
    }

    public static void state1( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "reject" );
        else
            switch ( word.charAt(pos) )
            {
                case 'a': state1( word, pos+1 ); break;
                case 'b': state2( word, pos+1 );
            }
    }

    public static void state2( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "reject" );
        else
            switch ( word.charAt(pos) )
            {
                case 'a': state1( word, pos+1 ); break;
                case 'b': state3( word, pos+1 );
            }
    }

    public static void state3( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "accept" );
        else
            switch ( word.charAt(pos) )
            {
                case 'a': state1( word, pos+1 ); break;
                case 'b': state0( word, pos+1 );
            }
    }
}
