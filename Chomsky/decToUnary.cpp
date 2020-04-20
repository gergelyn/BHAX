#include <iostream>
using namespace std;
int DecimalToUnary(int x)
{
    for (int i = 0; i < x; i++)
    {
        cout << 1;
    }

    return 0;
}
int main()
{
    int a;
    cout << "Decimalis szam: \n";
    cin >> a;
    DecimalToUnary(a);
}