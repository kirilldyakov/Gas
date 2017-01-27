package com.example.user.demo.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@DatabaseTable(tableName = "FillUp")
public class FillUp {

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField(canBeNull = false, dataType = DataType.LONG)
    private long DateTime;

    @DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
    private double Volume;

    @DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
    private double Price;

    @DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
    private double Summa;

    @DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
    private double Odometr;

    @DatabaseField(canBeNull = false, dataType = DataType.INTEGER)
    private int FullTank;

    //Конструктор без параметров. Нужен для ORMlite
    public FillUp() {
        //scheduleList = new ArrayList<Shedule>();
        //priorities = new ArrayList<PrioritySchedule>();
    }

    public FillUp(Long dateTime, double volume, double price, double summa, double odometr, int fulltank) {
        DateTime = dateTime;
        Volume = volume;
        Price = price;
        Summa = summa;
        Odometr = odometr;
        FullTank = fulltank;
    }

    public FillUp(double volume, double price, double summa, double odometr, int fulltank) {
        DateTime = System.currentTimeMillis();
        Volume = volume;
        Price = price;
        Summa = summa;
        Odometr = odometr;
        FullTank = fulltank;
    }

    @Override
    public String toString() {
        return "FillUp {" +
                "Id=" + Id +
                ", DateTime=" + DateTime +
                ", Volume=" + Volume +
                ", Price=" + Price +
                ", Summa=" + Summa +
                ", Odometr=" + Odometr +
                ", FullTank=" + FullTank +
                '}';
    }

    //ID
    public int getId() {
        return Id;
    }

    //DATETIME
    public long getDateTimeLong() {
        return DateTime;
    }

    public Date getDateTime() {
        return new Date(this.DateTime);
    }


    public String getFormatedDateTime(){
        return getFormatedDateTime("dd.MM.yy HH:mm");
    }

    public String getFormatedDateTime(String format) {
        Date date = getDateTime();
        return android.text.format.DateFormat.format(format, date).toString();
    }

    public void setDateTime(long dateTimeLong) {
        DateTime = dateTimeLong;
    }

    public void setDateTime(String sdateTimeLong) {
        sdateTimeLong = sdateTimeLong.toUpperCase();
        sdateTimeLong = sdateTimeLong.replace(" ", "");
        //TODO A-Z
        try {
            SimpleDateFormat sDate = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sDate.parse(sdateTimeLong);
            setDateTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    //VOLUME
    public double getVolume() {
        return Volume;
    }

    private void setVolume(double volume) {
        this.Volume = volume;
    }

    public void setVolume(String volume) {
        try {
            volume = prepare(volume);
            double value = Double.parseDouble(volume);
            setVolume(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //PRICE
    public double getPrice() {
        return Price;
    }

    private void setPrice(double price) {
        this.Price = price;
    }

    public void setPrice(String price) {
        try {
            price = prepare(price);
            double value = Double.parseDouble(price);
            setPrice(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //SUMMA
    public double getSumma() {
        return Summa;
    }

    private void setSumma(double summa) {
        this.Summa = summa;
    }

    public void setSumma(String summa) {
        try {
            summa = prepare(summa);
            double value = Double.parseDouble(summa);
            setSumma(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //ODOMETR
    public double getOdometr() {
        return Odometr;
    }

    private void setOdometr(double odometr) {
        this.Odometr = odometr;
    }

    public void setOdometr(String odometr) {
        try {
            odometr = prepare(odometr);
            double value = Double.parseDouble(odometr);
            setOdometr(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //FULLTANK
    public int getFullTank() {
        return FullTank;
    }

    public void setFullTank(int fullTank) {
        FullTank = (fullTank==1)?1:0;
    }


    //подготовка сторки
    private String prepare(String value){
        value= value.toUpperCase();
        value = value.replace(" ", "");
        value = value.replace(",", ".");
        return value;
    }
}