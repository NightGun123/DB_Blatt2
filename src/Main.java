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
        //println("Erfolgreich\nBeginne mit dem Insert");
        //csvFileImporter.readCVSandInsert();
        try {
            showBestellung(myDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        myDB.close();
    }

    public static void showARTIKEL(DatenbankGateway db) throws SQLException {
        printRS(db.sql_befehl_ausfuehren("Select * From ARTIKEL"));

    }

    public static void showBestellung(DatenbankGateway db) throws SQLException {
        printRS(db.sql_befehl_ausfuehren("Select * From Bestellung"));
    }
    public static void showKunden(DatenbankGateway db) throws SQLException {
        printRS(db.sql_befehl_ausfuehren("Select * From KUNDEN"));
    }
    public static void showArtikel_with_BPOS(DatenbankGateway db) throws SQLException {
        printRS(db.sql_befehl_ausfuehren("Select * From ARTIKEL a left join BPOS b on a.ARTNR=b.ARTNR"));
    }
    public static void showOneBSTNR(int BESTNR,DatenbankGateway db) throws SQLException {
        printRS(db.sql_befehl_ausfuehren(
                "Select b.*, bp.*, a.ARTBEZ From BESTELLUNG b left join BPOS bp " +
                        "on b.BESTNR=bp.BESTNR left join ARTIKEL a " +
                        "on bp.ARTNR=a.ARTNR where bp.bestnr=" +BESTNR));
    }

    public static void printRS(ResultSet R) throws SQLException {
        ResultSetMetaData rsmd = R.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (R.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = R.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }



    public static void foreachBESTNR(int BESTNR, DatenbankGateway db) throws SQLException {
        /*
        Funktion 1 von 3

         */
        ResultSet rsBPOS = db.sql_befehl_ausfuehren("Select POSNR From BPOS WHERE BESTNR = "+BESTNR);
        while(rsBPOS.next())
        {
            String POSNR_tmp = rsBPOS.getString(1);
            int POSNRint = Integer.parseInt(POSNR_tmp);
            setzeWertinBPOSdurchPOSNR(POSNRint,db);

        }
        setzeRSUMinBESTELLUNG(BESTNR,db);
    }

    public static void setzeWertinBPOSdurchPOSNR(int BPOSNR, DatenbankGateway db) throws SQLException {
        /*
        Funktion 2 von 3

         */
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
        /*
        Funktion 3 von 3

         */
        ResultSet rsBPOS = db.sql_befehl_ausfuehren("Select SUM(WERT) as Summe From BPOS WHERE BESTNR = "+BESTNR);
        rsBPOS.next();
        String Summe = rsBPOS.getString(1);
        db.sql_befehl_ausfuehren("UPDATE BESTELLUNG set RSUM = "+Summe+" WHERE BESTNR = "+BESTNR);
        db.sql_befehl_ausfuehren("UPDATE BESTELLUNG set status = 1 WHERE BESTNR = "+BESTNR);
    }


    public static void println(String out){
        System.out.println(out);
    }
}
