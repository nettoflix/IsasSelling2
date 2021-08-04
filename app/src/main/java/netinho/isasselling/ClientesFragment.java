package netinho.isasselling;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import netinho.isasselling.Manager.DesignByCode;
import netinho.isasselling.Manager.Handler;

import static android.content.Context.MODE_PRIVATE;

public class ClientesFragment extends Fragment {
    LinearLayout verticalClients;
    Button dlgbtn_addClient;
    Button btn_AddClient;
    TextView tv_DividaGeral;
    double DividaGeral;
    TextView tv_SaldoGeral;
    double SaldoGeral;
    Dialog dlg_AddClient;
    MainActivity activity;
    ArrayList<Cliente> clientes;

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
        tv_DividaGeral = fragmentView.findViewById(R.id.fragmentclientes_DividaGeral);
        tv_SaldoGeral = fragmentView.findViewById(R.id.fragmentclientes_SaldoGeral);
        LinearLayout paiDosGerais = (LinearLayout) tv_SaldoGeral.getParent();
        paiDosGerais.setTag("resetSaldo");
        paiDosGerais.setOnLongClickListener(new OnLong_Listener());
        btn_AddClient = fragmentView.findViewById(R.id.button_addClients);
        btn_AddClient.setOnClickListener(new btnAddClients_listener(0));
        dlg_AddClient = new Dialog(getContext());
        dialogFatherLayout = (LinearLayout) inflater.inflate(R.layout.dialog_addclient,container, false);
        //ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = DesignByCode.getScreenDimensions(activity).x;
        //layoutParams.height = DesignByCode.getScreenDimensions(activity).y/4;
        dlg_AddClient.setContentView(dialogFatherLayout, layoutParams);
        dlgbtn_addClient = (Button) dlg_AddClient.findViewById(R.id.dlgButton_addClient);
        dlgbtn_addClient.setOnClickListener(new btnAddClients_listener(1));
       loadClientes();
        return fragmentView;

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
    public void decrementDividaGeral(double valor)
    {
        DividaGeral-=valor;
        tv_DividaGeral.setText(Handler.formartDoubleToString(DividaGeral, "R$"));
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
//        Log.d("Json loading", jString);
        if(jString !=null)
        {
            JSONObject json = null;
            JSONArray jClientesArray = null;
            try {
                json = new JSONObject(jString);
                jClientesArray = json.getJSONArray("Clientes");

            } catch (JSONException e) {
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
                    JSONObject saldoObject = itemsArray.getJSONObject(1);
                    JSONObject pagoAcumuladoObject = itemsArray.getJSONObject(2);
                    cliente.saldo = saldoObject.getDouble("saldo");
                    cliente.pagoAcumulado = pagoAcumuladoObject.getDouble("pagoAcumulado");
                    cliente.updateDividaTotal(cliente.getTotalPrice_fromLoadedProducts() - cliente.pagoAcumulado);
                    //cliente.loadProducts();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //watch out for these 2 methods for the moment, it may delay too much the initialization of the application
        updateDividaGeral();
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

                //CLIENTE OBJETO
                JSONObject clientObj = new JSONObject();
                JSONArray arrayOfItems = new JSONArray();
                //PRODUTO OBJETO
                JSONObject produtoObject = new JSONObject();
                produtoObject.put("Produtos", jsonProductArray);
                arrayOfItems.put(produtoObject);
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
                //Log.d("json", jString);

        } catch (JSONException e) {
            e.printStackTrace();
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
                        decrementDividaGeral(cliente.divida);
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
