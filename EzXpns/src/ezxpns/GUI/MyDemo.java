package ezxpns.GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;

public class MyDemo extends JFrame {
   private static final long serialVersionUID = 834643894170724191L;

   JButton btn1 = new JButton("change renderer");
   JPanel panl = new JPanel();
   TimeSeries s1;
   DateAxis domainAxis;
   private AbstractXYItemRenderer Renderer;
   CombinedDomainXYPlot combinedplot;
   XYPlot mainPlot;
   int seriesnumber = 2;// already there are 2 series added to the chart
   int x = 5;

   public MyDemo(String stockSymbol)
   {
      super("MyDemo");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      domainAxis = new DateAxis("Date");
      NumberAxis rangeAxis = new NumberAxis("Price");
      Renderer = new CandlestickRenderer();
      XYDataset dataset = getDataSet(stockSymbol);

      mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, Renderer);
      // Create a line series
      TimeSeriesCollection timecollection = createMADataset(2);// value just to make difference in the new dataset
      mainPlot.setDataset(1, timecollection);
      mainPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
      mainPlot.getRenderer(1).setSeriesPaint(0, Color.blue);

      combinedplot = new CombinedDomainXYPlot(domainAxis);
      combinedplot.setDomainGridlinePaint(Color.white);
      combinedplot.setDomainGridlinesVisible(true);
      combinedplot.add(mainPlot, 3);

      // Do some setting up, see the API Doc
      Renderer.setSeriesPaint(0, Color.BLACK);
      rangeAxis.setAutoRangeIncludesZero(false);

      // Now create the chart and chart panel
      JFreeChart chart = new JFreeChart(stockSymbol, null, combinedplot, false);
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.setPreferredSize(new Dimension(600, 300));
      panl.add(new JTextField("Hello World"));
      panl.add(new JCheckBox("checkbox"));

      this.setLayout(new BorderLayout());
      this.add(chartPanel, BorderLayout.CENTER);
      this.add(panl, BorderLayout.SOUTH);
      this.add(btn1, BorderLayout.NORTH);
      this.pack();
   }

   protected AbstractXYDataset getDataSet(String stockSymbol)
   {
      // This is the dataset we are going to create
      DefaultOHLCDataset result = null;
      // This is the data needed for the dataset
      OHLCDataItem[] data;

      // This is where we go get the data, replace with your own data source
      data = getData(stockSymbol);

      // Create a dataset, an Open, High, Low, Close dataset
      result = new DefaultOHLCDataset(stockSymbol, data);

      return result;
   }

   // This method uses yahoo finance to get the OHLC data
   protected OHLCDataItem[] getData(String stockSymbol)
   {
      List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
      try
      {
         String strUrl = "http://ichart.finance.yahoo.com/table.csv?s=" + stockSymbol
               + "&a=0&b=1&c=2008&d=3&e=30&f=2008&ignore=.csv";
         URL url = new URL(strUrl);
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
         DateFormat df = new SimpleDateFormat("y-M-d");

         String inputLine;
         in.readLine();
         while ((inputLine = in.readLine()) != null)
         {
            StringTokenizer st = new StringTokenizer(inputLine, ",");

            Date date = df.parse(st.nextToken());
            double open = Double.parseDouble(st.nextToken());
            double high = Double.parseDouble(st.nextToken());
            double low = Double.parseDouble(st.nextToken());
            double close = Double.parseDouble(st.nextToken());
            double volume = Double.parseDouble(st.nextToken());
            // double adjClose = Double.parseDouble(st.nextToken());

            OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
            dataItems.add(item);
         }
         in.close();
      } catch (Exception e)
      {
         e.printStackTrace();
      }
      // Data from Yahoo is from newest to oldest. Reverse so it is oldest to newest
      Collections.reverse(dataItems);

      // Convert the list into an array
      OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);

      return data;
   }
   private TimeSeriesCollection createMADataset(int s)
   {
      s1 = new TimeSeries("Budget", "Year", "$ Million");
      try
      {
         s1.add(new Day(1, 1, 2008), 37.8 + s);
         s1.add(new Day(8, 1, 2008), 35.3 + s);
         s1.add(new Day(15, 1, 2008), 34.8 + s);
         s1.add(new Day(22, 1, 2008), 33.6 + s);
         s1.add(new Day(1, 2, 2008), 32.8 + s);
         s1.add(new Day(8, 2, 2008), 31.3 + s);
         s1.add(new Day(15, 2, 2008), 29.9 + s);
         s1.add(new Day(22, 2, 2008), 27.7 + s);
         s1.add(new Day(1, 3, 2008), 26.2 + s);
         s1.add(new Day(8, 3, 2008), 26.8 + s);
         s1.add(new Day(15, 3, 2008), 27.6 + s);
         s1.add(new Day(22, 3, 2008), 28.9 + s);
         s1.add(new Day(1, 4, 2008), 27.7 + s);
         s1.add(new Day(8, 4, 2008), 29.3 + s);
         s1.add(new Day(15, 4, 2008), 27.9 + s);
         s1.add(new Day(22, 4, 2008), 31.8 + s);
         s1.add(new Day(1, 5, 2008), 30.0 + s);
      } catch (Exception exception)
      {
         System.err.println(exception.getMessage());
      }
      TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(s1);
      return timeseriescollection;
   }

   static int cnt = 0;


   public static void main(String[] args)
   {
      new MyDemo("MSFT").setVisible(true);
   }
}