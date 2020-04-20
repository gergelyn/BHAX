#include <stdio.h>
// curses.h library is a terminal control library. We are using ncurses (new curses) the third and latest version of it
#include <curses.h>
// unistd.h library contains system call functions, like usleep()
#include <unistd.h>

int main(void)
{
    // initscr() function is belongs to curses.h library (ncurses)
    // We are using it to determine the terminal's type and to initialize all curses data structures
    // stdscr is created by call this function
    initscr();
    // noecho() function belongs to curses.h library
    // Don't echo any keypresses
    noecho();
    // curs_set() function belongs to curses.h library
    // Don't display a cursor
    curs_set(FALSE);
    // Starting x coordinate
    int x = 0;
    // Starting y coordinate
    int y = 0;

    // Step
    int xstep = 1;
    // Step
    int ystep = 1;

    // max-width
    int mx;
    // max-height
    int my;

    for (;;)
    {
        // clear() function is belongs to curses.h library (ncurses)
        // We are using it to clear the terminal's screen
        clear();
        // getmaxyx defines the screen's maximum height(y) and width(x) by
        // passing 3 variables (the screen, the empty maxY variable and the maxX variable)
        // and then set these variables to their value by the getmaxy() and getmaxx() functions
        getmaxyx(stdscr, my, mx);

        // myprintw() function is printing the "O" to the terminal's screen by the x and y coordinates
        mvprintw(y, x, "O");

        // refresh() function is belongs to curses.h library (ncurses)
        // We are using it to get the actual output to the terminal
        refresh();
        // usleep() function is belongs to unistd.h libray
        // usleep(100000) is suspending the execution for 100000ms(=100s=1m40s) intervals
        usleep(100000);

        x = x + xstep;
        y = y + ystep;

        // if the "O" is at the right side
        if (x >= mx - 1)
        {
            xstep = xstep * -1;
        }
        // if the "O" is at the left side
        if (x <= 0)
        {
            xstep = xstep * -1;
        }
        // if the "O" is on top
        if (y <= 0)
        {
            ystep = ystep * -1;
        }
        // if the "0" is at the bottom
        if (y >= my - 1)
        {
            ystep = ystep * -1;
        }
    }

    return 0;
}