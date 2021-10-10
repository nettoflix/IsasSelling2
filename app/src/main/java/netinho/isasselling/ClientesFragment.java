package netinho.isasselling;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

import static android.content.Context.MODE_PRIVATE;

public class ClientesFragment extends Fragment {
    LinearLayout verticalClients;
    Button dlgbtn_addClient;
    Button btn_AddClient;
    Spinner spn_dividaSpinner;
    TextView tv_DividaGeral;
    double DividaGeral;
    TextView tv_SaldoGeral;
    double SaldoGeral;
    Dialog dlg_AddClient;
    MainActivity activity;
    ArrayList<Cliente> clientes;
    EditText filtroText;
    //Button btn_save;
   // Button btn_load;
    @SuppressLint("ResourceType") LinearLayout dialogFatherLayout;

    public ClientesFragment(MainActivity activity)
    {
       this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View fragmentView = (LinearLayout) inflater.inflate(R.layout.fragment_clientes, container, false);
        //Get the vertical layout to lay out the clientes
        verticalClients = (LinearLayout)fragmentView.findViewById(R.id.verticalClients);
        //Array de clientes
        clientes = new ArrayList<Cliente>();
        //
        spn_dividaSpinner = fragmentView.findViewById(R.id.fragmentclientes_dividaSpinner);
        initMonthsSpinner();
        //Filtro do nome dos clientes
        filtroText = fragmentView.findViewById(R.id.fragmentclientes_filtro);
        filtroText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Log.d("Before: ", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Log.d("On: ", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("After: ", s.toString());
                String digitado = s.toString().toUpperCase(); //transforma em maisculo;
                filtrarClientes(digitado);
            }


        });

        tv_DividaGeral = fragmentView.findViewById(R.id.fragmentclientes_DividaGeral);
        tv_SaldoGeral = fragmentView.findViewById(R.id.fragmentclientes_SaldoGeral);

        LinearLayout paiDosGerais = (LinearLayout) tv_SaldoGeral.getParent();
        paiDosGerais.setTag("resetSaldo");
        paiDosGerais.setOnLongClickListener(new OnLong_Listener());

        btn_AddClient = fragmentView.findViewById(R.id.button_addClients);
        btn_AddClient.setOnClickListener(new btnAddClients_listener(0));
        dlg_AddClient = new Dialog(getContext());
        dialogFatherLayout = (LinearLayout) inflater.inflate(R.layout.dialog_addclient,container, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = DesignByCode.getScreenDimensions(activity).x;
        dlg_AddClient.setContentView(dialogFatherLayout, layoutParams);
        dlgbtn_addClient = (Button) dlg_AddClient.findViewById(R.id.dlgButton_addClient);
        dlgbtn_addClient.setOnClickListener(new btnAddClients_listener(1));
       loadClientes();
        return fragmentView;

    }
    private void filtrarClientes(String digitado) {

        for(Cliente cliente : clientes)
        {
            String nome = cliente.nome.toUpperCase(); //maiúsculo tb
            if(nome.contains(digitado))
            {
                cliente.show();
            }
            else
            {
                cliente.hide();
            }
        }
    }
    private void initMonthsSpinner() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String[] months = new String[16];
        //fill up the spinner with months to be selected from
        months[0] = "Geral";
        calendar.add(Calendar.MONTH, -3); //we want to be able to selecte the parcelas from at least  3 months past the current one
        months[1] = Handler.getMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
        calendar.add(Calendar.MONTH, 1);
        months[2] = Handler.getMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
        calendar.add(Calendar.MONTH, 1);
        months[3] = Handler.getMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
        calendar.add(Calendar.MONTH, 1);
        for(int i=4; i<months.length; i++) {
            String data = "";
            int month = calendar.get(Calendar.MONTH);
            data += Handler.getMonth(month) + "/" + calendar.get(Calendar.YEAR);
            months[i] = data;
            calendar.add(Calendar.MONTH, 1);
        }
        spn_dividaSpinner.setOnItemSelectedListener(new OnSelectedListener());

        Handler.setSpinnerAdapter_stringArray(activity, months, spn_dividaSpinner, R.layout.spinner_layout);
    }

    public void updateSaldoGeral() {
        SaldoGeral=0;
        for(int i=0; i<clientes.size(); i++)
        {
            SaldoGeral +=clientes.get(i).saldo;
        }
        tv_SaldoGeral.setText(Handler.formartDoubleToString(SaldoGeral, "R$"));
    }

    public void updateDividaGeral()
    {
       DividaGeral=0;
        for(int i=0; i<clientes.size(); i++)
       {
           DividaGeral +=clientes.get(i).divida;
        }
        tv_DividaGeral.setText(Handler.formartDoubleToString(DividaGeral, "R$"));
   }
   public String getMesSelecionado()
   {
       return spn_dividaSpinner.getSelectedItem().toString();

   }
