import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class TestBackend {

    static String fileType = "plainText";

    public static void main(String[] args) {
        try{
            if(args.length == 0){
                throw new Exception("Empty Arguments!");
            }
            if(args.length == 1 && args[0].equals("-h")){
                printHelp();
            }
            else{
                boolean isPathOutputExist = false;
                String pathInput = args[0];

                if(!pathInput.contains("var/log")){
                    throw new FileNotFoundException("File is not Linux System Log!");
                }

                String fileInput = pathInput.substring(pathInput.lastIndexOf("/")+1);
                
                if(fileInput.isEmpty() || !fileInput.contains(".log")){
                    throw new FileNotFoundException("File log not specified!");
                }

                String content = readTextFile(pathInput);
                String pathOutput = pathInput;

                for(int i = 1; i < args.length; i++){
                    String input = args[i];
                    switch(input){
                        case "-t":
                            fileType = args[i+1];
                            i++;
                            break;
                        case "-o":
                            pathOutput = args[i+1];
                            isPathOutputExist = true;
                            i++;
                            break;
                        case "-h":
                            throw new Exception("-h cannot be used with other arguments!");
                        default:
                            break;
                    }
                }
                if(!isPathOutputExist){
                    if(fileType.equals("plainText") || fileType.equals("text")){
                        pathOutput = pathOutput.replace(".log", ".txt");
                    }
                    else{
                        pathOutput = pathOutput.replace(".log", ".json");
                    }
                }

                String fileOutput = pathOutput.substring(pathOutput.lastIndexOf("/")+1);

                if(!fileOutput.contains(".txt") || !fileOutput.contains(".json")){
                    if(fileOutput.isEmpty()){
                        String[] fileInputSplit = fileInput.split("[.]");
                        if(fileType.equals("plainText") || fileType.equals("text")){
                            fileOutput = fileInputSplit[0] + ".txt";
                        }
                        else{
                            fileOutput = fileInputSplit[0] + ".json";
                        }
                        pathOutput = pathOutput + fileOutput;
                    }
                    else{
                        pathOutput = pathOutput.replace(fileOutput, "");
                        if(fileType.equals("plainText") || fileType.equals("text")){
                            fileOutput = fileOutput + ".txt";
                        }
                        else{
                            fileOutput = fileOutput + ".json";
                        }
                        pathOutput = pathOutput + fileOutput;
                    }
                }
                System.out.println(pathOutput);
                writeToTextFile(pathOutput, content);
            }
        }
        catch(Exception err){
            err.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Must Include [path_input] on first argument (except -h)");
        System.out.println("-h                  :     list of arguments");
        System.out.println("-t [text/json]      :     type of file output");
        System.out.println("-o [path_output]    :     output file path");
        System.out.println("If -t doesn't exists, default is plain text");
        System.out.println("-h can only be used without any arguments");
    }

    public static String readTextFile(String fileInput) throws IOException{
        String content = new String(Files.readAllBytes(Paths.get(fileInput)));
        return content;
    }

    public static void writeToTextFile(String fileOutput, String content) throws IOException {
        Files.write(Paths.get(fileOutput), content.getBytes(), StandardOpenOption.CREATE);
    }

    public static List<String> readTextFileByLines(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        return lines;
    }
}