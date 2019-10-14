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
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/circular-prime.kln")));
        assertTrue(parser.parse(), "circular-prime invalid");
    }

    @Test
    public void divide() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/divide.kln")));
        assertTrue(parser.parse(), "divide invalid");
    }

    @Test
    public void divisibleBySeven() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/divisible-by-seven.kln")));
        assertTrue(parser.parse(), "divisible-by-seven invalid");
    }

    @Test
    public void euclid() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/euclid.kln")));
        assertTrue(parser.parse(), "euclid invalid");
    }

    @Test
    public void factors() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/factors.kln")));
        assertTrue(parser.parse(), "factors invalid");
    }

    @Test
    public void farey() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/farey.kln")));
        assertTrue(parser.parse(), "farey invalid");
    }

    @Test
    public void fibonacci() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/fibonacci.kln")));
        assertTrue(parser.parse(), "fibonacci invalid");
    }

    @Test
    public void generateExcellent() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/generate-excellent.kln")));
        assertTrue(parser.parse(), "generate-excellent invalid");
    }

    @Test
    public void horner() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/horner.kln")));
        assertTrue(parser.parse(), "horner invalid");
    }

    @Test
    public void hornerParam() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/horner-param.kln")));
        assertTrue(parser.parse(), "horner-param invalid");
    }

    @Test
    public void isExcellent() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/is-excellent.kln")));
        assertTrue(parser.parse(), "is-excellent invalid");
    }

    @Test
    public void lib() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/lib.kln")));
        assertTrue(parser.parse(), "lib invalid");
    }

    @Test
    public void modulusByHand() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/modulus-by-hand.kln")));
        assertTrue(parser.parse(), "modulus-by-hand invalid");
    }

    @Test
    public void palindrome() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/palindrome.kln")));
        assertTrue(parser.parse(), "palindrome invalid");
    }

    @Test
    public void printOne() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/print-one.kln")));
        assertTrue(parser.parse(), "print-one invalid");
    }

    @Test
    public void publicPrivate() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/public-private.kln")));
        assertTrue(parser.parse(), "public-private invalid");
    }

    @Test
    public void russianPeasant() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/russian-peasant.kln")));
        assertTrue(parser.parse(), "russian-peasant invalid");
    }

    @Test
    public void sieve() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/sieve.kln")));
        assertTrue(parser.parse(), "sieve invalid");
    }

    @Test
    public void squareRoot() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-programs/square-root.kln")));
        assertTrue(parser.parse(), "square-root invalid");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void test1() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test1.kln.txt")));
        assertTrue(parser.parse(), "test1 invalid");
    }
    @Test
    public void test2() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test2.kln.txt")));
        assertTrue(parser.parse(), "test2 invalid");
    }
    @Test
    public void test3() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test3.kln.txt")));
        assertTrue(parser.parse(), "test3 invalid");
    }

    @Test
    public void test4() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test4.kln.txt")));
        assertTrue(parser.parse(), "test4 invalid");
    }

    @Test
    public void test5() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/test5.kln.txt")));
        assertTrue(parser.parse(), "test5 invalid");
    }

    @Test
    public void testKleinProgram() throws Exception
    {
        Parser parser = new Parser(new Scanner(readFile("./examples/klein-test-programs/testKleinProgram.kln.txt")));
        assertTrue(parser.parse(), "testKleinProgram invalid");
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String readFile(String fileName) throws Exception
    {
        String fullFile = new java.util.Scanner(new File(fileName)).useDelimiter("\\Z").next();
        return fullFile;
    }

}