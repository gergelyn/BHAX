#include <iostream>

using namespace std;

int main()
{
    int a = 1; // 00000001
    int i = 0;

    while (a != 0) // 00000000
    {
        a <<= 1;
        i++;
    }
    // i = 32 = 00100000
    cout << "Lepesek szama: " << i << "\n";
}