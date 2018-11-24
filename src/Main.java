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



    public static void positionen_von_bestellung_holen(int BESTNR, DatenbankGateway datenbankGateway) throws SQLException {

        /**
         * Zur übergebenen Bestellnummer werden alle Positionen zurückgegeben als ResultSet
         */

        ResultSet positionen_der_bestellung =
                datenbankGateway.sql_befehl_ausfuehren("Select POSNR From BPOS WHERE BESTNR = "+BESTNR);

        while(positionen_der_bestellung.next()) {

            int posnr = Integer.parseInt(positionen_der_bestellung.getString(1));

            gesamtpreis_position_SQL_update(posnr,datenbankGateway);
        }

        rech_summe_SQL_update(BESTNR,datenbankGateway);
    }

    public static void gesamtpreis_position_SQL_update(int BPOSNR, DatenbankGateway datenbankGateway) throws SQLException {

        /**
         * Bekommt eine Bestellpositionsnummer übergeben.
         */

        ResultSet artikelnummer_zu_bpos
                = datenbankGateway.sql_befehl_ausfuehren("Select ARTNR From BPOS WHERE POSNR = "+BPOSNR);

        artikelnummer_zu_bpos.next();

        // Artikelnummer der übergebenen Bestellposition speichern
        String ARTNR = artikelnummer_zu_bpos.getString(1);

        println("DIE ARTNR lautet:"+ARTNR);

        // Preis des gefundenen Artikels abfragen
        ResultSet preis_von_artikel =
                datenbankGateway.sql_befehl_ausfuehren("Select PREIS From ARTIKEL WHERE ARTNR = "+ARTNR);

        preis_von_artikel.next();

        // Ermittelten Preis speichern
        String APREIS = preis_von_artikel.getString(1);

        println("Der Preis  lautet:"+APREIS);

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

    public static void bestellung_eingeben(String kundennummer, String best_datum, DatenbankGateway datenbankGateway) {

        /**
         * Bekommt eine Kundennummer übergeben und ein Datum. Legt dann in der Tabelle Bestellungen
         * eine neue Zeile an. Die Bestellnummer wird per Autoimkrement bestimmt. Der Status wird auf 1 gesetzt.
         * In die Spalte BESTDAT wird der Parameter best_datum eingetragen, in die Spalte KNR der Parameter
         * kundennummer.
         *
         * Anschließend wird eine Funktion zur Eingabe von Bestellpositionen aufgerufen.
         *
         * Positionsnummer hat Autoinkrement in SQL
         */

        String insert_befehl = new String(

                "INSERT INTO bestellung (KNR, STATUS, BESTDAT) " +
                        "VALUES (" + kundennummer + ", 1, " + best_datum + ")"
        );

        ResultSet neue_bestellung = datenbankGateway.sql_befehl_ausfuehren(insert_befehl);
    }

    public static void bestellpositions_eingeben(String artikelnummer, String bestellnummer,String menge) {

        /**
         * Fügt der übergebenen Bestellung einen Artikel in angegebener Menge hinzu.
         */



    }


    public static void println(String out){

        System.out.println(out);
    }
}
