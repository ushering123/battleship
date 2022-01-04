import java.util.*;

public class Hunting extends AI {
    // queue hit points (only queue if they are different ships)
    // get the coordinate of the hit (and what ship it is)
    // put 4 coordinates around hit into queue
    // sum probability
    // hit the point where probability greatest
    // if point is same ship, remove points that are not in the orientation
    // queue 2 points on either side and repeat algo
    // if point is hit and not same ship

    public static void printArrayList(ArrayList<Coordinate> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }

    public static void initHitPoint() {
        for (String i : Main.playerShipsAlive) {
            Game.playerSunkShips.put(i, new ArrayList<String>());
            Game.AIMapOfShip.put(i, new ArrayList<String>());
        }
    }

    public static Coordinate huntGUI(Coordinate h, String ship) {
        int shipSize = Ship.getSize(ship);
        // System.out.println(shipSize);

        sumArray(h, shipSize, ship);
        Coordinate nextHit = max(h.getY(), h.getX(), shipSize);

        resetArray();
        return nextHit;
    }

    public static void hunt(Coordinate h, String ship) {
        int shipSize = Ship.getSize(ship);
        // System.out.println(shipSize);

        sumArray(h, shipSize, ship);
        Coordinate nextHit = max(h.getY(), h.getX(), shipSize);
        getInput(nextHit);
        resetArray();
    }

    public static void getInputGUI(Coordinate hit, int index, int shipGUIIndex) {

        while (true) {

            int y = hit.getY();
            int x = hit.getX();

            Coordinate cur = Main.AIAttackBoard[y][x];
            cur.setIsHit(true);

            if (index == 0) {
                printHashMap();
                String ship = GUI.getShips()[shipGUIIndex];
                int shipSize = Ship.getSize(ship);
                int shipIndex = shipSize;
                if (shipSize == 3) {
                    shipIndex = Ship.getIndexOfThreeShip(ship);
                }
                Main.AIHit++;
                Main.AIShot++;
                if (checkValidShip(ship)) {
                    // Game.playerSunkShips.put(ship, new ArrayList<String>());
                    // if we hit a new ship point, add new ship to list
                    if (!ship.equals(shipsHit.get(0))) {
                        // Game.playerSunkShips.get(ship).add(hit.toString());
                        // isHunting = true;
                        // cur.setIsShip(true);
                        // cur.setIsHit(true);
                        cur.setIsUnique(true);
                        uniqueHitPoints.add(hit);
                        shipsHit.add(ship);

                        // pointsHit[shipIndex].add(cur);
                        // same ship point, set is hit
                    } else {

                        // pointsHit.add(cur);
                        // cur.setIsHit(true);
                        cur.setIsShip(true);
                    }
                    cur.setIsHit(true);
                    pointsHit[shipIndex].add(cur);
                    Game.playerSunkShips.get(ship).add(hit.toString());
                    break;
                }
                // }

            } else if (index == 2) {
                // } else if (input.substring(0, 4).equals("SUNK")) {
                Main.AIHit++;
                Main.AIShot++;
                String ship = GUI.getShips()[shipGUIIndex];

                // cur.setIsHit(true);
                Game.playerSunkShips.get(ship).add(hit.toString());
                cur.setIsShip(true);
                // System.out.println(ship);
                Ship.getPlayerListOfShipsAlive().remove(ship);
                uniqueHitPoints.remove(0);
                shipsHit.remove(ship);
                printHashMap();
                for (String i : Game.playerSunkShips.get(ship)) {
                    // System.out.println("hello running this loop");
                    // System.out.println(i);
                    int columnInd = Coordinate.columnIndexAsInt(i.charAt(0));
                    int rowInd = Integer.parseInt(i.substring(1));
                    Main.AIAttackBoard[columnInd][rowInd].setIsShip(false);
                    Main.AIAttackBoard[columnInd][rowInd].setIsSunk(true);
                }

                if (uniqueHitPoints.size() == 0) {
                    isHunting = false;
                }
                break;
            }
        }

    }

