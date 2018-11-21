import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        try {
            showARTIKEL(myDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            setzeWertinBPOSdurchPOSNR(2,myDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        myDB.close();
    }

    public static void showARTIKEL(DatenbankGateway db) throws SQLException {
        ResultSet ret = db.sql_befehl_ausfuehren("Select * From ARTIKEL");

        ResultSetMetaData rsmd = ret.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (ret.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = ret.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }
    public static void setzeWertinBPOSdurchPOSNR(int BPOSNR, DatenbankGateway db) throws SQLException {

        ResultSet rsBPOS = db.sql_befehl_ausfuehren("Select ARTNR From BPOS WHERE POSNR = "+BPOSNR);
        rsBPOS.next();
        String ARTNR = rsBPOS.getString(1);
        println("DIE ARTNR lautet:"+ARTNR);
        ResultSet rsARTIKEL = db.sql_befehl_ausfuehren("Select PREIS From ARTIKEL WHERE ARTNR = "+ARTNR);
        rsARTIKEL.next();
        String APREIS = rsARTIKEL.getString(1);
        println("Der Preis  lautet:"+APREIS);
        db.sql_befehl_ausfuehren("UPDATE BPOS set WERT = (MENGE*"+APREIS+") WHERE POSNR ="+BPOSNR);

    }
    public static void setzeRSUMinBESTELLUNG(int BESTNR,DatenbankGateway db) throws SQLException {
        ResultSet rsBPOS = db.sql_befehl_ausfuehren("Select SUM(WERT) as Summe From BPOS WHERE BESTNR = "+BESTNR);
        rsBPOS.next();
        String Summe = rsBPOS.getString(1);
    }


    public static void println(String out){
        System.out.println(out);
    }
}
