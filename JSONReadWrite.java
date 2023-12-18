import java.io.*;

public class JSONReadWrite {
    public static void main(String[] args) throws IOException {
    readFile();
    }
    public static void writeFile() throws IOException {
        FileWriter writer = new FileWriter("./src/main/resources/text.txt");
        writer.write("Hellow ! This is my first project for JSON data write");
        writer.close();

    }

    public static void readFile() throws IOException {
        int character;
        FileReader reader = new FileReader("./src/main/resources/text.txt");
        while ((character = reader.read())!=-1) {
            System.out.print((char) character);
        }

    }
}
