import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Project {


    public static void main(String[] args) throws FileNotFoundException {
        File stops = new File("stops.txt");
        Scanner scanner = new Scanner(stops);
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String[] stop = scanner.nextLine().split(",");

        }

    }
}
class Stops{
    String stopId;
    String stopCode;
    String stopName;
    String stopDesc;
    public Stops(String[] stopInfo){
        stopId = stopInfo[0];
        stopCode = stopInfo[1];
        String[] name = stopInfo[2].split(" ");
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


        }
        String finalStopName = "";
        for(int i = 0; i< name.length;i++){
            finalStopName = finalStopName + name[i]+" " ;
        }
        stopName = finalStopName;
        stopDesc = stopInfo[3];
    }
}