public void updateDividaDoMes()
{

    double dividaDoMes=0;
    for(Cliente cliente : clientes)
    {
     dividaDoMes+= cliente.getDividaDoMes(getMesSelecionado());

    }

    tv_DividaGeral.setText(Handler.formartDoubleToString(dividaDoMes, "R$"));
}


    public void incrementSaldoGeral(double valor)
    {
        SaldoGeral+=valor;
        tv_SaldoGeral.setText(Handler.formartDoubleToString(SaldoGeral, "R$"));
    }
    public void decrementSaldoGeral(double valor)
    {
        SaldoGeral-=valor;
        tv_SaldoGeral.setText(Handler.formartDoubleToString(SaldoGeral, "R$"));
    }
  //  public void updateSaldoGeral()
  //  {
  //      SaldoGeral = 0;
  //      for(int i=0; i<clientes.size(); i++)
   //     {
   //         SaldoGeral +=clientes.get(i).divida;
   //     }
  //      tv_DividaGeral.setText(Handler.formartDoubleToString(DividaGeral, "R$"));
  //  }

    public void loadClientes()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("savedClients", MODE_PRIVATE);
        String jString = sharedPreferences.getString("jString", null);


     Log.d("Json loading", jString);
        if(jString !=null)
        {
            JSONObject json = null;
            JSONArray jClientesArray = null;
            try {
                json = new JSONObject(jString);
                jClientesArray = json.getJSONArray("Clientes");

            } catch (JSONException e) {;

                e.printStackTrace();
            }
            for (int b = 0; b < jClientesArray.length(); ++b) {
                try {

                    JSONObject jo = jClientesArray.getJSONObject(b);
                    String clientName = Handler.getArrayName(jo.toString());
                    Log.d("clientArray", jo.toString());
                    Log.d("cliente", clientName);
                    Cliente cliente = addClientToFragment(clientName);
                    JSONArray itemsArray = jo.getJSONArray(clientName);
                    JSONObject productObject = itemsArray.getJSONObject(0);
                    Log.d("itemsArray", itemsArray.toString());
                    JSONArray productsArray = productObject.getJSONArray("Produtos");
                    Log.d("produtosArray", productsArray.toString());
                    for (int p = 0; p < productsArray.length(); ++p) {
                        JSONObject currentlyProduct = productsArray.getJSONObject(p);
                        String name = currentlyProduct.getString("name");
                        String category = currentlyProduct.getString("category");
                        String marca = currentlyProduct.getString("marca");
                        String quantity = currentlyProduct.getString("quantity");
                        String unitPrice = currentlyProduct.getString("unitPrice");

                        //  Log.d(clientName + " name:", name);
                        // Log.d(clientName + " category:", category);
                        //Log.d(clientName + " quantity:", quantity);
                        //Log.d(clientName + " unityPrice:", unitPrice);
                        Produto product = new Produto(activity, name, category, marca, Double.parseDouble(unitPrice), Integer.parseInt(quantity), false, 80);
                        cliente.loadedProducts.add(product);
                        cliente.savedProducts.add(product);
                        //  Log.d("produto", productObject.toString());
                    }
                    JSONObject parcelaObject = itemsArray.getJSONObject(1);
                    JSONArray parcelasArray = parcelaObject.getJSONArray("Parcelas");
                    for (int i = 0; i <parcelasArray.length(); i++) {
                        JSONObject currentlyParcela = parcelasArray.getJSONObject(i);
                        double valor = currentlyParcela.getDouble("valor");
                        String data = currentlyParcela.getString("data");
                        Parcela parcela = new Parcela(activity,data , valor);
                        cliente.addToSavedParcelas(parcela);
                    }
                    //cliente.parcelasDialog = new DialogParcelas(activity, cliente);

                    JSONObject saldoObject = itemsArray.getJSONObject(2);
                    JSONObject pagoAcumuladoObject = itemsArray.getJSONObject(3);
                    cliente.saldo = saldoObject.getDouble("saldo");
                    cliente.pagoAcumulado = pagoAcumuladoObject.getDouble("pagoAcumulado");
                    cliente.updateDividaTotal(cliente.getTotalPrice_fromLoadedProducts() - cliente.pagoAcumulado);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        updateSaldoGeral();
        //
    }



    public void saveClients()
    {
        try {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("savedClients", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonClientsArray = new JSONArray();
        JSONObject json = new JSONObject();
            for(int i=0; i<clientes.size(); ++i) {
                Cliente cliente = clientes.get(i);
                Produto[] savedProductsArray = new Produto[cliente.savedProducts.size()];
                cliente.savedProducts.toArray(savedProductsArray);
                JSONArray jsonProductArray = new JSONArray();
                //saved products
                for (int p = 0; p < savedProductsArray.length; ++p) {
                    Produto currentlyProduct = savedProductsArray[p];
                    JSONObject jsonProduct = new JSONObject();
                    jsonProduct.put("name", currentlyProduct.name);
                    jsonProduct.put("category", currentlyProduct.category);
                    jsonProduct.put("marca",currentlyProduct.marca);
                    jsonProduct.put("quantity", currentlyProduct.quantity);
                    jsonProduct.put("unitPrice", currentlyProduct.unitPrice);
                    jsonProductArray.put(jsonProduct);
                }
                //saved parcelas
                ArrayList<Parcela> parcelas = cliente.parcelasReference;
                JSONArray jsonParcelasArray = new JSONArray();
                if(parcelas !=null)
                {
                    for (int j = 0; j <parcelas.size(); j++) {
                        Parcela parcela = parcelas.get(j);
                        JSONObject jsonParcela = new JSONObject();
                        jsonParcela.put("valor", parcela.valorDaParcela);
                        jsonParcela.put("data", parcela.data);
                        jsonParcelasArray.put(jsonParcela);
                    }
                }

                //CLIENTE OBJETO
                JSONObject clientObj = new JSONObject();
                JSONArray arrayOfItems = new JSONArray();
                //PRODUTO OBJETO
                JSONObject produtoObject = new JSONObject();
                produtoObject.put("Produtos", jsonProductArray);
                arrayOfItems.put(produtoObject);
                //PARCELAS OBJETO
                JSONObject parcelaObject = new JSONObject();
                parcelaObject.put("Parcelas", jsonParcelasArray);
                arrayOfItems.put(parcelaObject);

                //SALDO OBJETO
                JSONObject saldoObject = new JSONObject();
                saldoObject.put("saldo", cliente.saldo);
                arrayOfItems.put(saldoObject);
                //pagoAcumulado OBJETO
                JSONObject pagoAcumuladoObject = new JSONObject();
                pagoAcumuladoObject.put("pagoAcumulado", cliente.pagoAcumulado);
                arrayOfItems.put(pagoAcumuladoObject);
                if(arrayOfItems.length() > 0)
                {
                    clientObj.put(cliente.nome, arrayOfItems);
                }
                jsonClientsArray.put(clientObj);
            }
                json.put("Clientes", jsonClientsArray);



                String jString = json.toString();
                Log.d("jString in save", jString);
                editor.putString("jString", jString);
                editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void updateDivida() {
        if(getMesSelecionado().equals("Geral"))
        {
            updateDividaGeral();

        }
        else {
            updateDividaDoMes();
        }
    }

    class OnLong_Listener implements View.OnLongClickListener
    {

        @Override

        public boolean onLongClick(final View v) {
            if(v.getTag().equals("cliente"))
            {
                AlertDialog.Builder alertDialog = Handler.setAlertDialog(getContext(), "Remover Cliente", "Você deseja excluir esse cliente?");
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        Cliente cliente = (Cliente) v;
                        clientes.remove(cliente);
                        verticalClients.removeView(cliente);
                        updateDivida();
                        decrementSaldoGeral(cliente.saldo);
                    }
                });
                alertDialog.setNegativeButton(android.R.string.no, null);
                alertDialog.show();
            }
          if(v.getTag().equals("resetSaldo"))
          {
              AlertDialog.Builder alertDialog = Handler.setAlertDialog(getContext(), "Resetar Saldo", "Você deseja resetar o saldo?");
              alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                      // Continue with delete operation
                      SaldoGeral =0;
                      tv_SaldoGeral.setText("R$0.00");
                      for(int i=0; i<clientes.size(); i++)
                      {
                          clientes.get(i).resetSaldo();
                      }
                  }
              });
              alertDialog.setNegativeButton(android.R.string.no, null);
              alertDialog.show();

          }
            return true;
        }
    }
    //this class listen to 2 buttons and behave accordingly the keys
    class btnAddClients_listener implements View.OnClickListener
    {
        int key;
    public btnAddClients_listener(int key){
        this.key = key;
    }
        @Override
        public void onClick(View view) {
        //open dialog
           if(key==0)
           {
               dlg_AddClient.show();
           }
           //add the client
          else if(key==1)
           {
               EditText nameView = dlg_AddClient.findViewById(R.id.clientName);

               String name = nameView.getText().toString();
               addClientToFragment(name);
               //reset editText after adding the cliente
               nameView.setText("");
               dlg_AddClient.dismiss();
           }
         else if(key==2)
           {
               saveClients();
           }
         else if (key==3)
             {
                 loadClientes();
             }

        }
    }
    class OnSelectedListener implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           updateDivida();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public Cliente addClientToFragment(String name)
    {
        Cliente cliente = new Cliente(activity,name);
        cliente.setTag("cliente");
        cliente.setOnLongClickListener(new OnLong_Listener());
        clientes.add(cliente);
        verticalClients.addView(cliente);
        return cliente;
    }
}
