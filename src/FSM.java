public class FSM
{
    public static void state0( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "identifier " + word);
        else
            switch ( word.charAt(pos) )
            {
                case 'i': state1( word, pos+1 ); break;
                default: state0( word, pos+1 );
            }
    }

    public static void state1( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "keyword if" );
        else
            switch ( word.charAt(pos) )
            {
                case 'f': state1( word, pos+1 ); break;
                default: state0( word, pos+1 );
            }
    }
}
