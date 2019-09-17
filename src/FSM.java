public class FSM
{
    public static void state0( String word, int pos )
    {
        if ( pos >= word.length() )
            System.out.println( "reject" );
        else
            switch ( word.charAt(pos) )
            {
                //case 'a': state1( word, pos+1 ); break;
                case 'b': state0( word, pos+1 );
            }
    }
}
