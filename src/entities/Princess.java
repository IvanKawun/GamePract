package entities;

import static utilz.Constants.EnemyConstants.*;

public class Princess extends Enemy{
    public Princess(float x, float y){
            super(x, y, 50, 50, PRINCESS);
            initHitbox(28,52);
    }
}
