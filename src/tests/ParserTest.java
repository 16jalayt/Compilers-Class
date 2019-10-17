package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.Parser;
import src.Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest
{
    @Test
    public void empty() throws Exception
    {
        Parser parser = new Parser(new Scanner("")); // src.Parser is tested
        assertTrue(parser.parse(), "Empty file invalid");
    }

    @Test
    public void circularPrime() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/circular-prime.kln")));
        assertTrue(parser.parse(), "circular-prime invalid");
    }

    @Test
    public void divide() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/divide.kln")));
        assertTrue(parser.parse(), "divide invalid");
    }

    @Test
    public void divisibleBySeven() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/divisible-by-seven.kln")));
        assertTrue(parser.parse(), "divisible-by-seven invalid");
    }

    @Test
    public void euclid() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/euclid.kln")));
        assertTrue(parser.parse(), "euclid invalid");
    }

    @Test
    public void factors() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/factors.kln")));
        assertTrue(parser.parse(), "factors invalid");
    }

    @Test
    public void farey() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/farey.kln")));
        assertTrue(parser.parse(), "farey invalid");
    }

    @Test
    public void fibonacci() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/fibonacci.kln")));
        assertTrue(parser.parse(), "fibonacci invalid");
    }

    @Test
    public void generateExcellent() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/generate-excellent.kln")));
        assertTrue(parser.parse(), "generate-excellent invalid");
    }

    @Test
    public void horner() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/horner.kln")));
        assertTrue(parser.parse(), "horner invalid");
    }

    @Test
    public void hornerParam() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/horner-param.kln")));
        assertTrue(parser.parse(), "horner-param invalid");
    }

    @Test
    public void isExcellent() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/is-excellent.kln")));
        assertTrue(parser.parse(), "is-excellent invalid");
    }

    @Test
    public void lib() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/lib.kln")));
        assertTrue(parser.parse(), "lib invalid");
    }

    @Test
    public void modulusByHand() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/modulus-by-hand.kln")));
        assertTrue(parser.parse(), "modulus-by-hand invalid");
    }

    @Test
    public void palindrome() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/palindrome.kln")));
        assertTrue(parser.parse(), "palindrome invalid");
    }

    @Test
    public void printOne() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/print-one.kln")));
        assertTrue(parser.parse(), "print-one invalid");
    }

    @Test
    public void publicPrivate() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/public-private.kln")));
        assertTrue(parser.parse(), "public-private invalid");
    }

    @Test
    public void russianPeasant() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/russian-peasant.kln")));
        assertTrue(parser.parse(), "russian-peasant invalid");
    }

    @Test
    public void sieve() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/sieve.kln")));
        assertTrue(parser.parse(), "sieve invalid");
    }

    @Test
    public void squareRoot() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./tests/Wallingford/square-root.kln")));
        assertTrue(parser.parse(), "square-root invalid");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void test1() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test1.kln")));
        assertTrue(parser.parse(), "test1 invalid");
    }
    @Test
    public void test2() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test2.kln")));
        assertTrue(parser.parse(), "test2 invalid");
    }
    @Test
    public void test3() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test3.kln")));
        assertTrue(parser.parse(), "test3 invalid");
    }

    @Test
    public void test4() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test4.kln")));
        assertTrue(parser.parse(), "test4 invalid");
    }

    @Test
    public void test5() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test5.kln")));
        assertTrue(parser.parse(), "test5 invalid");
    }

    @Test
    public void testKleinProgram() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/testKleinProgram.kln")));
        assertTrue(parser.parse(), "testKleinProgram.kln invalid");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String readFile(String fileName) throws Exception
    {
        String fullFile = new java.util.Scanner(new File(fileName)).useDelimiter("\\Z").next();
        return fullFile;
    }

}