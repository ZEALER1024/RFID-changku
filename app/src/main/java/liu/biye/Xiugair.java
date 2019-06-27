package liu.biye;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;


/**
 * 修改入库页面
 *
 * @author 刘自强
 */

public class Xiugair extends Activity {
    private ListView listview;

    SqlHelpdemo db;
    int index_list = 0;
    SQLiteDatabase sDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shanchuc);
        setTitle("修改入库");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        listview = (ListView) findViewById(R.id.kehulist);
        final List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
        String selectStr = "select _id,comname,pername,tel,products,guige,danwei,num,date  from ruku";
        Cursor cursor = sDatabase.rawQuery(selectStr, null);

        cursor.moveToFirst();

        int count = cursor.getCount();
        Map<String, Object> map;

        do {
            try {
                map = new HashMap<String, Object>();
                String _id = cursor.getString(0);
                String comname = cursor.getString(1);
                String pername = cursor.getString(2);
                String tel= cursor.getString(3);
                String products= cursor.getString(4);
                String guige = cursor.getString(5);
                String danwei= cursor.getString(6);
                String num= cursor.getString(7);
                String date = cursor.getString(8);
                map.put("id", _id);
                map.put("comname", comname);
                map.put("pername", pername);
                map.put("tel", tel);
                map.put("products", products);
                map.put("guige", guige );
                map.put("danwei", danwei);
                map.put("num", num);
                map.put("date", date );
                slist.add(map);
                index_list++;
            } catch (Exception e) {
                // TODO: handle exception

            }

        } while (cursor.moveToNext());
        SimpleAdapter simple = new SimpleAdapter(this, slist,
                R.layout.churukukadpter, new String[]{"id", "comname", "pername",
                "tel", "products", "guige", "danwei", "num", "date"}, new int[]{R.id.t1, R.id.t2, R.id.t3,
                R.id.t4, R.id.t5, R.id.t6, R.id.t7, R.id.t8, R.id.t9,});
        listview.setAdapter(simple);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Map<String, Object> stringObjectMap = slist.get(arg2);
                final String id__=stringObjectMap.get("id").toString();
                builder.setTitle("确认消息");
                builder.setMessage("确定要修改该入库吗？");
                builder.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("chuangzhen", id__);
                        intent.putExtras(bundle);
                        intent.setClass(Xiugair.this, Xiugair1.class);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


                    }
                });
                builder.create().show();
            }
        });
    }

}
