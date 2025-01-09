package com.example.posyanduapps.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.posyanduapps.R;
import com.example.posyanduapps.Helper.DatabaseHelper;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;

    public UserAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_user, parent, false);
        }

        cursor.moveToPosition(position);
        TextView nameTextView = convertView.findViewById(R.id.namaLengkapTextView);
        TextView alamatTextView = convertView.findViewById(R.id.alamatTextView);
        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        alamatTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ALAMAT_LENGKAP)));
        usernameTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_USERNAME)));
        nameTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAMA_LENGKAP)));

        return convertView;
    }
}
