package com.company;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Language_In_Java_Main {

    public static void main(String[] args){
        //get input program
        String prog = "";
        Language_In_Java_Main run = new Language_In_Java_Main();
        Parser parse = new Parser();
        if(args.length > 0)
            prog = run.getInput(args[0]);
        else
        {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Please input the full name(including the extension) of the program you wish to lexicalize\neg. \"testProgram.txt\"");
            prog = run.getInput(keyboard.nextLine());
            while(prog.equalsIgnoreCase("Error: File Not Found"))
            {
                System.out.println(prog + "\n" + "Please input the full name(including the extension) of the program you wish to lexicalize\neg. \"testProgram.txt\"");
                prog = run.getInput(keyboard.nextLine());
            }
            keyboard.close();
        }
//        String prog = run.getInput("tester.txt");
        System.out.println(parse.parseProg(prog) ? "Program is Correct" : "Program is Incorrect");

//        //vestigial parse input program[lexer driver]
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

    public String getInput(String inputfile){
        try
        {
            URL url = getClass().getResource(inputfile);
            assert url != null;
            Path file = Paths.get(url.getPath().substring(1));
            return Files.readString(file);
        }
        catch(Exception E)
        {
            return "Error: File Not Found";
        }
    }
}

