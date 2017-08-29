package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ANDRE on 18/08/2017.
 */

public class DashBoardActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

    public void selecionarOpcao(View view){
       switch (view.getId()){
           case R.id.nova_viagem:
               startActivity(new Intent(this, ViagemActivity.class));
               break;
           case R.id.novo_gasto:
               startActivity(new Intent(this, GastoActivity.class));
               break;
       }
    }

}
