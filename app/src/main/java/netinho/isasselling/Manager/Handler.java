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
import java.util.List;
import java.util.Scanner;

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
    static public void setSpinnerAdapter_stringArray(Context ctx, String [] strings, Spinner spn)
    {
        List<CharSequence> listStrings = new ArrayList<CharSequence>(Arrays.asList(strings));
        ArrayAdapter spnAdapterMarcas = new ArrayAdapter(ctx,
                android.R.layout.simple_spinner_item,
                listStrings);
        spn.setAdapter(spnAdapterMarcas);
    }
    public final static String[] meses  = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
     public static String getMonth(int month) {
        return meses[month];
    }

}
