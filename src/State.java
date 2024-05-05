public interface State {
    void move(Elevator elevator);
}

class UpState implements State {

    @Override
    public void move(Elevator elevator) {
        if (elevator.getUpStops().ceiling(elevator.getCurrFloor()) != null) {
            elevator.movingUp();
        }
        else if (elevator.getDownStops().floor(elevator.getCurrFloor()) != null) {
            elevator.setState(new DownState());
            elevator.movingDown();
        }
        else if (!elevator.getUpStops().isEmpty() || !elevator.getCurrentStops().isEmpty()) {
            elevator.movingDown();
        }
        else {
            elevator.setState(new IdleState());
        }
    }
}

class DownState implements State {

    @Override
    public void move(Elevator elevator) {

        if (elevator.getDownStops().floor(elevator.getCurrFloor()) != null) {
            elevator.movingDown();
        }
        else if (elevator.getUpStops().ceiling(elevator.getCurrFloor()) != null) {
            elevator.setState(new UpState());
            elevator.movingUp();
        }
        else if (!elevator.getDownStops().isEmpty()  || !elevator.getCurrentStops().isEmpty()) {
            elevator.movingUp();
        }
        else {
            elevator.setState(new IdleState());
        }
    }
}

class IdleState implements State {

    @Override
    public void move(Elevator elevator) {
        if (elevator.getUpStops().ceiling(elevator.getCurrFloor()) != null) {
            elevator.setState(new UpState());
        }
        else if (elevator.getDownStops().floor(elevator.getCurrFloor()) != null) {
            elevator.setState(new DownState());
        }

        else if (!elevator.getUpStops().isEmpty() || !elevator.getCurrentStops().isEmpty()) {
            elevator.setState(new UpState());
        }
        else if (!elevator.getDownStops().isEmpty()  || !elevator.getCurrentStops().isEmpty()) {
            elevator.setState(new DownState());
        }
    }

}