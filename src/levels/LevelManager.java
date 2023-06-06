package levels;

import gameStates.GameState;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_SIZE;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;
    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites(LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS));
        levels = new ArrayList<>();
        buildAllLevels();
    }
    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex == 0){
            importOutsideSprites(LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS));
        }
        else if(lvlIndex == 1){
            importOutsideSprites(LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS_2));
        }
        if(lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("No more levels! Game Completed");
            GameState.state = GameState.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
    }
    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for(BufferedImage img: allLevels)
            levels.add(new Level(img));
    }

    public  void importOutsideSprites(BufferedImage img) {
        levelSprite = new BufferedImage[48];
        for(int j = 0; j<4; j++){
            for(int i =0; i<12; i++){
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32,j*32,32,32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset){
        for( int j = 0; j<Game.TILES_IN_HEIGHT; j++){
            for(int i = 0; i<levels.get(lvlIndex).getLvlData()[0].length; i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],TILES_SIZE*i - lvlOffset,TILES_SIZE*j,TILES_SIZE,TILES_SIZE,null);
            }
        }
    }
    public void update(){

    }
    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }
    public int getAmountOfLevels(){
        return levels.size();
    }
    public int getLvlIndex(){return lvlIndex;}

}
