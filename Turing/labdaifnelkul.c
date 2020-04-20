#include <stdio.h>
#include <stdlib.h>
#include <curses.h>
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

    int xj = 0;
    int xk = 0;
    int yj = 0;
    int yk = 0;

    int mx;
    int my;

    for (;;)
    {
        getmaxyx(stdscr, my, mx);
        xj = (xj - 1) % mx; // -1
        xk = (xk + 1) % mx; // 1
        yj = (yj - 1) % my; // -1
        yk = (yk + 1) % my; // 1
        // clear() function is belongs to curses.h library (ncurses)
        // We are using it to clear the terminal's screen
        clear();
        mvprintw(abs((yj + (my - yk))), abs((xj + (mx - xk))), "O");
        // refresh() function is belongs to curses.h library (ncurses)
        // We are using it to get the actual output to the terminal
        refresh();
        // usleep() function is belongs to unistd.h libray
        // usleep(100000) is suspending the execution for 100000ms(=100s=1m40s) intervals
        usleep(100000);
    }

    return 0;
}