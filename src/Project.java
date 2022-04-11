/**
 * Dijkstra, TST, and EdgeWeightdGraph were all taken from Princeton's algs4 library
 */

import edu.princeton.cs.algs4.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Project {


    public static void main(String[] args) throws IOException {
        File stops = new File("stops.txt");
        Scanner scanner = new Scanner(stops);
        scanner.nextLine();
        TST<String> stopNames = new TST<String>();
        while(scanner.hasNextLine()){
            String[] stop = scanner.nextLine().split(",");

                stop[2] = formatStopName(stop[2]);
                if(stop.length == 10){
                    stopNames.put(stop[2],  "Stop ID: " + stop[0]+", Stop Code: "+ stop[1]+ ", Stop Desc: "+ stop[3]
                            + ", Stop Lat: "+ stop[4]+ ", Stop Lon: "+ stop[5]+", Zone Id: "+ stop[6] +
                            ", Stop url: "+stop[7] + ", Location Type: " + stop[8]+", Parent Station: " + stop[9] );
                }
                else {
                    stopNames.put(stop[2],  "Stop ID: " + stop[0]+", Stop Code: "+ stop[1]+ ", Stop Desc: "+ stop[3]
                            + ", Stop Lat: "+ stop[4]+ ", Stop Lon: "+ stop[5]+", Zone Id: "+ stop[6] +
                            ", Stop url: "+stop[7] + ", Location Type: " + stop[8]+", Parent Station: " );
                }

        }
        scanner.close();
        File stopTimes = new File("stop_times.txt");
        scanner = new Scanner(stopTimes);
        scanner.nextLine();
        int largestStop = 0;
        while(scanner.hasNextLine()){
            String stopInfo = scanner.nextLine();
            String[] stopSplit = stopInfo.split(",");
            if (Integer.parseInt(stopSplit[3]) > largestStop){
                largestStop = Integer.parseInt(stopSplit[3]);
            }
        }
        scanner.close();
        EdgeWeightedDigraph adjacency = new EdgeWeightedDigraph(largestStop+1);
        int prevRoute = 0;
        int prevStop = 0;
        scanner = new Scanner(stopTimes);
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String removeSpaces = scanner.nextLine().replaceAll(" ","");
            String[] currentStop = removeSpaces.split(",");
            int route = Integer.parseInt(currentStop[0]);
            int stop = Integer.parseInt( currentStop[3]);
            if((convertStopTime(currentStop[1]) != -1 || convertStopTime(currentStop[2]) != -1)) {
                if (route == prevRoute && prevStop != 0 ){
                    Iterable<DirectedEdge> edges = adjacency.adj(prevStop);
                    Iterator<DirectedEdge> iterator = edges.iterator();
                    if(!hasEdge(iterator, stop)){
                        adjacency.addEdge(new DirectedEdge(prevStop, stop, 1));
                    }

            }
            }
            prevStop = stop;
            prevRoute = route;
        }
        scanner.close();
        File transfers = new File("transfers.txt");
        scanner = new Scanner(transfers);
        scanner.nextLine();

        while(scanner.hasNextLine()){
            String[] transfer = scanner.nextLine().split(",");
            int from = Integer.parseInt(transfer[0]);
            int to = Integer.parseInt(transfer[1]);
            if(Integer.parseInt( transfer[2]) == 0 && !hasEdge(adjacency.adj(from).iterator(), to)){
                adjacency.addEdge(new DirectedEdge(from, to, 2));
            }
            else if(Integer.parseInt(transfer[2]) == 2 && !hasEdge(adjacency.adj(from).iterator(), to)){
                adjacency.addEdge(new DirectedEdge(from, to, (Double.parseDouble(transfer[3]))/100));
            }
        }
        scanner.close();
        scanner = new Scanner(System.in);
        System.out.println("What would you like to do? \n" +
                "Type \"route\" to see the fastest route between two stops \n" +
                "Type \"stops\" to search for different stops\n" +
                "Type \"arrival time\" to search for bus stops that arrive at a certain time\n" +
                "Type \"exit\" to exit this program(can be entered after selecting a mode too)\n"+
                "The inputs are case sensitive but selecting a mode is not");
        System.out.print("Please enter the mode you wish to use:");
        boolean noExit = true;

            while (noExit && scanner.hasNextLine()){

                String input = scanner.nextLine();
                if(input.isEmpty()){
                    System.out.print("Please enter the mode you wish to use:");;
                }
                else if(input.equalsIgnoreCase("exit")){
                    noExit = false;
                }
                else if(input.equalsIgnoreCase("arrival time")){
                    System.out.print("Please input a time in the format of \"hh:mm:ss\":");
                    input = scanner.next();
                    input = input.replaceAll(" ","");
                    String[] time = input.split(":");
                    boolean isValid = true;
                    if(time.length == 3){
                        for(int i = 1; i<time.length;i++){
                            if(!time[i].matches("[0-9]+")){
                                isValid = false;
                            }
                            if(convertStopTime(time) == -1){
                                isValid = false;
                            }
                        }
                        if(isValid){
                            System.out.println("The Stops that match the time given are: ");
                            BufferedReader fileReader = new BufferedReader(new FileReader("stop_times.txt"));
                            String newLine;
                            ArrayList <String> store = new ArrayList<String>();
                            while((newLine = fileReader.readLine()) != null){
                                String[] lineForOutput = newLine.split(",");
                                if(lineForOutput[1].contains(input)){
                                    store.add(newLine);
                                }
                            }
                            String[] tempStore = new String[store.size()];
                            store.toArray(tempStore);
                            String[] sorted = quickSort(tempStore);
                            for(int i = 0; i< sorted.length; i++){
                                String[] lineForOutput = sorted[i].split(",");
                                if(lineForOutput.length == 9){
                                    System.out.println("Trip id: "+lineForOutput[0] +
                                            ", Arrival time: " +lineForOutput[1]+
                                            ", Departure Time: " + lineForOutput[2]+
                                            ", Stop Id: " + lineForOutput[3]+
                                            ", Stop Sequence: " + lineForOutput[4]+
                                            ", Stop Headsign: " + lineForOutput[5]+
                                            ", Pickup Type: " + lineForOutput[6]+
                                            ", Dropoff Type: " + lineForOutput[7]+
                                            ", Dist Travelled: " + lineForOutput[8]);
                                }
                                else if(lineForOutput.length == 8){
                                    System.out.println("Trip id: "+lineForOutput[0] +
                                            ", Arrival time: " +lineForOutput[1]+
                                            ", Departure Time: " + lineForOutput[2]+
                                            ", Stop Id: " + lineForOutput[3]+
                                            ", Stop Sequence: " + lineForOutput[4]+
                                            ", Stop Headsign: " + lineForOutput[5]+
                                            ", Pickup Type: " + lineForOutput[6]+
                                            ", Dropoff Type: " + lineForOutput[7]+
                                            ", Dist Travelled:  ");
                                }
                            }

                            System.out.println("Type exit to quit or one of the other options to search again");
                        }
                        else {
                            System.out.println("Sorry, the input you provided was either had incorrect formatting or\n " +
                                    "was out of the time range ");
                        }
                    }
                }
                else if(input.equalsIgnoreCase("route")){
                    System.out.print("Please enter the starting stop id:");
                    input = scanner.next();
                    try{
                        input = input.replaceAll(" ", "");
                        int from = Integer.parseInt(input);
                        System.out.print("Please enter the ending stop id: ");
                        input = scanner.next();
                        input = input.replaceAll(" ", "");
                        int to = Integer.parseInt(input);
                        if(isAStop(adjacency, from) && isAStop(adjacency, to)) {
                            DijkstraSP shortestPath = new DijkstraSP(adjacency, from);
                            if (!shortestPath.hasPathTo(to)) {
                                System.out.println("Sorry but there is no path from " + from + " to " + to);
                            }
                            else{
                                Iterator<DirectedEdge> iterator = shortestPath.pathTo(to).iterator();
                                System.out.print("The cost from "+ from + " to "+ to+ " is: "+ shortestPath.distTo(to)+
                                        ". The path from "+ from + " to "+ to+ " is: ");
                                while(iterator.hasNext()){
                                    DirectedEdge edge = iterator.next();
                                    System.out.print( edge.from() + "->");
                                }
                                System.out.println(to);
                            }
                        }
                        else{System.out.println("Sorry but you entered a stop that doesnt exist");}
                    }catch (Exception e){
                        System.out.println("Your input was not a stop");
                    }

                }
                else if(input.equalsIgnoreCase("stops")){
                    System.out.print("Search for a stop either by full name or a few letters. \nWill return all stops" +
                            " that match the given input.\nSearch:");
                    input = scanner.next();
                    input = input.toUpperCase();
                    Iterable<String> matches = stopNames.keysWithPrefix(input);
                    Iterator<String> iterator = matches.iterator();

                    if(iterator.hasNext()) {
                        System.out.println("Stops returned: ");
                        while (iterator.hasNext()) {
                            String currentStop = iterator.next();
                            System.out.println("Stop: " + currentStop + ", " + stopNames.get(currentStop));
                        }
                    }
                    else{
                        System.out.println("There were no stops that matched your input");
                    }
                }
                else{
                    System.out.println("Please provide a proper input");
                    System.out.print("Please enter the mode you wish to use:");
                }

            }







    }


    /**
     *
     * quicksort function altered slightly to take in all the stop info as a string array and sort it all by trip Id
     */
    static String [] quickSort (String[] a){

        recursiveQuick(a, 0, a.length-1);
        return a;

    }//end quicksort



    private static void recursiveQuick(String[] numbers, int lo, int hi) {
        if(hi <= lo) {
            return;
        }
        int pivotPos = partition(numbers, lo, hi);
        recursiveQuick(numbers, lo, pivotPos-1);
        recursiveQuick(numbers, pivotPos+1, hi);
    }

    private static int partition(String[] numbers, int lo, int hi) {
        int i = lo;
        int j = hi+1;
        String pivot = numbers[lo];
        while(true) {
            while((getTripId(numbers[++i]) < getTripId(pivot))) {
                if(i == hi) break;
            }
            while(getTripId(pivot)<getTripId(numbers[--j])) {
                if(j == lo) break;
            }
            if(i >= j) break;
            String temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        numbers[lo] = numbers[j];
        numbers[j] = pivot;
        return j;
    }


    /**
     *
     * This function simply checks if a vertex in the graph has any edges pointing to it or away from it.
     * if there are none then it is assumed that the vertex doesnt correspond with any of the stops
     * within the input file
     */
    public static boolean isAStop(EdgeWeightedDigraph digraph, int stop){
        if(stop > digraph.V()){
            return false;
        }
        boolean pointsTo = false;
        Iterator<DirectedEdge> iterator = digraph.edges().iterator();
        while (iterator.hasNext()){
            DirectedEdge edge = iterator.next();
            if(edge.to() == stop){
                pointsTo = true;
            }
        }
        if(digraph.indegree(stop) == 0 && !pointsTo){
            return false;
        }
        return true;
    }

    /**
     * This method is used to determine if a stop already has an edge leading towards a specific spot.
     * That way there is no doubling up of edges that are the same length
     */
    public static boolean hasEdge(Iterator<DirectedEdge> iterator, int stop){
        while(iterator.hasNext()){
            DirectedEdge edge = iterator.next();
            if(edge.to() ==stop){
                return true;
            }
        }
        return false;
    }


    /**
     *
     * small function to get the tripID from a string. used in the quicksort
     */
    public static int getTripId(String tripData){
        String[] sections = tripData.split(",");
        return Integer.parseInt(sections[0]);
    }




    /**
    * The convertStopTime method is used to change the times provided from hours, minutes, and seconds
     * to just seconds so that it can be more easily compared to other stop times as well as to check if the
     * time stamp is too large to fit into a 24 hour day
    *
    *
     */
    public static int convertStopTime (String input){
        String[] time = input.split(":");
        return  convertStopTime(time);
    }
    public static int convertStopTime (String[] input){
        int finalTime = 0;
        finalTime = 60*60*Integer.parseInt(input[0]);
        finalTime += 60*Integer.parseInt(input[1]);
        finalTime += Integer.parseInt(input[2]);

        //if The time converted to seconds is greater than 24 hours then return -1 as an invalid time
        if(finalTime >= 86400 || finalTime<0){
            return -1;
        }
        return finalTime;
    }


    /**
     *
     * This method formats the name of the stop properly for the assignment
     */
    public static String formatStopName(String unformattedName){
            String[] name = unformattedName.split(" ");
            if(name[0].equalsIgnoreCase("flagstop") ||
                    name[0].equalsIgnoreCase("wb")||
                    name[0].equalsIgnoreCase("sb")||
                    name[0].equalsIgnoreCase("nb")||
                    name[0].equalsIgnoreCase("eb"))
            {
                String store = name[0];
                for(int i = 1; i< name.length;i++ ){
                    name[i-1] = name[i];
                }
                name[name.length - 1] = store;

                String finalStopName = "";
                for(int i = 0; i< name.length -1;i++){
                    finalStopName = finalStopName + name[i]+" " ;
                }
                finalStopName = finalStopName + name[name.length-1];
                return finalStopName;
            }
            return unformattedName;
        }


}


