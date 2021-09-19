package netinho.isasselling;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;
import netinho.isasselling.Manager.MyMath;


public class Parcela extends LinearLayout {
    Spinner spinner;
    TextView nome;
    TextView valor;
    String data;
    int quantidade;
    double valorDaParcela;
    double total;
    public Parcela(Context ctx, String data, int quantidade, double valor) {
        super(ctx);
        LinearLayout horizontal = new LinearLayout(ctx);
        int textSize = 12;
        this.data = data;
        this.valorDaParcela = valor;
        this.quantidade = quantidade;
        this.nome = new TextView(ctx);
        DesignByCode.setTextViewStyle(this.nome, textSize, Color.BLACK, ctx);
        this.nome.setText(data);
        this.valor = new TextView(ctx);
        DesignByCode.setTextViewStyle(this.valor, textSize, Color.BLACK, ctx);
        this.valor.setText(Handler.formartDoubleToString(valor,"R$"));
        this.spinner = new Spinner(ctx);
        List<CharSequence> list = new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(quantidade)));
        ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                list);
        this.spinner.setAdapter(spnAdapter);
        spinner.setClickable(false);
        spinner.setFocusable(false);
        this.total = quantidade * valor;
        horizontal.addView(this.nome);
        horizontal.addView(this.spinner);
        horizontal.addView(this.valor);
      this.addView(horizontal);
    }
    public void updateParcela()
    {
        this.valor.setText(Handler.formartDoubleToString(valorDaParcela,"R$"));
    }
public String getSelectedQuantity()
{
 return spinner.getSelectedItem().toString();
}

    public void decreaseParcela(int quantidade_de_parcelas) {
        this.quantidade -= quantidade_de_parcelas;
        List<CharSequence> list = new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(quantidade)));
        ArrayAdapter spnAdapter= new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                list);
        this.spinner.setAdapter(spnAdapter);

    }
}
