public abstract class Request {
    private int floor;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}

enum Direction {
    UP,
    DOWN;
}

class InternalRequest extends Request {
    public InternalRequest(int floor){
        super.setFloor(floor);
    };
}

class ExternalRequest extends Request {

    private Direction direction;

    public ExternalRequest(int floor, Direction direction){
        super.setFloor(floor);
        this.direction = direction;
    };

    public Direction getDirection() {
        return direction;
    }
}
