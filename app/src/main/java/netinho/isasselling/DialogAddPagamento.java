
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

public class DialogAddPagamento extends Dialog {
    MainActivity ctx;
    Cliente cliente;
    Button btn_Add;
    TextView tv_Valor;
    public DialogAddPagamento(@NonNull MainActivity ctx, Cliente cliente) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(600, 200);
        LinearLayout dialog_addpagamento = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_addpagamento,null , false);
        this.setContentView(dialog_addpagamento,layoutParams);
        btn_Add = (Button) dialog_addpagamento.findViewById(R.id.addPagamento_add);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add();
            }
        });
        tv_Valor = (TextView)dialog_addpagamento.findViewById(R.id.addpagamento_Valor);


    }
    public void Add()
    {
        String input = tv_Valor.getText().toString();
        input = input.replace(",",".");
        double valor=0;
       if(Handler.isDoubleParsable(input))  //check for a valid input
       {
            valor = Double.valueOf(input);
           cliente.pagarDivida(valor);
           this.dismiss();
       }
       else
       {
           this.dismiss();
           final AlertDialog.Builder alertDialog = Handler.setAlertDialog(getContext(), "Pagamento inválido", "Formato incopatível");

           alertDialog.setNeutralButton(android.R.string.ok, new OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

               }
           });
           alertDialog.show();
       }

    }
    public void showDialog()
    {
        this.show();
    }

}
