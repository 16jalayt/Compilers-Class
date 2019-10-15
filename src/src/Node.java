package src;

public class Node
{
    public static class Program extends Node
    {

    }
    public static class Definitions extends Node
    {

    }
    public static class Identifier extends Node
    {

    }
    public static class Formals extends Node
    {

    }
    public static class Integer extends Node
    {
        public int value;
    }
    public static class Boolean extends Node
    {
        public boolean value;
    }
    public static class Binary extends Node
    {
        public char value;
        public Binary(char value)
        {
            this.value = value;
        }
    }
    public static class Unary extends Node
    {
        public char value;
    }
    public static class If extends Node
    {

    }
    public static class Actuals extends Node
    {

    }
    public static class FunctionCall extends Node
    {

    }
}
