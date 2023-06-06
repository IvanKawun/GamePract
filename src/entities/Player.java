package entities;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;


public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump ;
    private float playerSpeed = 1.5f * Game.SCALE;
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

    /**
     * Статус бар UI
     */
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(143*Game.SCALE);
    private int statusBarHeight = (int)(52*Game.SCALE);
    private int statusBarX = 0;
    private int statusBarY = 0;

    private int healthBarWidth =(int)(102*Game.SCALE);
    private int healthBarHeight =(int)(19*Game.SCALE);
    private int healthBarX =(int)(41*Game.SCALE);
    private int healthBarY =(int)(11*Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth =maxHealth;
    private int healthWidth = healthBarWidth;

    /**
     * Хітбокс атаки
     */
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;
    private Playing playing;

    private boolean attackChecked = false;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitbox(x,y,(int)(22*Game.SCALE),(int)(39*Game.SCALE));
        initAttackBox();

    }

    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(20*Game.SCALE), (int )(20*Game.SCALE));

    }

    public void update() {
        updateHealthBar();

        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateHealthBar();
        updateAttackBox();

        updatePos();
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x+ hitbox.width+ (int)(Game.SCALE*10);
        }
        else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE*10);
        }
        attackBox.y = hitbox.y + (Game.SCALE*10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth /(float)maxHealth)*healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],  (int)(hitbox.x - xDrawOffset) - lvlOffset+ flipX,
                (int) (hitbox.y - yDrawOffset), width*flipW, (int)(height-(14*Game.SCALE)), null);
        //drawHitbox(g,lvlOffset);
        drawAttackBox(g,lvlOffset);
        drawUI(g);

    }

    private void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - lvlOffsetX,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        g.setColor(Color.red);
        g.fillRect(healthBarX,(int)(healthBarY+(1*Game.SCALE)),(int)(healthWidth-2*Game.SCALE),healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
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


        if (attacking) {
            playerAction = ATTACK;
            if(startAni != ATTACK){
                aniTick = 0;
                aniIndex = 1;
                return;
            }
        }
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
        if(!inAir)
            if((!left&&!right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
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
    public void changeHealth(int value){
        currentHealth += value;

        if(currentHealth <=0){
            currentHealth = 0;
            //gameOver();
        }else if (currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][7];
        /**
         * Idle animation
         */
        animations[4][0] = img.getSubimage(0, 0, 50, 37);
        /**
         * Falling animation
         */
        animations[5][0] = img.getSubimage(50,111,50,37);
        /**
         * Getting hit animation
         */
        for(int i = 0; i<4; i++){
            animations[6][i] = img.getSubimage(150+(50*i), 296,50,37);
        }
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
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
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
    public void resetAll(){
        resetDirBooleans();
        inAir = false;
        moving = false;
        attacking = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox,lvlData))
            inAir = true;
    }
}
