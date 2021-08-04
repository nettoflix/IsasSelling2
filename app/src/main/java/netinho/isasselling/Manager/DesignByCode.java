package netinho.isasselling.Manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class DesignByCode {
    public static void customView(View v, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        //shape.setCornerRadii(new float[] { 8, 8, 8, 8, 0, 0, 0, 0 });
        shape.setColor(backgroundColor);
        shape.setStroke(3, borderColor);
        v.setBackground(shape);
    }
    public static void setMarginLeft(View view, int i)
    {

        if(view.getLayoutParams() == null)
        {
            view.setLayoutParams(new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.leftMargin= i;
        view.setLayoutParams(layoutParams);

    }
    public static void setMarginTop(View view, int i)
    {
        if(view.getLayoutParams() == null)
        {
            view.setLayoutParams(new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin= i;
        view.setLayoutParams(layoutParams);

    }
    public static Rect getTextBounds(TextView textView)
    {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }
    public static int getFontHeight(int textSize)
    {
        Paint p = new Paint();
        p.setTextSize(textSize);
        Paint.FontMetrics fm = p.getFontMetrics();
        int height = (int) (fm.descent - fm.ascent);
        return height;
    }
    public static void setTextViewStyle(TextView view, int textSize,int color, Context ctx)
    {
        textSize = (int) MyMath.converterSpToPixels(textSize,ctx);
        view.setTextColor(color);
        view.setTextSize(textSize);
    }
    public static void setCardSize(CardView cd, int width, int height)
    {
       //CardView.LayoutParams layoutParams = (CardView.LayoutParams) cd.getLayoutParams();
        LinearLayout.LayoutParams layoutParams= null;
        if(layoutParams==null)
        {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT);
        }

        layoutParams.height = height;
        layoutParams.width = width;
        cd.setLayoutParams(layoutParams);
    }
    public static Point getScreenDimensions(Activity ctx)
    {
        Point point = new Point();
        WindowManager w = ctx.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            point.x= size.x;
            point.y = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            point.x = d.getWidth();
            point.y = d.getHeight();
        }
        return point;
    }
}
