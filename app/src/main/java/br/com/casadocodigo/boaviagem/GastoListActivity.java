package br.com.casadocodigo.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANDRE on 18/08/2017.
 */

public class GastoListActivity extends ListActivity
        implements AdapterView.OnItemClickListener {
    private List<Map<String, Object>> gastos;
    @Override
    protected void onCreate(Bundle savedInsanceState){
        super.onCreate(savedInsanceState);

        String[] de = {"data","descricao","valor","categoria"};
        int [] para = {R.id.data, R.id.descricao, R.id.valor, R.id.categoria
        };

        SimpleAdapter adapter = new SimpleAdapter(this, listarGastos(), R.layout.lista_gasto, de, para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        // registramos aqui o novo menu de contexto
        registerForContextMenu(getListView());
    }
    private List<Map<String, Object>> listarGastos(){
        gastos = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("data","04/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor","R$ 260,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("data","05/02/2012");
        item2.put("descricao", "Almoço");
        item2.put("valor","R$ 25,00");
        item2.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item2);
        Map<String, Object> item3 = new HashMap<>();
        item3.put("data","05/02/2012");
        item3.put("descricao", "Jantar");
        item3.put("valor","R$ 20,00");
        item3.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item3);
        Map<String, Object> item4 = new HashMap<>();
        item4.put("data","07/02/2012");
        item4.put("descricao", "Almoço");
        item4.put("valor","R$ 25,00");
        item4.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item4);

        return gastos;
    }
    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id){
        Map<String, Object> map = gastos.get(position);
        String des = (String) map.get("descricao");
        String msg = "Gasto selecionado: " + des;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String dataAnterior="";

    private class GastoViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation){
            if(view.getId() == R.id.data){
                if(!dataAnterior.equals(data)){
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                }
                else{
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if(view.getId() == R.id.categoria){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId() == R.id.remover){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";

            return  true;
        }
        return super.onContextItemSelected(item);
    }
}
