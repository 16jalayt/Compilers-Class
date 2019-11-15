package src;

import java.io.IOException;
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

    public static void makeFrame(int numberArguments) throws IOException
    {//stack frame creation is inlined, could be optimized
        //See make frame in asm staging.txt

        saveRegisters();
        //save arguments, incrament top
        Generator.print("ldc", 5,numberArguments,0,"load numargs into r5");
        Generator.print("st", 5,1,6,"store number args at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");

        //store arguments
        /////Where get args from?

        //store num args again. I think we can remove the first time above
        Generator.print("st", 5,1,6,"store number args at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");

        int offset = 1;/////////Not actually set correct yet
        Generator.print("add", 5,offset,7,"add offset to pc");
        Generator.print("st", 5,1,6,"store pc at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
    }
}
