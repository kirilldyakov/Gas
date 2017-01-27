package com.example.user.demo;

import android.app.Activity;
import android.text.format.DateUtils;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.user.demo.database.FillUp;
import com.example.user.demo.database.HelperFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 19.12.16.
 */

@EActivity(R.layout.new_fillup)
public class NewFillUp extends Activity {
    String LOG_TAG = "LOG_TAG";
    private Context _context;

    @ViewById(R.id.btnOK)
    Button btnOK;

    @ViewById(R.id.btnDate)
    Button btnDate;

    @ViewById(R.id.btnTime)
    Button btnTime;

    @ViewById(R.id.edVolume)
    EditText edVolume;

    @ViewById(R.id.edPrice)
    EditText edPrice;

    @ViewById(R.id.edSumma)
    EditText edSumma;

    @ViewById(R.id.edOdometr)
    EditText edOdometr;

    @ViewById(R.id.swFullTank)
    Switch swFullTank;

    Calendar dateAndTime = Calendar.getInstance();


    @AfterViews
    protected void init() {
        setInitialDateTime();

        /*try {

            edTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        new TimePickerDialog(v.getContext(), t,
                                dateAndTime.get(Calendar.HOUR_OF_DAY),
                                dateAndTime.get(Calendar.MINUTE), true)
                                .show();
                        v.clearFocus();
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        try {
            double maxOdo = HelperFactory.getHelper().getFillUpDAO().getMaxOdometr();
            edOdometr.setText(Double.toString(maxOdo + 1.0));
        } catch (Exception e) {

        }

        try {
            double lastPrice = HelperFactory.getHelper().getFillUpDAO().getLastPrice();
            edPrice.setText(Double.toString(lastPrice));
        } catch (Exception e) {
            e.printStackTrace();
        }

        edVolume.setText("0.0");
        edSumma.setText("0.0");

        List<FillUp> fillups = new ArrayList<FillUp>();

        try {
            fillups = HelperFactory.getHelper().getFillUpDAO().getAllFillUps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (FillUp fillup : fillups) {
            Log.d(LOG_TAG, fillup.toString());
        }

    }

    @Click(R.id.btnDate)
    void btnDateClick() {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Click(R.id.btnTime)
    void btnTimeClick() {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    @Click(R.id.btnOK)
    void btnNewFillUpClick() {

        try {
            //String dateTime = edDate.getText().toString(); //TODO add Time
            String volume = edVolume.getText().toString();
            String price = edPrice.getText().toString();
            String summa = edSumma.getText().toString();
            String odometr = edOdometr.getText().toString();
            int fullTank = swFullTank.isChecked() ? 1 : 0;

            FillUp fillUp = new FillUp();
            //fillUp.setDateTime(dateTime);
            fillUp.setDateTime(dateAndTime.getTimeInMillis());
            fillUp.setVolume(volume);
            fillUp.setPrice(price);
            fillUp.setSumma(summa);
            fillUp.setOdometr(odometr);
            fillUp.setFullTank(fullTank);

            HelperFactory.getHelper().getFillUpDAO().create(fillUp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Main_.intent(this).start();
    }

    private String prepare(String value) {
        value = value.toUpperCase();
        value = value.replace(" ", "");
        value = value.replace(",", ".");
        if (value.equals("")) value = "0";
        return value;
    }

    @TextChange({R.id.edPrice, R.id.edVolume, R.id.edSumma})
    void onTextChanged(TextView tv, CharSequence text) {

        try {
            double dPrice = Double.parseDouble(prepare(edPrice.getText().toString()));
            double dVolume = Double.parseDouble(prepare(edVolume.getText().toString()));
            double dSumma = Double.parseDouble(prepare(edSumma.getText().toString()));

            switch (tv.getId()) {
                case R.id.edPrice: {
                    dSumma = dPrice * dVolume;
                    edSumma.setText(String.format("%.2f", dSumma).replace(",", "."));
                    break;
                }
            /*case R.id.edSumma : {
                dPrice = dSumma/dVolume;
                edPrice.setText(Double.toString(dPrice));
                break;
            }*/
                case R.id.edVolume: {
                    dSumma = dPrice * dVolume;
                    edSumma.setText(String.format("%.2f", dSumma).replace(",", "."));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка начальных даты и времени
    private void setInitialDateTime() {
        btnDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_ABBREV_ALL)
        );


        //SimpleDateFormat tsdf = new SimpleDateFormat("HH:mm");
        //edTime.setText(tsdf.format(dateAndTime));
        btnTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));


        //currentDateTime .setText(DateUtils.formatDateTime(this,
        //        dateAndTime.getTimeInMillis(),
        //        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
        //                | DateUtils.FORMAT_SHOW_TIME));
    }


}