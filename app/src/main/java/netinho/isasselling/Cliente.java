package netinho.isasselling;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;
import netinho.isasselling.Manager.MyMath;

public class Cliente extends LinearLayout  implements DialogWithSpinner.DialogListener{
    Set<Produto> savedProducts;
    ArrayList<Produto> loadedProducts;
    double divida;
    double saldo;
    double pagoAcumulado;
    String nome;

    TextView clientDebt;
    MainActivity ctx;
    LinearLayout father;
    LayoutInflater inflater;
    LinearLayout.LayoutParams params;
    DialogClienteDetails dialogClienteDetails;
    @SuppressLint("ResourceType")
    public Cliente(MainActivity ctx,String nome)
    {
        super(ctx);
        this.setClickable(true);
        //this.divida = 0.0;
        this.ctx = ctx;
        this.nome=nome;
        savedProducts = new TreeSet<Produto>();
        loadedProducts = new ArrayList<>();
        //init xml views
        inflater = LayoutInflater.from(ctx);
         father = (LinearLayout) inflater.inflate(R.layout.cliente_layout, null, false);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
         this.setOrientation(LinearLayout.HORIZONTAL);
         this.setLayoutParams(params);
        this.addView(father);
        this.setOnClickListener(new thisClickListener());
        this.setTag("show_dialog");

        //
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //params.setMarginStart(MyMath.convertDpToPixels(30,ctx));
        params.width = DesignByCode.getScreenDimensions(ctx).x;
        //params.height = DesignByCode.getScreenDimensions(activity).x;
        params.weight = 1.0f;
        father.setLayoutParams(params);
        int textSize = 20;
        int color = Color.BLACK;
        initInterface(color,textSize);
        //initDetailsDialog();

    }

    private void initInterface(int textColor, int textSize) {
        //Cliente  ox (interface
        LinearLayout nameCol = (LinearLayout) father.findViewById(R.id.nameCol);
        nameCol.setLayoutParams(params);
        LinearLayout priceCol = (LinearLayout) father.findViewById(R.id.priceCol);
        priceCol.setLayoutParams(params);
        TextView clientName= new TextView( ctx);
        clientName.setText(nome);
        DesignByCode.setTextViewStyle(clientName, textSize,textColor, ctx);
        clientDebt= new TextView( ctx);
        clientDebt.setText(Handler.formartDoubleToString(divida, "R$"));
        DesignByCode.setTextViewStyle(clientDebt, textSize, textColor, ctx);
        nameCol.addView(clientName);
        priceCol.addView(clientDebt);

    }

    public double getTotalPrice_fromLoadedProducts() {
        double value =0;
        for (int i = 0; i <loadedProducts.size(); i++) {
            Produto product = loadedProducts.get(i);
            value+= product.getTotalPrice()* product.quantity;
        }
        return value;
    }
    public void updateDividaTotal(double value)
    {
        String formatedDivida = Handler.formartDoubleToString(value, "R$");
        divida = value;
        clientDebt.setText(formatedDivida);

    }
    public void loadProducts() {
        for(int i=0; i<loadedProducts.size(); ++i)
        {
            dialogClienteDetails.addToBoughtProducts(loadedProducts.get(i));
        }
    }

public void updateSaldo(double valor)
{
    pagoAcumulado+=valor;
    saldo+=valor;
    dialogClienteDetails.tv_saldo.setText(String.valueOf(saldo));
    ctx.FragmentAdapter.clients.incrementSaldoGeral(valor);
    ctx.FragmentAdapter.clients.decrementDividaGeral(valor);

}
public void resetSaldo()
{
    saldo = 0;
    if(dialogClienteDetails!=null && dialogClienteDetails.tv_saldo!=null)  dialogClienteDetails.tv_saldo.setText(String.valueOf(saldo));
}
public void pagarDivida(double valor)
{
    if(valor <= divida) {

        double newValue = divida - valor;
        updateSaldo(valor);
        if (newValue <= 0) {
            newValue = 0;
             dialogClienteDetails.removeBoughtProducts();
            //
        }

        dialogClienteDetails.updateDividaTotal(newValue);
        dialogClienteDetails.parcelasDialog.pagarParcela(valor);

    }
}

    class thisClickListener implements View.OnClickListener
{
    //show the dialog with the information of the client and what he has bought
    @Override
    public void onClick(View view) {
        if(view.getTag().equals(Cliente.this.getTag())) {
            if(dialogClienteDetails==null) {
                dialogClienteDetails = new DialogClienteDetails(ctx, Cliente.this, Cliente.this.father, Cliente.this.nome);
                //now that we have the dialog, we can put the loaded products in it
                loadProducts();
            }
            dialogClienteDetails.show();
        }

    }
}




    //interface listener method from dialogWithSpinner Class
    @Override
    public void onClickDialog(Produto product) {
//boughtProducts.remove(product.category);
        CustomView list = (CustomView) product.getParent().getParent().getParent();

            list.removeProduct(product, dialogClienteDetails.hashCategories);
            savedProducts.remove(product);


    }

}
