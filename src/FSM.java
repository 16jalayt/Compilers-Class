public class FSM
{
    public static void state0( String prog, int pos )
    {
        if ( pos >= prog.length() )
            return;
        else
            switch ( prog.charAt(pos) )
            {
                case 'i': state1( prog, pos+1 ); break;
                default: state0( prog, pos+1 );
            }
    }

    public static void state1( String prog, int pos )
    {
        if ( pos >= prog.length() )
            return;
        else
            switch ( prog.charAt(pos) )
            {
                case 'f': state1( prog, pos+1 ); break;
                default: state0( prog, pos+1 );
            }
    }
}
