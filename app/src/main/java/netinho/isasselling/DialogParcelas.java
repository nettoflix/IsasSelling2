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
    Button deletarAllParcelas;
    ArrayList<Parcela>parcelas;
    boolean dialogIsOn = false;
    public DialogParcelas(@NonNull MainActivity ctx, Cliente cliente) {
        super(ctx);
        this.ctx = ctx;
        this.cliente =cliente;
        parcelas = new ArrayList<>();
        cliente.parcelasReference = parcelas;

        if(cliente.getSavedParcelas().size()>0)
        {
            loadParcelas(cliente.getSavedParcelas());
        }
    }
    public void init() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = DesignByCode.getScreenDimensions(ctx).x -  DesignByCode.getScreenDimensions(ctx).x/4;
        params.height = DesignByCode.getScreenDimensions(ctx).y - DesignByCode.getScreenDimensions(ctx).y/3;
        LinearLayout father = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_parcelas, null);
            parcelas_vertical = father.findViewById(R.id.parcelas_vertical);
            Button_Listener listener = new Button_Listener();
            add_parcela = father.findViewById(R.id.add_parcela);
            add_parcela.setTag("add_parcela");
            add_parcela.setOnClickListener(listener);
            deletarAllParcelas = father.findViewById(R.id.deletarAllParcelas);
            deletarAllParcelas .setTag("deletarAllParcelas");
            deletarAllParcelas .setOnClickListener(listener);
        dialogIsOn = true;
        for(Parcela p : parcelas)
        {
            addParcelaOnLayout(p);
        }
        this.setContentView(father,params);
    }

    public void addParcelaOnLayout(Parcela parcela)
    {
       // if(t == Type.padrao)
        //{
                parcela.setOnClickListener(new ClickListener());
                parcela.setOnLongClickListener(new LongClickListener());
                parcelas_vertical.addView(parcela);

        //}

    }
    class Button_Listener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {


                if (v.getTag().equals("add_parcela")) {
                    double dividaDisponivel = dividaTotal - getParcelasValues();
                    if (dividaDisponivel > 0) {
                    DialogCriarParcela dialog = new DialogCriarParcela(ctx, DialogParcelas.this, cliente, dividaDisponivel);
                    dialog.show();
                }
                }
                if(v.getTag().equals("deletarAllParcelas"))
                {
                    AlertDialog.Builder alertDialog = Handler.setAlertDialog(ctx, "Deletar Todas as parcelas", "Você tem certeza que deseja deletar todas as parcelas ao invés de pagá-las?");
                    alertDialog.setNegativeButton(android.R.string.no, null);
                    alertDialog.setPositiveButton(android.R.string.yes, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            removeAllParcelas();
                        }
                    });
                    alertDialog.show();
                }
        }
    }
    public void loadParcelas(ArrayList<Parcela> savedParcelas) {
        for(int i=0; i<savedParcelas.size(); i++)
        {
            parcelas.add(savedParcelas.get(i));
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
           AlertDialog.Builder alertDialog = Handler.setAlertDialog(ctx, "Pagar Parcela", "Você tem certeza que deseja pagar essa parcela?");
            // A null listener allows the button to dismiss the dialog and take no further action.
            alertDialog.setNegativeButton(android.R.string.no, null);
           alertDialog.setPositiveButton(android.R.string.yes, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        double newValue = cliente.divida - parcela.valorDaParcela;
                        removeParcela(parcela);
                        cliente.dialogClienteDetails.updateDividaTotal(newValue);
                        ctx.FragmentAdapter.clients.updateDivida();
                        cliente.checarDividaAndRemoveProducts(newValue);

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
        parcelas_vertical.removeView(parcela);
        parcelas.remove(parcela);

    }
    public void removeAllParcelas()
    {
        Iterator<Parcela> itr = parcelas.iterator();
        while (itr.hasNext()) {
            Parcela parc = itr.next();
                itr.remove();
                parcelas_vertical.removeView(parc);
        }

    }
}
