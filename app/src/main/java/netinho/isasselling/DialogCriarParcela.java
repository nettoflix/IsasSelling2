package netinho.isasselling;

import android.app.Dialog;
import android.content.Context;
import android.opengl.Visibility;
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

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

public class DialogCriarParcela extends Dialog {
    DialogParcelas dialogParcelas;
    CheckBox boxPersonalizado;
    Spinner spinner_parcelaPersonalizado;
    EditText et_valorPersonalizado;
    CheckBox boxPadrao;
    Spinner spinner_parcelaPadrao;
    Spinner spinner_dataPadrao;

    MainActivity ctx;
    Cliente cliente;
    LinearLayout father;
    public DialogCriarParcela(@NonNull MainActivity ctx, DialogParcelas dialogParcelas, Cliente cliente, double divida) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        this.dialogParcelas = dialogParcelas;

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
        et_valorPersonalizado = father.findViewById(R.id.valorBox);
         //listeners
        cancelar.setOnClickListener(listener);
        criar.setOnClickListener(listener);
        boxPadrao.setOnClickListener(listener);
        boxPersonalizado.setOnClickListener(listener);




         spinner_parcelaPadrao = father.findViewById(R.id.spinner_parcela);

        String[] parcelas = Handler.initParcelasPadrao(7, divida);
        List<CharSequence> list=
                new ArrayList<CharSequence>(Arrays.asList(parcelas));
        ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
               R.layout.spinner_layout2,
                list);
     spinner_parcelaPadrao.setAdapter(spnAdapter);
     spinner_dataPadrao = father.findViewById(R.id.spinner_dataPadrao);

     spinner_parcelaPersonalizado = father.findViewById(R.id.spinner_dataPersonalizado);
     initSpinner_data();




    }

    private void initSpinner_data() {
        String[] datasCalendario = Handler.getCalendarFromNowOn(12);
        Handler.setSpinnerAdapter_stringArray(ctx,datasCalendario,spinner_dataPadrao);
        Handler.setSpinnerAdapter_stringArray(ctx,datasCalendario,spinner_parcelaPersonalizado);
    }

    public DialogCriarParcela(@NonNull MainActivity ctx, DialogParcelas dialogParcelas, Cliente cliente, double divida, boolean esconderPersonalizado) {
        super(ctx);
        this.ctx = ctx;
        this.cliente = cliente;
        this.dialogParcelas = dialogParcelas;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        father = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_criar_parcela, null);
        setContentView(father, params);
        //Buttons
        button_listener listener = new button_listener();
        Button cancelar = father.findViewById(R.id.parcela_cancelar);
        cancelar.setTag("cancelar");
        Button criar = father.findViewById(R.id.parcela_criar);
        criar.setTag("criar");
        et_valorPersonalizado = father.findViewById(R.id.valorBox);
        spinner_parcelaPersonalizado = father.findViewById(R.id.spinner_dataPersonalizado);
        boxPersonalizado= father.findViewById(R.id.boxPersonalizado);
        //CheckBoxes
        boxPadrao = father.findViewById(R.id.boxPadrao);
        boxPadrao.setTag("boxPadrao");
        //listeners
        cancelar.setOnClickListener(listener);
        criar.setOnClickListener(listener);
        spinner_parcelaPadrao = father.findViewById(R.id.spinner_parcela);

        String[] parcelas = Handler.initParcelasPadrao(7, divida);
        List<CharSequence> list=
                new ArrayList<CharSequence>(Arrays.asList(parcelas));
        ArrayAdapter spnAdapter= new ArrayAdapter(ctx,
                R.layout.spinner_layout2,
                list);
        spinner_parcelaPadrao.setAdapter(spnAdapter);
        spinner_dataPadrao = father.findViewById(R.id.spinner_dataPadrao);
        String [] dataStrings = Handler.getCalendarFromNowOn(16);
        Handler.setSpinnerAdapter_stringArray(ctx, dataStrings, spinner_dataPadrao,R.layout.spinner_layout2);
        et_valorPersonalizado.setVisibility(View.GONE);
        spinner_parcelaPersonalizado.setVisibility(View.GONE);
        boxPersonalizado.setVisibility(View.GONE);
        boxPadrao.setChecked(true);
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
                    ctx.FragmentAdapter.clients.updateDivida();
                    DialogCriarParcela.this.dismiss();

                }
                else if(boxPersonalizado.isChecked())
                {
                    criarParcelaPersonalizado();
                    ctx.FragmentAdapter.clients.updateDivida();
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
    private void criarParcelaPersonalizado()
    {
        double valor =0;
        String valorStr = et_valorPersonalizado.getText().toString();
        if(Handler.isDoubleParsable(valorStr)) valor = Double.valueOf(valorStr);
        String data = spinner_parcelaPersonalizado.getSelectedItem().toString();
        Parcela parcela = new Parcela(ctx,data,valor);
        dialogParcelas.parcelas.add(parcela);
        dialogParcelas.addParcelaOnLayout(parcela);
    }
    private void criarParcelaPadrao() {


        String selectedParcela = spinner_parcelaPadrao.getSelectedItem().toString();
        Double valor = getValor(selectedParcela);
        int quantidade = getQuantidade(selectedParcela);
        String data="";
        //init calendar instance
        Calendar calendar = Calendar.getInstance();
        //get the selected date from the spinner
        int selectedPosition = spinner_dataPadrao.getSelectedItemPosition();
        data = spinner_dataPadrao.getAdapter().getItem(selectedPosition).toString();
        String[] splitDayMonthYear= Handler.sliptCalendarDate(data);
        //int day = Integer.parseInt(splitDayMonthYear[0]);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = Handler.getMonth(splitDayMonthYear[0]);
        int year = Integer.parseInt(splitDayMonthYear[1]);
        //set the calendar to that date
        //calendar.set(year, month, day);
        calendar.set(year, month, day);
        //add the months according to the quantity of parcelas
        for (int i = 0; i < quantidade; i++)
        {
            month = calendar.get(Calendar.MONTH);
            data = (calendar.get(Calendar.DAY_OF_MONTH)) + "/" + Handler.getMonth(month) + "/" + calendar.get(Calendar.YEAR);
            Parcela parcela = new Parcela(ctx,data,valor);
            dialogParcelas.parcelas.add(parcela);
            dialogParcelas.addParcelaOnLayout(parcela);
            calendar.add(Calendar.MONTH, 1);

        }





        // dialogParcelas.addParcela(parcela, DialogParcelas.Type.padrao);

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
