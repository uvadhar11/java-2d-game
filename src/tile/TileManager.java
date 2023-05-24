package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// tile management for tiles
public class TileManager {
    GamePanel gp; // game panel object
    public Tile[] tile; // array of tile types
    public int mapTileNum[][]; // 2d array of tiles

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10]; // 10 types of tiles
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; // stores the map tile data
        getTileImage();// get the tile images (populate them in the tile array)
        loadMap("/maps/world01.txt"); // call the method to load the map. argument to change map
    }
    // loading the images in this function
    public void getTileImage() {
        try {
            // load each tile in the array by making a new tile and then reading the file
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            // want the wall to be solid so set collision to true so cant move on it
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath) {
        // argument filePath to change map u pass in
        try {
            // used to import the text files
            InputStream is = getClass().getResourceAsStream(filePath);
            // read the text file
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            // using maxWorldCol and Row so the boundaries of the map aren't the screen size but the map size (the max)
            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                // read the text file in the while loop
                String line = br.readLine(); // reads the single line of the text file and stores it in this string
                while(col < gp.maxWorldCol) {
                    // stores each number as a string by spaces
                    String numbers[] = line.split(" ");
                    // change string to a number
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num; // store this num in the 2d array for the tiles
                    col++;
                }
                // if we get the max screen column then col = 0 and go to the next row
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close(); // end the file reading
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    // draw the tiles
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            // get the current tile's number
            int tileNum = mapTileNum[worldCol][worldRow];

            // In other words, tiles' screenX and screenY change depending on the player's current position.
            // On the other hand, tiles' worldX and worldY don't change since tiles position on the map are fixed.

            // get the tile's worldx and the coord is the col number times the size of one tiles on the screen
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // where to draw
            // worldx - pos on map, screenx is where on screen we draw it
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;

            // to make sure u are only rendering tiles u see on the screen to not slow the
            // game down, add this if. These checks get the boundary between the player to the ends of the screens on each side
            // we added a tile to each side since were seeing the black background by adding/subtracting a tile size from the world vars
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                // if statements for restricting values past the screen
                // if the current player's world position is 0 or less than 0 (past top of screen), then reset the value
                // back to the position where you can't see the black tiles anymore.
//                if (gp.player.worldY <= 0 + gp.maxScreenCol/2 * gp.tileSize + (gp.tileSize / 2)) {
//                    gp.player.worldY = 0 + gp.maxScreenCol/2 * gp.tileSize + (gp.tileSize / 2);
//                    // change player to not be in the center of the screen
//
//                }
//                else {
//                    // change the player to be in the middle
//                    screenX = gp.screenWidth/2 - (gp.tileSize /2);
//                    screenY = gp.screenHeight/2 - (gp.tileSize/2);
//                }

                // NEW SOLUTION:
                // limit the position of the player's movement
                // if the player is at the top of the map then set the pos of the player back to 0 (the y pos)
                if (gp.player.worldY <= 0) gp.player.worldY = 0;
                // subtract a tile size because we added a tile to each side to get rid of the black background when adding tiles for rendering
                if (gp.player.worldY >= (gp.worldHeight - gp.tileSize)*gp.multiplier) gp.player.worldY = (gp.worldHeight - gp.tileSize)*gp.multiplier;
                if (gp.player.worldX <= 0) gp.player.worldX = 0;
                if (gp.player.worldX >= (gp.worldWidth - gp.tileSize)*gp.multiplier) gp.player.worldX = (gp.worldWidth - gp.tileSize)*gp.multiplier;



                // print statements for understanding
                System.out.println("Current player: " + gp.player.worldX + ", " + gp.player.worldY);
                // System.out.println("Current screen: " + screenX + ", " + screenY);
                    // draw the image
                    g2.drawImage(tile[tileNum].image, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);

            }
            // increase col by 1 to go to the next column and increment x by the size of a tile to draw the next one
            worldCol++;

            // if the col is the max col number of the world then
            // set col, x to 0 and go to next row and increment
            // y by the tileSize to go to the next tile
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        // draw background scenery
//        while (rowCol >= gp.maxRowCol
    }
}
