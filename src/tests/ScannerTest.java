package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.Parser;
import src.Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest
{
    @Test
    public void empty() throws Exception
    {
        Assertions.assertEquals(
                "Comment     \n" +
                "Keyword     function\n" +
                "Identifier     test1\n" +
                "Punctuation     (\n" +
                "Identifier     var\n" +
                "Punctuation     :\n" +
                "Keyword     boolean\n" +
                "Punctuation     )\n" +
                "Punctuation     :\n" +
                "Keyword     boolean\n" +
                "Keyword     if\n" +
                "Identifier     var\n" +
                "Punctuation     =\n" +
                "Identifier     true\n" +
                "Punctuation     +\n" +
                "Punctuation     -\n" +
                "Punctuation     -\n" +
                "Punctuation     +\n" +
                "Identifier     true\n" +
                "Keyword     then\n" +
                "Identifier     print\n" +
                "Punctuation     (\n" +
                "Identifier     true\n" +
                "Punctuation     )\n" +
                "Keyword     else\n" +
                "Identifier     false\n" +
                "EOF     "
                , print("./examples/klein-programs/circular-prime.kln"));
    }

    private String print(String fileName) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String currentLine;
        String fullFile = "";
        //do pretty printing here
        while ((currentLine = br.readLine()) != null)
            //for some reason, concat removes newline. hack to fix issues. Should be removed down the line anyway
            fullFile += currentLine + '\n';
        Scanner scan = new Scanner(fullFile);

        String out = new String();
        System.out.println(scan.peek().toString());
        while (scan.peek().type != src.Token.Type.EOF && scan.peek().type != src.Token.Type.Error)
            out  +=  scan.next().toString();
        //one last call to display EOF
        out += scan.next().toString();
        System.out.println(out);
        return out;
    }


}
