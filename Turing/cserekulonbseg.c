#include <stdio.h>

int main()
{
    int a = 1;
    int b = 2;

    printf("Csere elôtt a = %d, b = %d\n", a, b);

    a = a + b; // a = 1 + 2 = 3
    b = a - b; // b = 3 - 2 = 1
    a = a - b; // a = 3 - 1 = 2

    printf("Csere után a = %d, b = %d\n", a, b);

    return 0;
}