package netinho.isasselling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

public class DialogClienteDetails extends Dialog {
    MainActivity ctx;
    Cliente cliente;

    Button btn_addCompra;
    Button btn_resetar;
    Button btn_addPagamento;
    Button btn_parcelas;

    LinearLayout listHolder;
    TextView tv_dividaTotal;
    TextView tv_saldo;
    double vDividaTotal=0;
    //
    boolean parcelasDialogIsOn=false;
    ComprasDialog comprasDialog;
    HashMap<String, CustomView> hashCategories;
    public DialogClienteDetails(@NonNull MainActivity ctx, Cliente cliente , LinearLayout father, String nome) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        hashCategories = new HashMap<>();
        //setUp detailsDialog
        int textSize = 14;
        int textColor = Color.BLACK;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width =  DesignByCode.getScreenDimensions(ctx).x- (DesignByCode.getScreenDimensions(ctx).x/4);
        LinearLayout dialog_client = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_client, null, false);
        TextView name = dialog_client.findViewById(R.id.dlgClient_name);
        name.setText(nome);
        DesignByCode.setTextViewStyle(name, textSize, textColor, getContext());
        tv_dividaTotal = dialog_client.findViewById(R.id.id_clienteDivida);
        tv_saldo = dialog_client.findViewById(R.id.id_clienteSaldo);

        this.setContentView(dialog_client, layoutParams);
        //init the views inside the dialog
        Button_Listener btnListener = new Button_Listener();


        btn_resetar = (Button) dialog_client.findViewById(R.id.btn_resetar);
        btn_resetar.setTag("reset");
        btn_resetar.setOnClickListener(btnListener);
        btn_parcelas = (Button) dialog_client.findViewById(R.id.btn_parcelas);
        btn_parcelas.setTag("parcelas");
        btn_parcelas.setOnClickListener(btnListener);
        btn_addCompra = (Button) dialog_client.findViewById(R.id.btn_addCompra);
        btn_addCompra.setOnClickListener( new Listener_btn_addCompra(cliente));
        btn_addPagamento = (Button) dialog_client.findViewById(R.id.btn_addPagamento);
        btn_addPagamento.setOnClickListener( new Listener_btn_addPagamento());
        listHolder = dialog_client.findViewById(R.id.list__productsBought_holder);

        //remember its "saldo" that was already loaded
        tv_saldo.setText(Handler.formartDoubleToString(cliente.saldo, "R$"));

        if(cliente.parcelasDialog ==null) cliente.parcelasDialog = new DialogParcelas(ctx,cliente);

    }





    public void addToBoughtProducts(Produto product)
    {
        String key = product.category;
        CustomView categoryList;
        if(!hashCategories.containsKey(key)) //doesn't contain this category already
        {

            categoryList = new CustomView(cliente,ctx, product.category);
            categoryList.setTag("listView");
            categoryList.setOnClickListener(new Button_Listener());
            categoryList.setOnItemClick(new OnProduct_Listener());
            categoryList.addSingleView(product);
            //as this product is going to be in the list of bought products, only to show to the user what he has bought, the quantity spinner is not necessary,
            // so we will replace it with a view
            //    categoryList.adapterProductsHash.get(product.name).replaceSpinnerWithView(ctx);
            hashCategories.put(key, categoryList);
            listHolder.addView(categoryList);
            cliente.savedProducts.add(categoryList.adapterProductsHash.get(product.name));

        }
        //there is already at least one products of this category
        else {
            categoryList = hashCategories.get(key);

            if(categoryList.adapterProductsHash.containsKey(product.name))
            {
                if(categoryList.adapterProductsHash.get(product.name).price == product.price) //besides havin the same name, they have the same price, so indeed they are the same product
                {
                    // this product has already been bought, so just increase the quantity value
                    categoryList.adapterProductsHash.get(product.name).incrementQuantity(product.quantity);
                }
                else
                {
                    { //it is the first time this product has been bought, so add the view and everything
                        categoryList.addSingleView(product);

                    }
                }

            }
            else
            { //it is the first time this product has been bought, so add the view and everything
                categoryList.addSingleView(product);

            }
            //replace the product to update the quantity
            if(cliente.savedProducts.contains(categoryList.adapterProductsHash.get(product.name)))
            {
                cliente.savedProducts.remove(categoryList.adapterProductsHash.get(product.name));
                cliente.savedProducts.add(categoryList.adapterProductsHash.get(product.name));

            }
            else{
                cliente.savedProducts.add(categoryList.adapterProductsHash.get(product.name));
            }


        }

        updateDividaTotal(getTotalPrice_fromCategories() - cliente.pagoAcumulado);



    }
    public void removeBoughtProducts()
    {
        hashCategories.clear();
        listHolder.removeAllViews();
        cliente.savedProducts.clear();
        cliente.pagoAcumulado=0;
    }
    public void resetar() {
        removeBoughtProducts();
        ctx.FragmentAdapter.clients.decrementSaldoGeral(cliente.saldo);
        cliente.saldo = 0;
        tv_saldo.setText(Handler.formartDoubleToString(cliente.saldo, "R$"));
        cliente.parcelasDialog.removeAllParcelas();;
        updateDividaTotal(0.0);
        ctx.FragmentAdapter.clients.updateDivida();


    }
    private double getTotalPrice_fromCategories()
    {
        double value=0;
        ArrayList<CustomView> array= new ArrayList<>();
        array.addAll(hashCategories.values());
        for(int i=0; i<array.size(); ++i)
        {
            value+=array.get(i).getTotalPrice();
        }
        return value;
    }
    public void updateDividaTotal(double value) {
        vDividaTotal = value;
        String formatedDivida = Handler.formartDoubleToString(vDividaTotal, "R$");
        if(tv_dividaTotal!=null) {
            tv_dividaTotal.setText(formatedDivida);
        }
       // cliente.clientDebt.setText(formatedDivida);
       if(cliente.parcelasDialog !=null) cliente.parcelasDialog.updateDividaTotal(value);
        cliente.updateDividaTotal(value);
    }
    class Button_Listener implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            if(view.getTag().equals("listView"))
            {
                CustomView customListView = (CustomView) view;
                customListView.showHideList();
            }
            else if(view.getTag().equals("reset"))
            {
                AlertDialog.Builder alertDialog = Handler.setAlertDialog(ctx,"Reset", "Você tem certeza que a dívida foi quitada?");
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        resetar();
                    }
                });
                // A null listener allows the button to dismiss the dialog and take no further action.
                alertDialog.setNegativeButton(android.R.string.no, null);
                alertDialog.show();
            }
            else if(view.getTag().equals("parcelas"))
            {
                if(!cliente.parcelasDialog.dialogIsOn) cliente.parcelasDialog.init();
                cliente.parcelasDialog.show();
            }


        }
    } class Listener_btn_addPagamento implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                DialogAddPagamento dlg = new DialogAddPagamento(ctx,cliente);
                dlg.show();
        }
    }
        class Listener_btn_addCompra implements View.OnClickListener
    {
        Cliente cliente;
        public  Listener_btn_addCompra(Cliente cliente)
        {
            this.cliente = cliente;
        }
        //shows the dialog with the products from stock, what he can buy
        @Override
        public void onClick(View view) {
            comprasDialog = null;
            comprasDialog = new ComprasDialog(ctx, cliente);
            comprasDialog.showDialog();

        }
    }
    //Listener of a item inside the list of bought products
    class OnProduct_Listener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            final Produto product = (Produto) adapterView.getItemAtPosition(pos);
            DialogWithSpinner alertDialog = new DialogWithSpinner(cliente, ctx, product, "Remover Produto", "Você tem certeza que deseja remover esse produto?");

            alertDialog.show(ctx.getSupportFragmentManager(), "test dialog");


        }
    }
}
