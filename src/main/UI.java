package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

// new class for all the onscreen UI/user interfaces
public class UI {
    // make an instance variable of type game panel
    GamePanel gp;
    Font arial_40, arial_80B;
    BufferedImage keyImage; // image and instantiate in UI constructor NOT in game loop otherwise will keep drawing 60/s
    // variables for messages like when you pick up an obj, etc.
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0; // count down for message
    public boolean gameFinished = false;
    double playTime = 0.0;
    // decimal formatting class
    DecimalFormat dFormat = new DecimalFormat("0.00");

    // constructor
    public UI(GamePanel gp) {
        this.gp = gp;

        // instantiate the font, params: font name, font style, font size
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key(); // make a new key obj
        keyImage = key.image; // we use the key obj for the image
    }

    // show message method
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    // draw the UI method
    // not recommended to make the UI in the game loop since it takes time to make one
    // and we will be making it like 60 times a second
    // SO: we made an instance variable for the font
    public void draw(Graphics2D g2) {
        // check if game finished or on
        if (gameFinished) {
            // setting fonts
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            //             // find center coordinates to display text in screen center
            String text;
            int textLength;
            int x;
            int y;

            text = "You found the treasure!";
            // get the length of the text in world coords
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            // x coord adjusted based on text
            x = gp.screenWidth/2 - textLength/2;
            // move y coord of text a little lower than character so not covered
            y = gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text, x, y);

            // play time string
            text = "Your Time is: " + dFormat.format(playTime) + "!";
            // get the length of the text in pixels
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            // x coord adjusted based on text
            x = gp.screenWidth/2 - textLength/2;
            // move y coord of text a little lower than character so not covered
            y = gp.screenHeight/2 + (gp.tileSize*4);
            g2.drawString(text, x, y);

            // last text but lower than the first text so its not covered
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Congratulations!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text, x, y);

            // ending the game thread/game
            gp.gameThread = null;

        } else {
            // setting the font with instance variable
            g2.setFont(arial_40);
            // set the color of the graphics2D to white
            g2.setColor(Color.white);
            // draw key image
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            // string to print
            g2.drawString("x " + gp.player.keys, 74, 63);
            // draw string y coordinate represents the baseline of the text, not like the obj top like before
            // baseline is slightly higher than the bottom, so you need to adjust that accordingly

            // TIMER DRAWING
            playTime += (double)1/60;
            g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize*11, 65);

            // NOTIFICATION DRAWING
            if (messageOn) {
                // changes the font size of an already made font
                g2.setFont(g2.getFont().deriveFont(30));
                g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);
                // increase countdown for notification
                messageCounter++;

                // this message will display for 2 seconds since
                // 1 second is 60 frames
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
