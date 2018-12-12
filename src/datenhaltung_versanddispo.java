import java.util.LinkedList;

public class datenhaltung_versanddispo {

    /**
     * Enthaelt die LinkedList fuer Bpd Objekte
     */

    protected LinkedList<Bpd> bpdispo;

    // Singleton
    private static datenhaltung_versanddispo instanz;
    private datenhaltung_versanddispo() {}

    public static datenhaltung_versanddispo getInstance() {
        return instanz;
    }



}
