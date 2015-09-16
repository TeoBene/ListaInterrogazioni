package it.nibbol.listainterrogazioni;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    SeekBar sb;
    Switch sw;
    EditText txtNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtNum = (EditText) findViewById(R.id.textNumero);
        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setEnabled(true);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                txtNum.setText(""+(progress+1));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        TextView tv=(TextView)findViewById(R.id.textViewLista);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        sw=(Switch)findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    txtNum.setText("");
                    txtNum.setEnabled(true);
                    txtNum.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtNum, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    txtNum.setEnabled(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtNum.getWindowToken(), 0);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    String slista="";
    Integer[] alista;
    public void GeneraLista(View view) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        builder.setTitle("Error");
        builder.setMessage("Inserire il numero di studenti della classe.");

        //alert effettivo
        AlertDialog alertDialog = builder.create();*/

        //hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtNum.getWindowToken(), 0);

        if (String.valueOf(txtNum.getText()).equals("")||String.valueOf(txtNum.getText()).equals("0"))
            //alertDialog.show();
            Toast.makeText(getApplicationContext(), "Inserire il numero di studenti della classe.", Toast.LENGTH_LONG).show();
        else {
            if (Integer.parseInt(String.valueOf(txtNum.getText())) > 1000) {
                Toast.makeText(getApplicationContext(), "Inserire un numero compreso tra 1 e 1000", Toast.LENGTH_LONG).show();
            } else {
                alista = new Integer[Integer.parseInt(String.valueOf(txtNum.getText()))];
                Random random = new Random();
                TextView lista = (TextView) findViewById(R.id.textViewLista);

                slista = "";
                lista.setText(slista);

                //I need Integer to compare numbers with a null value, int not allows that :C
                Integer[] valori = new Integer[Integer.parseInt(String.valueOf(txtNum.getText()))];

                for (int i = 0; i < Integer.parseInt(String.valueOf(txtNum.getText())); ) {
                    boolean presente = false;
                    int n = random.nextInt(Integer.parseInt(String.valueOf(txtNum.getText()))) + 1;
                    if (!Arrays.asList(valori).contains(n)) {
                        for (int j = 0; j < valori.length; j++)
                            if (valori[j] == null) {
                                valori[i] = n;
                                break;
                            }
                        slista = (slista + (String.valueOf(n)) + " - ");
                        i++;
                    }
                }
                lista.setText(slista.substring(0, slista.length() - 3));
                alista = valori;
            }
        }
    }

    //SAVE LIST
    public void salva(MenuItem item) {
        final EditText txtNome=new EditText(this);
        final SharedPreferences pref=getSharedPreferences("pref",Context.MODE_PRIVATE);
        final String lastID=pref.getString("id","0");




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setPositiveButton("SALVA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //salva la lista
                if (!String.valueOf(txtNome.getText()).equals("") && !String.valueOf(txtNum.getText()).equals("")) {

                    int idlist = Integer.parseInt(lastID);
                    idlist++;
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("id", String.valueOf(idlist).toString());
                    editor.commit();
                    try {
                        FileOutputStream fOut = null;
                        OutputStreamWriter osw = null;
                        fOut = openFileOutput("salvataggi.txt",MODE_APPEND);
                        osw = new OutputStreamWriter(fOut);
                        for (int i = 0; i < alista.length; i++) {
                            osw.write(idlist + ";" + txtNome.getText() + ";" + alista[i].toString() + ";" + "false"+System.getProperty("line.separator"));
                            osw.flush();
                        }
                        osw.close();
                        fOut.close();
                        Toast.makeText(getApplicationContext(), "Lista Salvata.", Toast.LENGTH_SHORT).show();
                        // }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setTitle("Salvare Lista?");
        builder.setMessage("Inserisci il nome della lista da salvare.");
        builder.setView(txtNome);
        //alert effettivo
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        txtNome.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtNome, InputMethodManager.SHOW_IMPLICIT);
    }

    //GOTO NEW ACTIVITY
    public void listeSalvate(MenuItem item) {
        Intent intent=new Intent(this,SavedList.class);
        startActivity(intent);
    }
}
