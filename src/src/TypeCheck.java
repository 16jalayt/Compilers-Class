package src;

import java.util.*;

public class TypeCheck {
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();
    //stable.put("func", new TT_Obj());
    String errorCode = "";

    public void traversalHelper(Node tree)
    {
        if (tree.name.equals("Body")){
            traversal(tree);
        }
        else if (tree.name.equals("Formals")){
            formalsTraversal(tree);
        }
        else if (tree.name.equals("Program")) {
            for (int i = 0; i < tree.children.size(); i++) {
                Node def = tree.children.get(i);
                traversalHelper(def);
            }
        }
        else if (tree.name.equals("Def")){
            //no idea how to make child#2 into a TT_Obj type
            //stable.put(tree.children.get(0).value.toString(), tree.children.get(2).value);
            //formals
            traversalHelper(tree.children.get(1));
            //body
            traversalHelper(tree.children.get(3));
        }
    }

    public void formalsTraversal(Node tree)
    {
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            //Not sure how to make second child into a TT_Obj
            //stable.put(child.children.get(0).value.toString(), child.children.get(1).value);
        }
    }

    public void traversal(Node tree)
    {
        for (int i=0; i < tree.children.size(); i++)
        {
            Node child = tree.children.get(i);
            traversal(child);
        }
        compareChildrenNodes(tree);
    }


    public boolean check(Node tree)
    {
        //need to do dfs down to leaf then percolate the return types back up, then build symbol table
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
        else if (currentNode.name == "Identifier") {
            compareIdentifierNode(currentNode); }
        else if (currentNode.name == "FunctionCalls") {
            compareFunctionCallsNode(currentNode); }
        else {
            // We would hit this if our node is a literal, since those already have their type
            //preset, so we would do nothing. Do we even need an else clause?
        }
    }

    public void compareBodyNode(Node body){
        //Added a for loop in case of print statement, might not be needed though
        for (int i=0; i < body.children.size(); i++) {
            Node child = body.children.get(i);
            if (child.name.equals("Expr")) {
                body.type = child.type;
            }
        }
    }

    public void compareExprNode(Node expr){
        expr.type = expr.children.get(0).type;
    }

    //Does a comparison for all types of unary nodes by looking into
    // it first by unary op and then by the type
    public void unaryCompareNodes(Node unaryNode){
        if(unaryNode.value.toString() == "not"){
            if(unaryNode.children.get(0).type == "boolean") {
                unaryNode.type = unaryNode.children.get(0).type; }
            else {
                unaryNode.type = "Error";
                generateError(unaryNode); }
        }
        else {
            if(unaryNode.children.get(0).type == "integer"){
                unaryNode.type = unaryNode.children.get(0).type; }
            else {
                unaryNode.type = "Error";
                generateError(unaryNode);
            }
        }
    }

    // Does a comparison for all types of binary nodes by comparing
    // first comparing the two child nodes of the binary parent.
    // Then it looks at if the child node is right for the parent's op
    public void binaryCompareNodes(Node binaryNode){
        if(!compare2Nodes(binaryNode.children.get(0), binaryNode.children.get(1))){
            binaryNode.type = "Error";
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
                binaryNode.type = "Error";
                generateError(binaryNode); }
        }
        else {
            if (binaryNode.children.get(0).type.equals("integer")){
                binaryNode.type = binaryNode.children.get(0).type;
            }
            else {
                binaryNode.type = "Error";
                generateError(binaryNode); }
        }
    }

    public void compareIfNode(Node If){
        if(If.children.get(0).type != "boolean" ){
            If.type = "Error";
            generateError(If); }
        else if(compare2Nodes(If.children.get(1),If.children.get(2))){
            If.type = If.children.get(1).type;}
        else {
            If.type = "Error";
            generateError(If); }
    }

    public void compareIdentifierNode(Node Identifier) {
        // If Identifier is contained in the Symbol table
        // for this Def, then true and set type. else false and error message
        if (stable.get(Identifier.value.toString()) != null){
            Identifier.type = stable.get(Identifier.value.toString()).toString();
        }
        else{
            Identifier.type = "Error";
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

    public void createSymbolTable(Node program) {


        for (Node def : program.children) {
            LinkedList<LinkedList<String>> formalsLinkedListWithLinkedList = new LinkedList<LinkedList<String>>();

            Node formals = def.children.get(1);
        }
    }
    public void generateError(Node error){
        //Creates a long list of errors, tracing back to the original node which caused the first error
        String temp;
        if (errorCode.equals("")) {
            if (error.value != null) {
                errorCode = "Error at " + error.name + "Node, with value of " + error.value;
            }
            else {
                errorCode = "Error at " + error.name + "Node";
            }
        }
        else{
            if (error.value != null) {
                temp = "Error at " + error.name + "Node, with value of " + error.value + ", caused by: ";
            }
            else {
                temp = "Error at " + error.name + "Node, caused by: ";
            }
            errorCode = temp + errorCode;
        }
    }

    public void printSymbolTable()
    {
        Set< Map.Entry< String,TT_Obj> > set = stable.entrySet();

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
