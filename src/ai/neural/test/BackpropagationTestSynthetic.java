package ai.neural.test;

import ai.neural.NeuralException;
import ai.neural.NeuralNet;
import ai.neural.data.DataSet;
import ai.neural.data.NeuralDataSet;
import ai.neural.init.UniformInitialization;
import ai.neural.learn.Backpropagation;
import ai.neural.learn.LearningAlgorithm;
import ai.neural.math.IActivationFunction;
import ai.neural.math.Linear;
import ai.neural.math.RandomNumberGenerator;
import ai.neural.math.Sigmoid;

/**
 *
 * BackpropagationTest This class solely performs Backpropagation learning
 * algorithm test
 * 
 * @authors Alan de Souza, Fábio Soares
 * @version 0.1
 * 
 */
public class BackpropagationTestSynthetic {
	public static void main(String[] args) {
		// NeuralNet nn = new NeuralNet
		RandomNumberGenerator.seed = 850;

		int numberOfInputs = 1;
		int numberOfOutputs = 1;
		int[] numberOfHiddenNeurons = { 5 };

		Linear outputAcFnc = new Linear(1.0);
		Sigmoid hl0Fnc = new Sigmoid(1.0);
		IActivationFunction[] hiddenAcFnc = { hl0Fnc };

		System.out.println("Creating Neural Network...");

		NeuralNet nn = new NeuralNet(numberOfInputs, numberOfOutputs, numberOfHiddenNeurons, hiddenAcFnc, outputAcFnc,
				new UniformInitialization(-1.0, 1.0));

		System.out.println("Neural Network created!");
		nn.print();

		DataSet dataSet = new DataSet("data", "xpow2.csv");
		double[][] _neuralDataSet = dataSet.getData();

		int[] inputColumns = { 1 };
		int[] outputColumns = { 2 };

		NeuralDataSet neuralDataSet = new NeuralDataSet(_neuralDataSet, inputColumns, outputColumns);

		System.out.println("Dataset created");
		neuralDataSet.printInput();
		neuralDataSet.printTargetOutput();

		System.out.println("Getting the first output of the neural network");

		Backpropagation backprop = new Backpropagation(nn, neuralDataSet, LearningAlgorithm.LearningMode.ONLINE);
		backprop.setLearningRate(0.1);
		backprop.setMaxEpochs(1000);
		backprop.setGeneralErrorMeasurement(Backpropagation.ErrorMeasurement.SimpleError);
		backprop.setOverallErrorMeasurement(Backpropagation.ErrorMeasurement.MSE);
		backprop.setMinOverallError(0.002);
		backprop.printTraining = true;
		backprop.setMomentumRate(0.7);

		try {
			backprop.forward();
			neuralDataSet.printNeuralOutput();

			backprop.train();
			System.out.println("End of training");
			if (backprop.getMinOverallError() >= backprop.getOverallGeneralError()) {
				System.out.println("Training successful!");
			} else {
				System.out.println("Training was unsuccessful");
			}
			System.out.println("Overall Error:" + String.valueOf(backprop.getOverallGeneralError()));
			System.out.println("Min Overall Error:" + String.valueOf(backprop.getMinOverallError()));
			System.out.println("Epochs of training:" + String.valueOf(backprop.getEpoch()));

			System.out.println("Target Outputs:");
			neuralDataSet.printTargetOutput();

			System.out.println("Neural Output after training:");
			backprop.forward();
			neuralDataSet.printNeuralOutput();
		} catch (NeuralException ne) {

		}

	}
}
