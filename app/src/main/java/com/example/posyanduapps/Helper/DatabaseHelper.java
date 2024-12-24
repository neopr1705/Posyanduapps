package com.example.posyanduapps.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "posyandu.db";
    private static final int DATABASE_VERSION = 1;

    // Kolom-kolom untuk tabel absensi
    public static final String TABLE_ABSENSI = "absensi";
    public static final String COLUMN_ABSENSI_ID = "id";
    public static final String COLUMN_ABSENSI_NAME = "nama";
    public static final String COLUMN_ABSENSI_TANGGAL = "tanggal";
    public static final String COLUMN_ABSENSI_HARI = "hari";
    public static final String COLUMN_ABSENSI_JAM = "jam";
    public static final String COLUMN_ABSENSI_TEMPAT = "tempat";

    // Kolom baru untuk alarm
    public static final String TABLE_ALARM = "alarm";
    public static final String COLUMN_ALARM_ID = "id";
    public static final String COLUMN_ALARM_TANGGAL = "tanggal";
    public static final String COLUMN_ALARM_JAM = "jam";
    public static final String COLUMN_ALARM_STATUS = "status";  // Status aktif/alarm terpasang

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ABSENSI_TABLE = "CREATE TABLE " + TABLE_ABSENSI + "("
                + COLUMN_ABSENSI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ABSENSI_NAME + " TEXT, "
                + COLUMN_ABSENSI_TANGGAL + " TEXT, "
                + COLUMN_ABSENSI_HARI + " TEXT, "
                + COLUMN_ABSENSI_JAM + " TEXT, "
                + COLUMN_ABSENSI_TEMPAT + " TEXT"
                + ")";
        db.execSQL(CREATE_ABSENSI_TABLE);

        // Tabel Alarm
        String CREATE_ALARM_TABLE = "CREATE TABLE"+ TABLE_ALARM +"("
                + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ALARM_TANGGAL + " TEXT, "
                + COLUMN_ALARM_JAM + " TEXT, "
                + COLUMN_ALARM_STATUS + " INTEGER" // 0 = Inaktif, 1 = Aktif
                + ")";
        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSENSI);
        db.execSQL("DROP TABLE IF EXISTS alarms");
        onCreate(db);
    }


    // Menyimpan data absensi
    public boolean insertAbsensi(String nama, String tanggal, String hari, String jam, String tempat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ABSENSI_NAME, nama);
        values.put(COLUMN_ABSENSI_TANGGAL, tanggal);
        values.put(COLUMN_ABSENSI_HARI, hari);
        values.put(COLUMN_ABSENSI_JAM, jam);
        values.put(COLUMN_ABSENSI_TEMPAT, tempat);

        long result = db.insert(TABLE_ABSENSI, null, values);
        return result != -1; // Jika -1 berarti gagal
    }

    // Mengambil semua data absensi
    public Cursor getAllAbsensi() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ABSENSI, null);
        return cursor; // Jangan tutup cursor di sini, biarkan yang memanggil yang menutupnya
    }


    // Mengupdate data absensi berdasarkan ID
    // Fungsi untuk menghapus absensi berdasarkan nama dan tanggal
    public boolean deleteAbsensi(String nama, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_ABSENSI,
                COLUMN_ABSENSI_NAME + "=? AND " + COLUMN_ABSENSI_TANGGAL + "=?",
                new String[]{nama, tanggal});

        return rowsDeleted > 0;
    }

    public boolean checkDuplicateAbsensi(String tanggal, String jam, String tempat) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ABSENSI +
                " WHERE " + COLUMN_ABSENSI_TANGGAL + " = ?" +
                " AND " + COLUMN_ABSENSI_JAM + " = ?" +
                " AND " + COLUMN_ABSENSI_TEMPAT + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tanggal, jam, tempat});
        boolean exists = (cursor.getCount() > 0);
        cursor.close(); // Menutup cursor setelah digunakan
        db.close(); // Menutup database setelah query selesai
        return exists;
    }



    // Fungsi untuk memperbarui absensi
    // Update data absensi
    public boolean updateAbsensi(String oldName, String oldDate, String newName, String newDate, String newDay, String newTime, String newPlace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ABSENSI_NAME, newName);
        values.put(COLUMN_ABSENSI_TANGGAL, newDate);
        values.put(COLUMN_ABSENSI_HARI, newDay);
        values.put(COLUMN_ABSENSI_JAM, newTime);
        values.put(COLUMN_ABSENSI_TEMPAT, newPlace);

        // Update berdasarkan nama lama dan tanggal lama
        int rowsAffected = db.update(TABLE_ABSENSI, values,
                COLUMN_ABSENSI_NAME + "=? AND " + COLUMN_ABSENSI_TANGGAL + "=?",
                new String[]{oldName, oldDate});

        return rowsAffected > 0;
    }

    // Menyimpan data alarm
    public boolean insertAlarm(String tanggal, String jam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARM_TANGGAL, tanggal);
        values.put(COLUMN_ALARM_JAM, jam);
        values.put(COLUMN_ALARM_STATUS, 1); // Set status alarm aktif

        long result = db.insert(TABLE_ALARM, null, values);
        return result != -1; // Jika -1 berarti gagal
    }

    // Mengambil semua data alarm
    public Cursor getAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM alarms", null);
    }

    // Mengambil alarm berdasarkan ID
    public Cursor getAlarmById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM alarms WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Menghapus alarm berdasarkan ID
    public boolean deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("alarms", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }



}
