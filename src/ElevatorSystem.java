public class ElevatorSystem {
    private static final int startingFloor = 1;

    private static ElevatorSystem _instance = null;
    private final Elevator elevator;

    private ElevatorSystem() {
        elevator = new Elevator(startingFloor);
    }

    public static ElevatorSystem getInstance() {
        if (_instance == null) {
            _instance = new ElevatorSystem();
        }
        return _instance;
    }

    public void handleInternalRequest(InternalRequest request) {
        _instance.elevator.handleInternalRequest(request);
    }

    public void handleExternalRequest(ExternalRequest request) {
        _instance.elevator.handleExternalRequest(request);
    }
}
