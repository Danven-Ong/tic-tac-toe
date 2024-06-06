import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    <<<<Tic Tac Toe game>>>>
                    1. Single Player Game
                    2. Two Player Game
                    3. Quit""");

            String option = scanner.nextLine();

            Pattern pattern = Pattern.compile("[1-3]");
            Matcher matcher = pattern.matcher(option);

            if (matcher.matches()) {

                int intOption = Integer.parseInt(option);
                if (intOption == 3) return;

                TicTacToe game = new TicTacToe(intOption);

                if (game.isStopGame()) return;

            } else {
                System.out.println("Invalid option!");
            }

        }

    }
}
