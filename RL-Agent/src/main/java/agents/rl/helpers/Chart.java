package agents.rl.helpers;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;

public class Chart extends ApplicationFrame {

    final XYSeries pegsLeftSeries;
    final XYSeries epsilon;

    public Chart(final String title) {

        super(title);
        pegsLeftSeries = new XYSeries("Pegs left plot");
        epsilon = new XYSeries("Epsilon");
    }

    public void addPegsLeft(double episode, double pegsLeft) {
        pegsLeftSeries.add(episode, pegsLeft);
    }
    public void addEpsilon(double episode, double eps) {
        epsilon.add(episode, eps);
    }

    public void makeChart() {
        final XYSeriesCollection dataPegs = new XYSeriesCollection(pegsLeftSeries);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Training data",
                "Pegs Left OR epsilon",
                "Episode",
                dataPegs,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );


        XYPlot plot = chart.getXYPlot();
        plot.setDataset(1, new XYSeriesCollection(epsilon));
        plot.setRenderer(1, new StandardXYItemRenderer());
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        setContentPane(chartPanel);
    }
}
