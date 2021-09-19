package netinho.isasselling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

public class DialogParcelas extends Dialog {




    public enum Type{personalized, padrao};
    MainActivity ctx;
    Cliente cliente;
    double dividaTotal;
    LinearLayout parcelas_vertical;
    Button add_parcela;
    ArrayList<Parcela>parcelas;
    public DialogParcelas(@NonNull MainActivity ctx, Cliente cliente, double dividaTotal) {
        super(ctx);
        this.ctx = ctx;
        this.cliente =cliente;
        this.dividaTotal = dividaTotal;
        parcelas = new ArrayList<>();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = DesignByCode.getScreenDimensions(ctx).x -  DesignByCode.getScreenDimensions(ctx).x/4;
        params.height = DesignByCode.getScreenDimensions(ctx).y - DesignByCode.getScreenDimensions(ctx).y/3;
        LinearLayout father = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_parcelas, null);
        parcelas_vertical = father.findViewById(R.id.parcelas_vertical);
        add_parcela = father.findViewById(R.id.add_parcela);
        add_parcela.setTag("add_parcela");
        add_parcela.setOnClickListener(new Button_Listener());
        this.setContentView(father,params);
      //  if(cliente.getSavedParcelas().size()>0)
       // {
         //   loadParcelas(cliente.getSavedParcelas());
       // }
    }
    public void addParcela(Parcela parcela)
    {
       // if(t == Type.padrao)
        //{
                parcela.setOnClickListener(new ClickListener());
                parcela.setOnLongClickListener(new LongClickListener());
                parcelas.add(parcela);
                parcelas_vertical.addView(parcela);

        //}

    }
    class Button_Listener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            double dividaDisponivel = dividaTotal - getParcelasValues();
            if (dividaDisponivel > 0) {
                if (v.getTag().equals("add_parcela")) {
                    DialogCriarParcela dialog = new DialogCriarParcela(ctx, DialogParcelas.this, cliente, dividaDisponivel);
                    dialog.show();
                }
            }
        }
    }
    public void loadParcelas(ArrayList<Parcela> savedParcelas) {
        for(int i=0; i<savedParcelas.size(); i++)
        {
            addParcela(savedParcelas.get(i));
        }
    }

    private double getParcelasValues() {
        double value=0;
        for (int i = 0; i <parcelas.size(); i++) {
            Parcela parcela = parcelas.get(i);
            value+= parcela.valorDaParcela;
        }
        return value;
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Parcela parcela = (Parcela) v;
           AlertDialog.Builder alertDialog = Handler.setAlertDialog(ctx, "Pagar Parcela", "Você tem certeza que deseja pagar " + parcela.getSelectedQuantity() + " parcela?");
            // A null listener allows the button to dismiss the dialog and take no further action.
            alertDialog.setNegativeButton(android.R.string.no, null);
           alertDialog.setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int quantidade_de_parcelas = Integer.parseInt(parcela.getSelectedQuantity());
                   cliente.pagarDivida(quantidade_de_parcelas * parcela.valorDaParcela);
                   if(parcela.quantidade - quantidade_de_parcelas > 0) {
                       parcela.decreaseParcela(quantidade_de_parcelas);
                   } else
                   {
                       removeParcela(parcela);

                   }
                }
            });
            alertDialog.show();
        }
    }

    private class LongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder alertDialog = Handler.setAlertDialog(ctx, "Deletar Parcela", "Você tem certeza que deseja deletar essa parcela, ao invés de paga-lá?");
            alertDialog.setNegativeButton(android.R.string.no, null);
            alertDialog.setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Parcela parcela = (Parcela) v;
                    removeParcela(parcela);
                }
            });
            alertDialog.show();
            return true;
        }
    }
    public void updateDividaTotal(double value) {
        this.dividaTotal = value;
    }
    public void pagarParcela(double paidValue) { //o preço pago é usado para pagar a primeira parcela, o valor excedente paga de forma parcial a parcela subsequente
        for(int i=0; i<parcelas.size(); i++)
        {
            if(parcelas.get(i).valorDaParcela >= paidValue)
            {
                parcelas.get(i).valorDaParcela = parcelas.get(i).valorDaParcela- paidValue; //o valor que ainda falta pagar da parcela
                parcelas.get(i).updateParcela();
                break;

            }
            else
            {
                paidValue = paidValue - parcelas.get(i).valorDaParcela; //o valor excedente que será usado para pagar a parcela subsequente
                parcelas.get(i).valorDaParcela=0;
            }
        }
        Iterator<Parcela> itr = parcelas.iterator();
        while (itr.hasNext()) {
            Parcela parc = itr.next();
        if(parc.valorDaParcela ==0)
        {
            itr.remove();
            parcelas_vertical.removeView(parc);
        }
        }





    }
    public void removeParcela(Parcela parcela)
    {
        parcelas.remove(parcela);
        parcelas_vertical.removeView(parcela);
    }
    public void removeAllParcelas()
    {
        for (int i = 0; i <parcelas.size(); i++) {
            removeParcela(parcelas.get(i));
        }

    }
}
