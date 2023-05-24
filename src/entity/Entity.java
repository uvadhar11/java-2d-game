package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// entity class used for objects.
public class Entity {
    // create instance variables we can then use

    // the worldx and worldy is the x and y coords of this entity in the world coordinates
    public double worldX, worldY; // x, y coords for the position of this entity on the world

    public double speed; // speeds

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // images of obj used for animation
    public String direction; // direction of the obj
    public int spriteCounter = 0; // var to make sure obj not animating 60 times/sec - explained in plr class.
    public int spriteNum = 1; // current image number in animation since there are 2 images for each animation for the obj.

    // collision - set a certain area of entity to be solid so easy to go thru
    // narrow tile spaces.
    public Rectangle solidArea;
    public boolean collisionOn = false;
}
