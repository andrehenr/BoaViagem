package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ANDRE on 17/08/2017.
 */

public class BoaviagemActivity extends Activity {

    private EditText usuario;
    private EditText senha;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
    }
    public void entrarOnClick(View v){

        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();

        if("leitor".equals(usuarioInformado) &&
                "123".equals(senhaInformada)){
                startActivity(new Intent(this, DashBoardActivity.class));
        }
        else{
            String mensagemErro = getString(R.string.erro_autenticacao);
            Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
