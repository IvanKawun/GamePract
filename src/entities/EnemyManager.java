package entities;

import gameStates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] skeletonArr;
    private BufferedImage princess;
    private ArrayList<Skeleton> skeletons = new ArrayList<>();
    private ArrayList<Princess> princesses = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        loadPrincessImgs();
    }

    public void loadEnemies(Level level) {
        skeletons = level.getSkeletons();
        princesses = level.getPrincesses();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (Skeleton c : skeletons)
            if (c.isActive()) {
                c.update(lvlData, player);
                isAnyActive= true;
            }
        if(!isAnyActive)
            if(playing.getLevelManager().getLvlIndex() == 0 ||playing.getLevelManager().getLvlIndex() == 1 ) {
                playing.setLevelCompleted(true);
            }
            else if(playing.getLevelManager().getLvlIndex() == 2){
                if(FINISHED) playing.setLevelCompleted(true);
            }
    }
    public boolean finished;
    public void draw(Graphics g, int xLvlOffset) {
        drawSkeletons(g, xLvlOffset);
        drawPrincess(g,xLvlOffset);
    }

    private void drawSkeletons(Graphics g, int xLvlOffset) {
        for (Skeleton c : skeletons) {
            if (c.isActive()) {

                g.drawImage(skeletonArr[c.getState()][c.getAniIndex()],
                        (int) c.getHitbox().x - xLvlOffset - SKELETON_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitbox().y - SKELETON_DRAWOFFSET_Y,
                        SKELETON_WIDTH * c.flipW(), SKELETON_HEIGHT, null);
                //c.drawHitbox(g,xLvlOffset);
                //c.drawAttackBox(g, xLvlOffset);
            }
        }
    }
    private void drawPrincess(Graphics g, int xLvlOffset) {
        for (Princess c : princesses) {
            if (c.isActive()) {

                g.drawImage( princess,
                        (int) c.getHitbox().x - xLvlOffset - 60,
                        (int) c.getHitbox().y - SKELETON_DRAWOFFSET_Y+25,
                        SKELETON_WIDTH, SKELETON_HEIGHT, null);
                //c.drawHitbox(g,xLvlOffset);
                //c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Skeleton c : skeletons) {
            if (c.isActive())
                if (attackBox.intersects(c.getHitbox())) {
                    c.hurt(10);
                    return;
                }
        }

    }
    public boolean checkPrincessTouched(Rectangle2D.Float hitbox){
        for (Princess c : princesses) {
                if (hitbox.intersects(c.getHitbox())) {
                    return true;
                }
        }
        return false;
    }


    public void loadEnemyImgs() {
        skeletonArr = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKELETON_SPRITES);
        for (int j = 0; j < skeletonArr.length; j++) {
            for (int i = 0; i < skeletonArr[j].length; i++) {
                skeletonArr[j][i] = temp.getSubimage(i * SKELETON_WIDTH_DEFAULT, j * SKELETON_HEIGHT_DEFAULT,
                        SKELETON_WIDTH_DEFAULT, SKELETON_HEIGHT_DEFAULT);
            }
        }
    }
    public void loadPrincessImgs(){
        princess = LoadSave.GetSpriteAtlas(LoadSave.PRINCESS_SPRITE);
    }

    public void resetAllEnemies() {
        for (Skeleton c : skeletons)
            c.resetEnemy();
    }
}
