package it.nibbol.listainterrogazioni;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class AdapterDettaglio extends ArrayAdapter<rigaDettaglio> {
    private final List<rigaDettaglio> list;
    private final Activity context;
    private final String idTabella;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    public AdapterDettaglio(Activity context, List<rigaDettaglio> list, String idTabella) {
        super(context, R.layout.rowdettaglio, list);
        this.context = context;
        this.list = list;
        this.idTabella=idTabella;
    }

    static class ViewHolder {
        protected TextView textNome;
        protected TextView textNum;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.rowdettaglio, null);
            viewHolder = new ViewHolder();
            viewHolder.textNum = (TextView) convertView.findViewById(R.id.txtIdPerson);
            viewHolder.textNome = (TextView) convertView.findViewById(R.id.txtNamePerson);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.Check);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.

                    //salvare su file
                    String idPersona=list.get(getPosition).getNumPersona();

                    //Carica tutto su array
                    ArrayList<String> righe=new ArrayList<String>();
                    String riga = "";

                    try {
                        InputStream in = context.openFileInput("salvataggi.txt");
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
                        Toast.makeText(context, "Errore", Toast.LENGTH_SHORT).show();
                    }
                    //riscrive tutto e modifica la riga con idLista e idPersona corrispondenti
                    try {
                        FileOutputStream fOut = null;
                        OutputStreamWriter osw = null;
                        fOut = context.openFileOutput("salvataggi.txt",context.MODE_PRIVATE);
                        osw = new OutputStreamWriter(fOut);
                        for (int i = 0; i < righe.size(); i++) {
                            String valori[]=righe.get(i).toString().split(";");
                            if(valori[0].equals(idTabella)&&valori[2].equals(idPersona))
                            {
                                if(isChecked==true)
                                    osw.write(valori[0]+";"+valori[1]+";"+valori[2]+";"+"true"+System.getProperty("line.separator"));
                                else
                                    osw.write(valori[0] + ";" + valori[1] + ";" + valori[2] + ";" + "false" + System.getProperty("line.separator"));
                                osw.flush();
                            }
                            else {
                                osw.write(righe.get(i).toString()+System.getProperty("line.separator"));
                                osw.flush();
                            }
                        }
                        osw.close();
                        fOut.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.txtIdPerson, viewHolder.textNum);
            convertView.setTag(R.id.txtNamePerson, viewHolder.textNome);
            convertView.setTag(R.id.Check, viewHolder.checkbox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.textNum.setText(list.get(position).getNumPersona());
        viewHolder.textNome.setText(list.get(position).getName());
        viewHolder.checkbox.setChecked(list.get(position).isChecked());

        return convertView;
    }
}
