/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import java.util.ArrayList;

/**
 * Created by Harsha on 5/16/2015.
 */
public class GraphLine {

    public Intent getIntent(Context context, ArrayList arrayList){

        TimeSeries series = new TimeSeries("User1");
        String name= "";

        for(int i =0; i< arrayList.size() ; i++)
        {
            double temp = (double) arrayList.get(i);
             series.add(i+1,temp);
            Log.v("Graph Activity",""+ arrayList.get(i));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.GREEN);
        renderer.setLineWidth(5);
        renderer.setPointStrokeWidth(10);
        renderer.setDisplayChartValues(true);

        XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
        mrenderer.addSeriesRenderer(renderer);

        mrenderer.setShowGrid(true);
        mrenderer.setAxisTitleTextSize(30);
        mrenderer.setLabelsTextSize(30);
        mrenderer.setXTitle("Days");
        mrenderer.setYTitle("Data Used!");
        mrenderer.setGridColor(Color.LTGRAY);

        return ChartFactory.getLineChartIntent(context, dataset, mrenderer,name);
    }
}
