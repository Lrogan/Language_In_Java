package com.company;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Lexer_In_Java_Main {

    public static void main(String[] args) throws Exception {
        //get input program
        String prog = "";
        Lexer_In_Java_Main run = new Lexer_In_Java_Main();
        Lexer lexical = new Lexer();
        if(args.length > 0)
            prog = run.getInput(args[0]);
        else
        {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Please input the full name(including the extension) of the program you wish to lexicalize\neg. \"testProgram.txt\"");
            prog = run.getInput(keyboard.nextLine());
        }

//        //parse input program[lexer driver]
//        Tuple lexd = lexical.lex(prog);
//        while(prog.length() > 0)
//        {
//            if(lexd.t != null)
//                System.out.println("TOKEN: " + lexd.t.l.toString() + "\nVALUE: " + lexd.t.p);
//            else
//                System.out.println("TOKEN: " + lexd.l.toString() + "\nVALUE: " + lexd.p);
//            System.out.println();
//            if(lexd.l == Lexer.Lexeme.ERROR || lexd.l == Lexer.Lexeme.END_OF_INPUT)
//            {
//                break;
//            }
//            lexd = lexical.lex(lexd.p);
//        }
    }

    public String getInput(String inputfile) throws Exception {
        try
        {
            URL url = getClass().getResource(inputfile);
            assert url != null;
            Path file = Paths.get(url.getPath().substring(1));
            return Files.readString(file);
        }
        catch(Exception E)
        {
            throw new Exception("File Not Found", E);
        }

    }
}

