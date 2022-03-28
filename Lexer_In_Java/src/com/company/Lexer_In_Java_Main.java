package com.company;

import java.util.Scanner;

public class Lexer_In_Java_Main {

    public static void main(String[] args) throws Exception {
        String prog = "";
        Lexer lexical = new Lexer();
        prog = lexical.getInput("tester.txt");
//        if(args.length > 0)
//            prog = lexical.getInput(args[0]);
//        else
//        {
//            Scanner keyboard = new Scanner(System.in);
//            System.out.println("Please input the full name(including the extension) of the program you wish to lexicalize\neg. \"testProgram.txt\"");
//            prog = lexical.getInput(keyboard.nextLine());
//        }

        Tuple lexd = lexical.lex(prog);
        while(prog.length() > 0)
        {
            if(lexd.t != null)
                System.out.println("TOKEN: " + lexd.t.l.toString() + "\nVALUE: " + lexd.t.p);
            else
                System.out.println("TOKEN: " + lexd.l.toString() + "\nVALUE: " + lexd.p);
            System.out.println();
            if(lexd.l == Lexer.Lexeme.ERROR || lexd.l == Lexer.Lexeme.END_OF_INPUT)
            {
                break;
            }
            lexd = lexical.lex(lexd.p);
        }
    }
}
