package com.elearning.tm.android.client.view.report;

import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import com.elearning.tm.android.client.model.TaskInfo;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class BalanceBarChart extends AbstractChart {

    public BalanceBarChart(Context context, int orientation, float dpRatio) {
        super(context, orientation, dpRatio);
    }
   
    public View createBarChartView(List<TaskInfo> tasks) {
    	    XYMultipleSeriesDataset dataset = buildBarDataset(tasks);
    	    int count = dataset.getSeriesCount();
    	    int[] colors =  createColor(count);
    	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
    	    setChartSettings(renderer, "", "", "工作时", 0, 10, 0, 100, Color.LTGRAY, Color.LTGRAY);
    	    renderer.setXLabels(0);//不自动生成X坐标
    	    renderer.setYLabels(10);
    	    renderer.setXLabelsAlign(Paint.Align.CENTER);
    	    renderer.setPanEnabled(true, false);
//    	    renderer.setBarSpacing(1f);
    	    renderer.setXLabelsAngle(15f);
    	    
    	    renderer.setApplyBackgroundColor(true);
    	    renderer.setBackgroundColor(Color.GRAY);
    	    renderer.setMarginsColor(Color.GRAY);
    	    
    	    for (int i = 0; i< count; i++) {
    	    	TaskInfo task = tasks.get(i);
    	    	if (task.getTotalTime() > 0) {
    	    		renderer.addXTextLabel(i+1, task.getAssignUserName());
            	}
            }
    	    return ChartFactory.getBarChartView(context, dataset, renderer, Type.STACKED);
      }



}
