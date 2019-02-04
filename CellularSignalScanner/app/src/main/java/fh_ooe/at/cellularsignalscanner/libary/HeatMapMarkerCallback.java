/*
 * HeatMapMarkerCallback.java
 *
 * Copyright 2017 Heartland Software Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fh_ooe.at.cellularsignalscanner.libary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import fh_ooe.at.cellularsignalscanner.R;

/**
 * A callback to allow markers to be drawn over the heatmap at each data point.
 * Created by Travis Redpath on 11/3/2017.
 */
public interface HeatMapMarkerCallback {

    void drawMarker(Canvas canvas, float x, float y, double data);

    class CircleHeatMapMarker implements HeatMapMarkerCallback {

        private Paint paint = new Paint();

        Context context;
        public CircleHeatMapMarker(Context context){
            this.context = context;
        }

        @Override
        public void drawMarker(Canvas canvas, float x, float y, double data) {
            if(data < 3.3)
                paint.setColor(context.getResources().getColor(R.color.gradient0));
            else if(data < 6.6)
                paint.setColor(context.getResources().getColor(R.color.gradient1));
            else if(data < 10)
                paint.setColor(context.getResources().getColor(R.color.gradient2));
            else if(data < 13.3)
                paint.setColor(context.getResources().getColor(R.color.gradient3));
            else if(data < 16.6)
                paint.setColor(context.getResources().getColor(R.color.gradient4));
            else if(data < 20)
                paint.setColor(context.getResources().getColor(R.color.gradient5));
            else if(data < 23.3)
                paint.setColor(context.getResources().getColor(R.color.gradient6));
            else if(data < 26.6)
                paint.setColor(context.getResources().getColor(R.color.gradient7));
            else if(data < 30)
                paint.setColor(context.getResources().getColor(R.color.gradient8));
            else if(data < 33.3)
                paint.setColor(context.getResources().getColor(R.color.gradient9));
            else if(data < 36.6)
                paint.setColor(context.getResources().getColor(R.color.gradient10));
            else if(data < 40)
                paint.setColor(context.getResources().getColor(R.color.gradient11));
            else if(data < 43.3)
                paint.setColor(context.getResources().getColor(R.color.gradient12));
            else if(data < 46.6)
                paint.setColor(context.getResources().getColor(R.color.gradient13));
            else if(data < 50)
                paint.setColor(context.getResources().getColor(R.color.gradient14));
            else if(data < 53.3)
                paint.setColor(context.getResources().getColor(R.color.gradient15));
            else if(data < 56.6)
                paint.setColor(context.getResources().getColor(R.color.gradient16));
            else if(data < 60)
                paint.setColor(context.getResources().getColor(R.color.gradient17));
            else if(data < 63.3)
                paint.setColor(context.getResources().getColor(R.color.gradient18));
            else if(data < 66.6)
                paint.setColor(context.getResources().getColor(R.color.gradient19));
            else if(data < 70)
                paint.setColor(context.getResources().getColor(R.color.gradient20));
            else if(data < 73.3)
                paint.setColor(context.getResources().getColor(R.color.gradient21));
            else if(data < 76.6)
                paint.setColor(context.getResources().getColor(R.color.gradient22));
            else if(data < 80)
                paint.setColor(context.getResources().getColor(R.color.gradient23));
            else if(data < 83.3)
                paint.setColor(context.getResources().getColor(R.color.gradient24));
            else if(data < 86.6)
                paint.setColor(context.getResources().getColor(R.color.gradient25));
            else if(data < 90)
                paint.setColor(context.getResources().getColor(R.color.gradient26));
            else if(data < 93.3)
                paint.setColor(context.getResources().getColor(R.color.gradient27));
            else if(data < 96.6)
                paint.setColor(context.getResources().getColor(R.color.gradient28));
            else
                paint.setColor(context.getResources().getColor(R.color.gradient29));
            canvas.drawCircle(x, y, 100, paint);
        }
    }
}