package netinho.isasselling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import netinho.isasselling.Manager.Handler;

public class ComprasDialog extends Dialog {
    MainActivity ctx;
    Cliente cliente;
    TextView m_price;
    Spinner m_marca;
    TextView m_name;
    Spinner m_quantity;
    Button m_button_addCompra;
    public ComprasDialog(@NonNull MainActivity ctx, Cliente cliente) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(400, 600);
        LinearLayout dialog_addcompra = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog__addcompra,null , false);
        this.setContentView(dialog_addcompra,layoutParams);
        m_name = dialog_addcompra.findViewById(R.id.addcompra_productName);
        m_marca = dialog_addcompra.findViewById(R.id.addcompra_SpinnerMarca);
        m_price = dialog_addcompra.findViewById(R.id.addcompra_productPrice);
        m_quantity= dialog_addcompra.findViewById(R.id.addcompra_SpinnerQuantidade);
        m_button_addCompra = dialog_addcompra.findViewById(R.id.addcompra_addProduct);
        m_button_addCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCompra();
            }
        });
        Handler.setSpinnerAdapter(ctx, 100, m_quantity);
        String marcas[] ={"Avon","Natura" ,"Oboticário", "Jequiti"};
        Handler.setSpinnerAdapter_stringArray(ctx, marcas, m_marca);


    }

    public void showDialog()
    {
        this.show();
    }
    public void addCompra()
    {
        String name = m_name.getText().toString();
        String marca = m_marca.getSelectedItem().toString();
        String category = marca;
        double price=0;
        String priceString = m_price.getText().toString();
        priceString = priceString.replace(",",".");
        if(Handler.isDoubleParsable(priceString))
        {
            price = Double.valueOf(priceString);
        }
        else
        {
            final AlertDialog.Builder alertDialog = Handler.setAlertDialog(getContext(), "Produto inválido", "Formato de preço incopatível");

            alertDialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            alertDialog.show();
            this.dismiss();
            return;
        }

        int quantity = Integer.valueOf(m_quantity.getSelectedItem().toString());
       // public Produto(Activity ctx, String name, String category,String marca , double unitPrice, int quantity, boolean spin, int marginSpace)

      cliente.dialogClienteDetails.addToBoughtProducts(new Produto(ctx, name, category, marca, price,quantity, false, 0 ));
      this.dismiss();
    }
}
