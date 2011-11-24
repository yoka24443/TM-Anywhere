package com.elearning.tm.android.client.view.report;

import java.text.DecimalFormat;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import com.elearning.tm.android.client.model.TaskInfo;

import android.content.Context;
import android.content.Intent;
import android.view.View;


public class BalancePieChart extends AbstractChart {

    DecimalFormat percentageFormat = new DecimalFormat("##0");

    public BalancePieChart(Context context, int orientation, float dpRatio) {
        super(context, orientation, dpRatio);
    }

    public Intent createIntent(List<TaskInfo> tasks) {
        double total = 0;
        for (TaskInfo task : tasks) {
            total += task.getTotalTime() <= 0 ? 0 : total;
        }
        CategorySeries series = new CategorySeries("ProjectPie");
        for (TaskInfo b : tasks) {
            if (b.getTotalTime() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(b.getProjectName());
                double p = (b.getTotalTime() * 100) / total;
                if (p >= 1) {
                    sb.append("(").append(percentageFormat.format(p)).append("%)");
                    series.add(sb.toString(), b.getTotalTime());
                }
            }
        }
        int[] color = createColor(series.getItemCount());
        DefaultRenderer renderer = buildCategoryRenderer(color);
        renderer.setLabelsTextSize(14 * dpRatio);
        renderer.setLegendTextSize(16 * dpRatio);
        return ChartFactory.getPieChartIntent(context, series, renderer, series.getTitle());
    }
    
    public View createPieChartView(List<TaskInfo> tasks){
    	 double total = 0;
         for (TaskInfo task : tasks) {
             total += task.getTotalTime();
         }
         CategorySeries series = new CategorySeries("ProjectPie");
         for (TaskInfo b : tasks) {
             if (b.getTotalTime() > 0) {
                 StringBuilder sb = new StringBuilder();
                 sb.append(b.getProjectName());
                 double p = (b.getTotalTime() * 100) / total;
                 if (p >= 1) {
                     sb.append("(").append(percentageFormat.format(p)).append("%)");
                     series.add(sb.toString(), b.getTotalTime());
                 }
             }
         }
         int[] color = createColor(series.getItemCount());
         DefaultRenderer renderer = buildCategoryRenderer(color);
         renderer.setLabelsTextSize(14 * dpRatio);
         renderer.setLegendTextSize(16 * dpRatio);
         return ChartFactory.getPieChartView(context, series, renderer);
    	
    	
    }
}
