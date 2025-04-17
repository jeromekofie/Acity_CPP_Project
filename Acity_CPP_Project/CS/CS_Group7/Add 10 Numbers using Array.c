

#include <stdio.h>

int main() {
    int arr[10];

    printf("Enter 10 elements:\n");
    for (int i = 0; i < 10; i++) {
        scanf("%d", &arr[i]);
    }

    printf("The elements are:\n");
    for (int i = 0; i < 10; i++) {
        printf("%d ", arr[i]);
    }

    printf("\n");
    return 0;
}






