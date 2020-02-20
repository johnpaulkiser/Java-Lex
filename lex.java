import java.util.Scanner;
import java.io.*;

public class lex{

    public static void main(String[] args) throws IOException {
        Scanner scanf = new Scanner(new File("input.txt"));
        int lineNum = 1;
        String operators = "()+=-*/,;";

        while (scanf.hasNextLine()){
            String line = scanf.nextLine();

            for (int i = 0; i < line.length(); i++) {
                if (Character.isLetter(line.charAt(i))) { // check for identifier/ keywords
                    i = handleLetter(i, lineNum, line);
                    if (i == -1) break;
                } else if (operators.contains(Character.toString(line.charAt(i)))){ // check for ops
                    String op = Character.toString(line.charAt(i));
                    System.out.println(String.format("Line %d: %d operator: %s", lineNum, i+1, op));
                } else if (Character.isDigit(line.charAt(i))) { // check for double/int
                    i = handleNumber(i, lineNum, line);
                    if (i == -1) break; 
                } else if (line.charAt(i) == '"'){ // check for strings
                    i = handleString(i, lineNum, line);
                    if (i == -1) break;
                } else if (!(line.charAt(i) == ' ')){ // check for errors
                    String err = Character.toString(line.charAt(i));
                    System.out.println(String.format("Line %d: %d error: %s, not recognized", lineNum, i+1, err));
                }
            }
            lineNum++;
        }
    }


    public static int handleLetter(int pos, int lineNum, String line) {
        String[] keywords = {"int", "String", "double"};
        String lexeme = "";
        int end = 0;

        for (int i = pos; i < line.length(); i++) {
            if (!Character.isLetter(line.charAt(i)) && !Character.isDigit(line.charAt(i))) {
                end = i;
                lexeme = line.substring(pos, end);
                break;
            }
        }

        if (lexeme.equals("")) {
            lexeme = line.substring(pos, line.length());
        }

        for(String word : keywords) {
            if (word.equals(lexeme)) {
                System.out.println(String.format("Line %d: %d keyword: %s", lineNum, pos, lexeme));
                return end-1;
            }
        }
        System.out.println(String.format("Line %d: %d identifier: %s", lineNum, pos, lexeme));
        return end-1; 
    }  

    public static int handleNumber(int pos, int lineNum, String line) {
        boolean decimalSeen = false;
        String lexeme = "";
        int end = 0;

        for (int i = pos; i < line.length(); i++) {
            if (!Character.isDigit(line.charAt(i)))
                if (line.charAt(i) == '.' && !decimalSeen)
                    decimalSeen = true;
                else {
                    end = i;
                    lexeme = line.substring(pos, end);
                    break;
                }
        }

        if (lexeme.equals("")) {
            lexeme = line.substring(pos, line.length());
        }
        
        if (decimalSeen) {
            System.out.println(String.format("Line %d: %d double constant: %s", lineNum, pos, lexeme));
            return end-1;
        }
        System.out.println(String.format("Line %d: %d int constant: %s", lineNum, pos, lexeme));
        return end-1; 
    }
  
    public static int handleString(int pos, int lineNum, String line) {
        String lexeme = "";
        int end = 0;

        for (int i = pos+1; i < line.length(); i++) {
            if (line.charAt(i) == '"'){
                end = i+1;
                lexeme = line.substring(pos, end);
                break;
            }
        }

        if (lexeme.equals("")) { // no closing "
            System.out.println(String.format("Line %d: %d error:no closing  %s, found", lineNum, pos, "\""));
            return end-1;
        }
        System.out.println(String.format("Line %d: %d string constant: %s", lineNum, pos, lexeme));
        return end-1; 
    }
}
