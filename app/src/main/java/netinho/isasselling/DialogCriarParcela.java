package netinho.isasselling;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import netinho.isasselling.Manager.Handler;

public class DialogCriarParcela extends Dialog {
    DialogParcelas dialogParcelas;
    CheckBox boxPersonalizado;
    CheckBox boxPadrao;
    Spinner spinner_parcelaPadrao;
    Spinner spinner_dataPadrao;
    HashMap<Integer, Double> parcelasPadrao;
    Context ctx;
    Cliente cliente;
    LinearLayout father;
    public DialogCriarParcela(@NonNull Context ctx, DialogParcelas dialogParcelas, Cliente cliente, double divida) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        this.dialogParcelas = dialogParcelas;
        parcelasPadrao = new HashMap<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        father = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_criar_parcela, null);
        setContentView(father, params);
        //Buttons
        button_listener listener = new button_listener();
        Button cancelar = father.findViewById(R.id.parcela_cancelar);
        cancelar.setTag("cancelar");
        Button criar = father.findViewById(R.id.parcela_criar);
        criar.setTag("criar");
        //CheckBoxes
         boxPadrao = father.findViewById(R.id.boxPadrao);
         boxPadrao.setTag("boxPadrao");
         boxPadrao.setOnClickListener(listener);
         boxPersonalizado= father.findViewById(R.id.boxPersonalizado);
         boxPersonalizado.setTag("boxPersonalizado");
         //EditTexts

         //listeners
        cancelar.setOnClickListener(listener);
        criar.setOnClickListener(listener);
        boxPadrao.setOnClickListener(listener);
        boxPersonalizado.setOnClickListener(listener);




         spinner_parcelaPadrao = father.findViewById(R.id.spinner_parcela);
        initParcelasPadrao(7, divida);
        String[] parcelas = convertParcelasToString();
        List<CharSequence> list=
                new ArrayList<CharSequence>(Arrays.asList(parcelas));
        ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                list);
    spinner_parcelaPadrao.setAdapter(spnAdapter);


     spinner_dataPadrao = father.findViewById(R.id.spinner_dataPadrao);
     initSpinner_dataPadrao();

    }
    private void initSpinner_dataPadrao()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String[] dataStrings = new String[6];
        for(int i=0; i<6; i++)
        {
            String data = "";
            int month = calendar.get(Calendar.MONTH);
            data += calendar.get(Calendar.DAY_OF_MONTH) + "/" + Handler.getMonth(month) + "/" + calendar.get(Calendar.YEAR);
            Log.d("Data: ", data);
            dataStrings[i] = data;
            calendar.add(Calendar.MONTH, 1);

        }
        Handler.setSpinnerAdapter_stringArray(ctx, dataStrings, spinner_dataPadrao);
    }

    private String[]convertParcelasToString() {
        String[] formated = new String[parcelasPadrao.size()];
        Integer[] x = new Integer[parcelasPadrao.size()];
        parcelasPadrao.keySet().toArray(x);
        Double[] values = new Double[(parcelasPadrao.size())];
        parcelasPadrao.values().toArray(values);
        for(int i=0; i<parcelasPadrao.size(); i++)
        {
            formated[i] = x[i].toString() + "x" + Handler.formartDoubleToString(values[i].doubleValue(), "R$");
        }
        return formated;
    }

    private void initParcelasPadrao(int p, double divida) {
        for(int i=1; i<=p; ++i)
        {
            parcelasPadrao.put(i, divida/i);
        }

    }
    class button_listener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.getTag().equals("cancelar"))
            {
                DialogCriarParcela.this.dismiss();
            }
            else if(v.getTag().equals("criar"))
            {
                if(boxPadrao.isChecked())
                {
                    criarParcelaPadrao();

                }
                else if(boxPersonalizado.isChecked())
                {
                    EditText quantidadeBox = father.findViewById(R.id.quantidadeBox);
                    EditText valorBox = father.findViewById(R.id.valorBox);
                    String quantidadeStr = quantidadeBox.getText().toString();
                    String valorStr = valorBox.getText().toString();
                    int quantidadeInt=0;
                    double valorDouble=0;
                    if(Handler.isIntParsable(quantidadeStr)) {
                        quantidadeInt = Integer.parseInt(quantidadeStr);
                    }
                  //  if (valorStr.contains(",")) {
                    //    valorStr = valorStr.replace(',', '.');
                  //  }
                    valorStr = valorStr.replace(',','.');
                    if (Handler.isDoubleParsable(valorStr)) {
                        valorDouble = Double.parseDouble(valorStr);
                    }
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    String data = day + "-" + month + "-" + year;
                    Parcela parcela = new Parcela(ctx,data,quantidadeInt,valorDouble);
                    dialogParcelas.addParcela(parcela);
                    DialogCriarParcela.this.dismiss();
                }
            }
            else if(v.getTag().equals("boxPadrao"))
            {
                boxPersonalizado.setChecked(false);
            }
            else if(v.getTag().equals("boxPersonalizado"))
            {
                boxPadrao.setChecked(false);
            }
        }


    }
    private int getQuantidade(String selectedParcela) {
        int index = selectedParcela.indexOf("x");
        String value = selectedParcela.substring(0,index);
        return Integer.parseInt(value);
    }
    private void criarParcelaPadrao() {


        String selectedParcela = spinner_parcelaPadrao.getSelectedItem().toString();
        Double valor = getValor(selectedParcela);
        int quantidade = getQuantidade(selectedParcela);
        String data="";

        for (int i = 0; i < quantidade; i++)
        {
        int selectedPosition = spinner_dataPadrao.getSelectedItemPosition();
        data = spinner_dataPadrao.getAdapter().getItem(selectedPosition+i).toString();
        Parcela parcela = new Parcela(ctx,data,1,valor);
        dialogParcelas.addParcela(parcela);
        }




        // dialogParcelas.addParcela(parcela, DialogParcelas.Type.padrao);
        DialogCriarParcela.this.dismiss();
    }

    private Double getValor(String selectedParcela) {
        int index = selectedParcela.indexOf("$");
        String value = selectedParcela.substring(index+1, selectedParcela.length());
        value = value.replace( ',', '.');
        return Double.parseDouble(value);
    }
    /*public String replace(String str, char oldChar, char newChar)
    {
        String oldStr= "";
        oldStr += oldChar;
        if (str.contains(oldStr)) {
            str = str.replace(oldChar, newChar);
        }
     return str;
    }*/

}
