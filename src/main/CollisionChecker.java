package main;

import entity.Entity;

// handle collision checking
public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // check whether the player is hitting a solid tile
    public void checkTile(Entity entity) {
        // need to check the 4 coordinates for whether plr is
        // hitting the solid area

        // get the world limits
        int entityLeftWorldX = (int) entity.worldX + entity.solidArea.x;
        int entityRightWorldX = (int) entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = (int) entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = (int) entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // get the col and row of the collided tile
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // variables storing 2 tiles could be colliding with
        int tileNum1, tileNum2;

        // switch statement
        switch(entity.direction) {
            case "up":
                // predicting where the player will be after movement by using the speed
                // since the player's direction is going up, the y coords decrease as go up
                // so subtract by speed to predict location had we moved the player
                // this is the row number of the tile colliding with
                entityTopRow = (int) (entityTopWorldY - entity.speed) / gp.tileSize;

                // there are 2 possible tiles we are colliding with
                // accesses the tile manager's map tile num 2d array and uses the
                // left col and right col where the player could be colliding (btwn 2 tiles)
                // and then checks the top row of the entity since this is the up condition
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                // check if the tiles are solid or not (either or both)
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    // set variable for entity colliding to be true
                    entity.collisionOn = true;
                }
                break;
            case "down":
                // plus here since y coord increases as u go down
                entityBottomRow = (int) (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (int) (entityLeftWorldX - entity.speed) / gp.tileSize;
                // for left check the top and bottom as well
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (int) (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
    // check if plr hitting any object, if so, return index of the object
    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        // loop through the object array
        for (int i = 0; i < gp.obj.length; i++) {
            // check if the obj is null
            if (gp.obj[i] != null) {
                // gets the solid area's current position
                // so we can then check for collision

                // Get entity's solid area position
                // this is the world x/y + solid area x/y which gets the x/y where
                // the object will collide with something
                entity.solidArea.x = (int) entity.worldX + entity.solidArea.x;
                entity.solidArea.y = (int) entity.worldY + entity.solidArea.y;

                // Get the object's solid area position
                // this is the same as with the entity object
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                // switch statement for checking direction
                // simulating entity movements and check where itll be after it moves
                switch (entity.direction) {
                    case "up":
                        // going up so subtract solid area by entity speed so
                        // we can check where the solid area positions will be
                        // (right before they move with the keys for movement) are pressed
                        // and we have to check before the movement so we can make sure if they collide
                        // they aren't moving into that object or to do certain tasks like these obj interactions
                        entity.solidArea.y -= entity.speed;

                        // checks if the 2 areas with the solid area rectangles
                        // (rects for collision) intersect, meaning they collided...
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            // check if the object is solid or not, if so set collisionOn to true
                            // to say its colliding with something actively
                            if (gp.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            // if the player is intersecting with this object, then set the index to be returned to the index
                            // of this object because this object is colliding with the player
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision == true) {
                                entity.collisionOn = true;
                            }
                            if (player == true) {
                                index = i;
                            }
                        }
                        break;
                }

                // after this switch statement we need to reset the values of the solid area
                // otherwise it will keep incrementing when we add world x and world y

                // set entity solidArea x and y to the default
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;

                // set the object solid areas for x and y to the default
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }
}
