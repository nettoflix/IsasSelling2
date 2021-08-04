package netinho.isasselling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import netinho.isasselling.CustomView.CustomListAdapter;
import netinho.isasselling.Manager.MyMath;

public class CustomListManager extends LinearLayout {
    ArrayList<Produto> products;
    ArrayList<String> categories;
    MainActivity ctx;
    Cliente cliente;
    ArrayList<CustomView> customViews;
   ArrayList<MyMath.Pair<TextView, ListView>> categoryList;
    public CustomListManager(Cliente cliente, MainActivity context, ArrayList<Produto> products, ArrayList<String> categories) {
        super(context);
        this.cliente = cliente;
        this.categories = categories;
        ctx = context;
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.products= products;
        customViews = new ArrayList<>();
       // findCategories(products);
        final Spinner marcaSpinner = new Spinner(ctx);
        String marcas[] = {"Todas","Avon", "Natura","Oboticário"};
        List<CharSequence> list = new ArrayList<CharSequence>(Arrays.asList(marcas));
        ArrayAdapter spnAdapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item,
                list);
        marcaSpinner.setAdapter(spnAdapter);
        marcaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             String marca = parent.getSelectedItem().toString();
             if(!marca.equals("Todas"))
             {
                 for (int i = 0; i <customViews.size() ; i++) {
                     ArrayList<Produto> products = customViews.get(i).listAdapter.productsIn;
                      //   EstoqueFragment.showOnlyThisMarca(marca, products);
                     }
                 }
             }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.addView(marcaSpinner);
        initCategoryList();

    }


    private void initCategoryList() {
        for(int i=0; i<categories.size(); ++i)
        {
            String category = categories.get(i);
            CustomView customView = new CustomView(cliente,ctx, category);
            customView.setOnItemClick(new OnProduct_Listener());
            customView.addToList(getProducts_of_thisCategory(category));
            customView.setOnClickListener(new Category_Listener());
            customViews.add(customView);
             this.addView(customView);
        }
    }

    private ArrayList<Produto> getProducts_of_thisCategory(String category) {
        ArrayList<Produto> productsOfCategory = new ArrayList<>();
        for(int i=0; i<products.size(); ++i)
        {
            Produto product = products.get(i);
            if(product.category.equals(category))
            {
                productsOfCategory.add(product);
            }
        }
        return productsOfCategory;
    }
    class Category_Listener implements OnClickListener
    {
        @Override
        public void onClick(View view) {
            CustomView cv = (CustomView) view;
            cv.showHideList();
        }
    }
    public boolean onClickOnProduct(final Produto product, final Cliente cliente, final AdapterView<?> adapterView)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setTitle("Compra de produto");
        alertDialog.setMessage("Você tem certeza que deseja comprar esse produto?");
        // Specifying a listener allows you to take an action before dismissing the dialog.
        // The dialog is automatically dismissed when a dialog button is clicked.
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Continue with delete operation
                cliente.dialogClienteDetails.addToBoughtProducts(product);
                //ctx.RemoveFromEstoque(product);
                //product.updateSpinner();
            }

        });
        // A null listener allows the button to dismiss the dialog and take no further action.
        alertDialog.setNegativeButton(android.R.string.no, null);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();
        return false;
    }
    class OnProduct_Listener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            Produto product = (Produto) adapterView.getItemAtPosition(pos);
            Cliente cliente = (Cliente) view.getTag();
            onClickOnProduct(product, cliente, adapterView);

        }
    }
}
