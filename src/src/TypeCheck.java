package src;

import java.util.ArrayList;
import java.util.List;

public class TypeCheck
{
    public String symbolTable = "";

    public String traversalHelper(Node tree)
    {
        String bodyType;
        String throwAway;
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            if (child.name.equals("Body")){
                bodyType = traversal(child);
                return bodyType;
            }
            else
                throwAway = traversalHelper(child);
        }
        return "";
    }

    public String traversal(Node tree)
    {
        ArrayList<String> nodeType = new ArrayList<String>();

        //traverse down AST
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            nodeType.add(traversal(child));
        }

        //begin type checking
        //when we reach body, return its type
        if (tree.name.equals("Body")) {
            return nodeType.get(0);
        }
        else if (tree.name.equals("BooleanValue") || tree.name.equals("boolean")) {
            return "Boolean";
        }
        else if (tree.name.equals("Number") || tree.name.equals("integer")) {
            return "Integer";
        }
        else if (tree.name.equals("Identifier")) {
            //look up tree's type in Symbol Table
        }
        else if (tree.name.equals("Expr")) {
            return nodeType.get(0);
        }
        else if (tree.name.equals("If")) {
            boolean ifStatement = false;
            boolean thenElse = false;
            if (nodeType.get(0).equals("Boolean")){
                ifStatement = true;
            }
            if (nodeType.get(1).equals(nodeType.get(2))){
                thenElse = true;
            }
            if (ifStatement && thenElse){
                return nodeType.get(1);
            }
            else
                return "Error: then/else types do not match";
        }
        else if (tree.name.equals("Binary")) {
            //Do binary comparison here
        }
        else if (tree.name.equals("Unary")) {
            //do Unary comparison here
        }
        else if (tree.name.equals("Actuals")) {
            //do something with actuals, look up in symbol table
        }
        else if (tree.name.equals("FunctionCalls")) {
            //do something with function call, look up in symbol table
        }
        else
            return "Parser error";

        //if it gets this far, there was an error
        return "Type Check Error";
    }



    public boolean check(Node tree)
    {
        //need to do dfs down to leaf then perculate the return types back up, then build symbol table
        //pre order traversal
        System.out.println("Type check");
        return true;
    }
}
