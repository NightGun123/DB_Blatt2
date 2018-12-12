import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    static DatenbankGateway schelling = null;

    public static void main(String args[]){

        // Init LinkedList
        datenhaltung_versanddispo.getInstance();

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
         * Gibt alle Artikel aus.
         */

        printRS(db.sql_befehl_ausfuehren("Select * From ARTIKEL"));
    }

    public static void alle_bestellungen_ausgeben(DatenbankGateway db) throws SQLException {

        /**
         * Gibt alle Bestellungen aus.
         */

        printRS(db.sql_befehl_ausfuehren("Select * From Bestellung"));
    }

    public static void bestellungen_status_1_ausgeben(DatenbankGateway db) throws SQLException {

        /**
         * Gibt alle Bestellungen mit Status 1 aus
         */

        printRS(db.sql_befehl_ausfuehren("Select * From Bestellung where status = 1"));
    }

    public static void alle_kunden_ausgeben(DatenbankGateway db) throws SQLException {

        /**
         * Gibt alle Kunden aus.
         */

        printRS(db.sql_befehl_ausfuehren("Select * From KUNDE"));
    }

    public static void zeige_stammdaten_und_bpos_von_artikel(

            String artikelnummer,DatenbankGateway db

    ) throws SQLException {

        /**
         * Bekommt eine Artikelnummer übergeben. Zu dieser Artikelnummer die Stammdaten des Artikels
         * ausgegeben und alle Bestelpositionen werden angezeigt.
         */

        printRS(db.sql_befehl_ausfuehren(
                "Select * From ARTIKEL a left join BPOS b on a.ARTNR=b.ARTNR WHERE a.artnr = " + artikelnummer)
        );
    }

    public static void bestellung_ausgeben(int BESTNR, DatenbankGateway datenbankGateway) throws SQLException {

        /**
         * Zu der übergebenen Bestellung werden alle Stammdaten ausgegeben. Es werden alle Bestelpositionen
         * ausgegeben. Es werden Die dazugehörenden Artikelbezeichnungen ausgegeben.
         */

        printRS(datenbankGateway.sql_befehl_ausfuehren(
                "Select b.*, bp.*, a.ARTBEZ From BESTELLUNG b left join BPOS bp " +
                        "on b.BESTNR=bp.BESTNR left join ARTIKEL a " +
                        "on bp.ARTNR=a.ARTNR where bp.bestnr=" +BESTNR));
    }

    public static void printRS(ResultSet R) throws SQLException {

        /**
         * Das übergebene ResultSet wird auf der Konsole ausgegeben.
         */

        ResultSetMetaData rsmd = R.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (R.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = R.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }

            System.out.println();
        }
    }



    public static void positionen_von_bestellung_ausgeben(int BESTNR, DatenbankGateway datenbankGateway) throws SQLException {

        /**
         * Zur übergebenen Bestellnummer werden alle Positionen auf der Konsole ausgegeben
         */

        ResultSet positionen_der_bestellung =
                datenbankGateway.sql_befehl_ausfuehren("Select POSNR From BPOS WHERE BESTNR = "+BESTNR);


        while(positionen_der_bestellung.next()) {

            int posnr = Integer.parseInt(positionen_der_bestellung.getString(1));

                gesamtpreis_position_SQL_update(posnr,datenbankGateway, false);
                gesamtpreis_position_SQL_update(posnr,datenbankGateway, true);
            }

        rech_summe_SQL_update(BESTNR,datenbankGateway);
    }

    public static void gesamtpreis_position_SQL_update(
            int BPOSNR, DatenbankGateway datenbankGateway, boolean silent) throws SQLException {

        /**
         * Bekommt eine Bestellpositionsnummer übergeben.
         */

        ResultSet artikelnummer_zu_bpos
                = datenbankGateway.sql_befehl_ausfuehren("Select ARTNR From BPOS WHERE POSNR = "+BPOSNR);

        artikelnummer_zu_bpos.next();

        // Artikelnummer der übergebenen Bestellposition speichern
        String ARTNR = artikelnummer_zu_bpos.getString(1);

        if (silent == false) {
            println("DIE ARTNR lautet:"+ARTNR);
        }

        // Preis des gefundenen Artikels abfragen
        ResultSet preis_von_artikel =
                datenbankGateway.sql_befehl_ausfuehren("Select PREIS From ARTIKEL WHERE ARTNR = "+ARTNR);

        preis_von_artikel.next();

        // Ermittelten Preis speichern
        String APREIS = preis_von_artikel.getString(1);

        if (silent == false) {
            println("Der Preis  lautet:"+APREIS);
        }

        // Preis berechnen und SQL Tabelle updaten
        datenbankGateway.sql_befehl_ausfuehren("UPDATE BPOS set WERT = (MENGE*"+APREIS+") WHERE POSNR ="+BPOSNR);

    }

    public static void rech_summe_SQL_update(int BESTNR, DatenbankGateway datenbankGateway) throws SQLException {

        /**
         * Zu gegebener Bestellnummer werden alle Einträge aus Bestellpositionen
         * ermittelt und deren Preise addiert und im Datensatz der Bestellung gespeichert.
         */

        // Summe der Preise aller Bestellpositionen mit der übergebenen Bestellnummer ermitteln
        ResultSet summe_aus_bpositionen =
                datenbankGateway.sql_befehl_ausfuehren("Select SUM(WERT) as Summe From BPOS WHERE BESTNR = "+BESTNR);

        summe_aus_bpositionen.next();

        // Wert speichern
        String Summe = summe_aus_bpositionen.getString(1);

        // Gesamtpreis eintragen und Status der Bestellung und Status wird auf 'geplant' gesetzt
        datenbankGateway.sql_befehl_ausfuehren("UPDATE BESTELLUNG set RSUM = "+Summe+" WHERE BESTNR = "+BESTNR);
        datenbankGateway.sql_befehl_ausfuehren("UPDATE BESTELLUNG set status = 1 WHERE BESTNR = "+BESTNR);
    }

    public static String bestellung_eingeben(String kundennummer, String best_datum, DatenbankGateway datenbankGateway)
            throws SQLException {

        /**
         * Bekommt eine Kundennummer übergeben und ein Datum. Legt dann in der Tabelle Bestellungen
         * eine neue Zeile an. Die Bestellnummer wird per Autoimkrement bestimmt. Der Status wird auf 1 gesetzt.
         * In die Spalte BESTDAT wird der Parameter best_datum eingetragen, in die Spalte KNR der Parameter
         * kundennummer.
         *
         * Positionsnummer hat Autoinkrement in SQL
         *
         * Rückgabe = BESTNR neuer Eintrag
         */

        String insert_befehl = new String(

                "INSERT INTO bestellung (KNR, STATUS, BESTDAT) " +
                        "VALUES (" + kundennummer + ", 1, '" + best_datum + "')"
        );

        // Insert ausführen
        datenbankGateway.sql_befehl_ausfuehren(insert_befehl);

        // ResultSet holen von hinzugefügten Eintrag
        ResultSet hinzugef_eintrag = resultset_holen_letzter_eintrag_bestellungen(datenbankGateway);

        hinzugef_eintrag.next();
        return hinzugef_eintrag.getString(1);
    }

    public static ResultSet bestellposition_eingeben(
            DatenbankGateway datenbankGateway, String artikelnummer, String bestellnummer,String menge)
            throws SQLException {

        /**
         * Fügt der übergebenen Bestellung einen Artikel in angegebener Menge als Bestelposition hinzu.
         *
         * Ruft Methode gesamtpreis_position_SQL_update auf, um den Gesamtwert der hinzugefügten Position in
         * SQL upzudaten.
         */

        // Befehlelsstring erstellen: Neuen Eintrag in Tabelle BPOS schreiben
        String insert_befehl =  new String(

                "INSERT INTO bpos (artnr, bestnr, menge) " +
                        "VALUES (" + artikelnummer + " ," + bestellnummer + ", " + menge + ")"

        );

        // Befehl ausführen
        datenbankGateway.sql_befehl_ausfuehren(insert_befehl);

        ResultSet letzter_eintrag_bestellpos = resultset_holen_letzter_eintrag_bestellpos(datenbankGateway);

        letzter_eintrag_bestellpos.next();

        gesamtpreis_position_SQL_update(
                Integer.parseInt(letzter_eintrag_bestellpos.getString(1)), datenbankGateway, false);

        return letzter_eintrag_bestellpos;
    }

    public static void bestellvorgang_starten(DatenbankGateway datenbankGateway) throws IOException, SQLException {

        /**
         * Starten den Bestellvorgang.
         *
         * Eine Bestellung eingeben und beliebig viele Positionen eingeben.
         */

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String datum;
        String kundennr;
        String bestellnummer;
        String eingabebuffer;
        String artikelnummer;
        String menge;
        int menuauswahl;

        println("Kundennummer eingeben:");
        kundennr = in.readLine();

        println("Datum eingeben DD.MM.YYYY : ");
        datum = in.readLine();

        // Bestellung anlegen und Bestellnummer speichern
        bestellnummer = bestellung_eingeben(kundennr, datum, datenbankGateway);

        // Eingabe Bestellpositionen

        while (true) {

            println("1: BestPos anlegen");
            println("2: Beenden");

            menuauswahl = Integer.parseInt(in.readLine());

            switch (menuauswahl) {

                case 1:

                    println("Artikelnummer eingeben: ");
                    artikelnummer = in.readLine();

                    println("Menge eingeben: ");
                    menge = in.readLine();

                    bestellposition_eingeben(datenbankGateway, artikelnummer, bestellnummer, menge);

                    break;

                case 2:

                    return;

                default:

                    return;
            }

        }

    }

    public static ResultSet resultset_holen_letzter_eintrag_bestellungen(DatenbankGateway datenbankGateway) {

        /**
         * Liefert den letzten Eintrag der Tabelle Bestellungen zurück.
         */


        return datenbankGateway.sql_befehl_ausfuehren(
                "SELECT * FROM bestellung WHERE bestnr=(SELECT max(bestnr) FROM bestellung)"
        );
    }

    public static ResultSet resultset_holen_letzter_eintrag_bestellpos(DatenbankGateway datenbankGateway) {

        /**
         * Liefert den letzten Eintrag der Tabelle Bestellpositionen zurück.
         */


        return datenbankGateway.sql_befehl_ausfuehren(
                "SELECT * FROM bpos WHERE posnr=(SELECT max(posnr) FROM bpos);"
        );
    }


    public static void println(Object out){

        System.out.println(out);
    }

    public static void bpdispo_erstellen(int BESTNR) throws SQLException {

        /**
         * LinkedList fuellen
         */

        // Bestellpositionen zu BESTNR holen
        ResultSet alle_BPOS = schelling.sql_befehl_ausfuehren("select * from bpos where bestnr = " + BESTNR);

        while (alle_BPOS.next()) {

            // TODO loop wird nie betreten

            Bpd tmp_bpd = new Bpd();

            tmp_bpd.posnr = alle_BPOS.getInt(1);
            tmp_bpd.artnr = alle_BPOS.getInt(2);
            tmp_bpd.menge = alle_BPOS.getInt(4);

            Artikel tmp_art = new Artikel(tmp_bpd.artnr);
            tmp_bpd.kuehl = tmp_art.kuehl;
            tmp_bpd.artbez = tmp_art.artbez;
            tmp_bpd.anzbo = tmp_art.anzbo;

            // ALGRAD berechnen lassen
            tmp_bpd.algrad_berechnen();

            datenhaltung_versanddispo.getInstance().bpdispo.add(tmp_bpd);
        }

        printLinkedList(datenhaltung_versanddispo.getInstance().bpdispo);
    }

    public static void printLinkedList(LinkedList<Bpd> liste) {

        /**
         * Die übergebene LinkedList wird auf der Konsole ausgegeben.
         */

        Iterator<Bpd> it = datenhaltung_versanddispo.getInstance().bpdispo.iterator();

        while (it.hasNext()) {

            it.next().printOut();
        }
    }
}
