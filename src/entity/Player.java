package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// player class is a child class of the entity class
public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    // screen x, y - where to draw player on the screen.
    // player's screen position (center of the screen) - so basically
    // the player is going to be in the center of the screen always and the world will move around him
    // so the screenx and y are the center of the screen where the player is.
    // the worldx and worldy is the x and y coords of the player in the world coordinates
    public final int screenX;
    public final int screenY;
    // number of keys the player has
    public int keys = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // have to subtract half a tile since the coordinates are the top left and we want center
        // so get the center tile and then subtract a tile so he is actually in the middle
        screenX = gp.screenWidth/2 - (gp.tileSize /2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // make a rectangle for the solid area of the player where collision is on
        solidArea = new Rectangle(0,0,48,48);

        // changing the coordinates of the rectangle for solid area.
        solidArea.x = 8;
        solidArea.y = 16;

        // default solid area values for x and y (For collision, etc)
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // setting the width and height of the player's solid area to be 32
        // so its not his entire width/height which is exactly one tile
        // so the player can easily move between one tile gaps
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues(); // set the defaults for player
        getPlayerImage(); // call the method to get the images
    }
    // set the default values of the player class.
    public void setDefaultValues() {
        // change player's location in world to center at init
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
//        speed = 4;
        // get the speed which is the world width divided by 600 (div by 600
        // since this ratio gets us 4
        speed = gp.worldWidth/600;
        direction = "down";
    }
    public void getPlayerImage() {
        try {
            // try to set the images equal to buffered image instance variables and this gets the image at this path.
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
        }
        // catch and print error
        catch(IOException e) {
            e.printStackTrace();
        }
    }


    // function to update the player's position (based on
    // key inputs).
    public void update() {
        // update player coordinates with player pos and player speed
        // using the instance variables like x, y, speed to
        // better update pos based on players.

        // this function is used to UPDATE variables like speed, direction, current image, etc.
        // THEN THE DRAW FUNCTION actually draws the images updated in the update state

        // all this code is in this if statement so player stops when no input
        // or the player stops when they aren't moving.
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            // pass in this object as an entity and its a player so true in the collision checker class
            // calling the checkObject method which returns the obj index its colliding with
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex); // call pick up obj method

            // if collision is false, player can move
            // moved the world position changing in here so u can only move
            // if you are not colliding
            if (collisionOn == false) {
                switch(direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            // update method called 60 times per second.
            // we only want the player image to change once
            // per 12 frames with the > 12 check so we can
            // actually see the animation and its not too fast.
            spriteCounter++; // increments every frame
            if (spriteCounter > 12) {
                // if statements to change the image number for the animation
                // since there are 2 images per movement for us.
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0; // set counter to 0 so we can know
                // when to animate next.
            }
        }
    }

    // method for picking up objects, accepts index for the obj
    public void pickUpObject(int i) {
        // index 999 means we didn't collide with any objects, so if we touched an object...
        if (i != 999) {
            // get the collided object's name
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "Key":
                    // if this object is a key, then
                    // play coin sound effect here
                    gp.playSE(1);
                    // increment keys count
                    keys++;
                    // set the current object to null, so it will disappear from the screen
                    gp.obj[i] = null;
                    // show the ui notification when u get a key
                    gp.ui.showMessage("You got a Key!");
                    break;
                case "Door":
                    // if the object is a door, then if keys count > 0
                    // set the current object to null (removed from screen)
                    // and decrement keys by 1, to simulate a key opening a door.
                    if (keys > 0) {
                        // play unlock sound effect
                        gp.playSE(3);
                        gp.obj[i] = null;
                        keys--;
                        gp.ui.showMessage("You opened the door!");
                    }
                    else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "Boots":
                    // play power up sound effect
                    gp.playSE(2);
                    // these boots give a speed power up
                    speed += 1;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed Boost Activated");
                    break;
                case "Chest":
                    // once you get the chest, you beat the game
                    gp.ui.gameFinished = true;
                    // stop the music
                    gp.stopMusic();
                    // play fanfare sound effect
                    gp.playSE(4);
                    break;
            }
        }
    }

    // moved draw function here which is used to draw the
    // player on the screen.
    public void draw(Graphics2D g2) {
        // for testing with rectangles.
//        g2.setColor(Color.white); // set color white
//        // fill rectangle. Params: x, y, l, w
//        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;

        // switch statement comparing the value of direction
        // with each of the strings so we can draw that specific image.
        // store the image with the direction in the image variable
        // that we can then use to draw the actual image.
        switch(direction) {
            case "up":
                // spriteNum is the number for the current number in the animation. so if its 1 (image 1 in the animation) then set that image.
                // if its 2 then set image to 2 for the respective direction
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right" :
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        // draw the image we get from the case statement.
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