    public static void getInput(Coordinate hit) {
        // ArrayList<Ship> shipsAlive = Ship.getList();
        while (true) {
            // printArrayList(uniqueHitPoints);
            // printArrayList(hitPointQueue);

            int y = hit.getY();
            int x = hit.getX();

            System.out.printf("The AI hit coordinate %c%d\n", Coordinate.columnIndex(y), x);
            System.out.println("Is it a hit or miss or sink?");
            String input = sc.nextLine();

            Coordinate cur = Main.AIAttackBoard[y][x];
            cur.setIsHit(true);
            if (input.length() < 4) {
                System.out.println("that is not a valid input. Is it a hit, miss, or sink?");
            } else if (input.equals("MISS")) {
                Main.AIShot++;
                Main.AIMiss++;
                // isHunting = false;
                break;
            } else if (input.substring(0, 3).equals("HIT")) {
                printHashMap();
                String ship = input.substring(5);
                int shipSize = Ship.getSize(ship);
                int shipIndex = shipSize;
                if (shipSize == 3) {
                    shipIndex = Ship.getIndexOfThreeShip(ship);
                }
                Main.AIHit++;
                Main.AIShot++;
                if (checkValidShip(ship)) {
                    Game.playerSunkShips.put(ship, new ArrayList<String>());
                    // if we hit a new ship point, add new ship to list
                    if (!ship.equals(shipsHit.get(0))) {
                        Game.playerSunkShips.get(ship).add(hit.toString());
                        // isHunting = true;
                        // cur.setIsShip(true);
                        // cur.setIsHit(true);
                        uniqueHitPoints.add(hit);
                        shipsHit.add(ship);
                        cur.setIsUnique(true);
                        // pointsHit[shipIndex].add(cur);
                        // same ship point, set is hit
                    } else {
                        // pointsHit.add(cur);
                        // cur.setIsHit(true);
                        cur.setIsShip(true);
                    }
                    pointsHit[shipIndex].add(cur);
                    break;
                } else {
                    System.out.println("That is not a valid ship entered. Please try again.");
                }
            } else if (input.substring(0, 4).equals("SUNK")) {
                Main.AIHit++;
                Main.AIShot++;
                String ship = input.substring(6);
                if (checkValidShip(ship)) {
                    // cur.setIsHit(true);
                    Game.playerSunkShips.get(ship).add(hit.toString());
                    cur.setIsShip(true);
                    // System.out.println(ship);
                    Ship.getPlayerListOfShipsAlive().remove(ship);
                    uniqueHitPoints.remove(0);
                    shipsHit.remove(ship);
                    // printHashMap();
                    for (String i : Game.playerSunkShips.get(ship)) {
                        // System.out.println("hello running this loop");
                        // System.out.println(i);
                        int columnInd = Coordinate.columnIndexAsInt(i.charAt(0));
                        int rowInd = Integer.parseInt(i.substring(1));
                        Main.AIAttackBoard[columnInd][rowInd].setIsShip(false);
                        Main.AIAttackBoard[columnInd][rowInd].setIsSunk(true);
                    }
                } else {
                    System.out.println("That is not a valid ship entered. Please try again.");
                }
                if (uniqueHitPoints.size() == 0) {
                    isHunting = false;
                }
                break;
            } else {
                System.out.println("that is not a valid input. Is it a hit or miss?");
            }
        }
    }

