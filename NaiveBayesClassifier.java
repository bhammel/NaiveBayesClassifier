/*
 * File: NaiveBayesClassifier.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 1
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class NaiveBayesClassifier {
	private static HashMap<String, Feature> features = new HashMap<String, Feature>();
	private static int totalPositiveReviews = 0;
	private static int totalNegativeReviews = 0;

	private static int getLabel(String line, String filename) {
		int label = Character.getNumericValue(line.charAt(0));
		if (label != 0 && label != 1) {
			System.err.println("Error: Invalid label in " + filename + " file: '" + line.charAt(0) + "'");
			System.exit(1);
		}

		return label;
	}

	private static void learn(int label, String[] tokens) {
		HashSet<String> words = new HashSet<String>();

		for (String token : tokens) {
			if (words.add(token)) {
				if (features.containsKey(token)) {
					Feature feature = features.get(token);
					if (label == 0) {
						feature.incrementNegativeReviewCount();
					} else {
						feature.incrementPositiveReviewCount();
					}
				} else {
					Feature feature = new Feature(label);
					features.put(token, feature);
				}
			}
		}

		if (label == 0) {
			totalNegativeReviews++;
		} else {
			totalPositiveReviews++;
		}
	}

	private static void calculateProbabilities() {
		for (String key : features.keySet()) {
			Feature feature = features.get(key);

			feature.setPositiveProbability(((double) feature.getPositiveReviewCount()) / totalPositiveReviews);
			feature.setNegativeProbability(((double) feature.getNegativeReviewCount()) / totalNegativeReviews);
		}
	}

	private static int classify(String[] tokens) {
		HashSet<String> words = new HashSet<String>();
		int classification;
		double positiveProbability = 0;
		double negativeProbability = 0;

		for (String token : tokens) {
			if (words.add(token)) {
				if (features.containsKey(token)) {
					Feature feature = features.get(token);

					if (feature.getPositiveProbability() > 0) {
						positiveProbability += Math.log(feature.getPositiveProbability());
					} else {
						positiveProbability -= 6.63;	// Chi-square critical value
					}

					if (feature.getNegativeProbability() > 0) {
						negativeProbability += Math.log(feature.getNegativeProbability());
					} else {
						negativeProbability -= 6.63;	// Chi-square critical value
					}
				}
			}
		}

		if (positiveProbability > negativeProbability) {
			classification = 1;
		} else {
			classification = 0;
		}

		return classification;
	}

	public static void main(String[] args) {
		try {
			if (args.length != 2) {
				System.err.println("Usage: java NaiveBayesClassifier training.txt testing.txt");
				System.exit(1);
			}

			String trainingFile = args[0];
			String testingFile = args[1];

			String line = null;

			long trainingTime = 0;
			long testingTime = 0;

			TimeWatch watch = TimeWatch.start();

			// Train the classifier
			BufferedReader bufferedReader = new BufferedReader(new FileReader(trainingFile));
			while ((line = bufferedReader.readLine()) != null) {
				int label = getLabel(line, "training");
				String review = line.substring(2).toLowerCase().replace("<br />", "").replace("--", " ");
				learn(label, review.split("[^A-Za-z'-]+"));
			}

			bufferedReader.close();

			// Calculate probabilities
			calculateProbabilities();

			trainingTime = watch.time(TimeUnit.SECONDS);

			watch.reset();

			int correct = 0;
			int incorrect = 0;

			// Apply classifier to training dataset
			bufferedReader = new BufferedReader(new FileReader(trainingFile));
			while ((line = bufferedReader.readLine()) != null) {
				int label = getLabel(line, "training");
				String review = line.substring(2).toLowerCase().replace("<br />", "").replace("--", " ");
				int classification = classify(review.split("[^A-Za-z'-]+"));

				if (classification == label) {
					correct++;
				} else {
					incorrect++;
				}
			}

			bufferedReader.close();

			double trainingAccuracy = ((double) correct) / (correct + incorrect);

			correct = 0;
			incorrect = 0;

			// Apply classifier to testing dataset
			bufferedReader = new BufferedReader(new FileReader(testingFile));
			while ((line = bufferedReader.readLine()) != null) {
				int label = getLabel(line, "testing");
				String review = line.substring(2).toLowerCase().replace("<br />", "").replace("--", " ");
				int classification = classify(review.split("[^A-Za-z'-]+"));

				if (classification == label) {
					correct++;
				} else {
					incorrect++;
				}

				System.out.println(classification);
			}

			bufferedReader.close();

			testingTime = watch.time(TimeUnit.SECONDS);

			double testingAccuracy = ((double) correct) / (correct + incorrect);

			System.out.println(trainingTime + " seconds (training)");
			System.out.println(testingTime + " seconds (labeling)");

			DecimalFormat decimalFormat = new DecimalFormat("#.###");
			decimalFormat.setRoundingMode(RoundingMode.CEILING);

			System.out.println(decimalFormat.format(trainingAccuracy) + " (training)");
			System.out.println(decimalFormat.format(testingAccuracy) + " (testing)");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
