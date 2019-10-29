package src;

import java.util.*;

public class TypeCheck {
    Map<String, TT_Obj> stable = new HashMap<String, TT_Obj>();
    //stable.put("func", new TT_Obj());

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

    public boolean compareChildrenNodes(Node parent){
        if (parent.name == "Unary"){
            return unaryCompareNodes(parent);
        } else if (parent.name == "Binary") {
            return binaryCompareNodes(parent);
        } else if (parent.name == "If") {
            return compareIfNode(parent);
        } else if (parent.name == "Identifier") {
            return compareIdentifierNode(parent);
        } else if (parent.name == "FunctionCalls") {
            return compareFunctionCallsNode(parent);
        } else {
            // I don't think this should ever be hit
            // we should be already returning once a node
            // has not children. If this happens there probably
            // is in error in the code.
            return false;
        }
        // ToDo: needed this statement to compile.
        return false;
    }

    //Does a comparison for all types of unary nodes by looking into
    // it first by unary op and then by the type
    public boolean unaryCompareNodes(Node unaryNode){
        if(unaryNode.value.toString() == "not"){
            if(unaryNode.children.get(0).type == "boolean") {
                unaryNode.type = unaryNode.children.get(0).type;
                return true;
            } else {
                return false;
            }
        } else {
            if(unaryNode.children.get(0).type == "integer"){
                unaryNode.type = unaryNode.children.get(0).type;
                return true;
            } else {
                return false;
            }
        }
    }

    // Does a comparison for all types of binary nodes by comparing
    // first comparing the two child nodes of the binary parent.
    // Then it looks at if the child node is right for the parent's op
    public boolean binaryCompareNodes(Node binaryNode){
        if(!compare2Nodes(binaryNode.children.get(0), binaryNode.children.get(1))){
            return false;
        } else {
            return compareBinaryOpToType(binaryNode);
        }
    }


    public boolean compare2Nodes(Node expr1, Node expr2){
        if(expr1.type == expr2.type){
            return true;
        } else {
            return false;
        }
    }

    // You only need one of the nodes since we already know that the Nodes match.
    // You have three top levels with = which must be true. Then and, or which are
    // boolean type. The rest would have to be of integer type including <
    public boolean compareBinaryOpToType(Node binaryNode){
        if(binaryNode.value.toString() == "="){
            binaryNode.type = binaryNode.children.get(0).type;
            return true;
        } else if(binaryNode.value.toString() == "and" || binaryNode.value.toString() == "or"){
            if (binaryNode.children.get(0).type == "boolean"){
                binaryNode.type = binaryNode.children.get(0).type;
                return true;
            } else {
                return false;
            }
        } else {
            if (binaryNode.children.get(0).type == "integer"){
                binaryNode.type = binaryNode.children.get(0).type;
                return true;
            }
                return false;
        }
    }




    public boolean compareIfNode(Node If){
        if(If.children.get(0).type != "boolean" ){
            return false;
        } else if(compare2Nodes(If.children.get(1),If.children.get(2))){
            return true;
        } else {
            return false;
        }
    }

    public boolean compareIdentifierNode(Node Identifier) {
        // If Identifier is contained in the Symbol table
        // for this Def, then true and set type. else false and error message
        return true;
    }

    public boolean compareFunctionCallsNode(Node FunctionCall) {
        // Call the two different children nodes of Identifier and Actuals.
        // Ensure the Identifier is a type of Def in the Symbol tables.
        // Then start checking the expr of the Actuals to see if they match
        // with the Formals in the symbol table for the Identifier called.
        // As long as this checks out then you pass what the FunctionCall's
        // Type returned. Otherwise complain at the expected point.
        return true;
    }

    public boolean compareActualsToFormals(List<Node> Actuals){
        // First check if it's the same length, false if not the same.
        // Now start comparing the different actuals and formals they
        // respectively come from.
        return true;
    }

    public void printSymbolTable()
    {
        Set< Map.Entry< String,TT_Obj> > set = stable.entrySet();

        for (Map.Entry< String,TT_Obj> stable:set)
        {
            System.out.println(stable.getKey()+":"+stable.getValue());
        }
    }





}
