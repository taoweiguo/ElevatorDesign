import com.sun.source.tree.Tree;

import java.util.TreeSet;

class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(int floor) {
    }
}

public class Elevator implements Runnable{
    private int id;
    private Integer currFloor;
    private ElevatorState elevatorState;
    private State state;
    private TreeSet<Integer> upStops;
    private TreeSet<Integer> downStops;
    private TreeSet<Integer> currentStops;
    private boolean isRunning;

    public Elevator(int id, int currFloor) {
        this.id = id;
        this.currFloor = currFloor;
        this.state = new IdleState();
        elevatorState = ElevatorState.IDLE;
        upStops = new TreeSet<>();
        downStops = new TreeSet<>();
        currentStops = new TreeSet<>();
        isRunning = false;
    }

    public void handleExternalRequest(ExternalRequest request) {
        if (Direction.UP.equals(request.getDirection())) {
            handleUpRequest(request);
        }
        else {
            handleDownRequest(request);
        }

    }

    public void handleUpRequest(ExternalRequest request) {
        synchronized (upStops) {
            upStops.add(request.getFloor());
            System.out.println("----- Request to go to floor " + request.getFloor() + " added -----");
            upStops.notifyAll();
        }
    }

    public void handleDownRequest(ExternalRequest request) {
        synchronized (downStops) {
            downStops.add(request.getFloor());
            System.out.println("----- Request to go to floor " + request.getFloor() + " added -----");
            downStops.notifyAll();

        }
    }

    public void handleInternalRequest(InternalRequest request) {

        if (request.getFloor() == -1000) {
            //openGate();
            return;
        }
        if (request.getFloor() == -2000) {
            //closeGate();
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

    public void move() {
        while (this.isRunning) {
            this.state.move(this);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Elevator operation interrupted.");
            }
//            System.out.println("Elevator state is " + this.state);
        }
    }

    public void movingUp() {
        if (upStops.ceiling(currFloor) != null) {
            synchronized (upStops) {
                while (upStops.isEmpty()) {
                    try {
                        upStops.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                Integer targetFloor = upStops.ceiling(currFloor);
                if (targetFloor == null) {
                    targetFloor = upStops.last();
                }

                if (targetFloor.equals(currFloor)) {
                    upStops.remove(targetFloor);
                    System.out.println("----- Elevator " + id + "arrived at floor " + targetFloor + ", Opening Gate -----");
                    return;
                }

                getIntermediateStops(currFloor, targetFloor); // populate currStops

                Integer nextFloor = currentStops.ceiling(currFloor); // moving to the next

                if (nextFloor == null) {
                    nextFloor = currentStops.last();
                }

                System.out.println("Elevator " + id + " starting moving from " + currFloor + " to floor " + nextFloor);
                currentStops.remove(nextFloor);
                currFloor = nextFloor;

                upStops.notifyAll();
            }
        }
        else {
            synchronized (downStops) {
                Integer targetFloor = downStops.last();
                if (targetFloor == null) {
                    targetFloor = downStops.first();
                }

                if (targetFloor.equals(currFloor)) {
                    downStops.remove(targetFloor);
                    System.out.println("----- Elevator " + id + "arrived at floor " + targetFloor + ", Opening Gate -----");
                    return;
                }

                getIntermediateStops(currFloor, targetFloor);
                Integer nextFloor = currentStops.floor(currFloor);

                if (nextFloor == null) {
                    nextFloor = currentStops.first();
                }

                System.out.println("Elevator " + id + "starting moving from " + currFloor + " to floor " + nextFloor);
                currentStops.remove(nextFloor);
                currFloor = nextFloor;

                downStops.notifyAll();
            }
        }
    }

    public void movingDown() {
        if (downStops.floor(currFloor) != null) {
            synchronized (downStops) {
                while (downStops.isEmpty()) {
                    try {
                        downStops.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                Integer targetFloor = downStops.floor(currFloor);
                if (targetFloor == null) {
                    targetFloor = downStops.first();
                }

                if (targetFloor.equals(currFloor)) {
                    downStops.remove(targetFloor);
                    System.out.println("----- Elevator " + id + "arrived at floor " + targetFloor + ", Opening Gate -----");
                    return;
                }

                getIntermediateStops(currFloor, targetFloor);

                Integer nextFloor = currentStops.floor(currFloor);

                if (nextFloor == null) {
                    nextFloor = currentStops.first();
                }

                System.out.println("Elevator " + id + "starting moving from " + currFloor + " to floor " + nextFloor);
                currentStops.remove(nextFloor);
                currFloor = nextFloor;

                downStops.notifyAll();
            }
        }
        else {
            synchronized (upStops) {
                Integer targetFloor = upStops.first();
                if (targetFloor == null) {
                    targetFloor = upStops.last();
                }

                if (targetFloor.equals(currFloor)) {
                    upStops.remove(targetFloor);
                    System.out.println("----- Elevator " + id + "arrived at floor " + targetFloor + ", Opening Gate -----");
                    return;
                }

                getIntermediateStops(currFloor, targetFloor);
                Integer nextFloor = currentStops.ceiling(currFloor);

                if (nextFloor == null) {
                    nextFloor = currentStops.last();
                }

                System.out.println("Elevator " + id + "starting moving from " + currFloor + " to floor " + nextFloor);
                currentStops.remove(nextFloor);
                currFloor = nextFloor;

                upStops.notifyAll();
            }
        }
    }

    @Override
    public void run() {
        isRunning = true;
        move();
    }

    public void getIntermediateStops(int currFloor, int nextFloor) {
        if (currFloor == nextFloor)
            return;

        int start = currFloor;
        int next = nextFloor;

        if (currFloor > nextFloor) {
            start = nextFloor;
            next = currFloor;
            for (int i = start; i < next; i++) {
                currentStops.add(i);
            }
            return;
        }

        for (int i = start + 1; i <= next; i++) {
            currentStops.add(i);
        }
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

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
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

    public TreeSet<Integer> getCurrentStops() {
        return currentStops;
    }

    public void setCurrentStops(TreeSet<Integer> currentStops) {
        this.currentStops = currentStops;
    }
}
