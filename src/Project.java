import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Project {


    public static void main(String[] args) throws FileNotFoundException {
        File stops = new File("stops.txt");
        Scanner scanner = new Scanner(stops);
        scanner.nextLine();
        TernarySearchTree stopNames = new TernarySearchTree();
        while(scanner.hasNextLine()){
            String[] stop = scanner.nextLine().split(",");
            if(stop.length >= 4 && !containsEmpty(stop)) {
                stop[2] = formatStopName(stop[2]);
                stopNames.insert(stop[2] + ", Stop ID: " + stop[0]+", Stop Code: "+ stop[1]+ ", Stop Desc: "+ stop[3]);
            }
        }
        scanner.close();
        File stopTimes = new File("stop_times.txt");
        scanner = new Scanner(stopTimes);
        scanner.nextLine();
        double[][] adjacency = new double[13000][13000];
        int prevRoute = 0;
        int prevStop = 0;
        System.out.println("made it to stopTimes");
        while(scanner.hasNextLine()){
            String removeSpaces = scanner.nextLine().replaceAll(" ","");
            String[] currentStop = removeSpaces.split(",");
            int route = Integer.parseInt(currentStop[0]);
            int stop = Integer.parseInt( currentStop[4]);
            if(stop > adjacency.length){
                adjacency = expandAdjacency(adjacency, stop);
            }
            if((convertStopTime(currentStop[1]) != -1 || convertStopTime(currentStop[2]) != -1)) {
                if (route == prevRoute && prevStop != 0){
                    adjacency[prevStop - 1][stop - 1] = 1;
            }
            }
            prevStop = stop;
            prevRoute = route;
        }
        scanner.close();
        File transfers = new File("transfers.txt");
        scanner = new Scanner(transfers);
        scanner.nextLine();
        System.out.println("made it to transfers");
        while(scanner.hasNextLine()){
            String[] transfer = scanner.nextLine().split(",");
            int from = Integer.parseInt(transfer[0]);
            int to = Integer.parseInt(transfer[1]);
            if(Integer.parseInt( transfer[2]) == 0){
                adjacency[from-1][to-1] = 2;
            }
            else if(Integer.parseInt(transfer[2]) == 2){
                adjacency[from-1][to-1] = (Double.parseDouble(transfer[3]))/100;
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
        boolean noExit = true;
            while (noExit){
                while(scanner.hasNextLine()){
                String input = scanner.nextLine();
                if(input.isEmpty()){
                    System.out.println("Please provide an input");
                }
                else if(input.equalsIgnoreCase("exit")){
                    noExit = false;
                    scanner.close();
                }
                else if(input.equalsIgnoreCase("arrival time")){
                    System.out.println("Please input a time in the format of \"hh:mm:ss\"");
                    input = scanner.next();
                    input = input.replaceAll(" ","");
                    String[] time = input.split(":");
                    boolean isValid = true;
                    if(time.length == 3){
                        for(int i = 1; i<time.length;i++){
                            if(!time[i].matches("[0-9]+")){
                                isValid = false;
                            }
                        }
                        if(isValid){
                            scanner.close();
                            System.out.println("The Stops that match the time given are: ");
                            scanner = new Scanner(stopTimes);
                            while(scanner.hasNext()){
                                String newLine = scanner.nextLine();
                                if(newLine.contains(input)){
                                    String[] lineForOutput = newLine.split(",");
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
                            }
                            scanner.close();
                            scanner = new Scanner(System.in);
                            System.out.println("Type exit to quit or one of the other options to search again");
                        }
                    }
                }

            }
            }






    }
    static int binarySearchStops(int time, Stop[] stops){
        int lo = 0, hi = stops.length-1;
        while(lo<=hi){
            int mid = lo +(hi-lo)/2;
            if(time< stops[mid].getSeconds()) hi = mid-1;
            else if(time> stops[mid].getSeconds()) lo = mid +1;
            else return mid;
        }
        return -1;
    }

    static Stop [] quickSort (Stop a[]){

        recursiveQuick(a, 0, a.length-1);
        return a;

    }//end quicksort



    private static void recursiveQuick(Stop[] numbers, int lo, int hi) {
        if(hi <= lo) {
            return;
        }
        int pivotPos = partition(numbers, lo, hi);
        recursiveQuick(numbers, lo, pivotPos-1);
        recursiveQuick(numbers, pivotPos+1, hi);
    }

    private static int partition(Stop[] numbers, int lo, int hi) {
        int i = lo;
        int j = hi+1;
        Stop pivot = numbers[lo];
        while(true) {
            while((numbers[++i].getSeconds() < pivot.getSeconds())) {
                if(i == hi) break;
            }
            while(pivot.getSeconds()<numbers[--j].getSeconds()) {
                if(j == lo) break;
            }
            if(i >= j) break;
            Stop temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        numbers[lo] = numbers[j];
        numbers[j] = pivot;
        return j;
    }


    public static double[][] expandAdjacency(double[][] adjacency, int biggerSize){
        int size = adjacency.length;
        while(size<biggerSize){
            size =size *2;
        }
        double[][] copy = new double[size][size];
        for(int i = 0; i< adjacency.length;i++){
            for(int j = 0; j<adjacency.length;j++){
                copy[i][j] = adjacency[i][j];
            }
        }
        return copy;
    }
    public static int convertStopTime (String input){
        String[] time = input.split(":");
        int finalTime = 0;
        finalTime = 60*60*Integer.parseInt(time[0]);
        finalTime += 60*Integer.parseInt(time[1]);
        finalTime += Integer.parseInt(time[2]);

        //if The time converted to seconds is greater than 24 hours then return -1 as an invalid time
        if(finalTime >= 86400 || finalTime<0){
            return -1;
        }
        return finalTime;
    }
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
                for(int i = 0; i< name.length;i++){
                    finalStopName = finalStopName + name[i]+" " ;
                }
                return finalStopName;
            }
            return unformattedName;
        }
    public static Boolean containsEmpty(String[] input){
        for(int i = 0;i< input.length;i++){
            if(input[i].length() ==0 || input[i] == null ){
                return true;
            }
        }
        return false;
    }



}
class Stop{
    int stopId;
    int tripId;
    String arrivalTime;
    int timeInSeconds;
    String departureTime;
    int stopSequence;
    int stopHeadsign;
    int pickupType;
    int dropOffType;
    double distTraveled;
    public Stop(String[] input){
        stopId = Integer.parseInt(input[3]);
        tripId =Integer.parseInt(input[0]);
        arrivalTime = input[1];
        departureTime = input[2];
        timeInSeconds = Project.convertStopTime(input[1]);
        if(!input[4].isEmpty()){
            stopSequence = Integer.parseInt(input[4]);
        }
        if(!input[5].isEmpty()){
            stopHeadsign = Integer.parseInt(input[5]);
        }
        if(!input[6].isEmpty()){
            pickupType = Integer.parseInt(input[6]);
        }
        if(!input[7].isEmpty()){
            dropOffType = Integer.parseInt(input[7]);
        }
        if(input.length == 9){
            distTraveled = Double.parseDouble(input[8]);
        }
    }
    public int getSeconds(){
        return timeInSeconds;
    }
}
