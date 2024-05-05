import java.util.Scanner;
import java.util.List;

public class ElevatorSystem implements Runnable{
    private static ElevatorSystem _instance = null;
    private final List<Elevator> elevators;
    private boolean running;
    private final Scanner scanner;

    private ElevatorSystem(List<Elevator> elevators) {
        this.scanner = new Scanner(System.in);
        this.running = false;
        this.elevators = elevators;
    }

    public static ElevatorSystem getInstance(List<Elevator> elevators) {
        if (_instance == null) {
            _instance = new ElevatorSystem(elevators);
        }
        return _instance;
    }

    public void handleInternalRequest(InternalRequest request, int elevatorNumber) {
        _instance.elevators.get(elevatorNumber).handleInternalRequest(request);
    }

    public void handleExternalRequest(ExternalRequest request) {
        int elevatorNumber = findElevator(request);
        _instance.elevators.get(elevatorNumber).handleExternalRequest(request);
    }

    private int findElevator(ExternalRequest request) {

        int best = 0;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < elevators.size(); i++) {
            Elevator elevator = elevators.get(i);
            int distance = Math.abs(request.getFloor() - elevator.getCurrFloor());
            if ((ElevatorState.IDLE == elevator.getElevatorState() ||
                    (ElevatorState.UP == elevator.getElevatorState() &&
                    request.getDirection() == Direction.UP &&
                    elevator.getCurrFloor() < request.getFloor())||
                    (ElevatorState.DOWN == elevator.getElevatorState() &&
                    request.getDirection() == Direction.DOWN &&
                    elevator.getCurrFloor() > request.getFloor())) && distance < minDistance) {
                minDistance = distance;
                best = i;
            }
        }
        System.out.println("Elevator " + best + " accept the task, Going " + elevators.get(best).getElevatorState());
        return best;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            System.out.println("----- Enter the floor you want to go to: ----- ");
            int nextFloor = scanner.nextInt();
            scanner.nextLine();
            System.out.println("----- Enter the direction you want to go to [u/d]: ----- ");
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
