package br.com.casadocodigo.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANDRE on 18/08/2017.
 */

public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private List<Map<String,Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferencias.getString("valor_limite","-1");
        valorLimite = Double.valueOf(valor);
        String[] de = { "imagem", "destino", "data",
                "total" };
        int[] para = { R.id.tipoViagem, R.id.destino,
                R.id.data, R.id.valor };
        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(),R.layout.lista_viagem, de, para);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();
    }

    @Override
    protected void onResume(){
        super.onResume();
        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferencias.getString("valor_limite","-1");
        valorLimite = Double.valueOf(valor);
        String[] de = { "imagem", "destino", "data",
                "total" };
        int[] para = { R.id.tipoViagem, R.id.destino,
                R.id.data, R.id.valor };
        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(),R.layout.lista_viagem, de, para);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();

    }

    private List<Map<String,Object>> listarViagens(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, tipo_viagem, destino, "+
                "data_chegada, data_saida, orcamento FROM viagem",null);
        cursor.moveToFirst();

        viagens = new ArrayList<>();

        for(int i = 0;i< cursor.getCount();i++){
            Map<String, Object> item = new HashMap<>();

            String id = cursor.getString(0);
            int tipoViagem = cursor.getInt(1);
            String destino = cursor.getString(2);
            long dataChegada = cursor.getLong(3);
            long dataSaida = cursor.getLong(4);
            double orcamento = cursor.getDouble(5);

            item.put("id", id);
            if(tipoViagem == Constantes.VIAGEM_LAZER){
                item.put("imagem", R.drawable.lazer);
            }
            else{
                item.put("imagem", R.drawable.negocios);
            }
            item.put("destino", destino);

            Date dataChegadaDate = new Date(dataChegada);
            Date dataSaidaDate = new Date(dataSaida);

            String periodo = dateFormat.format(dataChegadaDate) + " a "
                    + dateFormat.format(dataSaidaDate);
            item.put("data", periodo);

            double totalGasto = calcularTotalGasto(db, id);

            item.put("total", "Gasto total R$ "+totalGasto);

            viagens.add(item);

            cursor.moveToNext();
        }
        return viagens;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id){
        Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String msg = "Viagem selecionada: "+destino;
        this.viagemSelecionada = position;
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
        alertDialog.show();
    }
    @Override
    public void onClick(DialogInterface dialog, int item){
        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");
        switch (item){
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
               dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);
                removerViagem(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    private void removerViagem(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        String where [] = new String[] {id};
        db.delete("gasto","viagem_id = ?", where);
        db.delete("viagem","_id = ?",where);
    }

    private AlertDialog criaAlertDialog(){
        final CharSequence[] items = {
                getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções");
        builder.setItems(items, this);
        return builder.create();
    }

    private AlertDialog criaDialogConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();
    }
    private double calcularTotalGasto(SQLiteDatabase db, String id){
        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM gasto WHERE viagem_id = ?",
                new String[]{ id });
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }
}
