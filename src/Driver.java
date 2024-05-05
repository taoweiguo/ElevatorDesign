import java.util.ArrayList;
import java.util.List;

public class Driver {
    public static void main(String[] args) {
        int elevatorCount = 3;
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(new Elevator(i, 1));
        }
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance(elevators);

        Thread elevatorSystemThread = new Thread(elevatorSystem);
        List<Thread> elevatorThreads = new ArrayList<>();
        for (int i = 0; i < elevatorCount; i++) {
            elevatorThreads.add(new Thread(elevatorSystem.getElevators().get(i)));
        }

        for (Thread thread : elevatorThreads) {
            thread.start();
        }

        elevatorSystemThread.start();
    }
}
