#include <stdio.h>
#include <math.h>
// A kiir függvény bekér egy lebegôpont típusú tömböt (tomb)
// és egy egész tipusú változó (db = darab)
void kiir(double tomb[], int db)
{
    // Deklarálunk egy i változót, a for ciklushoz
    int i;
    // A for ciklusban megadjuk, hogy addig fusson a loop
    // míg el nem éri a db értékét
    for (i = 0; i < db; i++)
    {
        // Minden egyes ciklusban kiíratjuk az adott i oldal rankját
        // az adatot a tömb i-edik tagja adja
        printf("PageRank[%d]: %fl\n", i, tomb[i]);
    }
}

// A tavolsag függvény lebegôpontos értéket fog vissza adni
// bekér egy PR és egy PRv nevû, lebegôpont tipusú tömböt
// és egy egész tipusú változót (db = darab)
double tavolsag(double PR[], double PRv[], int db)
{
    // Deklarálunk egy i változót, a for ciklushoz
    int i;
    // Deklarálunk és inicializáljuk 0.0-val a lebegôpontos tipusú osszeg változót
    double osszeg = 0.0;
    // A for ciklusban megadjuk, hogy addig fusson a loop
    // míg el nem éri a db értékét
    for (i = 0; i < db; i++)
    {
        // Minden egyes ciklusban az osszeg változó értékéhez adjuk
        // kétszer a PRv tömb i-edik tagjának értékébôl kivont PR tömb i-edik tagjának értékét
        osszeg += (PRv[i] - PR[i]) * (PRv[i] - PR[i]);
    }
    // A for ciklus után (ha i elérte a db ot), a függvény az osszeg változó értékének négyzetét adja vissza
    return sqrt(osszeg);
}

int main()
{
    double L[4][4] = {
        {0.0, 0.0, 1.0 / 3.0, 0.0},
        {1.0, 1.0 / 2.0, 1.0 / 3.0, 1.0},
        {0.0, 1.0 / 2.0, 0.0, 0.0},
        {0.0, 0.0, 1.0 / 3.0, 0.0}};

    double PR[4] = {0.0, 0.0, 0.0, 0.0};
    double PRv[4] = {1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0, 1.0 / 4.0};
    int i, j;

    for (;;)
    {
        // for ciklus i változóval, addig fut amíg i kisebb mint 4 és i növekszik 1-gyel
        for (i = 0; i < 4; i++)
        {
            // a PR tömb i-edik eleme = 0.0-val
            PR[i] = 0.0;
            // Újabb for ciklus ugyanezen az elven csak j változóval
            for (j = 0; j < 4; j++)
            {
                // a PR tömb i-edik eleméhez (ami kezdetben mindig 0.0), adja hozzá
                // az L tömb i-edik sor, j-edik oszlopának értéke szorozva a PRv tömb j-edik elemével
                PR[i] += (L[i][j] * PRv[j]);
            }
        }

        // Hívjuk a tavolsag függvényt a PR, PRv tömbökkel és db = 4 -gyel
        // Ezekkel az adatokkal, ha az osszeg negyzete kisebb mint 0.0000000001...
        if (tavolsag(PR, PRv, 4) < 0.0000000001)
        {
            // ... akkor álljon le a program
            break;
        }

        // for ciklus i változóval, addig fut amíg i kisebb mint 4 és i növekszik 1-gyel
        for (i = 0; i < 4; i++)
        {
            // a PRv tömb i-edik elemének értéke legyen egyenlô a PR tömb i-edik eleméjével
            PRv[i] = PR[i];
        }
    }

    // Hívjuk a kiir függvényt a PR tömbbel és 4 db-al
    kiir(PR, 4);

    return 0;
}