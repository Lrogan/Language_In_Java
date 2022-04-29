package com.company;

public class Parser
{
    public Lexer lexer = new Lexer();
    public String input = "";
    public Tuple nextToken;
    public boolean res = true;

    public void error(String msg)
    {
        System.out.println("Error on line " + lexer.line + ": " + msg );
    }

    public boolean parseProg(String prog)
    {
        input = prog;
        lex();
        return parseStmtList();
    }

    public boolean parseStmtList()
    {
        if (res)
        {
            if (nextToken.l != Lexer.Lexeme.END_OF_INPUT && !(nextToken.l == Lexer.Lexeme.KEYWORD && nextToken.p.equalsIgnoreCase("end")))
            {
                res = parseStmt();
                lex();
                if(nextToken.l == Lexer.Lexeme.SEMICOLON)
                {
                    res = parseStmtList();
                }
                else
                {
                    error("Expected Semicolon");
                    res = false;
                }
            }
        }
        return res;
    }

    public boolean parseStmt()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.KEYWORD)
        {
            if(nextToken.p.equalsIgnoreCase("print"))
            {
                lex();
                return parsePrint();
            }
            else if(nextToken.p.equalsIgnoreCase("get"))
            {
                return parseGet();
            }
            else if(nextToken.p.equalsIgnoreCase("if"))
            {
                return parseIf();
            }
            else if(nextToken.p.equalsIgnoreCase("while"))
            {
                return parseWhile();
            }
            else if(nextToken.p.equalsIgnoreCase("for"))
            {
                return parseFor();
            }
            else
            {
                error("Unexpected Keyword: " + nextToken.p);
                return false;
            }
        }
        else if(nextToken.l == Lexer.Lexeme.ID)
        {
            lex();
            if(nextToken.l == Lexer.Lexeme.ASSIGN)
            {
                return parseExpr();
            }
            else
            {
                error("Expected \"=\" after ID for Assignment");
                return false;
            }

        }
        else
        {
            error("Expected Keyword or ID at beginning of Statement");
            return false;
        }
    }

    public boolean parseExpr()
    {
        boolean result = parseValue();
        if(result)
        {
            result = parseV_Expr();
        }
    }

    public boolean parseV_Expr()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.GT)
        {

        }
        else if(nextToken.l == Lexer.Lexeme.GTE)
        {

        }
        else if(nextToken.l == Lexer.Lexeme.LTE)
        {

        }
        else if(nextToken.l == Lexer.Lexeme.LT)
        {

        }
        else if(nextToken.l == Lexer.Lexeme.EQUAL)
        {

        }
        else if(nextToken.l == Lexer.Lexeme.NOT_EQUAL)
        {

        }
        else
        {
            return parseF_Expr();
        }
    }

    public boolean parseValue()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.OPENP)
        {
            lex();
            return parseExpr();
        }
        else if(nextToken.l == Lexer.Lexeme.KEYWORD && nextToken.p.equalsIgnoreCase("not"))
        {
            return parseValue());
        }
        else if (nextToken.l == Lexer.Lexeme.MINUS)
        {
            return parseValue();
        }
        else if(nextToken.l == Lexer.Lexeme.ID)
        {
            return true;
        }
        else if(nextToken.l == Lexer.Lexeme.INT)
        {
            return true;
        }
        else
        {
            error("Unexpected Value");
            return false
        }
    }


    public void lex()
    {
        nextToken = lexer.lex(input);
        if(nextToken.l == Lexer.Lexeme.ERROR)
        {
            System.out.println(nextToken.p);
        }
    }

    public boolean parseGet()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.ID)
        {
            lex();
            if(nextToken.l == Lexer.Lexeme.COMMA)
            {
                return parseIDList();
            }
            else
            {
                return true;
            }
        }
        else
        {
            error("ID Expected after \"get\"");
            return false;
        }
    }

    public boolean parseIDList()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.ID)
        {
            lex();

            if(nextToken.l == Lexer.Lexeme.KEYWORD && nextToken.p.equalsIgnoreCase("end"))
            {
                return true;
            }
            else if(nextToken.l == Lexer.Lexeme.COMMA)
            {
                return parseIDList();
            }
            else
            {
                error("Comma or \"end\" expected after ID");
                return false;
            }
        }
        else
        {
            error("ID expected after Comma");
            return false;
        }
    }
}
