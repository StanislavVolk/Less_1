package Lesson_5;
import java.io.*;

@FunctionalInterface
interface ConverterToInt
{
    int[] convert(String[] array);
}

@FunctionalInterface
interface ConverterToStr
{
    String[] convert(int[] array);
}


class AppData
{
    private String[] headers;
    private int[][] data;
    final String SEPARATOR = ";";

    public AppData(int size, String headerLine, int[][] data)
    {
        this.headers = headerLine.split(",");
        this.data = data;
    }

    public AppData(String dump)
    {
        String[] csvData = dump.split("\n");
        this.headers = new String[csvData[0].split(SEPARATOR).length];
        this.data = new int[csvData.length - 1][this.headers.length];
        this.setCSVDump(dump);
    }

    public void setCSVDump(String dump)
    {

        ConverterToInt converter = (array) ->
        {
            int[] res = new int[(array.length)];
            for (int j = 0; j < array.length; j++)
            {
                res[j]=Integer.valueOf(array[j]);
            }
            return res;
        };

        try {
            String[] csvData = dump.split("\n");
            this.headers = csvData[0].split(";");
            for (int i = 1; i < csvData.length; i++) {
                this.data[i-1]=converter.convert(csvData[i].split(SEPARATOR));
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public String getCSVDump()
    {

        ConverterToStr converter = (array) ->
        {
            String[] res = new String[array.length];
            for (int j = 0; j < array.length; j++)
            {
                res[j]=Integer.toString(array[j]);
            }
            return res;
        };

        String[] dumpArr = new String[(this.data.length+1)];
        dumpArr[0] = String.join(SEPARATOR, this.headers);
        for (int i = 0; i < this.data.length; i++)
        {
            dumpArr[i+1] = String.join(SEPARATOR, converter.convert(this.data[i]));
        }
        String res = "";
        for (String s : dumpArr)
        {
            res+=s+'\n';
        }
        return res;
    }

}

public class Main
{
    File file = new File("src/main/java/Lesson_5/test.txt");


    public static void saveToCSVFile(AppData data)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/Lesson_5/test.txt"))) {
            writer.write(data.getCSVDump());
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static AppData loadFromCSVFile(String file)
    {
        String dump = "";
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/Lesson_5/test.txt")))
        {
            while ((line=reader.readLine())!=null){
                dump+=line+'\n';
                System.out.println(line);
            }
        } catch (Exception e)
        {
            System.err.println(e);
        }
        AppData data = new AppData(dump);
        return data;
    }

    public static void main(String[] args)
    {
        int[][] array = {{100,200,123},{300,400,500}};
        AppData data1 = new AppData(2,"Value 1,Value 2,Value 3",array);
        saveToCSVFile(data1);

        AppData data2 = loadFromCSVFile("src/main/java/Lesson_5/test.txt");

        if (data1.getCSVDump().equals(data2.getCSVDump()))
        {
            System.out.println("Данные в выгруженом и загруженом объектах идентичны");
        } else {
            System.out.println("Данные в выгруженом и загруженом объектах различны");
        }

    }
}