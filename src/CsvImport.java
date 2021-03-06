import java.io.*;
import java.sql.SQLException;

public class CsvImport {
    String pathToFile;
    File myFile;
    DatenbankGateway gt;

    CsvImport(String tmppath,DatenbankGateway gt){

        pathToFile = tmppath;
        myFile = new File(pathToFile);
        this.gt = gt;
    }

    public void readCVSandInsert(){
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(myFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String idxLine = null;
        try {
            idxLine = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (idxLine != null){

            String[] fn = idxLine.split("\\;");
            //'Insert Into ARTIKEL(artbez, mge, preis, kuehl,anzbo) values ('+ +')'
            //tempidxlist.add(new indexlist(fn[0],Integer.parseInt(fn[1])));
            String sql = String.format("Insert Into ARTIKEL(ARTBEZ, MGE, PREIS, KUEHL,ANZBO) values ('%s','%s',%s,'%s',%s)",fn[1],fn[2],fn[3],fn[4],fn[5]);
            if(gt.sql_befehl_ausfuehren(sql)== null)
                System.out.println("Fehler bei:: "+fn[1]);
            else
                System.out.println("Added: "+fn[1]);

            try {
                idxLine = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
