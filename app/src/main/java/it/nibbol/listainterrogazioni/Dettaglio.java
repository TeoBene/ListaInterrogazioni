package it.nibbol.listainterrogazioni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Dettaglio extends ActionBarActivity{
    List<rigaDettaglio> righe=new ArrayList<rigaDettaglio>();
    ArrayAdapter<rigaDettaglio> adapter;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);

        Intent dettaglio = getIntent();
        id = dettaglio.getStringExtra("id");
        String nomelista = dettaglio.getStringExtra("nome");
        TextView txtnome = (TextView) findViewById(R.id.txtNome);
        txtnome.setText(nomelista);

        carica();
        adapter = new AdapterDettaglio(this, righe,id);
        ListView list = (ListView) findViewById(R.id.textViewLista);
        list.setAdapter(adapter);
    }
    private String isCheckedOrNot(CheckBox checkbox) {
        if(checkbox.isChecked())
            return "is checked";
        else
            return "is not checked";
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dettaglio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void carica()
    {
        String riga = "";
        righe=new ArrayList<rigaDettaglio>();
        try {
            InputStream in = openFileInput("salvataggi.txt");
            if (in != null) {
                InputStreamReader input = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(input);
                while ((riga = buffreader.readLine()) != null) {

                    String[] valori = riga.split(";");
                    if(valori[0].equals(id))
                    {
                        String nome="mario";
                        boolean checked=false;
                        if(valori[3].equals("true")) {
                            checked = true;
                        }
                        rigaDettaglio r=new rigaDettaglio(valori[2],nome,checked);
                        righe.add(r);
                    }
                }
            }
            in.close();
        }
        catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString() + e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT).show();
        }
    }
}
