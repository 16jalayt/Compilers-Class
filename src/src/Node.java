package src;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Node
{
    public Node parent;
    public String name;
    public Object value;
    public List<Node> children = new LinkedList<Node>();

    //need empty as default
    public Node()
    {
        this.parent = null;
        this.value = null;
    }

    public Node(Node[] children_array)
    {
        addChildren(children_array);
    }

    public void addChildren(Node[] children_array)
    {
        if(children_array == null)
            children = null;
        else
        {
            children.addAll(Arrays.asList(children_array));
        }
    }

    public void addChild(Node child)
    {
        //add in reversed order
        children.add(0,child);
    }

    public void removeChild(Node n){
        this.children=null;
    }

    //n is number of indents for children.
    public void Iterate(int n)
    {
        //Indents children
        for (int m=0; m<n; m++)
        {
            System.out.print("\t");
        }

        //print out current nodes name and value, if applicable
        System.out.print(this.name);
        if (this.value != null) {
            System.out.print(" ");
            System.out.println(this.value);
        }
        else{
            System.out.println("");
        }

        //Recursively Iterates for each child.
        for (int i=0; i < this.children.size(); i++)
        {
            Node child = this.children.get(i);
            //System.out.println("Child size is:" + this.children.size());
            child.Iterate(n+1);
        }
    }

    public boolean hasChildren(){
        if (this.children.size() == 0){
            return false;
        }
        else
            return true;
    }

    public static class Program extends Node
    {
        public Program() {
            if(Main.debugStage == 6)
                System.out.println("PROGRAM");
            this.name = "Program";
        }
    }
    public static class Def extends Node
    {
        public Def(Node[] children) {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("Def");
            this.name = "Def";
        }
    }
    public static class Body extends Node
    {
        public Body() {
            super();
            if(Main.debugStage == 6)
                System.out.println("Body");
            this.name = "Body";
        }
    }
    public static class Expr extends Node
    {
        public Expr(Node[] children) {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("Expr");
            this.name = "Expr";
        }
    }
    public static class Identifier extends Node
    {

        public Identifier(String value) {
            if(Main.debugStage == 6)
                System.out.println("Identifier");
            this.value = value;
            this.name = "Identifier";
        }
    }
    public static class Formals extends Node
    {
        public Formals()
        {
            super();
            if(Main.debugStage == 6)
                System.out.println("Formals");
            this.name = "Formals";
        }
    }
        public static class Formal extends Node
    {
        public Formal(Node[] children) { super(children);
            if(Main.debugStage == 6)
                System.out.println("Node");
            this.name = "Formal";
        }
    }
    public static class Number extends Node
    {

        public Number(int value)
        {
            if(Main.debugStage == 6)
                System.out.println("Number");
            this.value = value;
            this.name = "Number";
        }
    }
    public static class BooleanValue extends Node
    {

        public BooleanValue(boolean value)
        {
            if(Main.debugStage == 6)
                System.out.println("Boolean");
            this.value = value;
            this.name = "BooleanValue";
        }
    }
    public static class Integer extends Node
    {

        public Integer()
        {
            if(Main.debugStage == 6)
                System.out.println("Integer");
            this.name = "integer";
        }
    }
    public static class BooleanType extends Node
    {
        public BooleanType()
        {
            if(Main.debugStage == 6)
                System.out.println("BooleanType");
            this.name = "boolean";
        }
    }
    public static class Binary extends Node
    {

        public Binary(char value, Node[] children)
        {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("Binary");
            this.value = value;
            this.name = "Binary";
        }
    }
    public static class Unary extends Node
    {

        public Unary(char value, Node[] children)
        {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("Unary");
            this.value = value;
            this.name = "Unary";
        }
    }
    public static class If extends Node
    {
        public If(Node[] children) {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("If");
            this.name = "If";
        }
    }
    public static class Actuals extends Node
    {
        public Actuals() {
            super();
            if(Main.debugStage == 6)
                System.out.println("Actuals");
            this.name = "Actuals";
        }
    }
    public static class FunctionCall extends Node
    {
        public FunctionCall(Node[] children) {
            super(children);
            if(Main.debugStage == 6)
                System.out.println("FunctionCall");
            this.name = "FunctionCalls";
        }
    }

    @Override
    public String toString()
    {/*
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < this.children.size(); i++)
        {
            Node child = this.children.get(i);
            //Indents each child
            sb.append(child.name);
            sb.append(" ");
            //sb.append(child.value);

            if(child.children.isEmpty())
            {
                sb.append(child.toString());
            }
        }
        return String.format(sb.toString());*/
        return name;
    }
}
