
import java.io.*;
import java.util.*;

public class Crossword {

    private static DictInterface D;
    private static char[][] theBoard;
    private static char[] alphabet;
    private StringBuilder currentSolution;
    private static StringBuilder[] rowStr;
    private static StringBuilder[] colStr;
    private static int dim;
    private static boolean flag = false;
    private static char letter;
    private static StringBuilder r;
    private static StringBuilder c;

    public static void main(String[] args) throws IOException {

        new Crossword(args[0], args[1]);
    }

    public Crossword(String dictFile, String testFile) throws IOException {
        //Read the dictionary
        Scanner fileScan = new Scanner(new FileInputStream(dictFile));
        String st;
        D = new MyDictionary();

        while (fileScan.hasNext()) {
            st = fileScan.nextLine();
            D.add(st);
        }
        fileScan.close();

        // Parse input file of the Boggle board to create 2-d grid of characters
        Scanner fReader;
        File fName;
        String fString = "";

        // Make sure the file name for the Boggle board is valid
        while (true) {
            try {
                fString = testFile;
                fName = new File(fString);
                fReader = new Scanner(fName);

                break;
            } catch (IOException e) {
                System.out.println("Problem: " + e);
            }
        }

        String number = fReader.nextLine();
        dim = Integer.parseInt(number);

        theBoard = new char[dim][dim];

        for (int i = 0; i < dim; i++) {
            String rowString = fReader.nextLine();
            for (int j = 0; j < rowString.length(); j++) {
                theBoard[i][j] = rowString.charAt(j);
            }
        }
        fReader.close();

        // Show user the board
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                System.out.print(theBoard[i][j] + " ");
            }
            System.out.println();
        }
        rowStr = new StringBuilder[dim];
        colStr = new StringBuilder[dim];
        for (int i = 0; i < rowStr.length; i++) {
            rowStr[i] = new StringBuilder();
        }
        for (int i = 0; i < colStr.length; i++) {
            colStr[i] = new StringBuilder();
        }
        solve(0, 0);
        for(int o = 0; o < rowStr.length; o++){
          System.out.println(rowStr[o]);
        }
    }

    private void solve(int row, int col) {
        //for debugging
        // for(int i=0; i<depth; i++){
        // 	System.out.print(" ");
        // }
        // System.out.println(row + ", " + col + " " + currentSolution.toString());
        alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        if (row == dim) {
            flag = true;
            return;
        }
        if (col >= dim) {
            solve(row + 1, 0);
            if (flag) {
                return;
            }
        }
        else if (theBoard[row][col] == '-') {
            rowStr[row].append('-');
            colStr[col].append('-');
            solve(row, col + 1);
            if (flag) {
                return;
            }
            rowStr[row].deleteCharAt(rowStr[row].length() - 1);
            colStr[col].deleteCharAt(colStr[col].length() - 1);

        }
        else if(theBoard[row][col] == '+'){

        for (int i = 0; i < 26; i++) {
          letter = alphabet[i];
            if (isValid(row, col)) {
                rowStr[row].append(alphabet[i]);
                colStr[col].append(alphabet[i]);
                solve(row, col + 1);
            if(flag) {return;}

            rowStr[row].deleteCharAt(rowStr[row].length() - 1);
            colStr[col].deleteCharAt(colStr[col].length() - 1);
          }
        }
    }
    else{
      letter = theBoard[row][col];
      if(isValid(row, col)){
      rowStr[row].append(theBoard[row][col]);
      colStr[col].append(theBoard[row][col]);
      solve(row, col+1);
      if(flag) {return;}
      rowStr[row].deleteCharAt(rowStr[row].length() - 1);
      colStr[col].deleteCharAt(colStr[col].length() - 1);
    }


  }
}

    private static boolean isValid(int row, int col) {
        r = new StringBuilder(rowStr[row]);
        c = new StringBuilder(colStr[col]);
        r = findMinus(r.append(letter));
        c = findMinus(c.append(letter));
        int colRes = D.searchPrefix(c);
        int rowRes = D.searchPrefix(r);
        if (!isEndIndexRow(row, col)) {
            if (rowRes == 2 || rowRes == 0) {
                return false;
            };
        } else {
            if (rowRes == 1 || rowRes == 0) {
                return false;
            };
        }
        if (!isEndIndexCol(row, col)) {
            if (colRes == 2 || colRes == 0) {
                return false;
            };
        } else {
            if (colRes == 1 || colRes == 0) {
                return false;
            };
        }
        return true;
    }

    private static boolean isEndIndexRow(int row, int col) {
        if (col == dim - 1) {
            return true;
        }
        if (theBoard[row][col + 1] == '-') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isEndIndexCol(int row, int col) {
        if (row == dim - 1) {
            return true;
        }
        if (theBoard[row + 1][col] == '-') {
            return true;
        } else {
            return false;
        }

    }

    private static StringBuilder findMinus(StringBuilder value) {
        int last = -1;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '-') {
                last = i;
            }
        }
        if(last == -1){
          return value;
        }
        return new StringBuilder(value.substring(last+1));
    }

}
