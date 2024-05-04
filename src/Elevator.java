import java.util.TreeSet;

enum ElevatorState {
    UP,
    DOWN,
    IDLE,
}

class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(int floor) {
    }
}

public class Elevator {
    private int currFloor;
    private ElevatorState elevatorState;
    private State state;
    private TreeSet<Integer> upStops;
    private TreeSet<Integer> downStops;

    public Elevator(int currFloor) {
        this.currFloor = currFloor;
        this.state = new IdleState();
        elevatorState = ElevatorState.IDLE;
        upStops = new TreeSet<>();
        downStops = new TreeSet<>();
    }

    public void handleExternalRequest(ExternalRequest request) {
        if (Direction.UP.equals(request.getDirection())) {
            upStops.add(request.getFloor());
        }
        if (Direction.DOWN.equals(request.getDirection())){
            downStops.add(request.getFloor());
        }

        if (ElevatorState.IDLE.equals(elevatorState)) {
            move();
        }
    }

    public void handleInternalRequest(InternalRequest request) {

        if (request.getFloor() == -1000) {
            openGate();
            return;
        }
        if (request.getFloor() == -2000) {
            closeGate();
            return;
        }

        if (currFloor < request.getFloor()) {
            upStops.add(request.getFloor());
            move();
        }
        else if (currFloor > request.getFloor()) {
            downStops.add(request.getFloor());
            move();
        }
        else {
            throw new InvalidRequestException(request.getFloor());
        }
    }

    public void openGate() {
        closeGate();
    }

    public void closeGate() {
        move();
    }

    public void move() {
        this.state.move(this);
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public void setCurrFloor(int currFloor) {
        this.currFloor = currFloor;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public TreeSet<Integer> getUpStops() {
        return upStops;
    }

    public void setUpStops(TreeSet<Integer> upStops) {
        this.upStops = upStops;
    }

    public TreeSet<Integer> getDownStops() {
        return downStops;
    }

    public void setDownStops(TreeSet<Integer> downStops) {
        this.downStops = downStops;
    }
}
