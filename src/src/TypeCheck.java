package src;

import java.util.*;

public class TypeCheck {
    Map<Object, TT_Obj> stable = new HashMap<Object, TT_Obj>();
    //stable.put("func", new TT_Obj());
    String errorCode = "";

    public void startSemanticCheck(Node tree){
        createSymbolTable(tree);
        traversalHelper(tree);
        if (!errorCode.equals("")){
            System.out.println(errorCode);
        }
        else{
            System.out.println("No semantic errors found.");
        }
    }
    public void traversalHelper(Node tree)
    {
        for (int i = 0; i < tree.children.size(); i++) {
            Node child = tree.children.get(i);
            if (child.name.equals("Body")) {
                traversal(child);
                //after body gets a type, check if its the correct type
                compareBodyToFunctionType(child, tree.children.get(2), tree.children.get(0));
            }
            else{
                traversalHelper(child);
            }
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
        else if (currentNode.name.equals("Unary")){
            unaryCompareNodes(currentNode); }
        else if (currentNode.name.equals("Binary")) {
            binaryCompareNodes(currentNode); }
        else if (currentNode.name.equals("If")) {
            compareIfNode(currentNode); }
        else if (currentNode.name.equals("Expr")) {
            compareExprNode(currentNode); }
        else if (currentNode.name.equals("Identifier")) {
            compareIdentifierNode(currentNode); }
        else if (currentNode.name.equals("FunctionCalls")) {
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
            if(unaryNode.children.get(0).type.equals("boolean")) {
                unaryNode.type = unaryNode.children.get(0).type; }
            else {
                unaryNode.type = "Error";
                generateError(unaryNode); }
        }
        else {
            if(unaryNode.children.get(0).type.equals("integer")){
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
            if (binaryNode.children.get(0).type.equals("boolean")){
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
        if(!If.children.get(0).type.equals("boolean")){
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

    public void compareFunctionCallsNode(Node FunctionCall) {
        
        Node identifier = FunctionCall.children.get(0);

        System.out.println("Got here 1");

        // Check that identifier exists 
        if(!stable.containsKey(identifier.value)) {
            //The given identifier does not exist in the stable
            FunctionCall.type = "Error";
            if (errorCode.equals("")){
                errorCode = "Error, Identifier Node " + identifier + "does not exist.";
            }
            else{
                String temp = "Error, Identifier Node " + identifier + "does not exist: ";
                errorCode = temp + errorCode;
            }
        }
        System.out.println("Got here 2");

        Node actuals = FunctionCall.children.get(1);

        System.out.println("Got here 3");

        for(int i = 0; i < actuals.children.size(); i++){
            // compare the type of the actual at i to the formal at i
            System.out.println("Got here 3.5");
            if(!actuals.children.get(i).type.equals(stable.get(identifier).formals.get(i).get(1))){
                // Error that the type of the actual doesn't match the type of the formal
                FunctionCall.type = "Error";
                if (errorCode.equals("")){
                    String temp1 = "Error, Identifier Node " + actuals.children.get(i) + "of type " + actuals.children.get(i).type;
                    String temp2 = " does not type match" + stable.get(identifier).formals.get(i).get(1) + ".";
                    errorCode = temp1 + temp2;
                    break;
                }
                else {
                    String temp1 = "Error, Identifier Node " + actuals.children.get(i) + "of type " + actuals.children.get(i).type;
                    String temp2 = " does not type match" + stable.get(identifier).formals.get(i).get(1) + ":";
                    errorCode = temp1 + temp2 + errorCode;
                    break;
                }
            }
        }
        System.out.println("Got here 4");
        //After all checks, now we can assign this identifier its type, unless there was an error
        if (!FunctionCall.type.equals("Error")) {
            FunctionCall.type = stable.get(identifier).type;
        }
        System.out.println("Got here 5");
    }

    public void compareBodyToFunctionType(Node bodyNode, Node typeNode, Node funcName){
        if (!bodyNode.type.equals(typeNode.type)){
            if (errorCode.equals("")){
                String temp1 = "Error caused by body's returned type not matching the function ";
                String temp2 = funcName.name + "'s return type.";
                errorCode = temp1 + temp2;
            }
            else{
                String temp1 = "Error caused by body's returned type not matching the function ";
                String temp2 = funcName.name + "'s return type:";
                errorCode = temp1 + temp2 + errorCode;
            }
        }
    }


    public void createSymbolTable(Node program){

        addPrintToSymbolTable();
   
        for(Node def: program.children){
            Object defIdentifierValue = def.children.get(0).value;

            if(stable.containsKey(defIdentifierValue)){
                //ERROR definition name already exists in symbol table
            }
            LinkedList<LinkedList<String>> formalsLinkedListWithLinkedList = new LinkedList<LinkedList<String>>();

            Node formals = def.children.get(1);

            //The formal is input as a list with [0] being the identifier
            //and at location 1 the type
            for(Node formal: formals.children){
                Object formalIdentifierValue = def.children.get(0).value;

                checkForRepeatedIdentifier(formalsLinkedListWithLinkedList, formalIdentifierValue);
                
                LinkedList<String> formalLinkedList = new LinkedList<String>();
                formalLinkedList.add(formalIdentifierValue.toString());
                formalLinkedList.add(formal.type);
                formalsLinkedListWithLinkedList.add(formalLinkedList);
            }

            String defTypeString = def.children.get(2).type;

            stable.put(defIdentifierValue, new TT_Obj(formalsLinkedListWithLinkedList, defTypeString));
        }

        Object main = "main";

        if(!stable.containsKey(main)){
            // ERROR that there is no main function making it semantically incorrect
        }
    }

    public void addPrintToSymbolTable(){
        Object printValue = new Object();
        LinkedList<LinkedList<String>> printFormals = new LinkedList<LinkedList<String>>();
        LinkedList<String> printFormal = new LinkedList<String>();
        
        printFormal.add("exp");
        printFormal.add("or");
        printFormals.add(printFormal);
        stable.put(printValue, new TT_Obj(printFormals, "or"));
    }

    public void checkForRepeatedIdentifier(LinkedList<LinkedList<String>> formalsLinkedListWithLinkedList, Object formalIdentifierValue){
        for(int i = 0; i < formalsLinkedListWithLinkedList.size()-1; i++){
            if(formalIdentifierValue.toString().equals(formalsLinkedListWithLinkedList.get(i).get(0))){
                //Calls an error about multiple uses of an Identifier name
                if (errorCode.equals("")) {
                    errorCode = "Error caused by multiple uses of Identifier name " + formalIdentifierValue.toString() + ".";
                }
                else {
                    String temp = "Error caused by multiple uses of Identifier name " + formalIdentifierValue.toString() + ":";
                    errorCode = temp + errorCode;
                }
            }
        }
    }

    public void generateError(Node error){
        //Creates a long list of errors, tracing back to the original node which caused the first error
        String temp;
        if (errorCode.equals("")) {
            if (error.value != null) {
                errorCode = "Error at " + error.name + "Node, with value of " + error.value + ".";
            }
            else {
                errorCode = "Error at " + error.name + "Node.";
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
}
