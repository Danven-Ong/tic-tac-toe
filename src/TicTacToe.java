import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicTacToe {

    private boolean isPlayerOne = true;
    private boolean stopGame = false;
    private final String[] row1 = {"   ", "   ", "   "};
    private final String[] row2 = {"   ", "   ", "   "};
    private final String[] row3 = {"   ", "   ", "   "};
    private final String[][] rows = {row1, row2, row3};
    private final ArrayList<Integer> boxesOccupied = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);


    public TicTacToe(int numberOfPlayers) {

        boolean isSolo = (numberOfPlayers == 1);

        if (isSolo) {
            runGameSolo(startFirst());
        } else {
            runGameTwoPlayer();
        }
    }

    public boolean isStopGame() {
        return stopGame;
    }

    private void restartGame() {

        while (true) {

            System.out.println("Play again? Y/N");
            String option = scanner.next().toUpperCase();

            switch (option) {
                case "Y" -> {
                    stopGame = false;
                    return;
                }
                case "N" -> {
                    return;
                }
                default -> System.out.println("Please pick Y or N!");
            }
        }
    }
    private void printBoard(boolean checkForDraw, boolean printHeader) {

        if (printHeader) System.out.println(isPlayerOne ? "Player 1's move: " : "Player 2's move: ");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(rows[i][j]);
                if (j != 2) {
                    System.out.print("|");
                }
            }
            if (i != 2) {
                System.out.print("\n---|---|---\n");
            }
        }
        System.out.println("\n");


        if (checkForDraw && checkForDraw()) {

            System.out.println("It is a draw!");
            stopGame = true;
        }
    }

    private void runGameSolo(boolean startFirst) {

        if (startFirst) {

            printBoard(false, false);

            makeMove();
            isPlayerOne = !isPlayerOne;

            botMakesMove();
            isPlayerOne = !isPlayerOne;
        }

        if (!startFirst) {

            botMakesMove();
            isPlayerOne = !isPlayerOne;

            makeMove();
            isPlayerOne = !isPlayerOne;

            botMakesMove();
            isPlayerOne = !isPlayerOne;
        }

        while (true) {

            makeMove();
            if (stopGame) break;
            isPlayerOne = !isPlayerOne;

            botMakesMove();
            if (stopGame) break;
            isPlayerOne = !isPlayerOne;
        }

        restartGame();
    }

    private void runGameTwoPlayer() {

        printBoard(false, false);

        while (true) {

            makeMove();
            if (stopGame) break;
            isPlayerOne = !isPlayerOne;
            makeMove();
            if (stopGame) break;
            isPlayerOne = !isPlayerOne;

        }

        restartGame();
    }

    private boolean startFirst() {
        while (true) {
            System.out.print("Choose Player 1 or 2: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    return true;
                }
                case "2" -> {
                    return false;
                }
                default -> System.out.println("Pick a number 1 or 2!");
            }
        }
    }

    private void makeMove() {

        while (true) {

            System.out.print("Choose boxes 1 to 9: ");
            String option = scanner.nextLine();

            Pattern pattern = Pattern.compile("[1-9]");
            Matcher matcher = pattern.matcher(option);

            if (matcher.matches()) {

                int intOption = Integer.parseInt(option);

                if (boxesOccupied.contains(intOption)) {

                    System.out.println("Box is already occupied!");
                    continue;
                }

                boxesOccupied.add(intOption);
                updateBoard(intOption, isPlayerOne);

                if (checkForWin(intOption)) {

                    printBoard(false, true);
                    System.out.println(isPlayerOne ? "Player 1 has won!" : "Player 2 has won!");
                    stopGame = true;
                } else {
                    printBoard(true, true);
                }
                break;

            } else {

                System.out.println("Invalid option!");
            }
        }
    }

    private void updateBoard(int boxNumber, boolean isPlayerOne) {

        updateRows(getRow(boxNumber), boxNumber, isPlayerOne, false);
    }

    private void clearBoard(int boxNumber) {
        updateRows(getRow(boxNumber), boxNumber, isPlayerOne, true);
    }
    private void updateRows(int rowNumber, int boxNumber, boolean isPlayerOne, boolean clearBox) {

        boxNumber = (boxNumber % 3 == 0) ? 3 : boxNumber % 3;
        String element = (clearBox) ? "   " : (isPlayerOne) ? " O " : " X ";
        rows[rowNumber - 1][boxNumber - 1] =  element;
    }

    private int getColumn(int boxNumber) {
        return switch (boxNumber) {
            case 1, 4, 7 -> 1;
            case 2, 5, 8 -> 2;
            case 3, 6, 9 -> 3;
            default -> -1;
        };
    }

    private int getRow(int boxNumber) {
        return switch (boxNumber) {
            case 1, 2, 3 -> 1;
            case 4, 5, 6 -> 2;
            case 7, 8, 9 -> 3;
            default -> -1;
        };    }

    private int getDiagonal(int boxNumber) {
        return switch (boxNumber) {
            case 1, 9 -> 1;
            case 3, 7 -> 2;
            case 5 -> 3;
            default -> -1;
        };
    }

    private boolean checkForWin(int lastMove) {

        int rowNumber = getRow(lastMove);

        boolean rowResult = (rows[rowNumber - 1][0].equals(rows[rowNumber - 1][1])
                && rows[rowNumber - 1][0].equals(rows[rowNumber - 1][2]));

        int colNumber = getColumn(lastMove);
        boolean colResult = (rows[0][colNumber - 1].equals(rows[1][colNumber - 1])
                && rows[0][colNumber - 1].equals(rows[2][colNumber - 1]));

        int diagonalNumber = getDiagonal(lastMove);

        boolean diagonalResult = switch (diagonalNumber) {
            case 1 -> (rows[0][0].equals(rows[1][1])
                    && rows[0][0].equals(rows[2][2]));
            case 2 -> (rows[0][2].equals(rows[1][1])
                    && rows[0][2].equals(rows[2][0]));
            case 3 -> (rows[0][0].equals(rows[1][1]) && rows[0][0].equals(rows[2][2]))
                    || (rows[0][2].equals(rows[1][1]) && rows[0][2].equals(rows[2][0]));
            default -> false;
        };

        return rowResult || colResult || diagonalResult;
    }

    private boolean checkForDraw() {
        return boxesOccupied.size() == 9;
    }

    private void botMakesMove() {

        int moveToMake = botToWin();
        if (moveToMake != -1) {

            boxesOccupied.add(moveToMake);
            updateBoard(moveToMake, isPlayerOne);
            printBoard(false, true);

            System.out.println(isPlayerOne ? "Player 1 has won!" : "Player 2 has won!");
            stopGame = true;
            return;
        }

        moveToMake = botStopLose();

        if (moveToMake != -1) {

            boxesOccupied.add(moveToMake);
            updateBoard(moveToMake, isPlayerOne);
            printBoard(true, true);
            return;

        }

        moveToMake = randomizedMove();

        boxesOccupied.add(moveToMake);
        updateBoard(moveToMake, isPlayerOne);
        printBoard(true, true);
    }

    private int botToWin() {
        ArrayList<Integer> boxesEmpty = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (!boxesOccupied.contains(i)) {
                boxesEmpty.add(i);
            }
        }


        for (int boxNum : boxesEmpty) {

            updateBoard(boxNum, isPlayerOne);

            if (checkForWin(boxNum)) {
                clearBoard(boxNum);
                return boxNum;
            }
            clearBoard(boxNum);
        }

        return -1;
    }
    
    private int botStopLose() {
        ArrayList<Integer> boxesEmpty = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (!boxesOccupied.contains(i)) {
                boxesEmpty.add(i);
            }
        }

        for (int boxNum : boxesEmpty) {

            updateBoard(boxNum, !isPlayerOne);

            if (checkForWin(boxNum)) {

                clearBoard(boxNum);
                return boxNum;
            }

            clearBoard(boxNum);
        }
        return -1;
    }

    private int randomizedMove() {

        ArrayList<Integer> boxesEmpty = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (!boxesOccupied.contains(i)) {
                boxesEmpty.add(i);
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(0,boxesEmpty.size());
        return boxesEmpty.get(randomIndex);
    }

}

