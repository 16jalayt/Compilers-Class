import java.io.*;

public class Main
{
    public static void main(String[] args)
    {
        if(args.length != 1)
        {
            System.out.println("Correct usage is: program filename.kln");
            return;
        }

        File file = new File(args[0]);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String currentLine;
            String fullFile = "";
            //do pretty printing here
            while ((currentLine = br.readLine()) != null)
                fullFile += currentLine;

            Scanner scan = new Scanner(fullFile);
            System.out.println("Token returned: " + scan.next());

        }
        catch (FileNotFoundException e)
        {
            System.out.println("The specified file could not be found.");
        } catch (IOException e)
        {
            System.out.println("The specified file could not be read.");
        }

        System.out.println("Exiting...");
    }
}
