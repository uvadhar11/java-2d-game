package object;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// parent class of the objects
public class SuperObject {
    public BufferedImage image; // image of the object
    public String name;
    public boolean collision = false;
    public int worldX, worldY; // obj pos in x and y

    // draw method for drawing the object on the screen
    public void draw(Graphics2D g2, GamePanel gp) {
        // get screen x and y coordinates using player coords
        double screenX = worldX - gp.player.worldX + gp.player.screenX;
        double screenY = worldY - gp.player.worldY + gp.player.screenY;

        // to make sure u are only rendering objs u see on the screen to not slow the
        // game down, add this if. These checks get the boundary between the player to the ends of the screens on each side
        // we added a tile to each side since were seeing the black background by adding/subtracting a tile size from the world vars
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            // draw the image
            g2.drawImage(image, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);

        }
    }
}
