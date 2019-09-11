public class StateMachineAsObjects
{
    public static void main( String[] args )
    {
        State[] states = { new State0(), new State1(),
                           new State2(), new State3() };
        String candidate = args[0];
        State start = states[0];

        System.out.println( start.process( states, candidate, 0 ) );
    }
}

interface State
{
    public String process( State[] states, String word, int pos );
}

class State0 implements State
{
    public String process( State[] states, String word, int pos )
    {
        if ( pos >= word.length() )
            return "reject";

        if ( word.charAt(pos) == 'a' )
            return states[1].process( states, word, pos+1 );
        // ( word.charAt(pos) == 'b' )
        return states[0].process( states, word, pos+1 );
    }
}

class State1 implements State
{
    public String process( State[] states, String word, int pos )
    {
        if ( pos >= word.length() )
            return "reject";

        if ( word.charAt(pos) == 'a' )
            return states[1].process( states, word, pos+1 );
        // ( word.charAt(pos) == 'b' )
        return states[2].process( states, word, pos+1 );
    }
}

class State2 implements State
{
    public String process( State[] states, String word, int pos )
    {
        if ( pos >= word.length() )
            return "reject";

        if ( word.charAt(pos) == 'a' )
            return states[1].process( states, word, pos+1 );
        // ( word.charAt(pos) == 'b' )
        return states[3].process( states, word, pos+1 );
    }
}

class State3 implements State
{
    public String process( State[] states, String word, int pos )
    {
        if ( pos >= word.length() )
            return "accept";

        if ( word.charAt(pos) == 'a' )
            return states[1].process( states, word, pos+1 );
        // ( word.charAt(pos) == 'b' )
        return states[3].process( states, word, pos+1 );
    }
}
