import java.sql.ResultSet;
import java.sql.SQLException;

public class Artikel {

    /**
     * Artikel Objekt aus der SQL Datenbank
     */

    // Attribute
    int artnr;
    String artbez;
    String mge;
    double preis;
    String kuehl;
    int anzbo;

    // Konstruktor
    public Artikel(int artnr) throws SQLException {

        this.artnr = artnr;

        ResultSet artikel_set = Main.schelling.sql_befehl_ausfuehren("select * from artikel where artnr = " + artnr);

        try {
            artikel_set.next();
        } catch (SQLException e) {
            System.out.println("Fehler beim holen eines Artikels: " + e.getMessage());
        }

        artbez = artikel_set.getString(2);
        mge = artikel_set.getString(3);
        preis = artikel_set.getDouble(4);
        kuehl = artikel_set.getString(5);
        anzbo = artikel_set.getInt(6);

    }
}
