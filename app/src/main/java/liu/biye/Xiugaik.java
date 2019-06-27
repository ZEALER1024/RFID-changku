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
 * 修改客户页面
 *
 * @author 刘自强
 */

public class Xiugaik extends Activity {
    private ListView listview;

    String id[];
    String cname[];
    String pname[];
    String add[];
    String city[];
    String diqu[];
    String youbian[];
    String tel[];
    String chuanzhen[];
    String web[];

    SqlHelpdemo db;
    int i = 0;
    SQLiteDatabase sDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shanchuk);
        setTitle("修改客户");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        listview = (ListView) findViewById(R.id.kehulist);
        final List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
        String selectStr = "select _id,comname,pername,addr,youbian,tel  from guke";
        Cursor cursor = sDatabase.rawQuery(selectStr, null);

        cursor.moveToFirst();

        int count = cursor.getCount();
        id = new String[count];
        cname = new String[count];
        pname = new String[count];
        add = new String[count];
        youbian = new String[count];
        tel = new String[count];

        do {
            try {
                id[i] = cursor.getString(0);
                cname[i] = cursor.getString(1);
                pname[i] = cursor.getString(2);
                add[i] = cursor.getString(3);
                youbian[i] = cursor.getString(4);
                tel[i] = cursor.getString(5);
                i++;

            } catch (Exception e) {
                // TODO: handle exception

            }

        } while (cursor.moveToNext());

        for (int i = 0; i < id.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id[i]);
            map.put("cname", cname[i]);
            map.put("pname", pname[i]);
            map.put("add", add[i]);
            map.put("youbian", youbian[i]);
            map.put("tel", tel[i]);
            slist.add(map);
        }
        SimpleAdapter simple = new SimpleAdapter(this, slist,
                R.layout.kehukadpter, new String[]{"id", "cname", "pname",
                "add", "youbian", "tel"}, new int[]{R.id.t1, R.id.t2, R.id.t3,
                R.id.t4, R.id.t5, R.id.t6});
        listview.setAdapter(simple);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Map<String, Object> stringObjectMap = slist.get(arg2);
                final String id__=stringObjectMap.get("id").toString();
                builder.setTitle("确认消息");
                builder.setMessage("确定要修改该客户吗？");
                final int j = i;
                builder.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub


                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("compname", id__);
                        intent.putExtras(bundle);
                        intent.setClass(Xiugaik.this, Xiugaik1.class);
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
