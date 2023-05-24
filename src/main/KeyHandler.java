package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    // boolean vars
    public boolean upPressed, downPressed, leftPressed, rightPressed; // vars for key press state
    GamePanel gp;
    // key handler function accepting a game panel
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    public void keyTyped(KeyEvent e) {

    }
    // handles key pressed setting booleans to true when keys are pressed down
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        // up key for zooming
        if (code == KeyEvent.VK_UP) {
            // pass in 1 for zooming in and -1 is zoom out
            gp.zoomInOut(1);
        }
        // down key for zooming
        if (code == KeyEvent.VK_DOWN) {
            gp.zoomInOut(-1);
        }
    }
    // handle key released and set vars to false when the key is released
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode(); // get current input
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
