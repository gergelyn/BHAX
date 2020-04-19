// BHAX Myrmecologist
//
// Copyright (C) 2019
// Norbert Bátfai, batfai.norbert@inf.unideb.hu
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <https://www.gnu.org/licenses/>.
//
// https://bhaxor.blog.hu/2018/09/26/hangyaszimulaciok
// https://bhaxor.blog.hu/2018/10/10/myrmecologist
//

#ifndef ANTTHREAD_H
#define ANTTHREAD_H

#include <QThread>
#include "ant.h"

class AntThread : public QThread
{
    Q_OBJECT
    // Az AntThread osztály publikus tulajdonságai
public:
    // Konstruktor
    AntThread(Ants *ants, int ***grids, int width, int height,
              int delay, int numAnts, int pheromone, int nbrPheromone,
              int evaporation, int min, int max, int cellAntMax);
    // Destruktor
    ~AntThread();
    // A kezdés
    void run();
    // Ha vége, a futás legyen false
    void finish()
    {
        running = false;
    }
    // Álljon meg a hangya vagy ne
    void pause()
    {
        paused = !paused;
    }
    // A futást csekkoló funkció
    bool isRunnung()
    {
        return running;
    }
    // Az AntThread osztály privát tulajdonságai
private:
    // Boolean értékkel megy-e a hangya
    bool running{true};
    // Boolean értékkel áll-e a hangya
    bool paused{false};
    // A hangyák tuljadonságai
    Ants *ants;
    // Hangyákat tartalmazó cellák száma
    int **numAntsinCells;
    // Minimum és maximum
    int min, max;
    // A cella, hangyákat tartalmazó maximális száma
    int cellAntMax;
    // Feromon
    int pheromone;
    // Párolgás
    int evaporation;
    // Feromon szám
    int nbrPheromone;
    // Rácsok tulajdonságai
    int ***grids;
    // Szélesség
    int width;
    // Magasság
    int height;
    int gridIdx;
    // Késleltetés
    int delay;

    void timeDevel();

    int newDir(int sor, int oszlop, int vsor, int voszlop);
    void detDirs(int irany, int &ifrom, int &ito, int &jfrom, int &jto);
    int moveAnts(int **grid, int row, int col, int &retrow, int &retcol, int);
    double sumNbhs(int **grid, int row, int col, int);
    void setPheromone(int **grid, int row, int col);

signals:
    void step(const int &);
};

#endif
