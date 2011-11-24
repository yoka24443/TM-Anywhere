package com.elearning.tm.android.client.view.report;

import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.elearning.tm.android.client.model.TaskInfo;

import android.content.Context;

public abstract class AbstractChart {

	final protected Context context;

	final protected float dpRatio;

	final protected int orientation;

	public static final int RED = 0xD0FF0000;
	public static final int GREEN = 0xD000FF00;
	public static final int BLUE = 0xD00000FF;
	public static final int YELLOW = 0xD0FFFF00;
	public static final int CYAN = 0xD000FFFF;
	public static final int MAGENTA = 0xD0FF00FF;
	public static final int ORANGE = 0xD0FF8C66;

	public static final int RED1 = 0xA0FF7777;
	public static final int GREEN1 = 0xA077FF77;
	public static final int BLUE1 = 0xA07777FF;
	public static final int YELLOW1 = 0xA0FFFF77;
	public static final int CYAN1 = 0xA077FFFF;
	public static final int MAGENTA1 = 0xA0FF77FF;
	public static final int ORANGE1 = 0xD0FF8C66;

	public static final int[] colorPool = new int[] { GREEN, ORANGE, BLUE, RED,
			YELLOW, CYAN, MAGENTA, GREEN1, ORANGE1, BLUE1, RED1, YELLOW1,
			CYAN1, MAGENTA1 };

	public static final PointStyle[] pointPool = new PointStyle[] {
			PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.TRIANGLE,
			PointStyle.SQUARE, PointStyle.X };

	public AbstractChart(Context context, int orientation, float dpRatio) {
		this.context = context;
		this.dpRatio = dpRatio;
		this.orientation = orientation;
	}

	public PointStyle[] createPointStyle(int size) {
		PointStyle[] point = new PointStyle[size];
		if (size == 0)
			return point;
		int i = 0;
		int cmindex = 0;
		while (true) {
			cmindex = i % pointPool.length;
			point[i] = pointPool[cmindex];
			i++;
			if (i == point.length) {
				break;
			}

		}
		return point;
	}

	public int[] createColor(int size) {
		int[] color = new int[size];
		if (size == 0)
			return color;
		int i = 0;
		int cmindex = 0;
		int trans = 0xFFFFFFFF;
		while (true) {
			cmindex = i % colorPool.length;
			if (i != 0 && cmindex == 0) {
				trans = trans - 0x40000000;// increase transparent
			}
			color[i] = colorPool[cmindex];
			color[i] = color[i] & trans;
			i++;
			if (i == color.length) {
				break;
			}

		}
		return color;
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	protected XYMultipleSeriesDataset buildBarDataset(List<TaskInfo> tasks) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int i = 0;
		for (TaskInfo b : tasks) {
			if (b.getTotalTime() > 0) {
				i++;
				XYSeries series = new XYSeries(b.getAssignUserName());
				series.add(i, b.getTotalTime());
				dataset.addSeries(series);
			}
		}
		return dataset;
	}
}
