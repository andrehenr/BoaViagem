package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by ANDRE on 18/08/2017.
 */

public class ViagemActivity extends Activity {
    public void selecionarOpcao(View view){
        switch(view.getId()){
            case R.id.nova_viagem:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
        }
    }
}
