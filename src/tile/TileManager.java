package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp; // game panel object
    Tile[] tile; // array of tiles
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10]; // 10 types of tiles
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow]; // stores the map tile data
        getTileImage();// get the tile images (populate them in the tile array)
        loadMap("/maps/map01.txt"); // call the method to load the map. argument to change map
    }
    // loading the images in this function
    public void getTileImage() {
        try {
            // load each tile in the array by amking a new tile and then reading the file
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
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
            while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
                // read the text file in the while loop
                String line = br.readLine(); // reads the single line of the text file and stores it in this string
                while(col < gp.maxScreenCol) {
                    // stores each number as a string by spaces
                    String numbers[] = line.split(" ");
                    // change string to a number
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num; // store this num in the 2d array for the tiles
                    col++;
                }
                // if we get the max screen column then col = 0 and go to the next row
                if (col == gp.maxScreenCol) {
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
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            // get the current tile's number
            int tileNum = mapTileNum[col][row];
            // draw the image
            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            // increase col by 1 to go to the next column and increment x by the size of a tile to draw the next one
            col++;
            x += gp.tileSize;

            // if the col is the max col number then
            // set col, x to 0 and go to next row and increment
            // y by the tileSize to go to the next tile
            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
