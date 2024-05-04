public interface State {
    void move(Elevator elevator);
}

class UpState implements State {

    @Override
    public void move(Elevator elevator) {
        Integer nextLevel = null;
        if (!elevator.getUpStops().isEmpty()) {
            nextLevel = elevator.getUpStops().ceiling(elevator.getCurrFloor());
            System.out.println("Moving to level: " + nextLevel);
            elevator.getUpStops().remove(nextLevel);
        }
        else if (!elevator.getDownStops().isEmpty()) {
            nextLevel = elevator.getDownStops().first();
            System.out.println("Moving to level: " + nextLevel);
            elevator.getDownStops().remove(nextLevel);
            elevator.setState(new DownState());
        }
        else {
            elevator.setState(new IdleState());
            return;
        }

        elevator.setCurrFloor(nextLevel);
        elevator.openGate();
    }
}

class DownState implements State {

    @Override
    public void move(Elevator elevator) {
        Integer nextLevel = null;
        if (!elevator.getDownStops().isEmpty()) {
            nextLevel = elevator.getDownStops().floor(elevator.getCurrFloor());
            System.out.println("Moving to level: " + nextLevel);
            elevator.getUpStops().remove(nextLevel);
        }
        else if (!elevator.getUpStops().isEmpty()) {
            nextLevel = elevator.getUpStops().first();
            System.out.println("Moving to level: " + nextLevel);
            elevator.getDownStops().remove(nextLevel);
            elevator.setState(new UpState());
        }
        else {
            elevator.setState(new IdleState());
            return;
        }

        elevator.setCurrFloor(nextLevel);
        elevator.openGate();
    }
}

class IdleState implements State {

    @Override
    public void move(Elevator elevator) {
        Integer nextLevel = null;
        if (!elevator.getUpStops().isEmpty()) {
            nextLevel = elevator.getUpStops().first();
            System.out.println("Moving to level: " + nextLevel);
            elevator.getUpStops().remove(nextLevel);
            elevator.setState(new UpState());
        }
        else if (!elevator.getDownStops().isEmpty()) {
            nextLevel = elevator.getDownStops().first();
            System.out.println("Moving to level: " + nextLevel);
            elevator.getDownStops().remove(nextLevel);
            elevator.setState(new DownState());
        }
        else {
            System.out.println("No stop left, stay at " + elevator.getCurrFloor());
            return;
        }

        System.out.println("Reaching at " + nextLevel + ", opening door");
        elevator.setCurrFloor(nextLevel);
        elevator.openGate();
    }
}