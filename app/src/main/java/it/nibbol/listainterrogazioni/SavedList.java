package it.nibbol.listainterrogazioni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class SavedList extends ActionBarActivity {

    String[] slista;
    ArrayList<String> nomi;
    ArrayList<String> numeri;
    ArrayList<String> id;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list);

        nomi = new ArrayList<String>();
        numeri = new ArrayList<String>();
        id = new ArrayList<String>();
        carica();
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < nomi.size(); i++) {
            HashMap<String, Object> Map = new HashMap<String, Object>();//creiamo una mappa di valori
            Map.put("id", id.get(i).toString());
            Map.put("nome", nomi.get(i).toString());
            if(numeri.get(i).toString().length()>20)
                Map.put("numeri", numeri.get(i).toString().substring(0,20)+"...");
            else
                Map.put("numeri", numeri.get(i).toString());
            data.add(Map);
        }
        String[] from = {"id", "nome", "numeri"}; //dai valori contenuti in queste chiavi
        int[] to = {R.id.rid, R.id.rnome, R.id.rnumeri};//agli id delle view
        //costruzione dell adapter
        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                data,//sorgente dati
                R.layout.row, //layout contenente gli id di "to"
                from,
                to);

        //utilizzo dell'adapter
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
        ListView list=(ListView) findViewById(R.id.listView);
        //al click sulla riga
        list.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String selected = ((TextView) view.findViewById(R.id.rid)).getText().toString();
                String nomelista = ((TextView) view.findViewById(R.id.rnome)).getText().toString();
                //Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                Intent dettaglio=new Intent(getApplicationContext(), Dettaglio.class);
                dettaglio.putExtra("id",selected);
                dettaglio.putExtra("nome",nomelista);
                startActivity(dettaglio);
            }
        }));
        list.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String selected = ((TextView) view.findViewById(R.id.rid)).getText().toString();
                String nomelista = ((TextView) view.findViewById(R.id.rnome)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(SavedList.this);
                builder.setCancelable(true);

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //carica in memoria tutto il file
                        ArrayList<String> righe=new ArrayList<String>();
                        String riga = "";

                        try {
                            InputStream in = openFileInput("salvataggi.txt");
                            if (in != null) {
                                InputStreamReader input = new InputStreamReader(in);
                                BufferedReader buffreader = new BufferedReader(input);
                                while ((riga = buffreader.readLine()) != null) {
                                    righe.add(riga);
                                }
                            }
                            in.close();
                        }
                        catch (Exception e) {
                            //Toast.makeText(getApplicationContext(), e.toString() + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT).show();
                        }
                        //lo riscrive senza gli elementi con  id selected
                        try {
                            FileOutputStream fOut = null;
                            OutputStreamWriter osw = null;
                            fOut = openFileOutput("salvataggi.txt",MODE_PRIVATE);
                            osw = new OutputStreamWriter(fOut);
                            for (int i = 0; i < righe.size(); i++) {
                                String valori[]=righe.get(i).toString().split(";");
                                if(valori[0].equals(selected))
                                {}
                                else {
                                    osw.write(righe.get(i).toString()+System.getProperty("line.separator"));
                                    osw.flush();
                                }
                            }
                            osw.close();
                            fOut.close();
                            Toast.makeText(getApplicationContext(), "Eliminato", Toast.LENGTH_SHORT).show();
                            // }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }});

                builder.setNegativeButton("NO",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {}});

                builder.setTitle("ATTENZIONE");
                builder.setMessage("Vuoi eliminare: "+nomelista+"?");
                //alert effettivo
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



                return true;
            }
        }));

    }

    //LOAD LIST
    public void carica() {
        String riga = "";
        int prima = 1;
        String num = "";
        String appo = null;
        try {
            InputStream in = openFileInput("salvataggi.txt");
            if (in != null) {
                InputStreamReader input = new InputStreamReader(in);
                BufferedReader buffreader = new BufferedReader(input);
                while ((riga = buffreader.readLine()) != null) {

                    String[] valori = riga.split(";");
                    if (prima == 1) {
                        id.add(valori[0]);
                        appo = valori[0];//id
                        nomi.add(valori[1]);//nome
                        num += valori[2]+"-";
                        prima = 0;
                    } else {
                        if (appo.equals(valori[0])) {
                            num += valori[2]+"-";//numero
                        } else {
                            numeri.add(num.substring(0,num.length()-1));
                            num = valori[2]+"-";
                            appo = valori[0];
                            prima=1;
                        }
                    }
                }
                numeri.add(num.substring(0,num.length()-1));//se è l'ultimo della lista
            }
            in.close();
            // Toast.makeText(getApplicationContext(), "caricato", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.toString() + e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Nessuna Lista Salvata", Toast.LENGTH_SHORT).show();
        }
    }

}
