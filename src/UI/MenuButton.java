package UI;

import gameStates.GameState;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuButton {
    private int xOffsetCenter = B_WIDTH/2;
    private int XPos, YPos, rowIndex, index;
    private GameState state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    public MenuButton(int XPos, int YPos, int rowIndex, GameState state){
        this.XPos=XPos;
        this.YPos=YPos;
        this.rowIndex=rowIndex;
        this.state=state;
        loadImgs();
        initBounds();
    }
    private void initBounds(){
        bounds = new Rectangle(XPos-xOffsetCenter, YPos, B_WIDTH, B_HEIGHT);
    }
    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }
    public void draw(Graphics g){
        g.drawImage(imgs[index], XPos-xOffsetCenter, YPos, B_WIDTH, B_HEIGHT, null);
    }
    public void update(){
        index = 0;
        if(mouseOver)index = 1;
        if(mousePressed)index = 2;
    }
    public boolean isMouseOver() {
        return mouseOver;
    }
    public boolean isMousePressed() {
        return mousePressed;
    }
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    public Rectangle getBounds(){
        return bounds;
    }
    public void applyGamestate(){
        GameState.state = state;
    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
}

