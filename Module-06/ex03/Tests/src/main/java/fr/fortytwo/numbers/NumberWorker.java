package fr.fortytwo.numbers;

public class NumberWorker {

    public boolean isPrime(int number) {
        if (number < 2) {
            throw new IllegalNumberException();
        }

        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;

    }

    public int digitSum(int number) {
        if (number == 0)
            return number;
        final int remain = number % 10;
        return digitSum(number / 10) + remain;
    }

}
