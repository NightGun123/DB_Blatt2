import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Main {

    static DatenbankGateway schelling = null;

    public static void main(String args[]){

        println("Starte Programm...");
        System.out.print("Verbindungsaufbau: ");

        try {
            schelling = new DatenbankGateway();
            println("Erfolgreich!");

        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {

            Hauptmenu.starten();

        } catch (IOException e) {

            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void alle_artikel_ausgeben(DatenbankGateway db) throws SQLException {

        /**
         * Die Funktion bekommt ein Gateway übergeben und gibt alle
         * Artikel auf dem Bildschirm aus, die in der Datenbank vorhanden sind.
         */

        ResultSet antwort_von_datenbank = db.sql_befehl_ausfuehren("Select * From ARTIKEL");

        ResultSetMetaData antwort_metadaten = antwort_von_datenbank.getMetaData();

        int anzahl_spalten = antwort_metadaten.getColumnCount();

        String spaltenwert;

        while (antwort_von_datenbank.next()) {
            // while Loop durchläuft die Zeilen

            for (int i = 1; i <= anzahl_spalten; i++) {
                // for Loop durchläuft die Spalten

                if (i > 1) System.out.print(",  ");

                spaltenwert = antwort_von_datenbank.getString(i);

                System.out.print(spaltenwert + " " + antwort_metadaten.getColumnName(i));
            }

            System.out.println();
        }

    }


    public static void println(String out){

        System.out.println(out);
    }
}
