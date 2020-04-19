/*
 * Sejtautomata.java
 *
 * DIGIT 2005, Javat tanitok
 * Batfai Norbert, nbatfai@inf.unideb.hu
 *
 */
/**
 * Sejtautomata osztaly.
 *
 * @author Batfai Norbert, nbatfai@inf.unideb.hu
 * @version 0.0.1
 */
public class Sejtautomata extends java.awt.Frame implements Runnable {
    // Elo sejt
    public static final boolean eLo = true;
    // Halott sejt
    public static final boolean HALOTT = false;
    // Két rácsot használunk, az egyik a sejtter állapotát jellemzi, ami a t_n
    // a másik a t_n+1, ami az adott idôpillanátban jellemzi
    protected boolean[][][] racsok = new boolean[2][][];
    // Valamelyik rácsra mutat, technikai jellegû, hogy ne kelljen a [2][][]-bôl az
    // elsô dimenziót használni, mert vagy az egyikre állítjuk, vagy a másikra.

    protected boolean[][] racs;
    // Az aktuális rács
    protected int racsIndex = 0;
    // A cella szélessége (pixel)
    protected int cellaSzelesseg = 20;
    // A cella magassága (pixel)
    protected int cellaMagassag = 20;
    // A sejttér szélessége
    protected int szelesseg = 20;
    // A sejttér magassága
    protected int magassag = 10;
    // A sejttér két egymást követô t_n es t_n+1 diszkrét idôpillanata közötti valós
    // idô.
    protected int varakozas = 1000;
    // Pillanatfelvetel keszitesehez
    private java.awt.Robot robot;
    // Pillanatfelvétel
    private boolean pillanatfelvetel = false;
    // A pillanatfelvételek számozásához használandó számláló
    private static int pillanatfelvetelSzamlalo = 0;