    public static void sumArray(Coordinate h, int shipSize, String shipHitName) {
        int shipIndex = shipSize;
        if (shipSize == 3) {
            shipIndex = Ship.getIndexOfThreeShip(shipHitName);
        }
        // sum vertically and horizontally
        // VERTICAL
        // start point is hit point + 1 - shipsize (check if in bounds)
        int isVertical = 0;
        // System.out.print("Points hit for all: ");
        // for (int i = 2; i < 7; i++) {
        // for (Coordinate J : pointsHit[i]) {
        // System.out.print(i + "queue : ");
        // System.out.println(J.toString());
        // }
        // }
        if (pointsHit[shipIndex].size() >= 2) {
            // System.out.println("im running");
            Coordinate cur = pointsHit[shipIndex].get(0);
            Coordinate next = pointsHit[shipIndex].get(1);
            if (next.getY() == cur.getY()) {
                isVertical = 1;
            } else {
                isVertical = 2;
            }
        }
        // undetermined
        if (isVertical == 0) {
            sumVertical(shipSize, h);
            sumHorizontal(h, shipSize);
            // vertical
        } else if (isVertical == 1) {
            sumHorizontal(h, shipSize);
        } else {
            sumVertical(shipSize, h);
        }

        printArray(huntingProbability);
    }

    public static Coordinate max(int y, int x, int shipSize) {
        // horizontal add possible hits to arraylist
        for (int i = 1; i <= 10; i++) {
            if (huntingProbability[y][i].getProbability() > 0) {
                hitPointQueue.add(huntingProbability[y][i]);
            }
        }

        // vertical add possible hits to arraylist
        for (int i = 1; i <= 10; i++) {
            if (huntingProbability[i][x].getProbability() > 0) {
                hitPointQueue.add(huntingProbability[i][x]);
            }
        }
        Collections.sort(hitPointQueue, Collections.reverseOrder());

        return hitPointQueue.remove(0);

    }

    public static void main(String[] args) {
        Main.initArrays();
        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                huntingProbability[i][j] = new Coordinate(i, j);

            }
        }
        Coordinate test = Main.AIAttackBoard[5][5];
        Main.AIAttackBoard[5][5].setIsHit(true);
        Main.AIAttackBoard[3][5].setIsHit(true);
        hunt(test, "CARRIER");

    }

    public static void sumVertical(int shipSize, Coordinate h) {
        int y = h.getY();
        int x = h.getX();
        for (int i = (y + 1 - shipSize); i <= y; i++) {
            boolean ok = true;
            if (i < 1) {
                continue;
            }
            for (int j = i; j < i + shipSize; j++) {
                if (j > 10
                        || (Main.AIAttackBoard[j][x].getIsHit() && !Main.AIAttackBoard[j][x].getIsShip() && j != y)) {
                    ok = false;
                }
            }

            if (ok) {
                for (int j = i; j < i + shipSize; j++) {
                    if (j == y || Main.AIAttackBoard[j][x].getIsShip()) {
                        continue;
                    }
                    int currentProbability = huntingProbability[j][x].getProbability();
                    huntingProbability[j][x].setProbability(currentProbability + 1);

                }
            }

        }
    }

    public static void sumHorizontal(Coordinate h, int shipSize) {
        int y = h.getY();
        int x = h.getX();
        for (int i = (x + 1 - shipSize); i <= x; i++) {
            boolean ok = true;
            if (i < 1) {
                continue;
            }
            for (int j = i; j < i + shipSize; j++) {
                if (j > 10
                        || (Main.AIAttackBoard[y][j].getIsHit() && !Main.AIAttackBoard[y][j].getIsShip() && j != x)) {
                    ok = false;
                }
            }
            if (ok) {
                for (int j = i; j < i + shipSize; j++) {
                    if (j == x || Main.AIAttackBoard[y][j].getIsShip()) {
                        continue;
                    }
                    int currentProbability = huntingProbability[y][j].getProbability();
                    huntingProbability[y][j].setProbability(currentProbability + 1);

                }
            }

        }
    }

    public static void printHashMap() {
        for (String i : Game.playerSunkShips.keySet()) {
            // ship
            System.out.print(i);
            for (String point : Game.playerSunkShips.get(i)) {
                System.out.print(point + " ");
            }
            System.out.println();
        }
    }

}