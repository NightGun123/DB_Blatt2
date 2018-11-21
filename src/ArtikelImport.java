import java.io.*;

public class ArtikelImport {

    /**
     * Klasse für den Import einer .csv Datei in eine SQL Datenbank
     */

    String pfad_zur_csv_datei;
    File csv_datei;
    DatenbankGateway datenbankGateway;
    String zeile_aus_csv;

    // Kunstruktor
    ArtikelImport(String pfad_zur_csv_datei, DatenbankGateway datenbankGateway){

        this.pfad_zur_csv_datei = pfad_zur_csv_datei;
        csv_datei = new File(this.pfad_zur_csv_datei);
        this.datenbankGateway = datenbankGateway;
    }

    public void starte_csv_import() {

        /**
         * .csv Datei wird Zeilenweise eingelesen und die gesamte Zeile
         * wird mit einem SQL Befehl über die Funktion sql_befehl_ausfuehren
         * in die SQL Datenbank geschrieben.
         */

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(csv_datei));

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        do{

        /**
         * Der Loop liest alle Zeilen aus der .csv Datei und speichert die Spaltenwerte, die
         * durch Semikolons getrennt sind, in ein String Array.
         *
         * Jedes String Array wird jeweils hergenommen um einen SQL Befehlsstring zu erstellen. Der SQL
         * Befehl wird dann mit der Funktion sql_befehl_ausfuehren ausgeführt.
         *
         * Die Variable zeile_aus_csv soll eine gesamte Zeile aus der .csv Datei aufnehmen (Klartext mit Semikolons).
         */

        try {

            zeile_aus_csv = in.readLine();

            if(zeile_aus_csv == null) break;

        } catch (IOException e) {

            e.printStackTrace();
            break;
        }

        String[] werte_der_zeile = zeile_aus_csv.split("\\;");

        String sql_zeile_einfuegen_befehlsstring =

                String.format(
                        "Insert Into ARTIKEL(artbez, mge, preis, kuehl,anzbo) " +
                        "values ('%s','%s',%s,'%s','%s')",
                        werte_der_zeile[1],
                        werte_der_zeile[2],
                        werte_der_zeile[3],
                        werte_der_zeile[4],
                        werte_der_zeile[5]
                );

        if(datenbankGateway.sql_befehl_ausfuehren(sql_zeile_einfuegen_befehlsstring) == null) {

            // Fehlermeldung bei welchem Artikel das Ausführen eines SQL Befehls fehlschlägt
            System.out.println("Fehler bei: " + werte_der_zeile[1]);
            continue;

        }

        System.out.println("Übergeben: " + werte_der_zeile[1]);

        } while (zeile_aus_csv != null);
    }
}
