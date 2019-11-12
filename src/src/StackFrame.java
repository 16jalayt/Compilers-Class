package src;

import java.util.ArrayList;
import java.util.List;


//DONT NEED AN OBJECT, NEED SET OF ASM ROUTINES TO PARSE
public class StackFrame
{
    int numArguments;
    List<Integer> arguments = new ArrayList<Integer>();
    int numReg;
    List<Integer> registers = new ArrayList<Integer>();
    int retAddr;

    /*public StackFrame(int numArguments, List<Integer> arguments, int numReg, List<Integer> registers, int retAddr)
    {

    }*/

    public static void saveRegisters()
    {
        //See save register in asm staging.txt
    }

    public static void makeFrame()
    {
        //See make frame in asm staging.txt
        saveRegisters();
    }
}
