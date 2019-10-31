package src;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TT_Obj
{
    public LinkedList<LinkedList<String>> formals = LinkedList<LinkedList<String>>();
    public List<String> calledByFunctions = new LinkedList<String>();
    public List<String> callsFunctions = new LinkedList<String>();
    public String type = "null"; 

    public TT_Obj(LinkedList<LinkedList<String>> formalsLinkedListWithLinkedList, String type)
    {
        this.formals = formalsLinkedListWithLinkedList;
        this.type = type;
        
    }


    @Override
    public String toString()
    {
        return "Type:"+type + " Formals:" + formals + " Called by Functions:" + calledByFunctions +
        " Calls Functions:" + callsFunctions;
    }
}
