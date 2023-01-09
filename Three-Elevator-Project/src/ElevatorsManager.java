import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class ElevatorsManager {
    //static String str;

    private static int relocationCalculate(Vector<Integer> requestArray, int newFloor, int referenceFloor, String direction) {
        int relocation = 0, currentFloor, distance;
        ArrayList<Integer> up = new ArrayList<>();
        ArrayList<Integer> down = new ArrayList<>();
        for (Integer request : requestArray)
            if (request < referenceFloor)
                down.add(request);
            else if (request > referenceFloor)
                up.add(request);

        if (newFloor < referenceFloor)
            down.add(newFloor);
        else if (newFloor > referenceFloor)
            up.add(newFloor);
        Collections.sort(down);
        Collections.sort(up);

        if (direction.equals("up")) {
            for (Integer upRequest : up) {
                currentFloor = upRequest;
                distance = Math.abs(currentFloor - referenceFloor);
                relocation += distance;
                referenceFloor = currentFloor;
            }
            if (down.size() != 0)
            {
                relocation += Math.abs(referenceFloor - down.get(0));
                referenceFloor = down.get(0);
            }
        }

        for (Integer downRequest : down) {
            currentFloor = downRequest;
            distance = Math.abs(currentFloor - referenceFloor);
            relocation += distance;
            referenceFloor = currentFloor;
        }

        if (direction.equals("down")) {
            if (up.size() != 0)
            {
                relocation += Math.abs(referenceFloor - up.get(up.size() - 1));
                referenceFloor = up.get(up.size() - 1);
                for (int i = up.size() - 1; i >= 0; i--) {
                    currentFloor = up.get(i);
                    distance = Math.abs(currentFloor - referenceFloor);
                    relocation += distance;
                    referenceFloor = currentFloor;
                }
            }
        }
        return relocation;
}

    public static void main(String[] args) {
        int apartmentFloor = 15;
        Scanner input = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("                       All elevators in 0 floor at first.");
        System.out.println("-------------------------------------------------------------------------------------------");
        Elevator elevator = new Elevator("elevator A", new Vector<>(), 0, "up");
        Elevator elevator2 = new Elevator("elevator B", new Vector<>(), 0, "up");
        Elevator elevator3 = new Elevator("elevator C", new Vector<>(), 0, "up");
        Thread elevatorThread = new Thread(elevator);
        Thread elevator2Thread = new Thread(elevator2);
        Thread elevator3Thread = new Thread(elevator3);
        elevatorThread.start();
        elevator2Thread.start();
        elevator3Thread.start();
//        Thread reader = new Thread(() -> {
//            while (true) {
//                str = input.nextLine();
//            }
//        });
        String[] request;
        String str;
        int newFloor, elevatorRelocationNeeded, elevator2RelocationNeeded, elevator3RelocationNeeded, minRelocationNeeded;
        while (true) {
            str = input.nextLine();
            if (!str.isBlank())
            {
               request = str.split(":");
               newFloor = Integer.parseInt(request[1]);
                switch (request[0]) {
                    case "int" -> elevator.getRequestArray().add(newFloor);
                    case "int2" -> elevator2.getRequestArray().add(newFloor);
                    case "int3" -> elevator3.getRequestArray().add(newFloor);
                    case "ext" -> {
                        elevatorRelocationNeeded = relocationCalculate(elevator.getRequestArray(), newFloor, elevator.getReferenceFloor(), elevator.getDirection());
                        elevator2RelocationNeeded = relocationCalculate(elevator2.getRequestArray(), newFloor, elevator2.getReferenceFloor(), elevator2.getDirection());
                        elevator3RelocationNeeded = relocationCalculate(elevator3.getRequestArray(), newFloor, elevator3.getReferenceFloor(), elevator3.getDirection());
                        minRelocationNeeded = Math.min(Math.min(elevatorRelocationNeeded, elevator2RelocationNeeded), elevator3RelocationNeeded);
                        if (minRelocationNeeded == elevatorRelocationNeeded)
                            elevator.getRequestArray().add(newFloor);
                        else if (minRelocationNeeded == elevator2RelocationNeeded)
                            elevator2.getRequestArray().add(newFloor);
                        else
                            elevator3.getRequestArray().add(newFloor);
                    }
                }
            }
        }
    }
}
