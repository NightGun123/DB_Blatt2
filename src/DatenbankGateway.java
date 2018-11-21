import java.sql.* ;  // for standard JDBC programs
//import oracle.jdbc.pool.*;
// Download Links für jdbc File:
// https://www.oracle.com/technetwork/database/application-development/jdbc/downloads/jdbc-ucp-183-5013470.html

public class DatenbankGateway {

    Driver myDriver;
    //DriverManager;
    Connection conn;

    DatenbankGateway() throws SQLException {
        //TODO Passwort setzen

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }

//        try {
//            myDriver = new oracle.jdbc.driver.OracleDriver();
//            DriverManager.registerDriver( myDriver );
//        }
//        catch(ClassNotFoundException ex) {
//            System.out.println("Error: unable to load driver class!");
//            System.exit(1);
//        }
        String URL = "jdbc:oracle:thin:dnprak12/jebe2018@schelling.nt.fh-koeln.de:1521:xe";
        conn = DriverManager.getConnection(URL);
    }
    public boolean InsertSQL(String sql) throws SQLException {
        Statement st = conn.createStatement();

        //Example: "Update Employees SET age = ? WHERE id = ?";
        try{
            st.executeQuery(sql);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    public ResultSet SelectSQL(String sql){
        ResultSet rs = null;

        try{
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
        }catch(Exception e){
            return rs;
        }
        return rs;

    }


    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
