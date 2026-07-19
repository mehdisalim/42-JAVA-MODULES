import java.util.Scanner;

public class Program {

	final static int NUMBER_OF_CHART_VALUES = 4;
	final static int CHAR_INDEX = 0;
	final static int REPETATION_INDEX = 1;
	final static int ROUNDER_INDEX = 2;
	final static int IS_PRINTED_INDEX = 3;

	public static void main(String[] args) {
		final Scanner scan = new Scanner(System.in);
		System.out.print("-> ");
		if (!scan.hasNextLine()) {
			scan.close();
			return ;
		}
		final String input = scan.nextLine();
		final int inputLength = input.length();
		if (inputLength == 0) {
			scan.close();
			return ;
		}
		final char[] chars = input.toCharArray();

		countChars(chars);
		scan.close();


	}

	private static void countChars(final char[] chars) {

		final int[] charCounter = new int[65535];
		for (int i = 0; i < chars.length; i++) {
			charCounter[chars[i]]++;
		}

		int numberOfNonZeroChars = 0;
		for (int i = 0; i < charCounter.length; i++) {
			if (charCounter[i] > 0) {
				numberOfNonZeroChars++;
			}
		}
		if (numberOfNonZeroChars < 1) {
			System.exit(0);
		}

		final int[][] chartValues = new int[numberOfNonZeroChars][NUMBER_OF_CHART_VALUES];

		for (int i = 0, newCharCounterIndex = 0; i < charCounter.length; i++) {
			if (charCounter[i] < 1)
				continue ;
			setChartValues(chartValues[newCharCounterIndex], i, charCounter[i], 0, 0);
			newCharCounterIndex++;
		}
		sortChartValues(chartValues); 

		calculateTheChartHeight(chartValues);

		printTheChart(chartValues);

	}


	private static void sortChartValues(final int[][] chartValues) {
		for (int i = 0; i < chartValues.length; i++) {
			for (int j = i; j < chartValues.length; ++j) {
				if (chartValues[i][REPETATION_INDEX] < chartValues[j][REPETATION_INDEX]) {
					swapChartValues(chartValues[i], chartValues[j]);
				} else if (chartValues[i][REPETATION_INDEX] == chartValues[j][REPETATION_INDEX] && chartValues[i][CHAR_INDEX] > chartValues[j][CHAR_INDEX]) {
					swapChartValues(chartValues[i], chartValues[j]);
				}
			}
		}
	}


	static private void calculateTheChartHeight(final int[][] sortedChartValues) {
		final int maxChartValue = sortedChartValues[0][REPETATION_INDEX];
		if (maxChartValue == 0) {
			System.err.println("IllegalArgument");
			System.exit(1);
		}
		for (int i = 0; i < sortedChartValues.length && i < 10; i++) {
			sortedChartValues[i][ROUNDER_INDEX] = ((sortedChartValues[i][REPETATION_INDEX] * 10 ) / maxChartValue) + 1;
		}
	}

	static private void printTheChart(final int[][] chartValues) {
		System.out.println();
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < chartValues.length && j < 10; j++) {
				final int[] value = chartValues[j];
				if (i + value[ROUNDER_INDEX] == 11 && value[IS_PRINTED_INDEX] == 0) {
					value[IS_PRINTED_INDEX] = (value[REPETATION_INDEX] + "").length();
					System.out.print("  " + value[REPETATION_INDEX]);
					value[ROUNDER_INDEX]--;
					continue ;
				}
				if (i + value[ROUNDER_INDEX] == 11) {
					
					System.out.print("  ");
					for (int s = 0; s < value[IS_PRINTED_INDEX] - 1; s++) {
						System.out.print(" ");
					}
					System.out.print("#");
					value[ROUNDER_INDEX]--;
				}
			}
			System.out.println();
		}
		for (int i = 0; i < chartValues.length && i < 10; i++) {
			System.out.print("  ");
			for (int j = 0; j < chartValues[i][IS_PRINTED_INDEX] - 1; j++) {
				System.out.print(" ");
			}
			System.out.print((char)chartValues[i][CHAR_INDEX]);
		}
	}

	static private void swapChartValues(final int[] v1, final int[] v2) {
		final int tmpChar = v1[CHAR_INDEX];
		final int tmpCounter = v1[REPETATION_INDEX];
		v1[CHAR_INDEX] = v2[CHAR_INDEX];
		v1[REPETATION_INDEX] = v2[REPETATION_INDEX];
		v2[CHAR_INDEX] = tmpChar;
		v2[REPETATION_INDEX] = tmpCounter;

	}

	private static void setChartValues(final int[] array, final int charValue, final int repetations, final int roundedChart, final int isPrinted) {
		array[CHAR_INDEX] = charValue;
		array[REPETATION_INDEX] = repetations;
		array[ROUNDER_INDEX] = roundedChart;
		array[IS_PRINTED_INDEX] = isPrinted;
	}

}

