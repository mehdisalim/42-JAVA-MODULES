package fr.fortytwo.numbers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberWorkerTest {

    @ParameterizedTest(name = "valid inputs")
    @ValueSource(ints = { 2, 5, 7, 17, 239 })
    void isPrimeForPrimes(int number) {
        final NumberWorker numberWorker = new NumberWorker();
        assertTrue(numberWorker.isPrime(number));
    }

    @Test
    void isPrimeForNotPrimes() {
        final NumberWorker numberWorker = new NumberWorker();
        assertFalse(numberWorker.isPrime(6));
        assertFalse(numberWorker.isPrime(9));
        assertFalse(numberWorker.isPrime(45));
        assertFalse(numberWorker.isPrime(81));
        assertFalse(numberWorker.isPrime(189));
    }

    @ParameterizedTest(name = "invalid inputs")
    @ValueSource(ints = { 1, 0, -40, -7 })
    void isPrimeForIncorrectNumbers(int number) {
        final NumberWorker numberWorker = new NumberWorker();
        assertThrowsExactly(IllegalNumberException.class, () -> numberWorker.isPrime(number));
    }

    @ParameterizedTest(name = "digit sum")
    @CsvFileSource(resources = { "/data.csv" }, numLinesToSkip = 1, delimiter = ',')
    void testDigitSum(final int number, final int expectedValue) {
        final NumberWorker numberWorker = new NumberWorker();
        assertEquals(numberWorker.digitSum(number), expectedValue);
    }

}
