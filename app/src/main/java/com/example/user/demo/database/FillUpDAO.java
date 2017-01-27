package com.example.user.demo.database;


import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FillUpDAO extends BaseDaoImpl<FillUp, Integer> {

    FillUpDAO(ConnectionSource connectionSource,
              Class<FillUp> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<FillUp> getAllFillUps() {
        List<FillUp> listToReturn = new ArrayList<>();
        try {
            listToReturn = this.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return listToReturn;
    }

 /*   public List<FillUp> getAllFillUps3() {

        //List<FillUp> listToReturn = this.queryRaw();
        //return listToReturn;


        try {
            GenericRawResults<Object[]> rawResults =
                    this.queryRaw(
                            "select account_id, sum(amount) from orders group by account_id",
                            new DataType[]{DataType.LONG, DataType.INTEGER});
            for (Object[] resultArray : rawResults) {
                System.out.println("Account-id " + resultArray[0] + " has "
                        + resultArray[1] + " total orders");
            }

            rawResults.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/


    //Переделать
    public List<FillUp> getAllFillUps2() {
        List<FillUp> listToReturn = new ArrayList<>();
        //String minDT = Long.toString(this.getMinDT());

        listToReturn = (List<FillUp>) this.queryBuilder().selectColumns();
        return listToReturn;
    }

    //Переделать
/*    public List<FillUp> getAllFillUpsWhereAmplitudeGE(int filter) throws SQLException {
        List<FillUp> listToReturn = new ArrayList<FillUp>();
        QueryBuilder<FillUp, Integer> qb = this.queryBuilder();
        qb.where().ge("Amplitude", filter);
        qb.orderBy("Id", true);
        listToReturn = (List<FillUp>) qb.query();
        return listToReturn;
    }*/


    public long getMinDT() {
        long res = 0;
        try {
            res = this.queryRawValue("Select min(DateTime) from FillUp;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
/*
    public long getMaxDT() {
        long res = 0;
        try {
            res = this.queryRawValue("Select max(DateTime) from FillUp;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }*/


    private double getDoubleValue(String sql) {
        double res = 0.0;
        try {
            GenericRawResults<Object[]> rawResults =
                    this.queryRaw(
                            sql,
                            new DataType[]{DataType.DOUBLE});
            for (Object[] resultArray : rawResults) {
                res = (Double) resultArray[0];
            }
            rawResults.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public double getSumVolume() {
        double res = 0.0;
        try {
            res = getDoubleValue("Select sum(Volume) from FillUp;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public double getSumSumma() {
        double res = 0;
        try {
            res = getDoubleValue("Select sum(Summa) from FillUp;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public double getMaxOdometr() {
        double res = 0.0;
        try {
            res = getDoubleValue("Select max(Odometr) from FillUp;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public double getMinOdometr() {
        double res = 0;
        try {
            res = getDoubleValue("Select min(Odometr) from FillUp;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public double getLastPrice() {
        double res = 0.0;
        try {
            long id = this.queryRawValue("Select max(DateTime) from FillUp;");
            String sql = "Select Price from FillUp where DateTime = " + Long.toString(id) + ";";
            res = getDoubleValue(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public int getCount() {
        try {
            return (int) this.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteRecords() {
        int res = 0;
        try {
            res = this.deleteBuilder().delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;


    }
}