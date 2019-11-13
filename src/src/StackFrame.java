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
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 1,0,6,"store r1 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 2,0,6,"store r2 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 3,0,6,"store r3 at top in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 4,0,6,"store r4 at top in dmem");
    }

    public static void makeFrame() throws IOException
    {
        //See make frame in asm staging.txt

        //!Not quite right, would be current pos, need to sub some
        Generator.print("ldc", 6,1,6,"inc top by 1");
        Generator.print("st", 7,-2,6,"store pc at top in dmem");
        saveRegisters();
        //save arguments, inc top
        //
    }
}
