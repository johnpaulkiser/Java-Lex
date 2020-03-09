// Simple bottom up parser using limited token set


//Grammar
/*
1. E -> E + T
2. E -> T
3. E -> T * F
4. T -> F
5. F -> (E)
6. F -> id
*/

import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

class Parse {

    public static void main(String[] args) {

        
        String[] tokens = {"id", "+", "id", "*", "id", "$"};
        ArrayList<String> tokenList = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        stack.push("0");

        String[] actions = {"id", "+", "*", "(", ")", "$", "E", "T", "F"}; 
        HashMap<String, Integer> columns = new HashMap<String, Integer>();

        
        //initialize column values
        for(int i = 0; i < actions.length; i++) 
            columns.put(actions[i], i);
        
        //initialize input token List
        for(String token : tokens) 
            tokenList.add(token);
        
        // initialize parse table
        String[][] table = initTable();
        ArrayList<ArrayList<String>> grammar = initGrammar();


        String nextToken = "", fromStack = "", fromTable = "";
        System.out.println(String.format("\n%-15s %-20s %-15s\n", "Stack", "Input", "Action"));
        while(!tokenList.isEmpty()) {

            System.out.print(String.format("%-15s", reverseStackToString(stack)));
            
            System.out.print(String.format("%-20s", printList(tokenList)));
           
            // read first token and stack instruction
            nextToken = tokenList.get(0);
            fromStack = stack.peek();
            

            //look up in table
            fromTable = table[Integer.parseInt(fromStack)][columns.get(nextToken)];
            
            // reached $; finished parsing
            if (fromTable.equals("acc")) {
                System.out.println("Accept");
                return;
            }

            // handle empty string
            if (fromTable.equals("")) {

                //NOT FINISHED
                continue;
            }

            // handle shift 
            if (fromTable.charAt(0) == 's') {
                
                // push token & shift number on stack
                stack.push(tokenList.remove(0));
                String num = fromTable.substring(1, fromTable.length());
                stack.push(num);
                System.out.println("Shift " + num);


            // handle reduce
            } else if (fromTable.charAt(0) == 'r') { 
                // find rule to do replacement
                int rule = Integer.parseInt(fromTable.substring(1, fromTable.length()));
                String token = "", toAdd = "", popped = ""; 
                int peaked;
                System.out.print("Reduce " + rule);

                //loop through rule in reverse order
                // for each token pop off the stack until finding match 
                for(int i = grammar.get(rule).size()-1; i >= 0; i--) {
                    token = grammar.get(rule).get(i);
                    // check if reached left hand side of rule
                    if (token.equals("->")){
                        peaked = Integer.parseInt(stack.peek());
                        toAdd = grammar.get(rule).get(0); // get the lefthand side of the rule
                        stack.push(toAdd);
                        //GOTO
                        System.out.println(String.format(" GOTO[%d, %s]", peaked, toAdd));
                        stack.push(table[peaked][columns.get(toAdd)]);
                        break;
                    }
                    
                    popped = stack.pop();
                    while(!popped.equals(token)) {
                        popped = stack.pop();
                    }
                    
                }

            }

            


            

        }

        // System.out.println(table[1][columns.get("$")]);

    }

    public static String reverseStackToString(Stack<String> s) { 
        
        if (s.empty())  
            return ""; 

        String x = s.pop(); 
        String part = reverseStackToString(s) + x;
        s.push(x);
        return part;
    }

    public static String printList(ArrayList<String> l){
        String built = "";
        for(String s : l)
            built += " " + s;

        return built;
    }

    public static ArrayList<ArrayList<String>> initGrammar() {
        ArrayList<ArrayList<String>> grammar = new ArrayList<ArrayList<String>>();
        ArrayList<String> rule = new ArrayList<String>();

        //0. empty
        grammar.add(rule);
        rule = new ArrayList<String>();

        //1. E -> E + T
        rule.add("E");
        rule.add("->");
        rule.add("E");
        rule.add("+");
        rule.add("T");
        grammar.add(rule);
        rule = new ArrayList<String>();

        //2. E -> T
        rule.add("E");
        rule.add("->");
        rule.add("T");
        grammar.add(rule);
        rule = new ArrayList<String>();

        //3. T -> T * F
        rule.add("T");
        rule.add("->");
        rule.add("T");
        rule.add("*");
        rule.add("F");
        grammar.add(rule);
        rule = new ArrayList<String>();

        //4. T -> F
        rule.add("T");
        rule.add("->");
        rule.add("F");
        grammar.add(rule);
        rule = new ArrayList<String>();

        //5. F -> (E)
        rule.add("F");
        rule.add("->");
        rule.add("(");
        rule.add("E");
        rule.add(")");
        grammar.add(rule);
        rule = new ArrayList<String>();

        //6. F -> id
        rule.add("F");
        rule.add("->");
        rule.add("id");
        grammar.add(rule);
        rule = new ArrayList<String>();
        
        return grammar;

    }

    // builds parse table for above grammar
    public static String[][] initTable() {
        String[][] table = new String[12][9];
        table[0][0] = "s5";
        table[0][3] = "s4";
        table[0][6] = "1";
        table[0][7] = "2";
        table[0][8] = "3";
        table[1][1] = "s6";
        table[1][5] = "acc";
        table[2][1] = "r2";
        table[2][2] = "s7";
        table[2][4] = "r2";
        table[2][5] = "r2";
        table[3][1] = "r4";
        table[3][2] = "r4";
        table[3][4] = "r4";
        table[3][5] = "r4";
        table[4][0] = "s5";
        table[4][3] = "s4";
        table[4][6] = "8";
        table[4][7] = "2";
        table[4][8] = "3";
        table[5][1] = "r6";
        table[5][2] = "r6";
        table[5][4] = "r6";
        table[5][5] = "r6";
        table[6][0] = "s5";
        table[6][3] = "s4";
        table[6][7] = "9";
        table[6][8] = "3";
        table[7][0] = "s5";
        table[7][3] = "s4";
        table[7][8] = "10";
        table[8][1] = "s6";
        table[8][4] = "s11";
        table[9][1] = "r1";
        table[9][2] = "s7";
        table[9][4] = "r1";
        table[9][5] = "r1";
        table[10][1] = "r3";
        table[10][2] = "r3";
        table[10][4] = "r3";
        table[10][5] = "r3";
        table[11][1] = "r5";
        table[11][2] = "r5";
        table[11][4] = "r5";
        table[11][5] = "r5";

        return table;
    }    
}