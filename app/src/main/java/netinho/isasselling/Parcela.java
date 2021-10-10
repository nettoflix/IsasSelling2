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
    TextView nome;
    TextView valor;
    String data;
    double valorDaParcela;

    public Parcela(Context ctx, String data, double valor) {
        super(ctx);
        LinearLayout horizontal = new LinearLayout(ctx);
        int textSize = 24;
        this.data = data;
        this.valorDaParcela = valor;
        this.nome = new TextView(ctx);
        DesignByCode.setTextViewStyle(this.nome, textSize, Color.BLACK, ctx);
        this.nome.setText(data);
        this.valor = new TextView(ctx);
        DesignByCode.setTextViewStyle(this.valor, textSize, Color.BLACK, ctx);
        this.valor.setText(Handler.formartDoubleToString(valor,"R$"));
        horizontal.addView(this.nome);
        horizontal.addView(this.valor);
      this.addView(horizontal);
    }
    public void updateParcela()
    {
        this.valor.setText(Handler.formartDoubleToString(valorDaParcela,"R$"));
    }

}
