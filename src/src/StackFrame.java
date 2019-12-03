package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StackFrame
{
    public static void makeFrame(int[] args) throws IOException
    {//stack frame creation is inlined, could be optimized
     //See make frame in asm staging.txt


        Generator.printComment("-----Create Stack Frame-----\n");

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

        //retaddr  - probably need to fix - beg of mf?
        int offset = 2;/////////Not actually set correct yet
        Generator.addr("add", 5, offset,7,"stackframe");
        Generator.print("st", 5,1,6,"store pc at top + 1 in dmem");
        Generator.print("ldc", 6,1,6,"inc top by 1");

        Generator.printComment("-----End Stack Frame-----\n\n");
    }

    //get info out of frame
    //lecture 21
    public static void calledFunc() throws IOException
    {
        Generator.printComment("-----Begin Called Func-----\n\n");

        saveRegisters();
        //init data objects

        Generator.printComment("-----End Called Func-----\n\n");
    }

    //put return into (r5?)
    public static void returning() throws IOException
    {
        Generator.printComment("-----Begin Returning-----\n\n");

        //handle ret values
        restoreRegisters();
        //make sure top is fully dealloc

        Generator.printComment("-----End Returning-----\n\n");
    }

    //restore reg and deallocate stack
    public static void returned() throws IOException
    {
        Generator.printComment("-----Begin Returned-----\n");

        //copy ret values to reg

        Generator.printComment("-----End Returned-----\n\n");
    }

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
    public static void restoreRegisters() throws IOException
    {
        //See save register in asm staging.txt
        //could put in for loop too
        Generator.print("ldc", 6,-1,6,"dec top by 1");
        Generator.print("ld", 1,0,6,"store r1 at top in dmem");
        Generator.print("ldc", 6,-1,6,"dec top by 1");
        Generator.print("ld", 2,0,6,"store r2 at top in dmem");
        Generator.print("ldc", 6,-1,6,"dec top by 1");
        Generator.print("ld", 3,0,6,"store r3 at top in dmem");
        Generator.print("ldc", 6,-1,6,"dec top by 1");
        Generator.print("ld", 4,0,6,"store r4 at top in dmem");
    }
}
