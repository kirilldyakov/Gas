package com.example.user.demo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.demo.database.FillUp;
import com.example.user.demo.database.HelperFactory;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@EActivity(R.layout.activity_main)
public class Main extends AppCompatActivity {
    private static final String LOG_TAG = "LOG_TAG";
    private final int TYPE_BAR = 1101;
    private final int TYPE_LINE = 1102;

    private final int DATA_TYPE_PRICE = 1201;
    private final int DATA_TYPE_SUMMA = 1202;
    private final int DATA_TYPE_TRACK = 1203;
    private final int DATA_TYPE_VOLUME = 1204;

    private int dataType = DATA_TYPE_PRICE;

    private GraphView graphView;
    private LinearLayout graphLayout;
    private DataPoint[] data = null;

    @ViewById(R.id.btnNewFullUp)
    Button btnNewFillUp;

    @ViewById(R.id.btnListOfFillUps)
    Button btnListOfFillUps;


    @ViewById(R.id.txtTotalVolume)
    TextView txtTotalVolume;

    @ViewById(R.id.txtTotalSumma)
    TextView txtTotalSumma;

    @ViewById(R.id.txtTotalOdometr)
    TextView txtTotalOdometr;

    @ViewById(R.id.spnType)
    Spinner spinnerType;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    private void setTextsForViews() {
        //Начальная дата
        //long minDT = HelperFactory.getHelper().getFillUpDAO().getMinDT();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        //Date minDate = new Date(minDT);
        //String sdt = sdf.format(minDate);
        //txtTotalBeginDate.setText(sdt);

        //Суммарный Объем
        txtTotalVolume.setText(dblToStr(HelperFactory.getHelper().getFillUpDAO().getSumVolume(), 1));

        //Всего денег
        txtTotalSumma.setText(dblToStr(HelperFactory.getHelper().getFillUpDAO().getSumSumma(), 2));

        //Пройденое расстояние
        double minOdometr = HelperFactory.getHelper().getFillUpDAO().getMinOdometr();
        double maxOdometr = HelperFactory.getHelper().getFillUpDAO().getMaxOdometr();
        double odometr = maxOdometr - minOdometr;
        txtTotalOdometr.setText(dblToStr(odometr, 1));
    }

