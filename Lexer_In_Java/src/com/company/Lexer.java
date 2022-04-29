package com.company;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Lexer
{
    public enum Lexeme {
        SEMICOLON,
        ASSIGN,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MOD,
        GT, //Greater than
        GTE,
        LT, //Less than
        LTE,
        EQUAL,
        NOT_EQUAL,
        INT,
        STRING,
        ID,
        KEYWORD,
        END_OF_INPUT,
        OPENP,
        CLOSEP,
        COMMA,
        ERROR
    }

    List<String> keywords = Arrays.asList("print","get","if","then","else","end","while","do","and","or","not");

    public int line = 0;

    public void next_line()
    {
        line++;
    }

    public Tuple lex(String input)
    {
        int i = 0;

        while(i < input.length() && (input.charAt(i) == ' ' || input.charAt(i) == '\r' || input.charAt(i) == '\n'))
        {
            if(input.charAt(i) == '\n')
            {
                next_line();
            }
            i += 1;
        }

        if(i >= input.length())
        {
            return new Tuple(Lexeme.END_OF_INPUT, "");
        }

        //hard infini chars --------------------------------
        if(input.charAt(i) == '"')
        {
            return lex_string(input.substring(i));
        }
        else if(Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')
        {
            return lex_ID_or_keyword(input.substring(i));
        }
        else if(Character.isDigit(input.charAt(i)))
        {
            return lex_int(input.substring(i), 1);
        }
        else if(input.charAt(i) == '+')
        {
            i = i+1;
            if(i < input.length() && Character.isDigit(input.charAt(i)))
                return lex_int(input.substring(i), 1);
            else
                return new Tuple(Lexeme.PLUS, input.substring(i));
        }
        else if(input.charAt(i) == '-')
        {
            i = i+1;
            if(i < input.length() && Character.isDigit(input.charAt(i)))
                return lex_int(input.substring(i), -1);
            else
                return new Tuple(Lexeme.MINUS, input.substring(i));
        }
        //ez 2 chars ---------------------------------------
        else if(input.charAt(i) == '=')
        {
            i += 1;
            if(i < input.length() && input.charAt(i) == '=')
                return new Tuple(Lexeme.EQUAL, input.substring(i+1));
            else
                return new Tuple(Lexeme.ASSIGN, input.substring(i));
        }
        else if(input.charAt(i) == '>')
        {
            i += 1;
            if(i < input.length() && input.charAt(i) == '=')
                return new Tuple(Lexeme.GTE, input.substring(i+1));
            else
                return new Tuple(Lexeme.GT, input.substring(i));
        }
        else if(input.charAt(i) == '<')
        {
            i += 1;
            if(i < input.length() && input.charAt(i) == '=')
                return new Tuple(Lexeme.LTE, input.substring(i+1));
            else
                return new Tuple(Lexeme.LT, input.substring(i));
        }
        else if(input.charAt(i) == '!')
        {
            i += 1;
            if(i < input.length() && input.charAt(i) == '=')
                return new Tuple(Lexeme.NOT_EQUAL, input.substring(i+1));
            else
                return new Tuple(Lexeme.ERROR, "Unexpected Character at index " + i + " expected a =");
        }
        //ez single chars ----------------------------------
        else if(input.charAt(i) == ';')
        {
            return new Tuple(Lexeme.SEMICOLON, input.substring(i+1));
        }
        else if(input.charAt(i) == '*')
        {
            return new Tuple(Lexeme.MULTIPLY, input.substring(i+1));
        }
        else if(input.charAt(i) == '/')
        {
            return new Tuple(Lexeme.DIVIDE, input.substring(i+1));
        }
        else if(input.charAt(i) == '%')
        {
            return new Tuple(Lexeme.MOD, input.substring(i+1));
        }
        else if(input.charAt(i) == '(')
        {
            return new Tuple(Lexeme.OPENP, input.substring(i+1));
        }
        else if(input.charAt(i) == ')')
        {
            return new Tuple(Lexeme.CLOSEP, input.substring(i+1));
        }
        else if(input.charAt(i) == ',')
        {
            return new Tuple(Lexeme.COMMA, input.substring(i+1));
        }
        else
            return new Tuple(Lexeme.ERROR, "Could not find a lexeme");
    }

    public Tuple lex_int(String input, int sign)
    {
        int i = 0;
        StringBuilder integer = new StringBuilder();
        while(i < input.length() && (input.charAt(i) >= 48 && input.charAt(i) <= 57))
        {
            integer.append(input.charAt(i));
            i += 1;
        }
        int num = Integer.parseInt(integer.toString()) * sign;
        return new Tuple(new Tuple(Lexeme.INT, Integer.toString(num)),input.substring(i));
    }

    public Tuple lex_string(String input)
    {
        int i = 1;
        StringBuilder str = new StringBuilder();
        while(i < input.length() && input.charAt(i) != '"')
        {
            str.append(input.charAt(i));
            i += 1;
        }
        return new Tuple(new Tuple(Lexeme.STRING, str.toString()), input.substring(i+1));
    }

    public Tuple lex_ID_or_keyword(String input)
    {
        int i = 0;
        StringBuilder str = new StringBuilder();
        while(i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_'))
        {
            str.append(input.charAt(i));
            i += 1;
        }
        return new Tuple(lookup(str.toString()), input.substring(i));
    }

    public Tuple lookup(String key)
    {
        if(keywords.contains(key))
            return new Tuple(Lexeme.KEYWORD, key);
        else
            return new Tuple(Lexeme.ID, key);
    }
}
