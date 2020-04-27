package Sudoku;

public class SudokuLayout {
    public static String grid1 = "----3--67";
    public static String grid2 = "-------35";
    public static String grid3 = "---16---4";
    public static String grid4 = "6-8-9---2";
    public static String grid5 = "12--8--79";
    public static String grid6 = "9---3-8-6";
    public static String grid7 = "8---26---";
    public static String grid8 = "69-------";
    public static String grid9 = "35--9----";

    public static String getGrid(int nr) {
        switch(nr) {
            case 1: return grid1;
            case 2: return grid2;
            case 3: return grid3;
            case 4: return grid4;
            case 5: return grid5;
            case 6: return grid6;
            case 7: return grid7;
            case 8: return grid8;
            case 9: return grid9;
        }
        return null;
    }
}
