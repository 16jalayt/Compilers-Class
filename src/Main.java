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
                //for some reason, concat removes newline. hack to fix issues. Should be removed down the line anyway
                fullFile += currentLine + '\n';
            Scanner scan = new Scanner(fullFile);
            Parser parse = new Parser(scan);
           if (parse.parse())
                System.out.println("The program is: Valid");
            else
                System.out.println("The program is: Invalid");

            /*while (scan.peek().type != Token.Type.EOF && scan.peek().type != Token.Type.Error)
                System.out.println(scan.next().toString());
            //one last call to display EOF
            System.out.println(scan.next().toString());*/

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
