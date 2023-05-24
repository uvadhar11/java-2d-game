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
}