    /**
     * Létrehoz egy <code>Sejtautomata</code> objektumot.
     *
     * @param szelesseg a sejttér szélessége.
     * @param magassag  a sejttér szélessége.
     */
    public Sejtautomata(int szelesseg, int magassag) {
        this.szelesseg = szelesseg;
        this.magassag = magassag;
        // A két racs elkészítése
        racsok[0] = new boolean[magassag][szelesseg];
        racsok[1] = new boolean[magassag][szelesseg];
        racsIndex = 0;
        racs = racsok[racsIndex];
        // Halottra állítjuk a cellákat
        for (int i = 0; i < racs.length; ++i)
            for (int j = 0; j < racs[0].length; ++j)
                racs[i][j] = HALOTT;
        // élôlényeket helyezünk a cellákra
        // siklo(racs, 2, 2);
        sikloKilovo(racs, 5, 60);
        // Az ablak bezárásakor kilépünk a programból
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }
        });
        // A billentyűzetet figyelő event listenerek
        addKeyListener(new java.awt.event.KeyAdapter() {
            // A "K", "L", "N", "G", "S" gombok lenyomását figyelő event listerenek
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_K) {
                    // Cella szélességének felezése
                    cellaSzelesseg /= 2;
                    // Cella magasságának felezése
                    cellaMagassag /= 2;
                    setSize(Sejtautomata.this.szelesseg * cellaSzelesseg, Sejtautomata.this.magassag * cellaMagassag);
                    validate();
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_N) {
                    // Cella szélességének kétszerezése
                    cellaSzelesseg *= 2;
                    // Cella magasságának kétszerezése
                    cellaMagassag *= 2;
                    setSize(Sejtautomata.this.szelesseg * cellaSzelesseg, Sejtautomata.this.magassag * cellaMagassag);
                    validate();
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_S)
                    pillanatfelvetel = !pillanatfelvetel;
                else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_G)
                    varakozas /= 2;
                else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_L)
                    varakozas *= 2;
                repaint();
            }
        });
        // A kattintást figyelő event listenerek
        addMouseListener(new java.awt.event.MouseAdapter() {
            // A nagyítandó területet kattintással jelöljük ki
            public void mousePressed(java.awt.event.MouseEvent m) {
                // Az egérmutató X pozíciója
                int x = m.getX() / cellaSzelesseg;
                // Az egérmutató Y pozíciója
                int y = m.getY() / cellaMagassag;
                racsok[racsIndex][y][x] = !racsok[racsIndex][y][x];
                repaint();
            }
        });
        // Az egér mozgását figyelő event listenerek
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            // A négyzet kijelölése
            public void mouseDragged(java.awt.event.MouseEvent m) {
                // Az egérmutató X pozíciója
                int x = m.getX() / cellaSzelesseg;
                // Az egérmutató Y pozíciója
                int y = m.getY() / cellaMagassag;
                racsok[racsIndex][y][x] = eLo;
                repaint();
            }
        });
        // Kezdeti cellaméretek
        cellaSzelesseg = 10;
        cellaMagassag = 10;
        // Pillanatkép készítés
        try {
            robot = new java.awt.Robot(
                    java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
        } catch (java.awt.AWTException e) {
            e.printStackTrace();
        }
        // Az ablak adatai
        setTitle("Sejtautomata");
        setResizable(false);
        setSize(szelesseg * cellaSzelesseg, magassag * cellaMagassag);
        setVisible(true);
        // A sejttér készítése
        new Thread(this).start();
    }

    // A sejttér kirajzolása
    public void paint(java.awt.Graphics g) {
        // Az aktualis rácsot rajzoljuk ki
        boolean[][] racs = racsok[racsIndex];
        for (int i = 0; i < racs.length; ++i) {
            for (int j = 0; j < racs[0].length; ++j) {
                // Az élő cellák kirajzolása
                if (racs[i][j] == eLo)
                    g.setColor(java.awt.Color.BLACK);
                else
                    g.setColor(java.awt.Color.WHITE);
                g.fillRect(j * cellaSzelesseg, i * cellaMagassag, cellaSzelesseg, cellaMagassag);
                // A rács kirajzolása
                g.setColor(java.awt.Color.LIGHT_GRAY);
                g.drawRect(j * cellaSzelesseg, i * cellaMagassag, cellaSzelesseg, cellaMagassag);
            }
        }
        // Pillanatfelvétel
        if (pillanatfelvetel) {
            pillanatfelvetel = false;
            pillanatfelvetel(robot.createScreenCapture(new java.awt.Rectangle(getLocation().x, getLocation().y,
                    szelesseg * cellaSzelesseg, magassag * cellaMagassag)));
        }
    }

    /**
     * Az kérdezet állapotban lévő nyolcszomszédok száma.
     *
     * @param racs    a sejttér rács
     * @param sor     a rács sora
     * @param oszlop  a rács oszlopa
     * @param allapor a nyolcszomszédok
     * @return int a kérdezett állapotbeli nyolcszomszédok száma.
     */
    public int szomszedokSzama(boolean[][] racs, int sor, int oszlop, boolean allapot) {
        int allapotuSzomszed = 0;
        // A nyolcszomszédokon való végigmenés
        for (int i = -1; i < 2; ++i)
            for (int j = -1; j < 2; ++j)
                // A vizsgált sejt kivételével:
                if (!((i == 0) && (j == 0))) {
                    // A szomszédok a szélső oldalakon és
                    // a szembe oldalakon (ez a "periodikus határfeltétel")
                    int o = oszlop + j;
                    if (o < 0)
                        o = szelesseg - 1;
                    else if (o >= szelesseg)
                        o = 0;

                    int s = sor + i;
                    if (s < 0)
                        s = magassag - 1;
                    else if (s >= magassag)
                        s = 0;

                    if (racs[s][o] == allapot)
                        ++allapotuSzomszed;
                }

        return allapotuSzomszed;
    }

    /**
     * A sejtter idobeli fejlodese a John H. Conway fele eletjatek sejtautomata
     * szabalyai alapjan tortenik. A szabalyok reszletes ismerteteset lasd peldaul a
     * [MATEK JaTeK] hivatkozasban (Csakany Bela: Diszkret matematikai jatekok.
     * Polygon, Szeged 1998. 171. oldal.)
     */
    public void idoFejlodes() {

        boolean[][] racsElotte = racsok[racsIndex];
        boolean[][] racsUtana = racsok[(racsIndex + 1) % 2];

        for (int i = 0; i < racsElotte.length; ++i) {
            for (int j = 0; j < racsElotte[0].length; ++j) {

                int elok = szomszedokSzama(racsElotte, i, j, eLo);

                if (racsElotte[i][j] == eLo) {
                    /*
                     * elo elo marad, ha ketto vagy harom elo szomszedja van, kulonben halott lesz.
                     */
                    if (elok == 2 || elok == 3)
                        racsUtana[i][j] = eLo;
                    else
                        racsUtana[i][j] = HALOTT;
                } else {
                    /*
                     * Halott halott marad, ha harom elo szomszedja van, kulonben elo lesz.
                     */
                    if (elok == 3)
                        racsUtana[i][j] = eLo;
                    else
                        racsUtana[i][j] = HALOTT;
                }
            }
        }
        racsIndex = (racsIndex + 1) % 2;
    }

    // A fejlődő sejttér
    public void run() {

        while (true) {
            try {
                Thread.sleep(varakozas);
            } catch (InterruptedException e) {
            }

            idoFejlodes();
            repaint();
        }
    }

    /**
     * A sejtterbe "elolenyeket" helyezunk, ez a "siklo". Adott iranyban halad,
     * masolja magat a sejtterben. Az eloleny ismerteteset lasd peldaul a [MATEK
     * JaTeK] hivatkozasban (Csakany Bela: Diszkret matematikai jatekok. Polygon,
     * Szeged 1998. 172. oldal.)
     *
     * @param racs a sejttér ahova ezt az állatkát helyezzük
     * @param x    a befoglaló tégla bal felső sarkának oszlopa
     * @param y    a befoglaló tegla bal felső sarkának sora
     */
    public void siklo(boolean[][] racs, int x, int y) {

        racs[y + 0][x + 2] = eLo;
        racs[y + 1][x + 1] = eLo;
        racs[y + 2][x + 1] = eLo;
        racs[y + 2][x + 2] = eLo;
        racs[y + 2][x + 3] = eLo;

    }

    /**
     * A sejtterbe "elolenyeket" helyezunk, ez a "siklo agyu". Adott iranyban
     * siklokat lo ki. Az eloleny ismerteteset lasd peldaul a [MATEK JaTeK]
     * hivatkozasban /Csakany Bela: Diszkret matematikai jatekok. Polygon, Szeged
     * 1998. 173. oldal./, de itt az abra hibas, egy oszloppal told meg balra a bal
     * oldali 4 sejtes negyzetet. A helyes agyu rajzat lasd pl. az [eLET CIKK]
     * hivatkozasban /Robert T. Wainwright: Life is Universal./ (Megemlithetjuk,
     * hogy mindketto tartalmaz ket felesleges sejtet is.)
     *
     * @param racs a sejttér ahova ezt az allatkat helyezzuk
     * @param x    a befoglaló tégla bal felső sarkának oszlopa
     * @param y    a befoglaló tégla bal felső sarkának sora
     */
    public void sikloKilovo(boolean[][] racs, int x, int y) {

        racs[y + 6][x + 0] = eLo;
        racs[y + 6][x + 1] = eLo;
        racs[y + 7][x + 0] = eLo;
        racs[y + 7][x + 1] = eLo;

        racs[y + 3][x + 13] = eLo;

        racs[y + 4][x + 12] = eLo;
        racs[y + 4][x + 14] = eLo;

        racs[y + 5][x + 11] = eLo;
        racs[y + 5][x + 15] = eLo;
        racs[y + 5][x + 16] = eLo;
        racs[y + 5][x + 25] = eLo;

        racs[y + 6][x + 11] = eLo;
        racs[y + 6][x + 15] = eLo;
        racs[y + 6][x + 16] = eLo;
        racs[y + 6][x + 22] = eLo;
        racs[y + 6][x + 23] = eLo;
        racs[y + 6][x + 24] = eLo;
        racs[y + 6][x + 25] = eLo;

        racs[y + 7][x + 11] = eLo;
        racs[y + 7][x + 15] = eLo;
        racs[y + 7][x + 16] = eLo;
        racs[y + 7][x + 21] = eLo;
        racs[y + 7][x + 22] = eLo;
        racs[y + 7][x + 23] = eLo;
        racs[y + 7][x + 24] = eLo;

        racs[y + 8][x + 12] = eLo;
        racs[y + 8][x + 14] = eLo;
        racs[y + 8][x + 21] = eLo;
        racs[y + 8][x + 24] = eLo;
        racs[y + 8][x + 34] = eLo;
        racs[y + 8][x + 35] = eLo;

        racs[y + 9][x + 13] = eLo;
        racs[y + 9][x + 21] = eLo;
        racs[y + 9][x + 22] = eLo;
        racs[y + 9][x + 23] = eLo;
        racs[y + 9][x + 24] = eLo;
        racs[y + 9][x + 34] = eLo;
        racs[y + 9][x + 35] = eLo;

        racs[y + 10][x + 22] = eLo;
        racs[y + 10][x + 23] = eLo;
        racs[y + 10][x + 24] = eLo;
        racs[y + 10][x + 25] = eLo;

        racs[y + 11][x + 25] = eLo;

    }

    // Pillanatkép készítése.
    public void pillanatfelvetel(java.awt.image.BufferedImage felvetel) {
        // A pillanatkép kép neve
        StringBuffer sb = new StringBuffer();
        sb = sb.delete(0, sb.length());
        sb.append("sejtautomata");
        sb.append(++pillanatfelvetelSzamlalo);
        sb.append(".png");
        // png formátumú képet mentünk le
        try {
            javax.imageio.ImageIO.write(felvetel, "png", new java.io.File(sb.toString()));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Ne villogjon a felület
    public void update(java.awt.Graphics g) {
        paint(g);
    }

    // Létrehoz egy Conway-féle életjáték szabályos sejttér obektumot.
    public static void main(String[] args) {
        // 100 oszlop, 75 sor létrehozása
        new Sejtautomata(100, 75);
    }
}
