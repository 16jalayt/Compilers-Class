package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.Parser;
import src.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ScannerTest
{
    @Test
    public void empty()
    {
        Scanner scan = new Scanner("");
        Assertions.assertEquals( "EOF     ", scan.next().toString());
    }


}
