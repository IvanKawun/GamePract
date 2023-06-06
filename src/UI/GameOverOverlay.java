package UI;

import entities.Player;
import gameStates.GameState;
import gameStates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing){
        this.playing = playing;
    }
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game over", Game.GAME_WIDTH/2, 150);
        g.drawString("Press esc to enter Main Menu!", Game.GAME_WIDTH,300);
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            GameState.state = GameState.MENU;
        }
    }

}
