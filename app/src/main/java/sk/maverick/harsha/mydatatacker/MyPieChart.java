package sk.maverick.harsha.mydatatacker;/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by Harsha on 5/19/2015.
 */
public class MyPieChart {

    public Intent getIntent(Context context, ArrayList name, ArrayList dataused){

        CategorySeries series;

        series = new CategorySeries("Pie Graph");
        int k=0;

        for(int i = 0; i<name.size(); i++)
        {
            series.add(""+ name.get(i), (Double) dataused.get(i));
        }

        int[] colors = {Color.BLUE,
                        Color.CYAN,
                        Color.YELLOW,
                        Color.CYAN,
                        Color.DKGRAY,
                        Color.MAGENTA,
                        Color.RED};


        DefaultRenderer renderer = new DefaultRenderer();
        for (int i = 0; i<name.size(); i++)
        {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            Log.v("Simple Renderer", "" + r.getColor());
            renderer.addSeriesRenderer(r);
        }

        Intent it = ChartFactory.getPieChartIntent(context, series, renderer, "Pie");
        return it;
    }


}
