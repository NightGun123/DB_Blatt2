import java.sql.*;

public class DatenbankGateway {

    /**
     * Klasse DatenbankGateway
     *
     * Läd die Oracle JDBC Treiber und stellt eine Verbindung zu Schelling her.
     * Bietet Schnittstellenfunktionen an.
     */

    Connection conn;

    // Konstruktor
    DatenbankGateway() throws SQLException {

        /**
         * Der Kunstruktor läd die Treiber und stellt die Verbindung zu Schelling her.
         */

        // Oracle JDBC Treiber laden:
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }

        // Verbindung zur SQL DB herstellen:
        conn = DriverManager.getConnection("jdbc:oracle:thin:dbprak12/jebe2018@schelling.nt.fh-koeln.de:1521:xe");
    }

    public ResultSet sql_befehl_ausfuehren (String sql){

        /**
         * SQL Befehl wird als String Parameter entgegen genommen und dann an die SQL Datenbank weiter
         * gereicht. Die Funktion gibt das ResultSet als Rückgabewert zurück. Falls die Ausführung des SQL Befehls
         * fehl schlägt, gibt die Funktion 'null' zurück.
         */

        ResultSet rs;

        try{

            Statement st = conn.createStatement();

            rs = st.executeQuery(sql);

        }catch(Exception e){

            Main.println("Fehler beim Ausführen eines SQL Befehls!");
            return null;
        }

        return rs;
    }


    public void close(){

        /**
         * Kappt die Verbindung zu Schelling.
         */

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
