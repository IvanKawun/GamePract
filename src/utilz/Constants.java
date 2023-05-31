package utilz;

public class Constants {
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
                    return 1;
                case HIT:
                    return 4;
                default:
                    return 1;
            }
        }
    }
}
