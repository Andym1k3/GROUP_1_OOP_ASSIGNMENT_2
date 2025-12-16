import java.util.*;

// Abstract Device class. This particular class displays the use of abstraction.
abstract class UserDevice {
    protected String id;
    protected double x, y;
    protected double battery;
    protected DroneBaseStation connectedStation;

    public UserDevice(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.battery = 100;
    }

    public abstract void sendData();

    public void moveDrone(double newX, double newY) {
        x = newX;
        y = newY;
        battery -= 2;
    }

    public void connectToDrone  (DroneBaseStation station) {
        if (station.connectDevice(this)) {
            connectedStation = station;
        }
    }

    public void disconnect() {
        if (connectedStation != null) {
            connectedStation.disconnectDevice(this);
            connectedStation = null;
        }
    }
}

// SmartPhone device
class SmartPhone extends UserDevice {
    public SmartPhone(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public void sendData() {
        battery -= 5;
        System.out.println(id + " sending voice/video data");
    }
}

// IoT device
// The extends key word in this project is signifies inheritance. The class
// IoTDevice inherits its attributes from the Device class.
class IoTDevice extends UserDevice {
    public IoTDevice(String id, double x, double y) {
        super(id, x, y);
    }

    @Override
    public void sendData() {
        battery -= 1;
        System.out.println(id + " sending sensor data");
    }
}

// Drone base station
class DroneBaseStation {
    private String id;
    private double x, y;
    private int capacity;
    private List<UserDevice> devices = new ArrayList<>();

    public DroneBaseStation(String id, double x, double y, int capacity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public boolean connectDevice(UserDevice d) {
        if (devices.size() >= capacity) {
            System.out.println("Drone overloaded! Connection denied.");
            return false;
        }
        devices.add(d);
        return true;
    }

    public void disconnectDevice(UserDevice d) {
        devices.remove(d);
    }

    public void showStatus() {
        System.out.println("Drone " + id + " | Connected devices: " + devices.size() + "/" + capacity);
    }

    public double signalStrength(UserDevice d) {
        double dist = Math.hypot(d.x - x, d.y - y);
        return 100 / (dist + devices.size());
    }
}

// Simulation driver
public class Testing {

    static Scanner scanner = new Scanner(System.in); //This line creates a single input 
    // //reader that allows the program to read input from the keyboard.
    static DroneBaseStation drone = new DroneBaseStation("Drone-A", 0, 0, 3);
    static List<UserDevice> devices = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt(); //This line reads an integer value entered by the user and stores it in the variable choice

            switch (choice) {
                case 1: registerSmartPhone();
                case 2 : registerIoTDevice();
                case 3 : connectDevices();
                case 4 : moveDevice();
                case 5 : sendData();
                case 6 : drone.showStatus();
                case 7 : {
                    System.out.println("Exiting simulation...");
                    return;
                }
                default : System.out.println("Invalid option.");
            }
        }
    }

    // ---------------- MENU ----------------
    static void displayMenu() {
        System.out.println("\n=== Drone Mobile Network Simulator ===");
        System.out.println("1. Register SmartPhone");
        System.out.println("2. Register IoT Device");
        System.out.println("3. Connect All Devices");
        System.out.println("4. Move a Device");
        System.out.println("5. Send Data");
        System.out.println("6. Show Drone Status");
        System.out.println("7. Exit");
        System.out.print("Choose option: ");
    }

    // ---------------- ACTIONS ----------------
    static void registerSmartPhone() {
        System.out.print("Enter device ID: ");
        String id = scanner.next();
        devices.add(new SmartPhone(id, 1, 1));
        System.out.println("SmartPhone registered.");
    }

    static void registerIoTDevice() {
        System.out.print("Enter IoTDevice ID: ");
        String id = scanner.next();
        devices.add(new IoTDevice(id, 2, 2));
        System.out.println("IoT Device registered.");
    }

    static void connectDevices() {
        for (UserDevice d : devices) {
            d.connectToDrone(drone);
        }
    }

    static void moveDevice() {
        if (devices.isEmpty()) {
            System.out.println("No devices available.");
            return;
        }
        System.out.print("Enter device index (0 - " + (devices.size() - 1) + "): ");
        int index = scanner.nextInt();
        System.out.print("New X: ");
        double x = scanner.nextDouble();
        System.out.print("New Y: ");
        double y = scanner.nextDouble();
        devices.get(index).moveDrone(x, y);
    }

    static void sendData() {
        for (UserDevice d : devices) {
            d.sendData();
        }
    }
}