    @AfterViews
    protected void init() {
        graphView = new GraphView(this);
        graphLayout = (LinearLayout) findViewById(R.id.GraphLayout);
        graphLayout.addView(graphView);

        showGraph();
        setTextsForViews();

        String[] data = {"Цена", "Сумма", "Пробег", "Объем"};
// адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapter);
        // заголовок
        spinnerType.setPrompt("Тип");
        // выделяем элемент
        spinnerType.setSelection(0);


        // устанавливаем обработчик нажатия
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();

                switch (position) {
                    //Цена
                    case 0:
                        if (calcPrices()) draw(TYPE_LINE, Color.BLUE);
                        break;
                    //Сумма
                    case 1:
                        if (calcSumma()) draw(TYPE_LINE, Color.BLUE);
                        break;
                    //пробег
                    case 2:
                        if (calcOdo()) draw(TYPE_LINE, Color.BLUE);
                        break;
                    //объем
                    case 3:
                        if (calcVolume()) draw(TYPE_BAR, Color.BLUE);
                        break;
                    default:

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //было в onCreate
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //redrawGraph();
        //drawLineGraph(android.R.color.holo_orange_dark);
        setTextsForViews();

    }

    @Click(R.id.btnNewFullUp)
    void btnNewFillUpClick() {
        NewFillUp_.intent(this).start();
    }

    //Отображение списка заправок
    @Click(R.id.btnListOfFillUps)
    void setBtnListOfFillUps() {
        FillUpList_.intent(this).start();
    }

    private String dblToStr(double value, int N) {
        String res = "";
        try {
            String format = "%1$,." + Integer.toString(N) + "f";
            res = String.format(format, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    private void showGraph() {
        if (dataType == DATA_TYPE_PRICE)
            if (calcPrices()) draw(TYPE_LINE, Color.BLUE);

        if (dataType == DATA_TYPE_SUMMA)
            if (calcSumma()) draw(TYPE_LINE, Color.BLUE);

            else
                Toast.makeText(this, "Нет данных", Toast.LENGTH_SHORT).show();
    }

    private boolean calcPrices() {
        List<FillUp> fillups;
        try {
            fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        int cnt = fillups.size();
        data = new DataPoint[cnt];

        int i = 0;
        for (FillUp fillup : fillups) {
            long dtl = fillup.getDateTimeLong();
            Date x = new Date(dtl);
            double y = fillup.getPrice();
            data[i] = new DataPoint(x, y);
            i++;
        }
        return true;
    }

    private boolean calcSumma() {
        List<FillUp> fillups;
        try {
            fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        int cnt = fillups.size();
        data = new DataPoint[cnt];

        int i = 0;

        for (FillUp fillup : fillups) {
            long dateTimeLong = fillup.getDateTimeLong();
            Date x = new Date(dateTimeLong);
            double y = fillup.getSumma();
            data[i] = new DataPoint(x, y);
            i++;
        }
        return true;
    }


    private boolean calcOdo() {
        List<FillUp> fillups;
        try {
            fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        int cnt = fillups.size();
        data = new DataPoint[cnt];

        int i = 0;

        double minOdo = HelperFactory.getHelper().getFillUpDAO().getMinOdometr();

        for (FillUp fillup : fillups) {
            long dateTimeLong = fillup.getDateTimeLong();
            Date x = new Date(dateTimeLong);
            double y = fillup.getOdometr()- minOdo;
            data[i] = new DataPoint(x, y);
            i++;
        }
        return true;
    }

    private boolean calcVolume() {
        List<FillUp> fillups;
        try {
            fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        int cnt = fillups.size();
        data = new DataPoint[cnt];

        int i = 0;

        for (FillUp fillup : fillups) {
            long dateTimeLong = fillup.getDateTimeLong();
            Date x = new Date(dateTimeLong);
            double y = fillup.getVolume();
            data[i] = new DataPoint(x, y);
            i++;
        }
        return true;
    }


    //Отрисовка данных на графике
    void draw(int GraphType, int color) {
        try {
            graphView.getViewport().setScrollable(true);
            graphView.getViewport().setScalable(true);
            graphView.removeAllSeries();


            if (GraphType == TYPE_BAR) {
                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(this.data);
                series.setColor(color);
                series.setSpacing(20);
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.RED);
                series.setValuesOnTopSize(16);
                graphView.addSeries(series);
            }

            if (GraphType == TYPE_LINE) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(this.data);
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(5);
                series.setColor(color);
                graphView.addSeries(series);
            }

            String sDateFormat = getString(R.string.Graph_Power_AxisXFormat_DDMMM);
            graphView.getGridLabelRenderer().setLabelFormatter(
                    new DateAsXAxisLabelFormatter(this, new SimpleDateFormat(sDateFormat)));
            graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
            //graphView.getGridLabelRenderer().setHorizontalLabelsColor(R.color.colorTextDark);
            graphView.getGridLabelRenderer().setTextSize(24);
            //graphView.getGridLabelRenderer().setHorizontalAxisTitle(getString(R.string.Graph_Power_HorizontalAxisTitle));
            //graphView.getGridLabelRenderer().setVerticalAxisTitle(getString(R.string.Graph_Power_VerticalAxisTitle));

            //graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMinX(Math.ceil(this.data[0].getX()) - 24 * 60 * 60 * 1000);
            graphView.getViewport().setMaxX(Math.ceil(this.data[this.data.length - 1].getX()) + 24 * 60 * 60 * 1000);

            //graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(0.0);

        } catch (Exception e) {
            //return false;
        }

        //return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder ad1 = new AlertDialog.Builder(this);
        ad1.setMessage(R.string.ru_exit_alert);
        ad1.setCancelable(false);
        ad1.setNegativeButton(R.string.ruNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });
        ad1.setPositiveButton(R.string.ruYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        AlertDialog alert = ad1.create();
        alert.show();
    }
}




