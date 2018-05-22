public class Move {
    private Directions _dir;
    private GameBlock _blk;

    public Move(Directions dir, GameBlock blk) {
        _dir = dir;
        _blk = blk;
    }

    public GameBlock getBlock() {
        return _blk;
    }

    //Returns the opposite direction from this move
    public Directions getOpossiteDir() {
        if (_dir == Directions.UP) {
            return Directions.DOWN;
        }
        if (_dir == Directions.DOWN) {
            return Directions.UP;
        }
        if (_dir == Directions.LEFT) {
            return Directions.RIGHT;
        }
        return Directions.LEFT;
    }
}
