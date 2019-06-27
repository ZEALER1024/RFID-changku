package liu.biye;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改客户信息页面
 *
 * @author刘自强
 */
public class Xiugaik1 extends Activity {
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
        TextView BANNER=(TextView)findViewById(R.id.t) ;
        BANNER.setText("修改客户信息");
        setTitle("修改客户信息");
        Intent inte = getIntent();
        Bundle name = inte.getExtras();
        names = inte.getStringExtra("compname");
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        gsmc = (EditText) findViewById(R.id.gsmce);
        lxr = (EditText) findViewById(R.id.lxre);
        lxdz = (EditText) findViewById(R.id.lxdze);
        yzbm = (EditText) findViewById(R.id.yzbme);
        lxdh = (EditText) findViewById(R.id.lxdhe);
        Cursor cursor = sDatabase.rawQuery("select * from guke where _id = ?", new String[] { names.toString()});
        cursor.moveToFirst();
        do {
            try {
                String gs = cursor.getString(1);
                String pername = cursor.getString(2);
                String dizhi= cursor.getString(3);
                String youbian = cursor.getString(4);
                String dianhua= cursor.getString(5);
               gsmc.setText(gs);
               lxr.setText(pername);
               lxdz.setText(dizhi);
               yzbm.setText(youbian);
               lxdh.setText(dianhua);


            } catch (Exception e) {
                // TODO: handle exception

            }

        } while (cursor.moveToNext());

    }

    /**
     * 保存按钮监听
     *
     * @param v
     */
    public void save1(View v) {
        if (gsmc.getText().toString().equals("")) {
            DialogDemo.builder(Xiugaik1.this, "提示", "请输入公司名称");
        } else {
            // 查询语句
            String egsmc = gsmc.getText().toString();
            String elxr = lxr.getText().toString();
            String elxdz = lxdz.getText().toString();
            String eyzbm = yzbm.getText().toString();
            String elxdh = lxdh.getText().toString();
            String selectStr = "select comname,pername,addr,youbian,tel from guke";
            System.out.println("11111111111111");
            Cursor cursor = sDatabase.rawQuery(selectStr, null);
            System.out.println("22222222222222");
            cursor.moveToFirst();
            String cname = null;
            String pname = null;
            String padd = null;
            String pyoubian = null;
            String ptel = null;

            do {
                try {
                    cname = cursor.getString(0);
                    pname = cursor.getString(1);
                    padd = cursor.getString(2);
                    pyoubian = cursor.getString(5);
                    ptel = cursor.getString(6);

                    System.out.println("3333333333333333333333");
                } catch (Exception e) {
                    // TODO: handle exception
                    cname = "";
                    pname = "";
                    padd = "";
                    pyoubian = "";
                    ptel = "";
                }
                if (cname.equals(egsmc) && pname.equals(elxr) && padd.equals(elxdz) && pyoubian.equals(eyzbm) && ptel.equals(elxdh)) {
                    DialogDemo.builder(Xiugaik1.this, "错误信息", "该公司信息已存在");
                    cursor.close();
                    break;

                }
            } while (cursor.moveToNext());

            if (!(cname.equals(egsmc) && pname.equals(elxr) && padd.equals(elxdz)&& pyoubian.equals(eyzbm) && ptel.equals(elxdh))) {
                // 定义ID
                int id = 0;
                String select = "select max(_id) from guke";
                Cursor seCursor = sDatabase.rawQuery(select, null);
                try {
                    seCursor.moveToFirst();
                    id = Integer.parseInt(seCursor.getString(0));
                    id += 1;
                } catch (Exception e) {
                    // TODO: handle exception
                    id = 0;
                }
                sDatabase.execSQL("update guke set comname='" + egsmc + "',pername='" + elxr + "',addr='" + elxdz + "'," +
                        "youbian='" + eyzbm + "',tel='" + elxdh + "'where _id='" + names + "'"
                );
                Toast.makeText(Xiugaik1.this, "修改成功", Toast.LENGTH_LONG).show();

                seCursor.close();

            }
        }
    }

    public void back1(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(Xiugaik1.this, Xiugaik.class);
        startActivity(intent);
        finish();
    }

}
