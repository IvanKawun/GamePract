package levels;

import entities.Skeleton;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private ArrayList<Skeleton> skeletons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private int maxLvlOffsetX;
    private int[][] lvlData;
    private Point playerSpawn;
    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        calcLvlOffset();
        calcPlayerSpawn();
    }

    private void createSpikes(){
        spikes = HelpMethods.GetSpikes(img);
    }
    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }
    private void calcPlayerSpawn() {
        playerSpawn = GetPLayerSpawn(img);
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        skeletons = GetSkeletons(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int [][] getLvlData(){
        return lvlData;
    }
    public int getLvlOffset(){
        return  maxLvlOffsetX;
    }
    public ArrayList<Skeleton> getSkeletons(){
        return skeletons;
    }
    public Point getPlayerSpawn(){
        return playerSpawn;
    }
    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }
    public ArrayList<Spike> getSpikes(){
        return spikes;
    }}
