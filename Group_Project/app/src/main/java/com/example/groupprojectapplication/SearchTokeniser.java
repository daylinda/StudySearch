package com.example.groupprojectapplication;

import java.util.Scanner;
// @author Jack
// This has skeleton code taken from the labs(Thanks Llew!), has been *Heavily* customised by me for this particular application
public class SearchTokeniser {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private SearchToken currentToken;     // The current token. The next token is extracted when next() is called.


    //Thanks Llew!
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    //This entire Main section is from the labs, it is useful for testing but holds no relavence
    // To the actual application
    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        System.out.println("Provide a Search string to be tokenized:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            SearchTokeniser tokenizer = new SearchTokeniser(input);

            // Print all the tokens.
            while (tokenizer.hasNext()) {
                System.out.print(tokenizer.current() + " ");
                tokenizer.next();
            }
            System.out.println();
        }
    }

    public SearchTokeniser(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }


    public void next() {
        buffer = buffer.trim();     // remove whitespace, Thanks Llew!

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }
        int tokenLen = -1;
        char firstChar = buffer.charAt(0);

        if (firstChar=='<')
            currentToken = new SearchToken("<", SearchToken.Type.LESSTHAN);
        else if (firstChar=='>')
            currentToken = new SearchToken(">", SearchToken.Type.GREATERTHAN);
        else if (firstChar==';')
            currentToken = new SearchToken(";", SearchToken.Type.NEXTMARK);
        else if (firstChar==':')
            currentToken = new SearchToken(":", SearchToken.Type.CONTAINS);
        else if (firstChar==',')
            currentToken = new SearchToken(",", SearchToken.Type.LISTMARK);
        else if (firstChar=='(') {
            int i = 1;
            String tok = firstChar+ "";
            if (buffer.length()>1){
                while (!(buffer.charAt(i)==')')){
                    tok = tok + buffer.charAt(i);
                    i++;
                    if (buffer.substring(i).length()<1) {
                        throw new SearchToken.IllegalTokenException( "Didnt See a closing ) " +buffer);
                    }
                }
                tok = tok + buffer.charAt(i);
            }
            if (tok.equals("(TIME)"))
                currentToken = new SearchToken(tok, SearchToken.Type.TimeField);
            else if (tok.equals("(DAY)"))
                currentToken = new SearchToken(tok, SearchToken.Type.DayField);
            else if (tok.equals("(TAG)"))
                currentToken = new SearchToken(tok, SearchToken.Type.TagField);
            else if (tok.equals("(ALTSORT)"))
                currentToken = new SearchToken(tok, SearchToken.Type.ALTSORT);
            else
                throw new SearchToken.IllegalTokenException( "I dont recognise this key"+ tok);
        } else if (Character.isLetter(firstChar)) {
            int i = 1;
            String tok = firstChar + "";
            if (buffer.length()>1){
                while (Character.isLetter(buffer.charAt(i))){
                    tok = tok + buffer.charAt(i);
                    i++;
                    if (buffer.substring(i).length()<1) {
                        break;
                    }
                }
            }
            if (tok.equals("Mon") || tok.equals("Tue")|| tok.equals("Wed")|| tok.equals("Thurs")|| tok.equals("Fri")|| tok.equals("Sat")|| tok.equals("Sun"))
                currentToken = new SearchToken(tok, SearchToken.Type.DAY);
            else
                currentToken = new SearchToken(tok, SearchToken.Type.TAG);
            
        } else if (Character.isDigit(firstChar)) {
            int i = 1;
            String tok = firstChar + "";
            if (buffer.length()>1){
                while (Character.isDigit(buffer.charAt(i))){
                    tok = tok + buffer.charAt(i);
                    i++;
                    if (buffer.substring(i).length()<1) {
                        break;
                    }
                }
            }
            tokenLen = tok.length();
            tok = Integer.valueOf(tok).toString();
            tok = String.format("%5s", tok).replace(' ', '0');
            if (Integer.valueOf(tok)>62359){
                throw new SearchToken.IllegalTokenException( "Time is too large: "+ tok);
            }
            if (Integer.valueOf((String.valueOf(tok.charAt(0))))>6){
                throw new SearchToken.IllegalTokenException( "Day Int is too large: "+ tok.charAt(0));
            }
            if (Integer.valueOf(tok.substring(1))>2359){
                throw new SearchToken.IllegalTokenException( "Hour int is too large: "+ tok.substring(1));
            }
            if (Integer.valueOf(tok.substring(3))>59){
                throw new SearchToken.IllegalTokenException( "Minute Int is too large " + tok.substring(3));
            }
            currentToken = new SearchToken(tok, SearchToken.Type.TIME);
        } else
            throw new SearchToken.IllegalTokenException( "This token has not been recognised " + firstChar);

        if ((tokenLen==-1)){
            tokenLen = currentToken.getToken().length();
        }

        buffer = buffer.substring(tokenLen);
    }

    public SearchToken current() {
        return currentToken;
    }

    public boolean hasNext() {
        return currentToken != null;
    }
}
