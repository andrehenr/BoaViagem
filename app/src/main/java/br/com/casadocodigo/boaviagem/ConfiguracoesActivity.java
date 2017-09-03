package br.com.casadocodigo.boaviagem;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by ANDRE on 03/09/2017.
 */

public class ConfiguracoesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
