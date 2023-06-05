package entities;

import gameStates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[] []skeletonArr;
    private ArrayList<Skeleton> skeletons = new ArrayList<>();

    public EnemyManager(Playing playing){
    this.playing = playing;
    loadEnemyImgs();
    addEnemies();
    }

    private void addEnemies() {
        skeletons = LoadSave.GetSkeletons();
        System.out.println("Size of skeletons =" + skeletons.size());
    }

    public void update(){
        for(Skeleton c:skeletons)
            c.update();
    }
    public void draw(Graphics g, int xLvlOffset){
        drawSkeletons(g, xLvlOffset);
    }

    private void drawSkeletons(Graphics g, int xLvlOffest) {
        for(Skeleton c: skeletons){
            g.drawImage(skeletonArr[c.getEnemyState()][c.getAniIndex()],(int)c.getHitbox().x- xLvlOffest,
                    (int)c.getHitbox().y,
                    SKELETON_WIDTH,SKELETON_HEIGHT,null);
        }
    }

    public void loadEnemyImgs(){
        skeletonArr = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKELETON_SPRITES);
        for(int j = 0; j<skeletonArr.length; j++){
            for(int i =0; i<skeletonArr[j].length; i++){
                skeletonArr[j][i] = temp.getSubimage(i*SKELETON_WIDTH_DEFAULT, j*SKELETON_HEIGHT_DEFAULT,
                        SKELETON_WIDTH_DEFAULT,SKELETON_HEIGHT_DEFAULT);
            }
        }
    }
}
