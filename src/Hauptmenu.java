import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Hauptmenu {

    /**
     * Benutzerinterface bietet Funktionen über die Konsole an.
     */

    static int menu_auswahl;
    static String benutzereingabe_buffer;

    static BufferedReader in
            = new BufferedReader(new InputStreamReader(System.in));

    public static void starten() throws IOException, SQLException {


        while(true) {

            System.out.println();
            System.out.println("1: Alle Artikel anzeigen");
            System.out.println("2: Alle Bestellungen anzeigen");
            System.out.println("3: Alle Kunden anzeigen");
            System.out.println("4: Artikel suchen");
            System.out.println("5: Bestellung suchen");
            System.out.println("6: artikel.dat importieren");
            System.out.println("7: Bestellung berechnen und ausgeben");
            System.out.println("8: Bestellung eingeben");
            System.out.println("9: Versandplanung starten");
            System.out.println("0: Beenden");
            System.out.println();


            try {

                /**
                 * Benutzereingabe Menüauswahl
                 */

                menu_auswahl = Integer.parseInt(in.readLine());

            } catch (NumberFormatException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }

            switch (menu_auswahl) {

                case 1:

                    Main.alle_artikel_ausgeben(Main.schelling);
                    break;

                case 2:

                    Main.alle_bestellungen_ausgeben(Main.schelling);
                    break;

                case 3:

                    Main.alle_kunden_ausgeben(Main.schelling);
                    break;

                case 4:

                    System.out.println("Artikelnummer eingeben:");

                    benutzereingabe_buffer = in.readLine();

                    Main.zeige_stammdaten_und_bpos_von_artikel(benutzereingabe_buffer, Main.schelling);

                    break;

                case 5:

                    System.out.println("Bestellnummer eingeben:");

                    benutzereingabe_buffer = in.readLine();

                    Main.bestellung_ausgeben(Integer.parseInt(benutzereingabe_buffer), Main.schelling);

                    break;

                case 6:

                System.out.print("CSV Datei Import: ");

                ArtikelImport csvFileImporter =
                            new ArtikelImport("resources/artikel.dat",Main.schelling);

                System.out.println("Erfolgreich!\nBeginne mit dem Insert");

                csvFileImporter.starte_csv_import();

                break;


                case 7:

                    System.out.println("Bestellnummer eingeben:");

                    benutzereingabe_buffer = in.readLine();

                    Main.positionen_von_bestellung_holen(Integer.parseInt(benutzereingabe_buffer), Main.schelling);

                    break;

                case 8:

                    Main.bestellvorgang_starten(Main.schelling);

                    break;

                case 9:

                    Main.bestellungen_status_1_ausgeben(Main.schelling);

                    System.out.println("Bestellung auswäheln:");
                    benutzereingabe_buffer = in.readLine();

                    break;

                case 0:

                    Main.schelling.close();
                    System.exit(0);
                    break;

                default:

                    continue;

            }
        }

    }
}
