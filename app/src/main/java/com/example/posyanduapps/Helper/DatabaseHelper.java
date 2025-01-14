package com.example.posyanduapps.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "posyandu.db";
    private static final int DATABASE_VERSION = 2;

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
        String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_ALARM + "("
                + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_ALARM_TANGGAL + " TEXT, "
                + COLUMN_ALARM_JAM + " TEXT, "
                + COLUMN_ALARM_STATUS + " INTEGER" // 0 = Inaktif, 1 = Aktif
                + ")";
        db.execSQL(CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABSENSI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
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
        Log.d("insertAbsensi", "Inserting data: " + nama + ", " + tanggal + ", " + hari + ", " + jam + ", " + tempat);
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
    Log.d("Updated Absensi", "Old Data: " + oldName + ", " + oldDate + ", \nNew Data: " + newName + ", " + newDate + ", " + newDay + ", " + newTime + ", " + newPlace);
        // Update berdasarkan nama lama dan tanggal lama
        int rowsAffected = db.update(TABLE_ABSENSI, values,
                COLUMN_ABSENSI_NAME + "=? AND " + COLUMN_ABSENSI_TANGGAL + "=?",
                new String[]{oldName, oldDate});

        return rowsAffected > 0;
    }

    // Menyimpan data alarm
    public boolean insertAlarm(int id,String tanggal, String jam, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARM_ID, id);
        values.put(COLUMN_ALARM_TANGGAL, tanggal);
        values.put(COLUMN_ALARM_JAM, jam);
        values.put(COLUMN_ALARM_STATUS, status); // status alarm, 1 untuk aktif

        try {
            long result = db.insert(TABLE_ALARM, null, values);
            if (result == -1) {
                Log.e("insertAlarm", "Insert failed: " + tanggal + ", " + jam + ", " + status);
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e("insertAlarm", "Error inserting alarm: " + e.getMessage());
            return false;
        } finally {
            db.close(); // Pastikan database selalu ditutup
        }



    }

    // Mengambil semua data alarm
    public Cursor getAllAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ALARM, null);
    }
    // Mengambil alarm berdasarkan ID
    public Cursor getAlarmById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ALARM + " WHERE " + COLUMN_ALARM_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Menghapus alarm berdasarkan ID
    public boolean deleteAlarm(int alarmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Menghapus data berdasarkan ID
        int result = db.delete(TABLE_ALARM, COLUMN_ALARM_ID + " = ?", new String[]{String.valueOf(alarmId)});
        return result > 0; // Mengembalikan true jika berhasil dihapus
    }

    // Mengupdate alarm berdasarkan ID
    public boolean updateAlarm(int id, String tanggal, String jam, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALARM_TANGGAL, tanggal);
        values.put(COLUMN_ALARM_JAM, jam);
        values.put(COLUMN_ALARM_STATUS, status);

        int rowsUpdated = db.update(TABLE_ALARM, values,
                COLUMN_ALARM_ID + " = ?",
                new String[]{String.valueOf(id)});
        return rowsUpdated > 0; // Mengembalikan true jika ada yang terupdate
    }






}
