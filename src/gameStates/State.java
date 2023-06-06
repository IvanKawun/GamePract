package gameStates;
import UI.MenuButton;
import audio.AudioPlayer;
import main.Game;
import java.awt.event.MouseEvent;
import main.Game;
public class State {
    protected Game game;
    public State(Game game){
        this.game = game;
    }
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    public Game getGame(){
        return game;
    }
    public void setGameState(GameState state){
        switch(state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }
        GameState.state = state;
    }
}