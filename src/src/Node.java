package src;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Node
{
    public Node parent;
    public String name;
    public List<Node> children = new LinkedList<Node>();
    //need empty as default
    public Node()
    {
        this.parent = null;
    }

    public Node(Node[] children_array)
    {
        if(children_array == null)
            children = null;
        else
        {
            for (Node child : children_array)
                children.add(child);
        }
    }

    //Will probably need to tweak to get the indenting correct, but otherwise should work. n is # of indents
    public void Iterate(int n)
    {
        for (int i=0; i < this.children.size(); i++)
        {
            Node child = this.children.get(i);
            //Indents each child
            for (int m=0; m<n; m++)
            {
                System.out.print("\t");
            }
            System.out.print(child.name);
            System.out.print(" ");
            //System.out.println(child.value);

            if(child.children != null)
            {
                child.Iterate(n+1);
            }
        }

    }
    public static class Program extends Node
    {
        public Program() {

            this.name = "Program";
        }
    }
    public static class Def extends Node
    {
        public Def(Node[] children) {
            super(children);
            this.name = "Def";
        }
    }
    public static class Identifier extends Node
    {
        public Identifier(Node[] children) {
            super(children);
            this.name = "Identifier";
        }
    }
    public static class Formals extends Node
    {
        public Formals(Node[] children) { super(children);
            this.name = "Formals";
        }
    }
    public static class Integer extends Node
    {
        public int value;

        public Integer(int value)
        {
            this.value = value;
            this.name = "Integer";
        }
    }
    public static class Boolean extends Node
    {
        public boolean value;

        public Boolean(boolean value)
        {
            this.value = value;
            this.name = "Boolean";
        }
    }
    public static class Binary extends Node
    {
        public char value;

        public Binary(char value, Node[] children)
        {
            super(children);
            this.value = value;
            this.name = "Binary";
        }
    }
    public static class Unary extends Node
    {
        public char value;

        public Unary(char value, Node[] children)
        {
            super(children);
            this.value = value;
            this.name = "Unary";
        }
    }
    public static class If extends Node
    {
        public If(Node[] children) {
            super(children);
            this.name = "If";
        }
    }
    public static class Actuals extends Node
    {
        public Actuals(Node[] children) {
            super(children);
            this.name = "Actuals";
        }
    }
    public static class FunctionCall extends Node
    {
        public FunctionCall(Node[] children) {
            super(children);
            this.name = "FunctionCalls";
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < this.children.size(); i++)
        {
            Node child = this.children.get(i);
            //Indents each child
            sb.append(child.name);
            sb.append(" ");
            //sb.append(child.value);

            if(child.children != null)
            {
                sb.append(child.toString());
            }
        }
        return String.format(sb.toString());
    }
}
