package netinho.isasselling;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;
import netinho.isasselling.Manager.MyMath;

public class CustomView extends LinearLayout {
    Cliente cliente;
    MainActivity ctx;
    public TextView title;
    public ListView listView;
    public ArrayList<Produto> adapterProducts;
    HashMap<String,Produto> adapterProductsHash;
    private boolean listShown;
    LayoutParams listParams;
    String category;
    private int textSize;
    private int textColor;
    public CustomView(Cliente cliente,MainActivity context, String category) {
        super(context);
        this.cliente = cliente;
        this.category = category;
        this.adapterProducts = new ArrayList<>();
        this.adapterProductsHash = new HashMap<>();
        this.ctx = context;
        this.listAdapter = new CustomListAdapter(ctx, adapterProducts);
        textColor = Color.BLACK;
        textSize = 28;
        this.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams thisParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(thisParams);
        title = new TextView(context);
        title.setText(category);
        DesignByCode.setTextViewStyle(title, textSize, textColor, ctx);
        listView = new ListView(context);
        listView.requestFocus();
        listView.setAdapter(listAdapter);
        listView.setVisibility(GONE);

        this.addView(title);
        this.addView(listView);
        listShown = false;

    }
   public void removeProduct(Produto product, HashMap<String,CustomView> categories)
   {
       listHeight-=MyMath.converterSpToPixels(40,ctx);
       adapterProductsHash.remove(product.name);
       adapterProducts.remove(product);
       listAdapter.notifyDataSetChanged();
       listAdapter.count-=1;
       if(adapterProducts.size() ==0)
       {
           LinearLayout parent = (LinearLayout) getParent();
           parent.removeView(this);
           categories.remove(this.category);
       }
   }
public void setOnItemClick(AdapterView.OnItemClickListener listener)
{
    listView.setOnItemClickListener(listener);
}
    //adapter and list
    CustomListAdapter listAdapter;
    int listHeight;
public void addToList(ArrayList<Produto> produtos){

    ArrayList<Produto> produtosCopy = copyArray(produtos);
    listAdapter = new CustomListAdapter(ctx, produtosCopy);
    int viewHeight=30;
    if(listAdapter.getCount()==1)
    {
        listHeight = MyMath.convertDpToPixels(viewHeight, ctx);
    }
   else if(listAdapter.getCount() <= 3) {
        listHeight = MyMath.convertDpToPixels(produtos.size() * viewHeight, ctx);
    }
    else
    {
        listHeight = MyMath.convertDpToPixels(3 * viewHeight, ctx);
    }
    listView.setAdapter(listAdapter);
//for(int i=0; i<produtos.size(); ++i)
//{
  //  addSingleView(produtos.get(i));
//}
//listView.setLayoutParams(params);

}

    private ArrayList<Produto> copyArray(ArrayList<Produto> produtos) {
    ArrayList<Produto> copy = new ArrayList<>();
    for (int i=0; i<produtos.size(); ++i) {
        copy.add(new Produto(ctx,produtos.get(i), true));
        }
    return copy;
    }


    public void addSingleView(Produto product)
{
    listHeight+=MyMath.converterSpToPixels(40,ctx);
    String key = product.name;
    if(!adapterProductsHash.containsKey(key)) {
        listAdapter.count+=1;
        Produto newProduct = new Produto(ctx,product, false);
        newProduct.setPrice(product.quantity*product.unitPrice);
        adapterProducts.add(newProduct);
        adapterProductsHash.put(newProduct.name, newProduct);
        listAdapter.notifyDataSetChanged();
    }
    else {
    //increase quanitity spinner of the bought products
    }

}
public double getTotalPrice()
{
    double value=0;
    for(int i=0; i<adapterProducts.size(); ++i)
    {
        value+= adapterProducts.get(i).getTotalPrice();
    }
    return value;
}
public void showHideList()
{
//listHeight =
    if(!listShown)
    {
        listParams.height = listHeight;
       listView.setVisibility(VISIBLE);
    }
    else {
        //listHeight = 0;
        listParams.height = 0;
        listView.setVisibility(GONE);
     }
    listShown = !listShown;
    listView.setLayoutParams(listParams);

}


class CustomListAdapter extends BaseAdapter
{
    int count;
    Context ctx;
    ArrayList<Produto> productsIn;
    LinearLayout verticalLayout;

  public  CustomListAdapter(Context ctx, ArrayList<Produto> products)
  {
      this.ctx = ctx;
      productsIn = products;
      count = products.size();
  }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return productsIn.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
           LinearLayout verticalLayout = new LinearLayout(ctx);
           verticalLayout.setOrientation(VERTICAL);
        if(productsIn !=null && productsIn.size() >0) {
            Produto currentProduct = (Produto) productsIn.get(position);
            if(currentProduct.getParent()!=null)
            {
                LinearLayout parent = (LinearLayout)  currentProduct.getParent();
                parent.removeView(currentProduct);
            }
            verticalLayout.addView(currentProduct);
            view = verticalLayout;
            view.setTag(cliente);


        }
        return view;
    }
}
}
