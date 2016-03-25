package com.neuro;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.sun.rowset.providers.RIXMLProvider;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.knowm.xchart.Chart;
import org.knowm.xchart.SwingWrapper;

import java.lang.Double;

import org.knowm.xchart.StyleManager.LegendPosition;
import org.knowm.xchart.StyleManager.ChartType;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Created by nikita on 14/01/16.
 */
public class NEURO {

    static final int PERCENT = 20;

    //COSX
//    static final double TOP_Y = 1;
//    static final double BOTTOM_Y = -1;
//    static final double LEFT_X = 3.14;
//    static final double RIGHT_X = 3.14 * 4;
//    static final double RANGE_X = 0.25;
//    static final double MAX = 1.0;
//    static final double RANDOM_COUNT = 30;

//    5x^3 + x^2 + 5
    static final double TOP_Y = 100;
    static final double BOTTOM_Y = -100;
    static final double LEFT_X = -7;
    static final double RIGHT_X = 7;
    static final double RANGE_X = 0.20;
    static final double MAX = 2000;
    static final double RANDOM_COUNT = 50;

//    sin
//    static final double TOP_Y = 3.14;
//    static final double BOTTOM_Y = -3.14;
//    static final double LEFT_X = 0;
//    static final double RIGHT_X = 3.14;
//    static final double RANGE_X = 0.1;
//    static final double MAX = 3.14;
//    static final double RANDOM_COUNT = 30;

    private final UniformRealDistribution distribution = new UniformRealDistribution(LEFT_X, RIGHT_X);
    private final NormalDistribution normalDistribution = new NormalDistribution();

    private List<List<Double>> funcData = solvData();
    private List<List<Double>> funcDataError = solvDataError();

    List<List<Double>> cosData() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (double i = LEFT_X; i <= RIGHT_X; i+= RANGE_X) {
            xData.add(i);
            yData.add(Math.cos(i));
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }

    List<List<Double>> cosDataError() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (int i = 0; i < RANDOM_COUNT; i++) {
            double x = distribution.sample();
            double y = Math.cos(x);
            xData.add(x);
            yData.add(y);
        }

        for (int i = 0; i < yData.size(); i++) {
            if (new Random().nextInt(100) < PERCENT) {
                yData.set(i, normalDistribution.sample());
            }
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }


    List<List<Double>> sinData() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (double i = LEFT_X; i <= RIGHT_X; i+= RANGE_X) {
            xData.add(i);
            yData.add(i * Math.sin(2 * Math.PI * i));
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }

    List<List<Double>> sinDataError() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (int i = 0; i < RANDOM_COUNT; i++) {
            double x = distribution.sample();
            double y = x * Math.sin(2 * Math.PI * x);
            xData.add(x);
            yData.add(y);
        }

        for (int i = 0; i < yData.size(); i++) {
            if (new Random().nextInt(100) < PERCENT) {
                yData.set(i, normalDistribution.sample());
            }
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }


    List<List<Double>> solvData() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (double i = LEFT_X; i <= RIGHT_X; i+= RANGE_X) {
            xData.add(i);
            yData.add(5*Math.pow(i,3) + Math.pow(i,2) + 5);
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }

    List<List<Double>> solvDataError() {

        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();

        for (int i = 0; i < RANDOM_COUNT; i++) {
            double x = distribution.sample();
            double y = 5 * Math.pow(x, 3) + Math.pow(x, 2) + 5;
            xData.add(x);
            yData.add(y);
        }

        for (int i = 0; i < yData.size(); i++) {
            if (new Random().nextInt(100) < PERCENT) {
                yData.set(i, normalDistribution.sample()*30);
            }
        }

        List<List<Double>> data = new ArrayList<List<Double>>();
        data.add(xData);
        data.add(yData);

        return data;
    }


    List<Double> normirovka(List<Double> vector) {

        List<Double> list = new ArrayList<Double>();

        for (Double value : vector) {
            Double normV = value / MAX;
            list.add(normV);
        }
        return list;
    }


    public static void main(String... aArgs) {

        NEURO neuro = new NEURO();

        List<Chart> charts = new ArrayList<Chart>();

        List<List<Double>> erroData = neuro.funcDataError;
        List<Double> xDataError = erroData.get(0);
        List<Double> yDataError = erroData.get(1);

        charts.add(neuro.chartForBullets(xDataError, yDataError)); // POINT CHART

        yDataError = neuro.normirovka(yDataError);

        Double[] inputVector = xDataError.toArray(new Double[xDataError.size()]);
        Double[] outputVector = yDataError.toArray(new Double[yDataError.size()]);

        //cos = {1, 5, 1} = 250000  DEFINE
        //sin = {1, 5, 1} = 200000  DEFINE
        //pol = {1, 5, 1} = 250000  DEFINE

        NeuralNetwork neuralNetwork = new NeuralNetwork(new int[]{1, 5, 1});
        neuralNetwork.trainCount = 250000;
        neuralNetwork.trainNeuralNetwork(inputVector, outputVector);

        charts.add(neuro.chartForError(neuralNetwork.errors));
        charts.add(neuro.chartForGraphic(neuralNetwork));

        new SwingWrapper(charts).displayChartMatrix();
    }

    Chart chartForBullets(List<Double> xData, List<Double> yData) {

        Chart chart = new Chart(800, 600);
        chart.getStyleManager().setYAxisMin(BOTTOM_Y);
        chart.getStyleManager().setYAxisMax(TOP_Y);
        chart.getStyleManager().setXAxisMax(RIGHT_X);
        chart.getStyleManager().setXAxisMin(LEFT_X);
        chart.getStyleManager().setChartType(ChartType.Scatter);
        chart.getStyleManager().setChartType(ChartType.Scatter);
        chart.getStyleManager().setChartTitleVisible(false);
        chart.getStyleManager().setLegendPosition(LegendPosition.OutsideE);
        chart.getStyleManager().setMarkerSize(7);

        chart.addSeries("Gaussian Blob", xData, yData);
        return chart;
    }

    Chart chartForGraphic(NeuralNetwork neuralNetwork) {

        List<Double> xDataResult = new ArrayList<Double>();
        List<Double> yDataResult = new ArrayList<Double>();

        for (double x = LEFT_X; x <= RIGHT_X; x += RANGE_X) {
            xDataResult.add(x);
            yDataResult.add(neuralNetwork.valueOutput(x) * MAX);
        }

        Chart chart = new Chart(800, 600);
        chart.getStyleManager().setYAxisMin(BOTTOM_Y);
        chart.getStyleManager().setYAxisMax(TOP_Y);
        chart.getStyleManager().setXAxisMax(RIGHT_X);
        chart.getStyleManager().setXAxisMin(LEFT_X);

        chart.addSeries("neural", xDataResult, yDataResult);

        List<List<Double>> cosData = funcData;
        List<Double> xData = cosData.get(0);
        List<Double> yData = cosData.get(1);

        chart.addSeries("V", xData, yData);

        return chart;
    }


    Chart chartForError(List<Double> errors) {

        List<Double> xDataResult = new ArrayList<Double>();
        List<Double> yDataResult = new ArrayList<Double>();

        for (int i = 0; i < errors.size(); i++) {
            yDataResult.add(errors.get(i));
            xDataResult.add((double)i);
        }

        Chart chart = new Chart(800, 600);
        chart.getStyleManager().setYAxisMin(0);
        chart.getStyleManager().setYAxisMax(Collections.max(errors));
        chart.getStyleManager().setXAxisMax(errors.size());
        chart.getStyleManager().setXAxisMin(0);

        chart.addSeries("errors", xDataResult, yDataResult);
        return chart;
    }
}
