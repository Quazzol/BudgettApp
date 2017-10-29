package com.okanerkan.budgettapp;

/**
 * Created by Quazzol on 29.10.2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "budgettapp_db";

    private static final String TABLE_NAME = "kitap_listesi";
    private static String KITAP_ADI = "kitap_adi";
    private static String KITAP_ID = "id";
    private static String KITAP_YAZARI = "yazar";
    private static String KITAP_BASIM_YILI = "yil";
    private static String KITAP_FIYATI = "fiyat";

    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KITAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KITAP_ADI + " TEXT,"
                + KITAP_YAZARI + " TEXT,"
                + KITAP_BASIM_YILI + " TEXT,"
                + KITAP_FIYATI + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void kitapSil(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KITAP_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void kitapEkle(String kitap_adi, String kitap_yazari,String kitap_basim_yili,String kitap_fiyat) {
        //kitapEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KITAP_ADI, kitap_adi);
        values.put(KITAP_YAZARI, kitap_yazari);
        values.put(KITAP_BASIM_YILI, kitap_basim_yili);
        values.put(KITAP_FIYATI, kitap_fiyat);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public HashMap<String, String> kitapDetay(int id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.
        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> kitap = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            kitap.put(KITAP_ADI, cursor.getString(1));
            kitap.put(KITAP_YAZARI, cursor.getString(2));
            kitap.put(KITAP_BASIM_YILI, cursor.getString(3));
            kitap.put(KITAP_FIYATI, cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return kitap
        return kitap;
    }

    public  ArrayList<HashMap<String, String>> kitaplar(){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> kitaplist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                kitaplist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return kitaplist;
    }

    public void kitapDuzenle(String kitap_adi, String kitap_yazari,String kitap_basim_yili,String kitap_fiyat,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(KITAP_ADI, kitap_adi);
        values.put(KITAP_YAZARI, kitap_yazari);
        values.put(KITAP_BASIM_YILI, kitap_basim_yili);
        values.put(KITAP_FIYATI, kitap_fiyat);

        // updating row
        db.update(TABLE_NAME, values, KITAP_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}