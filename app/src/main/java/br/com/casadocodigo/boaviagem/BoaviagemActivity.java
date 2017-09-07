package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ANDRE on 17/08/2017.
 */

public class BoaviagemActivity extends Activity {

    private static final String MANTER_CONECTADO = "manter_conectado";
    private EditText usuario;
    private EditText senha;
    private CheckBox manterConectado;
    private Button btnEntrar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);

        SharedPreferences preferenciais = getPreferences(MODE_PRIVATE);
        boolean conectado =
                preferenciais.getBoolean(MANTER_CONECTADO, false);
        if(conectado){
            startActivity(new Intent(this, DashBoardActivity.class));
        }
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setVisibility(View.INVISIBLE);
    }
    public void entrarOnClick(View v){

        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();

        if("leitor".equals(usuarioInformado) &&
                "123".equals(senhaInformada)){
                SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putBoolean(MANTER_CONECTADO,
                        manterConectado.isChecked());
                editor.commit();
                startActivity(new Intent(this, DashBoardActivity.class));
        }
        else{
            String mensagemErro = getString(R.string.erro_autenticacao);
            Toast toast = Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
