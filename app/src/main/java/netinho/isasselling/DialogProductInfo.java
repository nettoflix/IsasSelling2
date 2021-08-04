package netinho.isasselling;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

public class DialogProductInfo extends Dialog {
    TextView tvNome;
    TextView tvMarca;
    TextView tvPrice;
    public DialogProductInfo(@NonNull MainActivity context) {
        super(context);
        LinearLayout dialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.produto_info, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width =  DesignByCode.getScreenDimensions(context).x - (DesignByCode.getScreenDimensions(context).x/4);
        layoutParams.height =  290;
        tvNome = dialogLayout.findViewById(R.id.info_nome);
        tvMarca = dialogLayout.findViewById(R.id.info_marca);
        tvPrice = dialogLayout.findViewById(R.id.info_preço);
        this.setContentView(dialogLayout, layoutParams);
    }
    public void update(String nome, String marca, Double price)
    {
        tvNome.setText(nome);
        tvMarca.setText(marca);
        tvPrice.setText(Handler.formartDoubleToString(price, "R$"));
    }
}
