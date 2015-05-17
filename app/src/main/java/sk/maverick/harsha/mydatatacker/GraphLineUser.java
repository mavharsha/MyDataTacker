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
import java.util.HashMap;

/**
 * Created by Harsha on 5/16/2015.
 */
public class GraphLineUser {

    public Intent getIntent(Context context, ArrayList arrayList){

        TimeSeries series = new TimeSeries("User1");
        String name= "";

        for(int i =0; i< arrayList.size() ; i++)
        {
            double temp = (double) arrayList.get(i);
             series.add(i,temp);
            Log.v("Graph Activity",""+ arrayList.get(i));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.GREEN);
        renderer.setLineWidth(3);
        renderer.setDisplayChartValues(true);

        XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
        mrenderer.addSeriesRenderer(renderer);

        mrenderer.setShowGrid(true);
        mrenderer.setAxisTitleTextSize(20);
        mrenderer.setLabelsTextSize(20);
        mrenderer.setXTitle("Days");
        mrenderer.setYTitle("Data Used!");
        mrenderer.getInitialRange();
        mrenderer.setGridColor(Color.LTGRAY);


        return ChartFactory.getLineChartIntent(context, dataset, mrenderer,name);
    }
}
