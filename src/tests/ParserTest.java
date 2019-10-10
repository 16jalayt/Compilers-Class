package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.Parser;
import src.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest
{
    @Test
    public void empty()
    {
        Parser parser = new Parser(new Scanner("")); // src.Parser is tested

        //assert statements
        assertTrue(parser.parse(), "Empty file invalid");
    }
}