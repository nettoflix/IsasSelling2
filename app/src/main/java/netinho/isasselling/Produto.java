package netinho.isasselling;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;
import netinho.isasselling.Manager.MyMath;

public class Produto  extends LinearLayout implements Comparable{

    public String name;
    public String category;
    public String marca;
    public double unitPrice;
    public Double price;
    public int quantity;
    private final int nameId =1;
    private final int categoryId=2;
   private final int priceId=3;
    public int spinQuantity;
    private Spinner quantitySpin;
    private int textSize;
    private int color;
    TextView quantityText;
    Activity context;
    public Produto(Activity ctx, Produto copy, boolean spin)
    {
        super(ctx);
        this.context = ctx;
        this.name = copy.name;
        this.category = copy.category;
        this.marca= copy.marca;
        this.unitPrice = copy.unitPrice;
        this.quantity = copy.quantity;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textSize = 12;
         color = Color.BLACK;
        //HORIZONTAL LAYOUT
        LinearLayout horizontalLayout = new LinearLayout(ctx);
        horizontalLayout.setLayoutParams(hParams);
        //NAME VIEW
        TextView nameView= new TextView(ctx);
        params.gravity = Gravity.LEFT;
        nameView.setLayoutParams(params);
        nameView.setId(nameId);
        nameView.setText(name);
        DesignByCode.setTextViewStyle(nameView, textSize, color, ctx);
        //PRICE VIEW
        params.gravity = Gravity.RIGHT;
        TextView priceView = new TextView(ctx);
        priceView.setLayoutParams(params);
        priceView.setId(priceId);
        DesignByCode.setTextViewStyle(priceView, textSize, color, ctx);
        //add views
        horizontalLayout.addView(nameView);
        horizontalLayout.addView(priceView);
        //SPINNER tracking the quantity
        if(spin)
        {
            spinQuantity = quantity;
            quantitySpin = new Spinner(ctx);
            List<CharSequence> list100 =
                    new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(spinQuantity)));
            ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                    android.R.layout.simple_spinner_item,
                    list100);
            quantitySpin.setAdapter(spnAdapter);
            //quantitySpin.setSelection(quantity);
            quantitySpin.setOnItemSelectedListener(new Spinner_listener());
            horizontalLayout.addView(quantitySpin);
            quantitySpin.setFocusable(false);
            quantitySpin.setClickable(false);

        }
        else
        {
            quantityText = new TextView(ctx);
            DesignByCode.setTextViewStyle(quantityText, textSize,color, ctx);
            quantityText.setText(String.valueOf(quantity));
            horizontalLayout.addView(quantityText);
            DesignByCode.setMarginLeft(quantityText, 90);
        }
        this.addView(horizontalLayout);
        this.setOrientation(LinearLayout.VERTICAL);
        setPrice(unitPrice);
    }
    public Produto(Activity ctx, AttributeSet attrs)
    {
        super(ctx,attrs);
        this.context = ctx;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        textSize = 12;
         color = Color.BLACK;
        //HORIZONTAL LAYOUT
        LinearLayout horizontalLayout = new LinearLayout(ctx);
        horizontalLayout.setLayoutParams(hParams);
        //NAME VIEW
        TextView nameView= new TextView(ctx);
        params.gravity = Gravity.LEFT;
        nameView.setLayoutParams(params);
        nameView.setId(nameId);
        DesignByCode.setTextViewStyle(nameView, textSize,color, ctx);
        //SPINNER tracking the quantity

        quantitySpin = new Spinner(ctx);
        List<CharSequence> list100 =
                new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(quantity)));
        ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                list100);
        quantitySpin.setAdapter(spnAdapter);
        //quantitySpin.setSelection(quantity);
        quantitySpin.setOnItemSelectedListener(new Spinner_listener());
        //PRICE VIEW
        params.gravity = Gravity.RIGHT;
        TextView priceView = new TextView(ctx);
        priceView.setLayoutParams(params);
        priceView.setId(priceId);
        DesignByCode.setTextViewStyle(priceView, textSize, color, ctx);
        //add views
        horizontalLayout.addView(nameView);
        horizontalLayout.addView(quantitySpin);
        horizontalLayout.addView(priceView);
        this.addView(horizontalLayout);
        this.setOrientation(LinearLayout.VERTICAL);
    }
    public Produto(Activity ctx, String name, String category,String marca , double unitPrice, int quantity, boolean spin, int marginSpace) {
        super(ctx);
        this.context = ctx;
        this.name = name;
        this.category = category;
        this.marca = marca;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        textSize = 12;
         color = Color.BLACK;
        //params.weight = 0.3f;
        //HORIZONTAL LAYOUT
        LayoutInflater inflater = ctx.getLayoutInflater();
        ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.produto_layout, null);
        //NAME VIEW
        TextView nameView= constraintLayout.findViewById(R.id.nameView);
        nameView.setId(nameId);
        nameView.setText(name);
        DesignByCode.setTextViewStyle(nameView, textSize, color, ctx);

        //PRICE VIEW
        TextView priceView= constraintLayout.findViewById(R.id.priceView);
        DesignByCode.setMarginLeft(priceView, marginSpace);
        priceView.setId(priceId);
        DesignByCode.setTextViewStyle(priceView, textSize, color, ctx);
        //add views
        //SPINNER tracking the quantity
        LinearLayout quantityHolder = (LinearLayout) constraintLayout.findViewById(R.id.quantityHolder);
        if(spin)
        {
            spinQuantity = quantity;
            quantitySpin = new Spinner(ctx);
            List<CharSequence> list100 =
                    new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(spinQuantity)));
            ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                    android.R.layout.simple_spinner_item,
                    list100);
            quantitySpin.setAdapter(spnAdapter);
         //   quantitySpin.setSelection(quantity);
            quantitySpin.setOnItemSelectedListener(new Spinner_listener());
            quantitySpin.setFocusable(false);
            quantitySpin.setClickable(false);
            quantityHolder.addView(quantitySpin);

        }
        else
        {
            quantityText = new TextView(ctx);
            DesignByCode.setTextViewStyle(quantityText, textSize, color, ctx);
            quantityText.setText(String.valueOf(quantity));
            DesignByCode.setMarginLeft(quantityText, marginSpace);
            quantityHolder.addView(quantityText);
        }
        this.addView(constraintLayout);
        this.setOrientation(LinearLayout.VERTICAL);
        setPrice(unitPrice);
    }

