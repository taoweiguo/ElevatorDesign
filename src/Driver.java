public class Driver {
    public static void main(String[] args) {
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance();

        elevatorSystem.handleExternalRequest(new ExternalRequest(5, Direction.UP));
        elevatorSystem.handleInternalRequest(new InternalRequest(7));
        elevatorSystem.handleExternalRequest(new ExternalRequest(6, Direction.UP));
        elevatorSystem.handleExternalRequest(new ExternalRequest(7, Direction.DOWN));
        elevatorSystem.handleExternalRequest(new ExternalRequest(8, Direction.UP));
    }
}
