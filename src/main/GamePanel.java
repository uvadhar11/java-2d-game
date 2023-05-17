package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile - default size of player character, npcs, etc. Some games use 32px
    // modern computers have more pixels so 16 pixel characters looks tiny in mordern computers so we need to sclae it.
    final int scale = 3;
    // public so we can access in other classes
    public final int tileSize = originalTileSize * scale; // calculate the actual tile size displayed on the screen
    public int maxScreenCol = 16; // 16 cols here
    public int maxScreenRow = 12; // 12 rows
    public int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels
    // MIGHT want to change it to scale depending on the size of the player's screen.

    // fps
    int FPS = 60;
    // make tile manager
    TileManager tileM = new TileManager(this); // passing in this gp object

    // NEED a thread
    KeyHandler keyH = new KeyHandler(); // make a key handler
    Thread gameThread; // keeps a program running until you stop it - for things you want to repeat again and again
    // set player's default position
    Player player = new Player(this, keyH); // make player
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // for better rendering purposes
        this.addKeyListener(keyH); // make a key listener
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this); // passing this class into the thread constructor to make a thread.
        gameThread.start(); // start the gameThread
    }

    // sleep method game loop
//    public void run() {
//        double drawInterval = 1000000000/FPS; // nanosecond/fps to get interval
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        // run method called when make a thread
//        // game loop comes in here
//        while (gameThread != null) {
//            // 1. update: update info such as character positions
//            update();
//            // 2. draw: draw the screen with updated info
//            repaint();
//
//            try {
//                // time left for the next drawing so doesn't draw inf times
//                // otherwise the object can go out of the windows fast.
//                // next draw time - the current time
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime/1000000; // convert to ms since sleep uses that
//                // just in case this happens. In case the update and repaint took more time than the time interval, it doesn't need to use more.
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long) remainingTime); // pause game loop until sleep time over.
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }

    // DIFFERENCE between sleep and delta accumulator
    // The difference between the 2 methods is the sleep
    // method sleeps the thread so you can’t run other
    // processes on the same thread, but with the delta
    // accumulator method, you can run other processes
    // on the same thread while still regulating the frame
    // rate. So using the delta.


    // another game loop called DeltaAccumulator method.
    public void run() {
        // draw interval: amount of time per frame in nanosec (most accurate).
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime(); // get time in nano-sec
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            // delta += time passed / interval size
            // this accumulates until it gets to 1 which is 1 frame.
            // currentTime - lastTime is the change in time from the
            // last iteration. then divide by drawInterval (time for
            // one frame)
            // this gives how much time has elapsed of one frame.
            // so we add the fractions together until its a whole number.
            delta += (currentTime - lastTime) / drawInterval;
//            System.out.println("Time change: " + (currentTime - lastTime));
//            System.out.println("draw int: " + drawInterval);
//            System.out.println("delta" + delta);
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            // allows delta to accumulate until its 1 or > meaning 1
            // frame has happened so then call update and repaint
            // methods and subtract delta by 1. So, it calls the
            // update and repaint methods once per frame.
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        // update player coordinates with player pos and player speed
        player.update();
    }

    public void paintComponent(Graphics g) {
        // built in function
        super.paintComponent(g); // calls parent paint component

        // draw rectangles
        Graphics2D g2 = (Graphics2D)g; // use graphics 2d class and type cast graphics object to grpahics 2d

        tileM.draw(g2); // need to draw the tiles first then the player
        // otherwise the player will be underneath the tiles. LAYERS
        player.draw(g2); // draw player
        g2.dispose(); // memory clean up with this graphic object
    }

}
