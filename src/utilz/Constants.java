package utilz;

import main.Game;

public class Constants {
    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT*Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT*Game.SCALE);
        }
    }
    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        /**
         * Константи для визначення дії гравця
         */
        public static final int RUNNING = 0;
        public static final int JUMP  = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int IDLE = 4;
        public static final int FALLING = 5;


        /**
         * Метод, що визначає скільки елементів в масиві анімацій для різних дій
         * @param player_action
         * @return
         */
        public static int GetSpriteAmount(int player_action){
            switch(player_action){
                case RUNNING:
                case ATTACK:
                case JUMP:
                    return 6;
                case IDLE:
                case FALLING:
                    return 1;
                case HIT:
                    return 4;
                default:
                    return 1;
            }
        }
    }
}
