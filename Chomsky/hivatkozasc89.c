#include <stdio.h>

int main()
{
    int n = 0;
    for (int i = 10; n <= i; n++) // Mivel az i változó deklarációja a for cilus fejében történt, így C89-ben nem fog lefordulni, míg C99-ben igen
    {
        printf("%d\n", n);
    }
    return 0;
}
