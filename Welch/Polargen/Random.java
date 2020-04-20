//A program forrása: Bátfai Norbert, Juhász István: Javát tanítok könyv 57. ←􏰩 o.
public class Random { // Random osztály létrehozása boolean exist = true; // Van korábbi érték
    double value; // Az érték

    public Random() { // Konstruktor létrehozása exist = true;
    }

    public double get() { // Get függvény létrehozása
        if (exist) { // Ha létezik
            double u1, u2, v1, v2, w;
            do { // Az algoritmus kezdete
                u1 = Math.random();
                u2 = Math.random();
                v1 = 2 * u1 - 1;
                v2 = 2 * u2 - 1;
                w = v1 * v1 + v2 * v2;
            } while (w > 1);
            double r = Math.sqrt((-2 * Math.log(w) / w));
            value = r * v2;
            exist = !exist;
            return r * v1;
            // Az algoritmus vége
        } else {
            exist = !exist;
            return value;
        }
    }

    public static void main(String[] args) {
        Random g = new Random();
        for (int i = 0; i < 10; ++i) {
            System.out.println(g.get());
        }
    }
}