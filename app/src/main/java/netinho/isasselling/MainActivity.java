package netinho.isasselling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import netinho.isasselling.CustomView.CustomListAdapter;
import netinho.isasselling.Manager.DesignByCode;

public class MainActivity extends AppCompatActivity {
    public int measuredWidth = 0;
    public int measuredHeight = 0;
 HashMap<String,Produto> produtos;
 ArrayList<String> categories;
 PageAdapter FragmentAdapter;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        categories = new ArrayList<>();
        //init lists
        produtos = new HashMap<>();

        //init tab layout (clients/estoque)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Clientes"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       final ViewPager viewPager = (ViewPager)findViewById(R.id.ViewPager_tab);
     FragmentAdapter = new PageAdapter(getSupportFragmentManager(),this);
       viewPager.setAdapter(FragmentAdapter);
       tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00A6E8"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.BLACK, Color.parseColor("#ffffff"));
        WindowManager w = getWindowManager();
        this.setSupportActionBar(toolbar);
       //GET SCREEN DIMENSIONS IN PIXELS
       measuredWidth = DesignByCode.getScreenDimensions(this).x;
       measuredHeight = DesignByCode.getScreenDimensions(this).y;
    }

    private ArrayList<Produto> getProducts_of_thisCategory(String category, ArrayList<Produto> products) {
        ArrayList<Produto> productsOfCategory = new ArrayList<>();
        for(int i=0; i<products.size(); ++i)
        {
            Produto product = products.get(i);
            if(product.category.equals(category))
            {
                productsOfCategory.add(product);
            }
        }
        return productsOfCategory;
    }
//GETTERS
    @SuppressLint("LongLogTag")
    public Produto getProduct(String key)
    {
        if(produtos.containsKey(key))
        {
            return produtos.get(key);
        }
        else
        {
             Log.d("HashMap doesn't contay key: ",key);
        }
        return null;
    }
    public final HashMap<String,Produto> getAllProducts()
    {
        return produtos;
    }
    //CLIENT STUFF


protected void onPause() {

    super.onPause();
    if(FragmentAdapter.clients!=null)
    this.FragmentAdapter.clients.saveClients();
  //  if(FragmentAdapter.estoque!=null)
  //  this.FragmentAdapter.estoque.saveEstoque();
}



    public class PageAdapter extends FragmentPagerAdapter {

 MainActivity activity;
 ClientesFragment clients;
 //EstoqueFragment estoque;
        public PageAdapter(@NonNull FragmentManager fm, MainActivity activity) {
            super(fm);
            this.activity = activity;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    clients = new ClientesFragment(activity);
                    return clients;
              //  case 1:
                //     estoque = new EstoqueFragment(activity);
                   // return estoque;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }

        public CharSequence getPageTitle(int position) {
            switch(position)
            {
                case 0: return "clientes";
              //  case 1: return "estoque";
                default: return "??";
            }

        }
    }
}
