import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Project {


    public static void main(String[] args) throws FileNotFoundException {
        File stops = new File("stops.txt");
        Scanner scanner = new Scanner(stops);
        scanner.nextLine();
        Stops[] store = new Stops[0];
        while(scanner.hasNextLine()){
            String[] stop = scanner.nextLine().split(",");
            if(stop.length >= 3 && !containsEmpty(stop)) {
                stop[2] = formatStopName(stop[2]);
                Stops[] copy  = Arrays.copyOf(store,store.length+1);
                store = copy;
                store[store.length-1]= new Stops(stop);
            }
        }
        scanner.close();
        Stops[] sortedStops = quickSort(store);




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

    //Taken from an assignment and edited to handle stops
    static Stops [] quickSort (Stops[] a){

        recursiveQuick(a, 0, a.length-1);
        return a;

    }//end quicksort



    private static void recursiveQuick(Stops[] numbers, int lo, int hi) {
        if(hi <= lo) {
            return;
        }
        int pivotPos = partition(numbers, lo, hi);
        recursiveQuick(numbers, lo, pivotPos-1);
        recursiveQuick(numbers, pivotPos+1, hi);
    }
    private static int partition(Stops[] numbers, int lo, int hi) {
        int i = lo;
        int j = hi+1;
        Stops pivot = numbers[lo];
        while(true) {
            while((numbers[++i].getStopId() < pivot.getStopId())) {
                if(i == hi) break;
            }
            while(pivot.getStopId()<numbers[--j].getStopId()) {
                if(j == lo) break;
            }
            if(i >= j) break;
            Stops temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
        numbers[lo] = numbers[j];
        numbers[j] = pivot;
        return j;
    }


}
class Stops{
    int stopId;
    String stopCode;
    String stopName;
    String stopDesc;
    public Stops(String[] stopInfo){
        stopId = Integer.parseInt(stopInfo[0]);
        stopCode = stopInfo[1];
        stopName = stopInfo[2];
        stopDesc = stopInfo[3];
    }
    public String getStopInfo(){
        return "Stop ID: " + stopId +", Stop Code: "
                + stopCode+", StopName: " + stopName+ ", Stop Desc: " + stopDesc;
    }
    public int getStopId(){
        return stopId;
    }

}
