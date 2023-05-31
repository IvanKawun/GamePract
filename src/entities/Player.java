package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;


public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump ;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 19 * Game.SCALE;
    private float yDrawOffset = 8 * Game.SCALE;

    /**
     *  Змінні для стрибків та гравітації
     */
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private  float jumpSpeed  = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;


    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x,y,22*Game.SCALE,39*Game.SCALE);

    }

    public void update() {

        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][aniIndex],  (int)(hitbox.x - xDrawOffset),
                (int) (hitbox.y - yDrawOffset), width, (int)(height-(14*Game.SCALE)), null);
        // drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;

        if(inAir){
            if(airSpeed<0)
                playerAction = JUMP;
            else
                playerAction = FALLING;

        }


        if (attacking)
            playerAction = ATTACK;
        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;
        if(jump)
            jump();
        if (!left && !right && !inAir)
            return;

        float xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;

        if (right)
            xSpeed += playerSpeed;

        if(!inAir){
            if(!IsEntityOnFloor(hitbox,lvlData))
                inAir = true;
        }

        if(inAir){

            if(CanMoveHere(hitbox.x, hitbox.y +airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed+= gravity;
                updateXPos(xSpeed);
            } else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                 if(airSpeed > 0)
                     resetInAir();
                 else
                     airSpeed = fallSpeedAfterCollision;
                 updateXPos(xSpeed);
            }
        }else
            updateXPos(xSpeed);
        moving = true;


    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        }else{
            hitbox.x = GetEntityXPosNextToWall(hitbox,xSpeed);
        }

    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[6][7];
        animations[4][0] = img.getSubimage(0, 0, 50, 37);
        animations[5][0] = img.getSubimage(50,111,50,37);
        /**
         * Filling the running animation
         */
        for (int i = 0; i < 6; i++) {
            animations[0][i] = img.getSubimage((i + 1) * 50, 37, 50, 37);
        }
        /**
         * Filling jumping animation
         */
        for (int i = 0; i < 4; i++) {
            animations[1][i] = img.getSubimage(50 * i, 74, 50, 37);
        }
        animations[1][4] = img.getSubimage(50, 111, 50, 37);
        animations[1][5] = img.getSubimage(250, 0, 50, 37);
        /**
         * Filling attacking animation
         */
        animations[2][0] = img.getSubimage(250, 222, 50, 37);
        animations[2][1] = img.getSubimage(300, 222, 50, 37);
        for (int i = 0; i < 4; i++) {
            animations[2][i + 2] = img.getSubimage(i * 50, 259, 50, 37);
        }
        /**
         * Filling dying animation
         */
        for (int i = 0; i < 4; i++) {
            animations[3][i] = img.getSubimage(50 * (i + 1), 333, 50, 37);
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!IsEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
