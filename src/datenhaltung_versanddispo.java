import java.util.LinkedList;

public class datenhaltung_versanddispo {

    /**
     * Enthaelt die LinkedList fuer Bpd Objekte
     */

    public LinkedList<Bpd> bpdispo;

    // Singleton
    private static datenhaltung_versanddispo instanz;
    private datenhaltung_versanddispo() {
        this.bpdispo = new LinkedList<Bpd>();
    }

    public static datenhaltung_versanddispo getInstance() {

        if(instanz == null) instanz = new datenhaltung_versanddispo();

        return instanz;
    }



}
