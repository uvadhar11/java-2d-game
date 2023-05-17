package entity;

import java.awt.image.BufferedImage;

// entity class used for objects.
public class Entity {
    // create instance variables we can then use
    public int x, y; // x, y coords
    public int speed; // speed

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // images of obj used for animation
    public String direction; // direction of the obj
    public int spriteCounter = 0; // var to make sure obj not animating 60 times/sec - explained in plr class.
    public int spriteNum = 1; // current image number in animation since there are 2 images for each animation for the obj.
}