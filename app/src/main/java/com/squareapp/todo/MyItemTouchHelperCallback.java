package com.squareapp.todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

/**
 * Created by Valentin Purrucker on 15.09.2017.
 */

public class MyItemTouchHelperCallback extends ItemTouchHelperExtension.Callback
{



    private Context context;


    private float roundedCornerRadius;

    public MyItemTouchHelperCallback(Context context)
    {
        this.context = context;

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {

        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {

    }




    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {

        viewHolder.itemView.setTranslationX(dX);

            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;


            Paint paint = new Paint();
            Paint textPaint = new Paint();
            setPixelFromDp(8);

            if (dX > 0)
            {

            /* Set your color for positive displacement */
                paint.setColor(Color.parseColor("#83D22C"));
                textPaint.setColor(Color.parseColor("#40AA28"));
                // Draw Rect with varying right side, equal to displacement dX
                c.drawRoundRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), roundedCornerRadius, roundedCornerRadius, paint);


                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done_all_white_36dp);

                c.drawBitmap(icon,
                        (float) itemView.getWidth() / (4.5f * 2) - icon.getWidth() / 2,
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                        textPaint);


            }
            else
            {
            /* Set your color for negative displacement */
                paint.setColor(Color.parseColor("#D22C2C"));
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                c.drawRoundRect((float) itemView.getRight() +dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), roundedCornerRadius, roundedCornerRadius, paint);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    private void setPixelFromDp(float roundedCorner)
    {

        this.roundedCornerRadius = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                roundedCorner,
                Resources.getSystem().getDisplayMetrics()
        );

    }
}
