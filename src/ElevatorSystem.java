import java.util.Scanner;

public class ElevatorSystem implements Runnable{
    private static final int startingFloor = 1;
    private static final int elevatorCount = 3;
    private static ElevatorSystem _instance = null;
//    private final List<Elevator> elevators;
    private final Elevator elevator;
    private boolean running;
    private final Scanner scanner;

    private ElevatorSystem(Elevator elevator) {
        this.scanner = new Scanner(System.in);
        this.running = false;
        this.elevator = elevator;
    }

    public static ElevatorSystem getInstance(Elevator elevator) {
        if (_instance == null) {
            _instance = new ElevatorSystem(elevator);
        }
        return _instance;
    }

    public void handleInternalRequest(InternalRequest request) {
        _instance.elevator.handleInternalRequest(request);
    }

    public void handleExternalRequest(ExternalRequest request) {
        _instance.elevator.handleExternalRequest(request);
    }

    public Elevator getElevator() {
        return elevator;
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            System.out.println("Enter the floor you want to go to: ");
            int nextFloor = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the direction you want to go to [u/d]: ");
            String direction = scanner.nextLine();
            if (direction.equals("u")) {
                handleExternalRequest(new ExternalRequest(nextFloor, Direction.UP));
            }
            else if (direction.equals("d")) {
                handleExternalRequest(new ExternalRequest(nextFloor, Direction.DOWN));
            }
            else {
                throw new IllegalArgumentException("Invalid direction: " + direction);
            }
        }
    }
}
