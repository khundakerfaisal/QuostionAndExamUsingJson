import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONEmployeeInfo {
    public static void main(String[] args) throws IOException, ParseException {
//        empInfoWrite();
        empInfoRead();
    }

    public static void empInfoWrite() throws IOException {
        JSONObject empWrite= new JSONObject();
        empWrite.put("Name","Faisal");
        empWrite.put("Email","Faisal@cg-bd.com");
        empWrite.put("Role","Admin");
        FileWriter writer = new FileWriter("./src/main/resources/employee.json");
        writer.write(empWrite.toJSONString());
        writer.flush();
        writer.close();

    }
    public static void empInfoRead() throws IOException, ParseException {
        JSONParser pharse = new JSONParser();
        JSONObject empReadData = (JSONObject)pharse.parse(new FileReader("./src/main/resources/employee.json"));
        String dataRead = (String) empReadData.get("Name");
        System.out.println(dataRead);
    }

}
