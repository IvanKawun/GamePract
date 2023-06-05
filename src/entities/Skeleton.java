package entities;
import main.Game;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Skeleton extends Enemy{
    public Skeleton(float x, float y) {
        super(x, y, SKELETON_WIDTH, SKELETON_HEIGHT, SKELETON);
        initHitbox(x,y,(int)(28* Game.SCALE),(int)(52*Game.SCALE));
    }
    public void update(int [][] lvlData,Player player){
        updateMove(lvlData,player);
        updateAnimationTick();
    }
    private void updateMove(int [][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);
        if (inAir)
            updateInAir(lvlData);
         else {
            switch (enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData,player))
                        turnTowardsPlayer(player);
                    if(isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
            }
        }
    }
}
