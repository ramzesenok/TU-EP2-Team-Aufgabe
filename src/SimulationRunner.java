import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimulationRunner {
    public static void main(String[] args) {
        proceedRequestData();
    }

    public static void proceedRequestData() {
        Scanner scanner = new Scanner(System.in);
        String firstMessage = "Insert a number from 0 to 5 for the simulation of the request-data: (End with X)";
        System.out.println(firstMessage);

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0 && input <= 5) {

                    System.out.println("> Simulation of ’initial-collectors-" + input + ".csv’ and ’queries-" + input + ".txt’");
                    System.out.println();

                    try (Scanner collectorScanner = new Scanner(new File(System.getProperty("user.dir") + "/data/initial-collectors-" + input + ".csv"), StandardCharsets.UTF_8.toString())) {
                        SensorsTrie trie = new SensorsTrie();
                        Sensor lastDeactivatedAfter1000RequestsSensor = null, lastActivatedAfterDivisionSensor = null;
                        int maxSize = 0, minSize = 0;

                        while (collectorScanner.hasNext()) {
                            Sensor sensor = new Sensor(collectorScanner.next());
                            trie.add(sensor);
                        }

                        try (Scanner sensorRequestScanner = new Scanner(new File(System.getProperty("user.dir") + "/data/queries-" + input + ".txt"), StandardCharsets.UTF_8.toString())) {
                            int requestCount = 0;
                            int size = 0;

                            int initNetworkSize = trie.size();
                            maxSize = initNetworkSize;
                            minSize = initNetworkSize;

                            while (sensorRequestScanner.hasNext()) {
                                requestCount++;

                                String scannerValue = sensorRequestScanner.next();
                                TrieNode node = trie.find(scannerValue);
                                Sensor sensor = node.sensor;
                                Sensor newSensor = new Sensor(scannerValue + ";0;0");

                                sensor.makeRequest();

                                if (sensor.reachedOveralRequestsLimit()) {
                                    trie.delete(node);
                                    trie.add(newSensor);

                                    lastDeactivatedAfter1000RequestsSensor = sensor;

                                } else if (sensor.reachedRequestsSinceLastDivisionLimit()) {
                                    sensor.setRequestsSinceLastDivisionToZero();
                                    sensor.setOriginallyOverallRequestsToCurrentOverallRequestsCount();
                                    trie.add(newSensor);

                                    lastActivatedAfterDivisionSensor = newSensor;
                                }

                                if (requestCount == 500000) {

                                    size = trie.size();
                                    if (size > maxSize) {
                                        maxSize = size;
                                    }
                                    if (size < minSize) {
                                        minSize = size;
                                    }
                                    for (TrieNode untouchedNode : trie.getUntouchedCollector()) {
                                        trie.delete(untouchedNode);

                                    }
                                    requestCount = 0;

                                }
                            }
                            int lastSize = trie.size();
                            if (lastSize > maxSize){
                                maxSize = lastSize;
                            }
                            if (lastSize < minSize){
                                minSize = lastSize;
                            }

                            System.out.println("minimal size of network: " + minSize);
                            System.out.println("maximal size of network: " + maxSize);
                            System.out.println("last deactivated collector: " + (lastDeactivatedAfter1000RequestsSensor != null ? lastDeactivatedAfter1000RequestsSensor.getId() : "Gibt's keinen"));
                            System.out.println("last activated collector: " + (lastActivatedAfterDivisionSensor != null ? lastActivatedAfterDivisionSensor.getId() : "Gibt's keinen"));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                } else {
                    System.out.println("There are no testdata available with this number: " + input);
                    System.out.println(firstMessage);
                }
            }else{
                String x = scanner.next();
                if (x.equals("X")) {
                    System.out.println("Thank you, goodbye!");
                    System.exit(0);
                } else {
                    System.out.println("Wrong Input: " + x);
                    System.out.println(firstMessage);
                }
            }
        }
    }
}