public void update(Produto otherProduct)
{
    setCategory(otherProduct.category);
    setPrice(otherProduct.price);
    setQuantity(otherProduct.quantity);
}

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if(quantityText!=null)
        {
            quantityText.setText(String.valueOf(quantity));
        }
    }

    public void setName(String n)
    {
        this.name = n;
        TextView nameView = (TextView) findViewById(nameId);
        nameView.setText(n);

    }
    public void setCategory(String c)
    {
        this.category = c;
        //TextView categoryView = (TextView) findViewById(categoryId);
        //categoryView.setText(c);

    }
    public void setPrice(Double p)
    {
        this.price = p;
        TextView priceView= (TextView) findViewById(priceId);
        String formatedPrice;
        formatedPrice = Handler.formartDoubleToString(p, "R$");
        if(formatedPrice.contains("."))
        {
            formatedPrice = formatedPrice.replace('.',',');
        }
        priceView.setText(formatedPrice);
    }
   public double getTotalPrice()
    {
        return price;
    }
 public Spinner getSpinner()
 {
     return quantitySpin;
 }



    public void replaceSpinnerWithView(Context ctx) {
      LinearLayout parent = (LinearLayout) quantitySpin.getParent();
      parent.removeView(quantitySpin);
      quantityText = new TextView(ctx);
      DesignByCode.setTextViewStyle(quantityText, textSize, color, ctx);
      quantityText.setText(quantitySpin.getSelectedItem().toString());
      parent.addView(quantityText);
    }

    public void incrementQuantity(int v) {
        this.quantity += v;
        quantityText.setText(String.valueOf(this.quantity));
        TextView priceView= (TextView) findViewById(priceId);
        setPrice(this.quantity*unitPrice);
    }

    public void decrementQuantity(int quantity) {
        if(quantityText!=null)
        {
            this.quantity-=quantity;
            quantityText.setText(String.valueOf(this.quantity));
        }
    }
    public void updateSpinner()
    {
        if(quantitySpin!=null)
        {
            spinQuantity-= quantity;
            if(spinQuantity>0) {
                List<CharSequence> list =
                        new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(spinQuantity)));
                ArrayAdapter spnAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_spinner_item,
                        list);
                quantitySpin.setAdapter(spnAdapter);
            }
            else
            {
                LinearLayout parent = (LinearLayout) getParent();
                parent.removeView(this);
            }
            return;
        }
    }
    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * quantity;
        long creditsLong = Double.doubleToLongBits(unitPrice);
        result = 31 * result + (int) (creditsLong ^ (creditsLong >>> 32));
        return result;
    }
    public boolean equals(Object o)
    {
       if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto otherProduct = (Produto) o;
        return name.equals(otherProduct.name);
    }

    @Override
    public int compareTo(Object o) {
        Produto p = (Produto) o;
        if(this.equals(p)) return 0;
        return 1;
    }

    public void removeQuantityView() {
        LinearLayout parent = (LinearLayout) quantityText.getParent();
        parent.removeView(quantityText);

    }

    private class Spinner_listener implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
            quantity = pos+1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}

