package liu.biye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改出库信息页面
 *
 * @author 刘自强
 */
public class Xiugaic1 extends Activity {
    public NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    public IntentFilter[] mFilters;
    public String[][] mTechLists;
    SqlHelpdemo db;
    SQLiteDatabase sDatabase = null;
    Spinner gsmc;
    EditText lxr;
    EditText lxdh;
    Spinner spmc;
    EditText spgg;
    EditText jldw;
    EditText spdj;
    EditText spgs;
    String da;
    int i = 0;
    int j = 0;
    DatePicker date;
    int year;
    int mon;
    int day;
    String names;
    String Old_Num,New_Num;
    private Switch aSwitch;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tianjiac);
        Intent inte = getIntent();
        TextView TS=(TextView)findViewById(R.id.save);
        TS.setText("靠近标签并保持一会");
        names = inte.getStringExtra("chuangzhen");
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        TextView jmmc = (TextView)findViewById(R.id.jmmc);
        jmmc.setText("修改出库");
        gsmc = (Spinner) findViewById(R.id.gsmcs);
        spmc = (Spinner) findViewById(R.id.spmcs);
        spmc.setClickable(false);
        gsmc.setClickable(false);
        lxr = (EditText) findViewById(R.id.lxre);
        lxdh = (EditText) findViewById(R.id.lxdhe);
        spgg = (EditText) findViewById(R.id.spgge);
        jldw = (EditText) findViewById(R.id.jldwe);
        spdj = (EditText) findViewById(R.id.jhdje);
        spgs = (EditText) findViewById(R.id.jhsle);
        aSwitch=(Switch)findViewById(R.id.switch1);
        aSwitch.setVisibility(View.INVISIBLE);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        mon = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        da = year + "年" + (mon + 1) + "月" + day + "日";
        date = (DatePicker) findViewById(R.id.jhrqd);
        date.init(year, mon, day
                , new OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker arg0, int year
                            , int month, int day) {
                        Xiugaic1.this.year = year;
                        Xiugaic1.this.mon = month;
                        Xiugaic1.this.day = day;
                        //显示当前日期、时间
                        da = year + "年" + (month + 1) + "月" + day + "日";
                        System.out.println(da);

                    }
                });

        Cursor cursor = sDatabase.rawQuery("select * from chuku where _id = ?", new String[] { names.toString()});
        cursor.moveToFirst();
        System.out.println("time为"+names);
        do {
            try {
                    String gs = cursor.getString(1);
                    String pername = cursor.getString(2);
                    String tel= cursor.getString(3);
                    String sp= cursor.getString(4);
                    String guige = cursor.getString(5);
                    String danwei= cursor.getString(6);
                    String shuliang=cursor.getString(7);
                    String date = cursor.getString(8);
                    String gsname[]={gs};
                    String spname[]={sp};
                    Old_Num=shuliang;
                    ArrayAdapter gsnameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gsname);
                    gsmc.setAdapter(gsnameAdapter);
                    ArrayAdapter spnameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spname);
                    spmc.setAdapter(spnameAdapter);
                    lxr.setText(pername);
                    lxdh.setText(tel);
                    spgg.setText(guige);
                    jldw.setText(danwei);
                    spgs.setText(shuliang);
                    System.out.println("time为"+date);
            } catch (Exception e) {
                // TODO: handle exception

            }

        }
        while (cursor.moveToNext());
        /*
         * nfc权限
         */
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            ndef.addDataType("*/*");

        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{ndef,};
        mTechLists = new String[][]{{IsoDep.class.getName()}, {NfcA.class.getName()},};
        Log.d(" mTechLists", NfcF.class.getName() + mTechLists.length);

        if (mAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
            return;
        }
        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
            return;
        }
    }
    @Override
    /*
    rfid写入
     */
    protected   void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(mAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.indexOf("MifareClassic") >= 0) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            Toast.makeText(this, "不支持MifareClassic", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            String ta=null;
            List<NfcData> mlist1 = new ArrayList<>();
            NfcData nfcData1 = new NfcData(2, 2, " ".getBytes());
            mlist1.add(nfcData1);
            readTagMe(tag, mlist1);
            if(mlist1.get(0).getData()==null){
                Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            String sl=filterCode(new String(mlist1.get(0).getData()));
            String spgs_r = spgs.getText().toString();
            New_Num=spgs_r;
            int a = Integer.parseInt(Old_Num);
            int b = Integer.parseInt(New_Num);
            int c = Integer.parseInt(sl);
            if (a>b){
                ta=String.valueOf(c+(a-b));
            }
            if (a<b){
                 ta=String.valueOf(c-(b-a));
            }
            if (a==b){
                ta=String.valueOf(c);
                Toast.makeText(this, "未修改数据", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ta==null){
                Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
            }
            System.out.println(spgs_r);
            List<NfcData> mlist = new ArrayList<>();
            NfcData nfcData = new NfcData(2, 2, stringToBytes16(ta));
            mlist.add(nfcData);
            writeTagMe(tag, mlist);
            Intent intent1 = new Intent();
            intent1.setClass(Xiugaic1.this, Xiugaic.class);
            startActivity(intent1);
            Toast.makeText(this, "修改出库成功", Toast.LENGTH_LONG).show();
            save();
            finish();
        }
    }
    public void readTagMe(Tag tag, List<NfcData> readList) {
        MifareClassic mfc = MifareClassic.get(tag);
        boolean auth = false;
        //读取TAG
        try {
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            for (int j = 0; j < readList.size(); j++) {
                NfcData nfcData = readList.get(j);
                //Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyA(nfcData.getSectorIndex(), MifareClassic.KEY_DEFAULT);
                int bIndex;
                if (auth) {
                    bIndex = mfc.sectorToBlock(nfcData.getSectorIndex());
                    nfcData.setData(mfc.readBlock(bIndex + nfcData.getBlockIndex()));
                } else {
                    nfcData.setData(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private String filterCode(String string) {
        if (string != null) {

            byte[] zero = new byte[1];
            zero[0] = (byte) 0;
            String s = new String(zero);
            string = string.replace(s, "");
        }
        return string;
    }

    public void save() {
        if (spgs.getText().toString().equals("")) {
            DialogDemo.builder(Xiugaic1.this, "提示", "请填写完整信息");

        } else {

            // 查询语句

            String elxr = lxr.getText().toString();
            String elxdh = lxdh.getText().toString();
            String espgg = spgg.getText().toString();
            String ejldw = jldw.getText().toString();
            String gs=gsmc.getSelectedItem().toString();
            String sp=spmc.getSelectedItem().toString();
            // 定义ID

            String select = "select max(_id) from chuku";
            Cursor seCursor = sDatabase.rawQuery(select, null);
            try {
                seCursor.moveToFirst();

            } catch (Exception e) {
                // TODO: handle exception

            }
            sDatabase.execSQL("update chuku set comname='" + gs + "',pername='" + elxr + "',tel='" + elxdh + "',products='" + sp + "',guige='" + espgg + "',danwei='"
                    + ejldw + "" + "',num='" + New_Num + "',date='" + da + "'where _id='" + names + "'"
            );
            Toast.makeText(Xiugaic1.this, "修改成功", Toast.LENGTH_LONG).show();

            seCursor.close();


        }


    }
    public void writeTagMe(Tag tag, List<NfcData> mlist) {
        MifareClassic mfc = MifareClassic.get(tag);
        try {
            mfc.connect();
            boolean auth = false;
            for (int i = 0; i < mlist.size(); i++) {
                NfcData nfcData = mlist.get(i);
                auth = mfc.authenticateSectorWithKeyA(nfcData.getSectorIndex(),
                        MifareClassic.KEY_DEFAULT);
                if (auth) {
                    // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                    mfc.writeBlock(nfcData.getSectorIndex() * 4 + nfcData.getBlockIndex(), nfcData.getData());

                }
            }
            mfc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] stringToBytes16(String str) {
        if(str==null){return null;}
        byte[] newData = new byte[16];
        byte[] data = str.getBytes();
        if (data.length > 16) {
            System.arraycopy(data, 0, newData, 0, 16);
        } else {
            System.arraycopy(" \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes(), 0, newData, 0, 16);
            System.arraycopy(data, 0, newData, 0, data.length);
        }
        return newData;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        mAdapter.disableForegroundDispatch(this);
    }
}
