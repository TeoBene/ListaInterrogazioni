package it.nibbol.listainterrogazioni;

import android.widget.ImageView;

/**
 * Created by Matteo on 09/09/2015.
 */
public class rigaDettaglio {
    private String numPersona;
    private String nome;
    private boolean checked;
    public rigaDettaglio(String numpersona,String nome, boolean checked)
    {
        this.numPersona=numpersona;
        this.nome=nome;
        this.checked=checked;
    }
    public boolean isChecked() {
        return checked;
    }
    public String getNumPersona() {
        return numPersona;
    }
    public String getName() {
        return nome;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
