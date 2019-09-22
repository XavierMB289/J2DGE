/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.neural.data;

import java.util.ArrayList;

import ai.neural.math.ArrayOperations;

/**
 * 
 * TimeSeries This class creates time series to be used by Chart class.
 * 
 * @author Alan de Souza, Fábio Soares
 * @version 0.1
 *
 */
public class TimeSeries extends DataSet {

	private int indexTimeColumn;

	public TimeSeries(double[][] _data, String[] _columns) {
		super(_data, _columns);
	}

	public TimeSeries(String path, String filename) {
		super(path, filename);
	}

	public TimeSeries(String filename) {
		super(filename, false, ",");
	}

	public TimeSeries(DataSet ds) {
		super(ds.getData(), ds.getColumns());
	}

	public void setIndexColumn(int col) {
		this.indexTimeColumn = col;
		this.sortBy(indexTimeColumn);
	}

	public int getIndexColumn() {
		return this.indexTimeColumn;
	}

	public double[] shiftColumn(int col, int shift) {
		double[][] _data = ArrayOperations.arrayListToDoubleMatrix(data);
		return ArrayOperations.shiftColumn(_data, indexTimeColumn, shift, col);
	}

	public void shift(int col, int shift) {
		String colName = columns.get(col);
		if (shift > 0)
			colName = colName + "_" + String.valueOf(shift);
		else
			colName = colName + "__" + String.valueOf(-shift);
		addColumn(shiftColumn(col, shift), colName);
	}

	public double[][] getData(int[] cols, double start, double end) {
		ArrayList<ArrayList<Double>> result = new ArrayList<>();
		int ii = 0;
		for (int i = 0; i < numberOfRecords; i++) {
			if ((data.get(i).get(indexTimeColumn) >= start) && (data.get(i).get(indexTimeColumn) <= end)) {
				result.add(new ArrayList<Double>());
				for (int j = 0; j < cols.length; j++) {
					double value = data.get(i).get(cols[j]);
					result.get(ii).add(value);
				}
				ii++;
			}
		}
		return ArrayOperations.arrayListToDoubleMatrix(result);
	}

	public NeuralDataSet makeNeuralDataSet(int[] inputColumns, int[] outputColumns) {
		int ic = inputColumns.length;
		int nc = ic + outputColumns.length;
		String[] inputColumnNames = new String[inputColumns.length];
		String[] outputColumnNames = new String[outputColumns.length];
		String[] targetColumnNames = new String[outputColumns.length];
		for (int i = 0; i < inputColumns.length; i++)
			inputColumnNames[i] = columns.get(inputColumns[i]);
		for (int i = 0; i < outputColumns.length; i++) {
			outputColumnNames[i] = "Neural" + columns.get(outputColumns[i]);
			targetColumnNames[i] = columns.get(outputColumns[i]);
		}
		int[] inpcol = new int[ic];
		int[] outcol = new int[nc - ic];
		for (int i = 0; i < ic; i++)
			inpcol[i] = i;
		for (int i = ic; i < nc; i++)
			outcol[i - ic] = i;
		double[][] alldata = this.getData();
		double[][] neuraldata = new double[this.numberOfRecords][nc];
		for (int i = 0; i < this.numberOfRecords; i++) {
			for (int j = 0; j < nc; j++) {
				int jj;
				if (j < ic)
					jj = inputColumns[j];
				else
					jj = outputColumns[j - ic];
				neuraldata[i][j] = alldata[i][jj];
			}
		}
		NeuralDataSet nn = new NeuralDataSet(neuraldata, inpcol, outcol);
		nn.inputNames = inputColumnNames;
		nn.targetNames = targetColumnNames;
		nn.outputNames = outputColumnNames;
		return nn;
	}

}
