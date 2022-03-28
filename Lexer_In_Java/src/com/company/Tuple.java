package com.company;



public class Tuple
{
    Tuple t = null;
    Lexer.Lexeme l;
    String p = "";

    public Tuple(Lexer.Lexeme lex, String prog)
    {
        l = lex;
        p = prog;
    }

    public Tuple(Tuple token, String prog)
    {
        t = token;
        p = prog;
    }

    public Lexer.Lexeme getLexeme() {
        return l;
    }

    public String getProgString() {
        return p;
    }

    public Tuple getT() {
        return t;
    }
}
