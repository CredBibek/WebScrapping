package org.example;


import java.util.Scanner;


public class Sum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = {1, 2, 3, 4, 5};
        numbers[0] = scanner.next().charAt(0);

        numbers[3] = scanner.next().charAt(0);

        int sum = 0;
        for(int number : numbers) {
            sum += number;
        }

        System.out.println("The sum is: " + sum);
    }
}
