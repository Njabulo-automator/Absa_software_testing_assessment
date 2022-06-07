package codeComprehension;

import java.util.Scanner;

public class Solution {
    // Complete the miniMaxSum function below.
    static void miniMaxSum(int[] arr) {
       int minimum = 0;
       for (int i =0; i<arr.length-1; i++){
           minimum = minimum + arr[i];

       }

       System.out.print("Minimum is equal to" + " "+ minimum);
        int maximum = 0;
        for (int j =1; j<arr.length; j++){
            maximum = maximum + arr[j];

        }
        System.out.print("\tand maximum is equal to" + " "+ maximum);

    }
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int[] arrItems = new int[5];

        for (int i = 0; i < arrItems.length; i++)
        {
            System.out.println("Please enter number");
            arrItems[i] = input.nextInt();
        }
        miniMaxSum(arrItems);
        scanner.close();
    }
}
