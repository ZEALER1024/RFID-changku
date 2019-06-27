package liu.biye;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.SimpleAdapter;


/**
 * 查询客户页面
 *
 * @author 刘自强
 */

public class Chaxunk extends Activity {
    private ListView listview;

    String id[];
    String cname[];
    String pname[];
    String add[];
    String youbian[];
    String tel[];

    SqlHelpdemo db;
    int i = 0;
    SQLiteDatabase sDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shanchuk);
        setTitle("查询客户");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        listview = (ListView) findViewById(R.id.kehulist);
        List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
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

    }

}
