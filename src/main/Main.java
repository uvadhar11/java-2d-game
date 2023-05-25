package main;

import main.GamePanel;

import javax.swing.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
//        // Press Alt+Enter with your caret at the highlighted text to see how
//        // IntelliJ IDEA suggests fixing it.
//        System.out.printf("Hello and welcome!");
//
//        // Press Shift+F10 or click the green arrow button in the gutter to run the code.
//        for (int i = 1; i <= 5; i++) {
//
//            // Press Shift+F9 to start debugging your code. We have set one breakpoint
//            // for you, but you can always add more by pressing Ctrl+F8.
//            System.out.println("i = " + i);
//        }

        // make a j frame for the game
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        // make the game panel class for the set-up stuff
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        // make the window visible and location not rel to
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // set the game up
        gamePanel.setupGame();

        // start the game thread
        gamePanel.startGameThread();
    }
}