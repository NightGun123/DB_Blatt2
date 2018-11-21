import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Hauptmenu {

    /**
     * Benutzerinterface bietet Funktionen über die Konsole an.
     */

    static int menu_auswahl;

    public static void starten() throws IOException, SQLException {

        while(true) {

            System.out.println();
            System.out.println("1: Alle Artikel anzeigen");
            System.out.println("2: Alle Bestellungen anzeigen");
            System.out.println("3: Alle Kunden anzeigen");
            System.out.println("4: Artikel suchen");
            System.out.println("5: Bestellung suchen");
            System.out.println("6: artikel.dat importieren");
            System.out.println("7: Beenden");
            System.out.println();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            menu_auswahl = Integer.parseInt(in.readLine());

            switch (menu_auswahl) {

                case 1:

                    Main.alle_artikel_ausgeben(Main.schelling);
                    break;

                case 6:

                    System.out.print("CSV Datei Import: ");

                    ArtikelImport csvFileImporter =
                            new ArtikelImport("resources/artikel.dat",Main.schelling);

                    System.out.println("Erfolgreich!\nBeginne mit dem Insert");

                    csvFileImporter.starte_csv_import();

                    break;
            }
        }

    }
}
