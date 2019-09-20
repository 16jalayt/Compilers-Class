public class Token
{//token class. type, value  string not enum. can do diff obj per type
    public Type type;
    public enum Type
    {
        Punctuation, Keyword, Identifier, Integer, EOF, Error, Comment;
    }
    public String value;

    public Token(Type typeInit, String valueInit)
    {
        type = typeInit;
        value = valueInit;
    }
    public Token()
    {
        type = Type.Error;
        value = "";
    }
    public String toString()
    {
        return type.toString() + "      " + value;
    }

}
