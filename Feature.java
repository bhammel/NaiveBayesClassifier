/*
 * File: Feature.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 1
 */

public class Feature {
	private int positiveReviewCount;
	private int negativeReviewCount;
	private double positiveProbability;
	private double negativeProbability;

	public Feature() {
		positiveReviewCount = 0;
		negativeReviewCount = 0;
		positiveProbability = 0;
		negativeProbability = 0;
	}

	public Feature(int label) {
		if (label == 0) {
			positiveReviewCount = 0;
			negativeReviewCount = 1;
		} else {
			positiveReviewCount = 1;
			negativeReviewCount = 0;
		}

		positiveProbability = 0;
		negativeProbability = 0;
	}

	public int getPositiveReviewCount() {
		return positiveReviewCount;
	}

	public void incrementPositiveReviewCount() {
		positiveReviewCount++;
	}

	public int getNegativeReviewCount() {
		return negativeReviewCount;
	}

	public void incrementNegativeReviewCount() {
		negativeReviewCount++;
	}

	public double getPositiveProbability() {
		return positiveProbability;
	}

	public void setPositiveProbability(double positiveProbability) {
		this.positiveProbability = positiveProbability;
	}

	public double getNegativeProbability() {
		return negativeProbability;
	}

	public void setNegativeProbability(double negativeProbability) {
		this.negativeProbability = negativeProbability;
	}
}
