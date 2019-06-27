package liu.biye;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 添加客户信息页面
 *
 * @author刘自强
 */
public class Tianjiak extends Activity {
    private EditText gsmc;
    private EditText lxr;
    private EditText lxdz;
    private EditText yzbm;
    private EditText lxdh;
    SqlHelpdemo db;
    SQLiteDatabase sDatabase = null;
    String names;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tianjiak);
        setTitle("添加客户信息");
        Intent inte = getIntent();
        Bundle name = inte.getExtras();
        names = inte.getStringExtra("username");
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        gsmc = (EditText) findViewById(R.id.gsmce);
        lxr = (EditText) findViewById(R.id.lxre);
        lxdz = (EditText) findViewById(R.id.lxdze);
        yzbm = (EditText) findViewById(R.id.yzbme);
        lxdh = (EditText) findViewById(R.id.lxdhe);
    }

    /**
     * 保存按钮监听
     *
     * @param v
     */
    public void save1(View v) {
        if (gsmc.getText().toString().equals("")) {
            DialogDemo.builder(Tianjiak.this, "提示", "请输入公司名称");
        } else {
            // 查询语句
            String egsmc = gsmc.getText().toString();
            String elxr = lxr.getText().toString();
            String elxdz = lxdz.getText().toString();
            String eyzbm = yzbm.getText().toString();
            String elxdh = lxdh.getText().toString();
            String selectStr = "select comname  from guke";
            System.out.println("11111111111111");
            Cursor cursor = sDatabase.rawQuery(selectStr, null);
            System.out.println("22222222222222");
            cursor.moveToFirst();
            String nameg = null;

            do {
                try {
                    nameg = cursor.getString(0);

                    System.out.println("3333333333333333333333");
                } catch (Exception e) {
                    // TODO: handle exception
                    nameg = "";

                }
                if (nameg.equals(egsmc)) {
                    DialogDemo.builder(Tianjiak.this, "错误信息", "该公司信息已存在");
                    cursor.close();
                    break;

                }
            } while (cursor.moveToNext());

            if (!nameg.equals(egsmc)) {
                // 定义ID
                int id = 1;
                String select = "select max(_id) from guke";
                Cursor seCursor = sDatabase.rawQuery(select, null);
                try {
                    seCursor.moveToFirst();
                    id = Integer.parseInt(seCursor.getString(0));
                    id += 1;
                } catch (Exception e) {
                    // TODO: handle exception
                    id = 1;
                }
                sDatabase.execSQL("insert into guke values('" + id + "','"
                        + egsmc + "','" + elxr + "','" + elxdz + "','" + eyzbm + "','" + elxdh + "')");
                Toast.makeText(Tianjiak.this, "添加成功", Toast.LENGTH_LONG).show();

                seCursor.close();


            }
        }
    }


}
