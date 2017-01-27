package com.example.user.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.user.demo.database.FillUp;
import com.example.user.demo.database.HelperFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.list_fillups)
public class FillUpList extends Activity {
    private final String ATTRIBUTE_NAME_PRICE = "tv_itm_price";
    private final String ATTRIBUTE_NAME_VOLUME = "tv_itm_volume";
    private final String ATTRIBUTE_NAME_FULLTANK = "tv_itm_fulltank";
    private final String ATTRIBUTE_NAME_SUMMA = "tv_itm_summa";
    private final String ATTRIBUTE_NAME_ID = "tv_itm_id";
    private final String ATTRIBUTE_NAME_ODOMETR = "tv_itm_odometr";
    private final String ATTRIBUTE_NAME_DATETIME = "tv_itm_datetime";

    private static final int CM_DELETE_ID = 1;

    private SimpleAdapter sAdapter;
    private ArrayList<Map<String, Object>> data;


    private ListView lvSimple;

    @ViewById(R.id.btnDeleteRecords)
    Button btnDeleteRecords;

    @AfterViews
    protected void init() {

        List<FillUp> fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        data = new ArrayList<>();


        for (FillUp fillup : fillups){
            //m = new HashMap<String, Object>();
            Map<String, Object> m = new HashMap<>();
            m.put(ATTRIBUTE_NAME_ID, Long.toString(fillup.getId()));
            m.put(ATTRIBUTE_NAME_DATETIME, fillup.getFormatedDateTime());
            m.put(ATTRIBUTE_NAME_VOLUME, Double.toString(fillup.getVolume())+" " + getString(R.string.ru_liters));
            m.put(ATTRIBUTE_NAME_PRICE, Double.toString(fillup.getPrice())+" " + getString(R.string.ru_currency));
            m.put(ATTRIBUTE_NAME_SUMMA, Double.toString(fillup.getSumma())+" " + getString(R.string.ru_currency));
            m.put(ATTRIBUTE_NAME_ODOMETR, Double.toString(fillup.getOdometr())+" "+getString(R.string.ru_kilometers));
            //m.put(ATTRIBUTE_NAME_FULLTANK, Integer.toString(fillup.getFullTank()));
            m.put(ATTRIBUTE_NAME_FULLTANK, (fillup.getFullTank()==1)?"+":"");
            data.add(m);
        }

        // массив имен атрибутов, из которых будут читаться данные
        String[] from = {ATTRIBUTE_NAME_ID
                , ATTRIBUTE_NAME_DATETIME
                , ATTRIBUTE_NAME_VOLUME
                , ATTRIBUTE_NAME_PRICE
                , ATTRIBUTE_NAME_SUMMA
                , ATTRIBUTE_NAME_ODOMETR
                , ATTRIBUTE_NAME_FULLTANK};
        // массив ID View-компонентов, в которые будут вставлять данные
        int[] to = { R.id.tv_itm_id
                ,R.id.tv_itm_datetime
                ,R.id.tv_itm_volume
                ,R.id.tv_itm_price
                ,R.id.tv_itm_summa
                ,R.id.tv_itm_odometr
                ,R.id.tv_itm_fulltank};

        // создаем адаптер
        sAdapter = new SimpleAdapter(this, data, R.layout.item, from, to);

        // определяем список и присваиваем ему адаптер

        lvSimple = (ListView) findViewById(R.id.lvSimple);
        lvSimple.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        arg1.toString()+ "|" + arg2 + "|" + arg3, Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = lvSimple.getItemAtPosition(position);
            }
        });

        lvSimple.setAdapter(sAdapter);
        registerForContextMenu(lvSimple);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fillups);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем инфу о пункте списка
            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            // удаляем Map из коллекции, используя позицию пункта в списке
            data.remove(acmi.position);
            // уведомляем, что данные изменились
            sAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Click(R.id.btnDeleteRecords)
    void btnDeleteRecordsClick() {
        int r = HelperFactory.getHelper().getFillUpDAO().deleteRecords();
        Log.d("LOG_TAG", "Удалено записей:" + Integer.toString(r));
    }
}