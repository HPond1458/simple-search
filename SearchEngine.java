import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    ArrayList<String> stringList = new ArrayList<>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            if (stringList.size() == 0) {
                return String.format("No items have been added to the list. Try /add?s=___ to add an item.");
            }
            else {
                String concatList = String.join("\n", stringList);
                return concatList;
            }
        }
        else if (url.getPath().equals("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                String queryOutput = "The following items contain \"" + parameters[1] + "\": \n";
                for (String s : stringList) {
                    if (s.contains(parameters[1])) {
                        queryOutput = queryOutput + s + "\n";
                    }
                }
                return queryOutput;
            }
            return "No search parameter.";
        } 
        else {
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    stringList.add(parameters[1]);
                    //num += Integer.parseInt(parameters[1]);
                    return String.format(parameters[1] + " added to list.");
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}