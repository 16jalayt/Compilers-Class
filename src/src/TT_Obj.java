package src;

import java.util.LinkedList;
import java.util.List;

public class TT_Obj
{
    public List<String> formals = new LinkedList<String>();
    public List<String> calledByFunction = new LinkedList<String>();
    public String type = "null";

    public TT_Obj(){ }

    //do we know this on creation?
    public TT_Obj(String type)
    {
        this.type = type;
    }


    @Override
    public String toString()
    {
        return "Tpye:"+type + " Formals:" + formals + " Called by Functions:" + calledByFunction;
    }
}
