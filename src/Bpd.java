public class Bpd {

    /**
     * Die Klasse Bpd enspricht jeweils einer BPOS einer ausgewaehlten Bestellung
     */

    // TODO Beschreibung

    // Attribute
    protected int posnr;
    protected String kuehl;
    protected int artnr;
    protected String artbez;
    protected int anzbo;
    protected int menge;
    protected int algrad;
    protected boolean verpackt;

    // Standard Konstruktor
    public Bpd(){};

    // Konstruktor
    public Bpd(int posnr, String kuehl, int artnr, String artbez, int anzbo, int menge) {

        this.posnr = posnr;
        this.kuehl = kuehl;
        this. artnr = artnr;
        this.artbez = artbez;
        this.anzbo = anzbo;
        this.menge = menge;
        this.verpackt = false;
        this.algrad = 0;
    }

    // Methoden
    public void algrad_berechnen() {

        this.algrad = ((int) (this.menge * 100) / this.anzbo) + 1;
    }
}
