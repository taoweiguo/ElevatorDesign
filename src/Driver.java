public class Driver {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(0);
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance(elevator);


        Thread elevatorSystemThread = new Thread(elevatorSystem);
        Thread elevatorThread = new Thread(elevatorSystem.getElevator());

        elevatorThread.start();
        elevatorSystemThread.start();
    }
}
