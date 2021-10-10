package netinho.isasselling.Manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import netinho.isasselling.R;

public class Handler {
    public static boolean isIntParsable(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public static boolean isDoubleParsable(String input){
        try{
            Double.parseDouble(input);
            return true;
        }catch(Exception e){
            return false;
        }
    }
  public static String formartDoubleToString(Double x, String prefix) {
      DecimalFormat numberFormat = new DecimalFormat("0.00");
      return prefix + numberFormat.format(x);
  }

    public static AlertDialog.Builder setAlertDialog (Context ctx,String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        return alertDialog;
    }
    //json
    public static String getArrayName(String str)
        {
            String toReturn = "";
            for(int i=0; i<str.length(); ++i)
            {
                if(str.charAt(i)=='[')
                {
                    return toReturn;
                }
                if(str.charAt(i)!= '{' && str.charAt(i)!='\"' && str.charAt(i)!=':')
                {
                    toReturn+=str.charAt(i);

                }
            }
            return toReturn;
        }
    static public void setSpinnerAdapter(Context ctx, int length, Spinner spinner)
    {
        List<CharSequence> list100 =
                new ArrayList<CharSequence>(Arrays.asList(MyMath.makeArraySequence(length)));
        ArrayAdapter spnAdapter = new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                list100);
        spinner.setAdapter(spnAdapter);
    }
    static public void setSpinnerAdapter_stringArray(Context ctx, String [] strings, Spinner spn,int layout)
    {

        List<CharSequence> listStrings = new ArrayList<CharSequence>(Arrays.asList(strings));
        ArrayAdapter<CharSequence> spnAdapter = new ArrayAdapter<CharSequence>(ctx, layout,listStrings);
        spn.setAdapter(spnAdapter);

    }
    static public void setSpinnerAdapter_stringArray(Context ctx, String [] strings, Spinner spn)
    {
        List<CharSequence> listStrings = new ArrayList<CharSequence>(Arrays.asList(strings));
        ArrayAdapter spnAdapter = new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                listStrings);
        spn.setAdapter(spnAdapter);

    }
    public final static String[] meses  = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
     public static String getMonth(int month) {
        return meses[month];
    }
      public static int getMonth(String monthStr) { return Arrays.asList(meses).indexOf(monthStr); }

public static String[] sliptCalendarDate(String date)
{
    return date.split("/");
}
public static String[] getCalendarFromNowOn(int monthsQuantityOn)
{
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String[] dataStrings = new String[monthsQuantityOn];
        for(int i=0; i<monthsQuantityOn; i++)
        {
            String data = "";
            int month = calendar.get(Calendar.MONTH);
            //data += (calendar.get(Calendar.DAY_OF_MONTH)) + "/" + Handler.getMonth(month) + "/" + calendar.get(Calendar.YEAR);
            data +=  Handler.getMonth(month) + "/" + calendar.get(Calendar.YEAR);
            dataStrings[i] = data;
            calendar.add(Calendar.MONTH, 1);
        }
        return dataStrings;
}
public static String[] initParcelasPadrao(int p, double divida) {
        HashMap<Integer, Double> parcelasPadrao = new HashMap<>();
        //calar o valor de cada parcela para a dada quantia de parcelas
        for(int i=1; i<=p; ++i)
        {
            parcelasPadrao.put(i, divida/i);
        }
        // converter para String
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


}
