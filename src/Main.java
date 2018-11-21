import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String args[]){

        println("Starte Programm");
        println("Verbindungsaufbau:");
        DatenbankGateway myDB = null;
        try {
            myDB = new DatenbankGateway();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        println("Erfolgreich");
        println("CSV datei Improt");
        CsvImport csvFileImporter = new CsvImport("resources/artikel.dat",myDB);
        println("Erfolgreich\nBeginne mit dem Insert");
        //csvFileImporter.readCVSandInsert();
    }

    public void showARTIKEL(DatenbankGateway db){
        ResultSet ret = db.SelectSQL("Select * From ARTIKEL");

    }


    public static void println(String out){
        System.out.println(out);
    }
}
