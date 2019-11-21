package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//DONT NEED AN OBJECT, NEED SET OF ASM ROUTINES TO PARSE
public class StackFrame
{
    //dont need this to be an object, just static methods
    /*int numArguments;
    List<Integer> arguments = new ArrayList<Integer>();
    int numReg;
    List<Integer> registers = new ArrayList<Integer>();
    int retAddr;

    public StackFrame(int numArguments, List<Integer> arguments, int numReg, List<Integer> registers, int retAddr)
    {

    }*/

    public static void saveRegisters() throws IOException
    {
        //See save register in asm staging.txt
        //could put in for loop too
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 1,0,6,"store r1 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 2,0,6,"store r2 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 3,0,6,"store r3 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 4,0,6,"store r4 at top in dmem");
    }

    public static void makeFrame(int[] args) throws IOException
    {//stack frame creation is inlined, could be optimized
        //See make frame in asm staging.txt


        Generator.printComment("-----Create Stack Frame-----\n");
        saveRegisters();
        //save arguments, incrament top
        //store args
        for(int i=0; i<args.length; i++)
        {
            Generator.print("ldc", 5,args[i],0,"load arg " + i + " into r5");
            Generator.print("st", 5,1,6,"store arg at top + 1 in dmem");
            Generator.print("ldc", 6,1,6,"inc top by 1");
        }

        //num args
        Generator.print("ldc", 5,args.length,0,"load numargs into r5");
        Generator.print("st", 5,1,6,"store number args at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");

        int offset = 2;/////////Not actually set correct yet
        Generator.print("add", 5, offset,7,"add offset of " +offset + " to pc");
        Generator.print("st", 5,1,6,"store pc at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");

        Generator.printComment("-----End Stack Frame-----\n\n");
    }

    //get info out of frame
    public static void calledFunc() throws IOException
    {
        Generator.printComment("-----Begin Called Func-----\n\n");

        Generator.printComment("-----End Called Func-----\n\n");
    }

    //put return into (r5?)
    public static void returning() throws IOException
    {
        Generator.printComment("-----Begin Returning-----\n\n");

        Generator.printComment("-----End Returning-----\n\n");
    }

    //restore reg and deallocate stack
    public static void returned() throws IOException
    {
        Generator.printComment("-----Begin Returned-----\n");

        Generator.printComment("-----End Returned-----\n\n");
    }

    //make func to call the function on the stackframe/deallacate stack frame
}
