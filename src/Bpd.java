public class Bpd {

    /**
     * Die Klasse Bpd enspricht jeweils einer BPOS einer ausgewaehlten Bestellung
     */

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
    }

    // Methoden
    public int algrad_berechnen() {

        /**
         * Berechnet Wert für algrad und gibt dieses zurück.
         */

        this.algrad = ((int) (this.menge * 100) / this.anzbo) + 1;
        return this.algrad;
    }

    public void printOut() {

        /**
         * Konsolenausgabe aller Attribute
         */

        String ausgabe = new String(

                "POSNR: " + posnr +
                        " KUEHL: " + kuehl +
                        " ARTNR: " + artnr +
                        " ARTBEZ: " + artbez +
                        " ANZBO: " + anzbo +
                        " MENGE: "  + menge +
                        " ALGRAD: " + algrad +
                        " VERPACKT: " + verpackt
        );

        System.out.println(ausgabe);
    }
}
