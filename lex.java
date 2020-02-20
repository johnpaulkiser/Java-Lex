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

                // check for identifier/ keywords
                if (Character.isLetter(line.charAt(i))) { 
                    i = handleLetter(i, lineNum, line);
                    if (i == -1) break;
                
                // check for operators
                } else if (operators.contains(Character.toString(line.charAt(i)))){ 
                    String op = Character.toString(line.charAt(i));
                    System.out.println(String.format("Line %d: %d operator: %s", lineNum, i+1, op));
                   
                // check for double/int
                } else if (Character.isDigit(line.charAt(i))) { 
                    i = handleNumber(i, lineNum, line);
                    if (i == -1) break; 
                    
                // check for strings
                } else if (line.charAt(i) == '"'){ 
                    i = handleString(i, lineNum, line);
                    if (i == -1) break;
                    
                // check for errors
                } else if (!(line.charAt(i) == ' ')){ 
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


        // loop from first letter until hitting neither a digit nor letter
        for (int i = pos; i < line.length(); i++) {
            if (!Character.isLetter(line.charAt(i)) && !Character.isDigit(line.charAt(i))) {
                end = i;
                lexeme = line.substring(pos, end);
                break;
            }
        }

        // checks if hits end of line before finding delimiter 
        if (lexeme.equals("")) {
            lexeme = line.substring(pos, line.length());
        }

        // check if lexeme is a keyword
        for(String word : keywords) {
            if (word.equals(lexeme)) {
                System.out.println(String.format("Line %d: %d keyword: %s", lineNum, pos, lexeme));
                return end-1;
            }
        }
        // lexeme must be an identifier
        System.out.println(String.format("Line %d: %d identifier: %s", lineNum, pos, lexeme));
        return end-1; 
    }  

    public static int handleNumber(int pos, int lineNum, String line) {
        boolean decimalSeen = false;
        String lexeme = "";
        int end = 0;

        // loop until you hit !digit 
        for (int i = pos; i < line.length(); i++) {
            if (!Character.isDigit(line.charAt(i)))
                
                // if hit first decimal point then we know our lexeme is a double
                if (line.charAt(i) == '.' && !decimalSeen)
                    decimalSeen = true;
                // otherwise its an int
                else { 
                    end = i;
                    lexeme = line.substring(pos, end);
                    break;
                }
        }
        
        // check if numbers run to end of the line
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

        // loop over line until hitting closing quotes
        for (int i = pos+1; i < line.length(); i++) {
            if (line.charAt(i) == '"'){
                end = i+1;
                lexeme = line.substring(pos, end);
                break;
            }
        }
        
        // if we reach end of line without finding closing quotes we'll throw error
        // no multi-line strings :(
        if (lexeme.equals("")) { 
            System.out.println(String.format("Line %d: %d error:no closing  %s, found", lineNum, pos, "\""));
            return end-1;
        }
        System.out.println(String.format("Line %d: %d string constant: %s", lineNum, pos, lexeme));
        return end-1; 
    }
}
