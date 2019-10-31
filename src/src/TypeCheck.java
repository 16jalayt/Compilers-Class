package src;

import java.util.*;

public class TypeCheck {
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();
    //stable.put("func", new TT_Obj());

    public void traversalHelper(Node tree)
    {
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            if (child.name.equals("Body")){
                traversal(child);
            }
            else{
                traversalHelper(child);
                //add function name and formals to symbol table here
            }
        }
        //add coce for function anme an formals here
    }

    public void traversal(Node tree)
    {
        //traverse down AST
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            traversal(child);
        }
        compareChildrenNodes(tree);

        //begin type checking

        //when we reach body, return its type
        /*if (tree.name.equals("Body")) {
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
        return "Type Check Error";*/
    }




    public boolean check(Node tree)
    {
        //need to do dfs down to leaf then perculate the return types back up, then build symbol table
        //pre order traversal
        System.out.println("Type check");
        return true;
    }

    public void compareChildrenNodes(Node currentNode){
        if (currentNode.name.equals("Body")) {
            compareBodyNode(currentNode); }
        else if (currentNode.name == "Unary"){
            unaryCompareNodes(currentNode); }
        else if (currentNode.name == "Binary") {
            binaryCompareNodes(currentNode); }
        else if (currentNode.name == "If") {
            compareIfNode(currentNode); }
        else if (currentNode.name.equals("Expr")) {
            compareExprNode(currentNode); }
        else if (currentNode.name.equals("Number") || currentNode.name.equals("integer")) {
            compareNumberNode(currentNode); }
        else if (currentNode.name.equals("BooleanValue") || currentNode.name.equals("boolean")) {
            compareBooleanNode(currentNode); }
        else if (currentNode.name == "Identifier") {
            compareIdentifierNode(currentNode); }
        else if (currentNode.name == "FunctionCalls") {
            compareFunctionCallsNode(currentNode); }
        else {
            // I don't think this should ever be hit
            // we should be already returning once a node
            // has not children. If this happens there probably
            // is in error in the code.

        }
    }

    //Does a comparison for all types of unary nodes by looking into
    // it first by unary op and then by the type
    public void unaryCompareNodes(Node unaryNode){
        if(unaryNode.value.toString() == "not"){
            if(unaryNode.children.get(0).type == "boolean") {
                unaryNode.type = unaryNode.children.get(0).type; }
            else {
                generateError(unaryNode); }
        }
        else {
            if(unaryNode.children.get(0).type == "integer"){
                unaryNode.type = unaryNode.children.get(0).type; }
            else {
                generateError(unaryNode);
            }
        }
    }

    // Does a comparison for all types of binary nodes by comparing
    // first comparing the two child nodes of the binary parent.
    // Then it looks at if the child node is right for the parent's op
    public void binaryCompareNodes(Node binaryNode){
        if(!compare2Nodes(binaryNode.children.get(0), binaryNode.children.get(1))){
            generateError(binaryNode); }
        else {
            compareBinaryOpToType(binaryNode); }
    }


    public boolean compare2Nodes(Node expr1, Node expr2){
        if(expr1.type.equals(expr2.type)){
            return true; }
        else {
            return false; }
    }

    // You only need one of the nodes since we already know that the Nodes match.
    // You have three top levels with = which must be true. Then and, or which are
    // boolean type. The rest would have to be of integer type including <
    public void compareBinaryOpToType(Node binaryNode){
        if(binaryNode.value.toString().equals("=")){
            binaryNode.type = binaryNode.children.get(0).type; }
        else if(binaryNode.value.toString().equals("and") || binaryNode.value.toString().equals("or")){
            if (binaryNode.children.get(0).type == "boolean"){
                binaryNode.type = binaryNode.children.get(0).type; }
            else {
                generateError(binaryNode); }
        }
        else {
            if (binaryNode.children.get(0).type.equals("integer")){
                binaryNode.type = binaryNode.children.get(0).type;
            }
            else {
                generateError(binaryNode); }
        }
    }


    public void compareIfNode(Node If){
        if(If.children.get(0).type != "boolean" ){
            generateError(If); }
        else if(compare2Nodes(If.children.get(1),If.children.get(2))){
            If.type = If.children.get(1).type;}
        else {
            generateError(If); }
    }

    public void compareIdentifierNode(Node Identifier) {
        // If Identifier is contained in the Symbol table
        // for this Def, then true and set type. else false and error message
        if (stable.get(Identifier.value.toString()) != null){
            Identifier.type = stable.get(Identifier.value.toString()).toString();
        }
        else{
            generateError(Identifier);
        }
    }

    // public boolean compareFunctionCallsNode(Node FunctionCall) {

        

    //     Node identifier = FunctionCall.children.get(0);

    //     // First compare the identifier to see that it exists if the function 
    //     // actually exists. Next compare each expression in the actuals to each
    //     // formal in the formals.
    //     if(stable.containsKey(identifier.value)) {
    //         return true; 
    //     } 


    //     return true;
    // }

    // public boolean compareIdentifierOfFunctionCallsNode(Node Identifier) {
    //     if(stable.containsKey(Identifier.value)) {
    //         return true; 
    //     } 
    // }

    public boolean compareActualsToFormals(List<Node> Actuals){
        // First check if it's the same length, false if not the same.
        // Now start comparing the different actuals and formals they
        // respectively come from.
        return true;
    }

    public void createSymbolTable(Node program){

        
        for(Node def: program.children){
            LinkedList<LinkedList<String>> formalsLinkedListWithLinkedList = new LinkedList<LinkedList<String>>();

            Node formals = def.children.get(1);

            //The formal is input as a list with [0] being the identifier
            //and at location 1 the type
            for(Node formal: formals.children){
                Object formalIdentifierValue = def.children.get(0).value;

                // if(formalsHashMap.containsKey(formal.children.get(0).value)){
                //     //ERROR MESSAGE OF REPEAT identifier name
                // }
                LinkedList<String> formalLinkedList = new LinkedList<String>();
                formalLinkedList.add(formalIdentifierValue.toString());
                formalLinkedList.add(formal.type);
                formalsLinkedListWithLinkedList.add(formalLinkedList);
            }
            Object defIdentifierValue = def.children.get(0).value;
            String defTypeString = def.children.get(2).type;

            stable.put(defIdentifierValue, new TT_Obj(formalsLinkedListWithLinkedList, defTypeString));
        }
    }




}
