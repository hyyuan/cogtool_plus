package cogtoolplus_analyser;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;

public class VisualiserFreeChart extends ApplicationFrame{
	//protected Color colorBG = new Color(200,200,200);
	protected Color colorMax = new Color(125, 0, 0);
	protected Color colorMin = new Color(0, 125, 0);
	protected Color colorOth = new Color(0, 0, 125);
	protected String CHART_NAME = "Bar Chart";
	protected String HISTOGRAM_NAME = "Histogram";
	class CustomRenderer extends BarRenderer {
		private static final long serialVersionUID = 1L;
		private Paint[] colors;
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }
        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }
	
	private static final long serialVersionUID = 1L;
	
	public VisualiserFreeChart(String appTitle){
		super(appTitle);	
		}
		
	
	
	public void draw2DBarChartColorV1(GraphElement graph, HashMap<String, Double> input) {
		ArrayList<ArrayList<String>> dataSetList = graph.dataSetList;
		ArrayList<Double> temp = new ArrayList<Double>();
		DefaultStatisticalCategoryDataset data = new DefaultStatisticalCategoryDataset();
		for (int m = 0; m < dataSetList.size(); m++) {
			ArrayList<String> dataset = dataSetList.get(m);
			String name = dataset.get(0);
			for (int i = 1; i < dataset.size(); i++) {
				String key = dataset.get(i);
				System.out.println("key is " + key);
				if (input.containsKey(key)) {
					Double mean = input.get(key);
					Double std = 0.0;
					data.add(mean, std, key, name);					
					temp.add(mean);
				}
			}
		}		        
		JFreeChart barChart = ChartFactory.createBarChart(graph.title, graph.x_title, graph.y_title, data, PlotOrientation.VERTICAL,
				true, true, false);		
		CategoryPlot plot = barChart.getCategoryPlot();
		plot.setNoDataMessage("NO DATA!");
		//CategoryItemRenderer renderer = new CustomRenderer(new Paint[] { Color.red, Color.blue, Color.green,
		//		Color.yellow, Color.orange, Color.cyan, Color.magenta, Color.blue });
		// renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
		CategoryItemRenderer renderer = new StatisticalBarRenderer();
		//final ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0);
		
		float max = Collections.max(temp).floatValue();
		float min = Collections.min(temp).floatValue();
		System.out.println(temp.size());
		for (int i=0;i<temp.size();i++){
			if(temp.get(i).floatValue() == max){
				renderer.setSeriesPaint(i, colorMax); 
			}
			else if(temp.get(i).floatValue() == min){
				renderer.setSeriesPaint(i, colorMin);
			}
			else{
				renderer.setSeriesPaint(i, colorOth); 
			}
		}		
		plot.setRenderer(renderer);

            
		// change the margin at the top of the range axis...
		final CategoryAxis xAxis = new CategoryAxis(graph.x_title);
        xAxis.setLowerMargin(0.01d); // percentage of space before first bar
        xAxis.setUpperMargin(0.01d); // percentage of space after last bar
        xAxis.setCategoryMargin(0.05d); // percentage of space between categories
        final ValueAxis yAxis = new NumberAxis(graph.y_title);
        
		ChartFrame frame = new ChartFrame(CHART_NAME, barChart);
		frame.pack();
		frame.setVisible(true);
	}	
	
	public void draw2DBarChartColorV2(GraphElement graph, HashMap<String, ArrayList<Double>> input) {
		ArrayList<ArrayList<String>> dataSetList = graph.dataSetList;
		ArrayList<Double> temp = new ArrayList<Double>();
		DefaultStatisticalCategoryDataset data = new DefaultStatisticalCategoryDataset();		 
		for (int m = 0; m < dataSetList.size(); m++) {
			ArrayList<String> dataset = dataSetList.get(m);
			String name = dataset.get(0);
			System.out.println("name is " + name);
			for (int i = 1; i < dataset.size(); i++) {
				String key = dataset.get(i);
				if (input.containsKey(key)) {
					ArrayList<Double> durList = input.get(key);
					Double durTotal = 0.0;
					for (int j = 0; j < durList.size(); j++) {
						durTotal += durList.get(j);
					}
					Double mean = durTotal / durList.size();
					Double sum_difference = 0.0;
					for (int n = 0; n < durList.size(); n++) {
						sum_difference += Math.pow((durList.get(n) - mean), 2);
					}
					Double std = Math.sqrt(sum_difference / durList.size());
					data.add(mean, std, key, name);
					temp.add(mean);
				}
			}
		}
		
		JFreeChart barChart = ChartFactory.createBarChart(graph.title, graph.x_title, graph.y_title, data, PlotOrientation.VERTICAL,
				true, true, false);		
		CategoryPlot plot = barChart.getCategoryPlot();
		plot.setNoDataMessage("NO DATA!");
		//CategoryItemRenderer renderer = new CustomRenderer(new Paint[] { Color.red, Color.blue, Color.green,
		//		Color.yellow, Color.orange, Color.cyan, Color.magenta, Color.blue });
		// renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
		CategoryItemRenderer renderer = new StatisticalBarRenderer();
		//final ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0);
		
		float max = Collections.max(temp).floatValue();
		float min = Collections.min(temp).floatValue();
		for (int i=0;i<temp.size();i++){
			if(temp.get(i).floatValue() == max){
				renderer.setSeriesPaint(i, colorMax); 
			}
			else if(temp.get(i).floatValue() == min){
				renderer.setSeriesPaint(i, colorMin); 
			}
			else{
				renderer.setSeriesPaint(i, colorOth); 
			}
		}		
		plot.setRenderer(renderer);
            
		// change the margin at the top of the range axis...
		final CategoryAxis xAxis = new CategoryAxis(graph.x_title);
        xAxis.setLowerMargin(0.01d); // percentage of space before first bar
        xAxis.setUpperMargin(0.01d); // percentage of space after last bar
        xAxis.setCategoryMargin(0.05d); // percentage of space between categories
        final ValueAxis yAxis = new NumberAxis(graph.y_title);
        
		ChartFrame frame = new ChartFrame(CHART_NAME, barChart);
		frame.pack();
		frame.setVisible(true);	
	}	
	
	public void draw2DHistogram(GraphElement graph, HashMap<String, ArrayList<Double>> input, int divisions) {
		ArrayList<ArrayList<String>> dataSetList = graph.dataSetList;
		HistogramDataset data = new HistogramDataset();
		// ArrayList<String> dataset = graph.dataSet;
		for (int n = 0; n < dataSetList.size(); n++) {
			ArrayList<String> dataset = dataSetList.get(n);
			String name = dataset.get(0);
			for (int m = 1; m < dataset.size(); m++) {
				String key = dataset.get(m);
				if (input.containsKey(key)) {
					ArrayList<Double> durList = input.get(key);
					double[] doubleNumbers = new double[durList.size()];
					for (int i = 0; i < durList.size(); i++) {
						doubleNumbers[i] = durList.get(i);
					}
					data.addSeries(key, doubleNumbers, divisions > 0 ? divisions : 10);
				}
			}
			JFreeChart histogram = ChartFactory.createHistogram(graph.title, graph.x_title, graph.y_title, data,
					PlotOrientation.VERTICAL, true, true, false);

			XYPlot plot = (XYPlot) histogram.getPlot();
			ValueAxis axis = plot.getDomainAxis();
			// axis.setLowerBound(0);
			XYBarRenderer r = (XYBarRenderer) plot.getRenderer();
			r.setBarPainter(new StandardXYBarPainter());
			// r.setSeriesPaint(0, Color.blue);

			ChartPanel chartPanel = new ChartPanel(histogram);
			ChartFrame frame = new ChartFrame(HISTOGRAM_NAME, histogram);
			frame.pack();
			frame.setVisible(true);
		}
	}	
}
