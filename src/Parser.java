import java.util.HashMap;

public class Parser
{
    private static Scanner scan;
    HashMap<String, Integer> map;

    public Parser(Scanner scan)
    {
        this.scan = scan;
        map = new HashMap<>();
        createTable();
    }

    //TODO: return the tree
    public void parse()
    {
        //example usage. should put if contains to throw error
        System.out.println(map.get("Key"));

        Token tok;
        while (scan.peek().type != Token.Type.EOF && scan.peek().type != Token.Type.Error)
        {
            tok = scan.peek();
            System.out.println(map.get(tok.type.toString()));
            //put token in tree
            //parse.run(tok);
            //System.out.println(tok.toString());
        }

        //one last call to display EOF
        //System.out.println(scan.peek().toString());
    }

    private void createTable()
    {
        map.put("Punctuation",0);
        map.put("Keyword",1);
        map.put("Identifier",2);
        map.put("Integer",3);
    }
}
