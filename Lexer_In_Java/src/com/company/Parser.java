package com.company;

public class Parser
{
    public Lexer lexer = new Lexer();
    public String input = "";
    public Tuple nextToken;
    public boolean res = true;
    public Tuple lastToken;

    public void error(String msg)
    {
        System.out.println("Error on line " + lexer.line + ": " + msg + "\nRemaining Prog: " + (nextToken.t == null ? nextToken.p : nextToken.t.p) + "\nCurrentToken: " + (nextToken.t == null ? nextToken.l : nextToken.t.l));
    }

    public boolean parseProg(String prog)
    {
        input = prog;
        return parseStmtList();
    }

    public boolean parseStmtList()
    {
        lex();
        if (res)
        {
            if (nextToken.l != Lexer.Lexeme.END_OF_INPUT && !(nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("end")))
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

    public boolean parseIfStmtList()
    {
        lex();
        if (res)
        {
            if (nextToken.l != Lexer.Lexeme.END_OF_INPUT && !(nextToken.t.l == Lexer.Lexeme.KEYWORD && (nextToken.t.p.equalsIgnoreCase("end") || nextToken.t.p.equalsIgnoreCase("else"))))
            {
                res = parseStmt();
                lex();
                if(nextToken.l == Lexer.Lexeme.SEMICOLON)
                {
                    res = parseIfStmtList();
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
        if(nextToken.t.l == Lexer.Lexeme.KEYWORD)
        {
            if(nextToken.t.p.equalsIgnoreCase("print"))
            {
                return parsePrint();
            }
            else if(nextToken.t.p.equalsIgnoreCase("get"))
            {
                return parseGet();
            }
            else if(nextToken.t.p.equalsIgnoreCase("if"))
            {
                return parseIf();
            }
            else if(nextToken.t.p.equalsIgnoreCase("while"))
            {
                return parseWhile();
            }
            else if(nextToken.t.p.equalsIgnoreCase("for"))
            {
                return parseFor();
            }
            else
            {
                error("Unexpected Keyword: " + nextToken.p);
                return false;
            }
        }
        else if(nextToken.t.l == Lexer.Lexeme.ID)
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

    public boolean parseGet()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.OPENSTBRACE)
        {
            return parseIDList();
        }
        else if(nextToken.t.l == Lexer.Lexeme.ID)
        {
            return true;
        }
        else
        {
            error("Expected either ID or ID List for Get");
            return false;
        }
    }

    public boolean parseIDList()
    {
        lex();
        if(nextToken.t.l == Lexer.Lexeme.ID)
        {
            lex();
            if (nextToken.l == Lexer.Lexeme.CLOSESTBRACE)
            {
                return true;
            }
            else if (nextToken.l == Lexer.Lexeme.COMMA)
            {
                return parseIDList();
            }
            else
            {
                error("Comma or \"]\" expected after ID");
                return false;
            }
        }
        else
        {
            error("ID expected after Comma");
            return false;
        }
    }

    public boolean parseAssign()
    {
        lex();
        if(nextToken.t.l == Lexer.Lexeme.ID)
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
            error("Expected ID for Assignment");
            return false;
        }

    }


    public boolean parsePrint()
    {
        lastToken = nextToken;
        lex();
        if(nextToken.t.l == Lexer.Lexeme.STRING)
        {
            return true;
        }
        nextToken = lastToken;
        input = lastToken.p;
        if(parseExpr())
        {
            nextToken = lastToken;
            input = lastToken.p;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean parseIf()
    {
        boolean result = parseExpr();
        if(result)
        {
            if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("then"))
            {
                result = parseIfStmtList();
                if(result)
                {
                    if(nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("else"))
                    {
                        result = parseIfStmtList();
                        if(result)
                        {
                            if(nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("end"))
                            {
                                return true;
                            }
                            else
                            {
                                error("Expected keyword \"end\"");
                                return false;
                            }
                        }
                    }
                    else
                    {
                        error("Expected keyword \"else\"");
                        return false;
                    }
                }
            }
            else
            {
                error("Expected keyword \"then\"");
                return false;
            }
        }
        return false;
    }

    public boolean parseWhile()
    {
        boolean result = parseExpr();
        if(result)
        {
            if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("do"))
            {
                result = parseStmtList();
                if(result)
                {
                    if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("end"))
                    {
                       return true;
                    }
                    else
                    {
                        error("Expected keyword \"end\"");
                        return false;
                    }
                }
            }
            else
            {
                error("Expected keyword \"do\"");
                return false;
            }
        }
        return false;
    }

    public boolean parseFor()
    {
        boolean result = parseAssign();
        if(result)
        {
            if(nextToken.l == Lexer.Lexeme.SEMICOLON)
            {
                result = parseExpr();
                if(result)
                {
                    lex();
                    if(nextToken.l == Lexer.Lexeme.SEMICOLON)
                    {
                        result = parseAssign();
                        if(result)
                        {
                            lex();
                            if(nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("do"))
                            {
                                result = parseStmtList();
                                if(result)
                                {
                                    if(nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("end"))
                                    {
                                        return true;
                                    }
                                    else
                                    {
                                        error("Expected keyword \"end\"");
                                        return false;
                                    }
                                }
                            }
                            else
                            {
                                error("Expected keyword \"do\"");
                                return false;
                            }
                        }
                    }
                    else
                    {
                        error("Expected \";\"");
                        return false;
                    }
                }
            }
            else
            {
                error("Expected \";\"");
                return false;
            }
        }
        return false;
    }

    public boolean parseExpr()
    {
        boolean result = parseN_Expr();
        if(nextToken.l == Lexer.Lexeme.CLOSEP)
        {
            lex();
            return parseV_Expr();
        }
        else if(result)
        {
            return parseB_Expr();
        }
        else
        {
            return false;
        }
    }

    public boolean parseV_Expr()
    {
        if(nextToken.l == Lexer.Lexeme.GT || nextToken.l == Lexer.Lexeme.GTE || nextToken.l == Lexer.Lexeme.LTE || nextToken.l == Lexer.Lexeme.LT || nextToken.l == Lexer.Lexeme.EQUAL || nextToken.l == Lexer.Lexeme.NOT_EQUAL)
        {
            return parseValue();
        }
        else
        {
            return parseF_Expr();
        }
    }

    public boolean parseFactor()
    {
        boolean result = parseValue();
        if(result)
        {
            lastToken = nextToken;
            lex();
            if (!parseV_Expr())
            {
                nextToken = lastToken;
                input = (lastToken.t == null ? lastToken.p : lastToken.t.p);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean parseF_Expr()
    {
        if(nextToken.l == Lexer.Lexeme.MULTIPLY || nextToken.l == Lexer.Lexeme.DIVIDE || nextToken.l == Lexer.Lexeme.MOD)
        {
            return parseTerm();
        }
        else
        {
            return parseT_Expr();
        }
    }

    public boolean parseTerm()
    {
        boolean result = parseFactor();
        if(result)
        {
            return parseF_Expr();
        }
        else
        {
            return false;
        }
    }

    public boolean parseT_Expr()
    {
        if(nextToken.l == Lexer.Lexeme.PLUS || nextToken.l == Lexer.Lexeme.MINUS)
        {
            return parseN_Expr();
        }
        else
        {
            return parseB_Expr();
        }
    }

    public boolean parseB_Expr()
    {
        if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.KEYWORD && (nextToken.t.p.equalsIgnoreCase("and") || (nextToken.t.p.equalsIgnoreCase("or"))))
        {
            return parseN_Expr();
        }
        else
        {
            return true;
        }
    }

    public boolean parseN_Expr()
    {
        boolean result = parseTerm();
        if(result)
        {
            return parseT_Expr();
        }
        else
        {
            return false;
        }
    }

    public boolean parseValue()
    {
        lex();
        if(nextToken.l == Lexer.Lexeme.OPENP)
        {
            return parseExpr();
        }
        else if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.KEYWORD && nextToken.t.p.equalsIgnoreCase("not"))
        {
            return parseValue();
        }
        else if (nextToken.l == Lexer.Lexeme.MINUS)
        {
            return parseValue();
        }
        else if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.ID)
        {
            return true;
        }
        else if(nextToken.t != null && nextToken.t.l == Lexer.Lexeme.INT)
        {
            return true;
        }
        else
        {
            error("Unexpected Value");
            return false;
        }
    }


    public void lex()
    {
        nextToken = lexer.lex(input);
        input = nextToken.p;
        if(nextToken.l == Lexer.Lexeme.ERROR)
        {
            System.out.println(nextToken.t.p);
            System.out.println(nextToken.p);
        }
    }


}
