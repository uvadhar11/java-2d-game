package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile - default size of player character, npcs, etc. Some games use 32px
    // modern computers have more pixels so 16 pixel characters looks tiny in mordern computers so we need to sclae it.
    final int scale = 3;
    // public so we can access in other classes
    public int tileSize = originalTileSize * scale; // calculate the actual tile size displayed on the screen
    public int maxScreenCol = 16; // 16 cols here
    public int maxScreenRow = 12; // 12 rows
    public int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels
    // MIGHT want to change it to scale depending on the size of the player's screen.

    // WORLD SETTINGS FOR LIKE CAMERA AND STUFF
    // max number of cols and rows for the world file
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    // width and height of the world based on row/col * size of a tile
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // multiplier for the zoom
    public double multiplier = 1;

    // FPS
    int FPS = 60;
    // make tile manager
    TileManager tileM = new TileManager(this); // passing in this gp object

    // NEED a thread
    KeyHandler keyH = new KeyHandler(this); // make a key handler
    Thread gameThread; // keeps a program running until you stop it - for things you want to repeat again and again
    // set player's default position

    // make a new collision checker object
    public CollisionChecker cChecker = new CollisionChecker(this);

    // create the asset setter object for object setting
    public AssetSetter aSetter = new AssetSetter(this);

    // make the player public so we can access it outside of this class.
    public Player player = new Player(this, keyH); // make player
    public SuperObject obj[] = new SuperObject[10]; // array for displaying up to 10 objects in the game at a time

    // METHODS AND CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // for better rendering purposes
        this.addKeyListener(keyH); // make a key listener
        this.setFocusable(true);
    }

    // game setup method
    public void setupGame() {
        // call this method before the game starts
        aSetter.setObject();
    }

    // zooming in and out function
    public void zoomInOut(int i) {
        // get the old world width
        int oldWorldWidth = tileSize * maxWorldCol;
        tileSize += i; // changing the tile size based on the zooming in or out param
        // we pass 1 for zoom in so +1 and -1 for zoom out so subtracts 1 from tile size

        // new world width variable
        int newWorldWidth = tileSize * maxWorldCol;

        // change the player's speed accordingly so doesn't move faster if he
        // zooms in or out
        player.speed = (double) newWorldWidth/600;

        // multiplier is the ratio with which to change the players world x and y
        multiplier = (double) newWorldWidth/oldWorldWidth;
        double newPlayerWorldX = player.worldX * multiplier;
        double newPlayerWorldY = player.worldX * multiplier;

        // change the players world x and y accordingly
        player.worldX = newPlayerWorldX;
        player.worldY = newPlayerWorldY;
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

        // draw tile
        tileM.draw(g2); // need to draw the tiles first then the player
        // otherwise the player will be underneath the tiles. LAYERS

        // draw objects
        // need to find what object we are going to draw
        // and looping thru the obj array with our individual objects
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                // if the index place isnt null (an obj exists)
                // draw the image calling draw in the super obj class
                obj[i].draw(g2, this);
            }
        }

        // draw player
        player.draw(g2); // draw player
        g2.dispose(); // memory clean up with this graphic object
    }

}
