import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CSVReader {
    private static String csvPath = "";

    //Return a legal permutation
    public static int[][] getPermutation(int n) throws Exception {
        String line = "";
        String splitBy = ",";
        boolean needRead = false;
        int indexOfLine = 0;
        int indexOfBoard = -1;
        List<int[][]> lstOfBoard = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            while ((line = br.readLine()) != null) {
                //split the line into an array
                String[] lineArr = line.split(splitBy);
                if (lineArr.length == 0) {
                    throw new Exception("Empty Line");
                }
                //the n in the CSV file
                if (lineArr.length == 1) {
                    if (Integer.parseInt(lineArr[0]) == n) {   //if it's the desired n
                        needRead = true;
                        indexOfLine = 0;
                        lstOfBoard.add(new int[n][n]);
                        indexOfBoard++;
                    }
                    else {
                        needRead = false;
                    }
                }
                else if (needRead){   //it's a locations line
                    for (int i=0; i<n; i++) {
                        lstOfBoard.get(indexOfBoard)[indexOfLine][i] = Integer.parseInt(lineArr[i]);
                    }
                    indexOfLine++;
                }
            }
            if (lstOfBoard.isEmpty()) {
                //We don't have a board to return!
                return null;
            }
            Random rnd = new Random();
            //We will return a random valid board
            return lstOfBoard.get(rnd.nextInt(lstOfBoard.size()));
        }
        catch (Exception e) {
            //We will pass the exception to the frame so we can close the frame correctly
            throw new Exception();
        }
    }
}
