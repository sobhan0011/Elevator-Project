import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Elevator implements Runnable{
    private final String name;
    private String direction;
    private Vector<Integer> requestArray;
    private int referenceFloor;

    public Elevator(String name, Vector<Integer> requestArray, int referenceFloor, String direction)
    {
        this.name = name;
        this.requestArray = requestArray;
        this.referenceFloor = referenceFloor;
        this.direction = direction;
    }


    public Vector<Integer> getRequestArray() {
        return requestArray;
    }

    public void setRequestArray(Vector<Integer> requestArray) {
        this.requestArray = requestArray;
    }

    public String getName() {
        return name;
    }

    public int getReferenceFloor() {
        return referenceFloor;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public void run() {
        while (true) {
            if (this.getRequestArray().size() == 0)
                continue;
            Vector<Integer> currentRequestArray = (Vector<Integer>) this.getRequestArray().clone();
            setRequestArray(new Vector<>());
            int relocation = 0, currentFloor, distance;
            ArrayList<Integer> up = new ArrayList<>();
            ArrayList<Integer> down = new ArrayList<>();
            ArrayList<Integer> floorSequence = new ArrayList<>();
            floorSequence.add(referenceFloor);
            for (Integer request : currentRequestArray)
                if (request < this.referenceFloor)
                    down.add(request);
                else if (request > this.referenceFloor)
                    up.add(request);
            Collections.sort(down);
            Collections.sort(up);

            if (up.size() != 0)
                this.direction = "up";
            for (Integer upRequest : up) {
                distance = Math.abs(upRequest - this.referenceFloor);
                try {
                    Thread.sleep(distance * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFloor = upRequest;
                 /*
                    elevator go up distance pixel in distance / 10 time
                 */
                floorSequence.add(currentFloor);
                relocation += distance;
                this.referenceFloor = currentFloor;
            }

            if (down.size() != 0)
            {
                this.direction = "down";
                distance = Math.abs(this.referenceFloor - down.get(0));
                relocation += distance;
                /*
                   elevator go down distance pixel in distance / 10 time
                   sleep(distance / 10)
                */
                try {
                    Thread.sleep(distance * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.referenceFloor = down.get(0);
            }

            for (Integer downRequest : down) {
                distance = Math.abs(downRequest - this.referenceFloor);
                try {
                    Thread.sleep(distance * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentFloor = downRequest;
                 /*
                    elevator go up distance pixel in distance / 10 time
                    sleep(distance / 10)
                 */
                floorSequence.add(currentFloor);
                relocation += distance;
                this.referenceFloor = currentFloor;
            }

            if (relocation != 0)
            {
                System.out.println("-------------------------------------------------------------------------------------------");
                System.out.println("                                      " + this.name);
                System.out.println("Total amount of relocation is: " + relocation + ".");
                System.out.print("Floor sequence is: ");
                for (int i = 0; i < floorSequence.size() - 1; i++)
                    System.out.print(floorSequence.get(i) + " --> ");
                System.out.println(floorSequence.get(floorSequence.size() - 1));
                System.out.println("-------------------------------------------------------------------------------------------");
            }


        }
    }
}