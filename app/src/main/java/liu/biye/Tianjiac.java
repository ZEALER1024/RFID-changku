package liu.biye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.app.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加出库信息页面
 *
 * @author 刘自强
 */
public class Tianjiac extends Activity {
    /**
     * NFC相关
     */
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
    EditText spgs;
    String Old_Num,New_Num;
    String gs;
    String sp;
    String gss;
    String sps;
    String sl;
    String da;
    String gsmc_r,spmc_r,spgs_r;
    int a=0;
    int b=0;
    int i = 0;
    int j = 0;
    DatePicker date;
    int year;
    int mon;
    int day;
    Calendar c;
    String names;
    private Switch aSwitch;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tianjiac);
        Intent inte = getIntent();
        Bundle name = inte.getExtras();
        names = inte.getStringExtra("username");
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        TextView jmmc = (TextView)findViewById(R.id.jmmc);
        jmmc.setText("添加出库");
        TextView TS=(TextView)findViewById(R.id.save);
        TS.setText("靠近标签并保持一会");
        gsmc = (Spinner) findViewById(R.id.gsmcs);
        spmc = (Spinner) findViewById(R.id.spmcs);
        spmc.setClickable(false);
        gsmc.setClickable(false);
        lxr = (EditText) findViewById(R.id.lxre);
        lxdh = (EditText) findViewById(R.id.lxdhe);
        spgg = (EditText) findViewById(R.id.spgge);
        jldw = (EditText) findViewById(R.id.jldwe);
        spgs = (EditText) findViewById(R.id.jhsle);
        spgs.setText("待扫描");
        aSwitch=(Switch)findViewById(R.id.switch1);
        aSwitch.setChecked(true);
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
                        Tianjiac.this.year = year;
                        Tianjiac.this.mon = month;
                        Tianjiac.this.day = day;
                        //显示当前日期、时间
                        da = year + "年" + (month + 1) + "月" + day + "日";
                        System.out.println(da);

                    }
                });
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

    public void save() {

            // 查询语句
            String elxr = lxr.getText().toString();
            String elxdh = lxdh.getText().toString();
            String espgg = spgg.getText().toString();
            String ejldw = jldw.getText().toString();
            String espgs = spgs.getText().toString();

            // 定义ID
            int id = 1;
            String select = "select max(_id) from chuku";
            Cursor seCursor = sDatabase.rawQuery(select, null);
            try {
                seCursor.moveToFirst();
                id = Integer.parseInt(seCursor.getString(0));
                id += 1;
            } catch (Exception e) {
                // TODO: handle exception
                id = 1;
            }
            gs = filterCode(gss);
            sp = filterCode(sps);
            sDatabase.execSQL("insert into chuku values('" + id + "','"
                    + gs + "','" + elxr + "','" + elxdh + "','" + sp + "','"
                    + espgg + "','" + ejldw + "','" + espgs + "','" + da + "')");
            Toast.makeText(Tianjiac.this, "货物出库成功，出库单已建立", Toast.LENGTH_LONG).show();
            spgs.setText("");
            seCursor.close();

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
        if(aSwitch.isChecked()){
            List<NfcData> mlist = new ArrayList<>();
            NfcData nfcData = new NfcData(2, 0, " ".getBytes());
            mlist.add(nfcData);
            nfcData = new NfcData(2, 1, " ".getBytes());
            mlist.add(nfcData);
            nfcData = new NfcData(2, 2, " ".getBytes());
            mlist.add(nfcData);
            readTagMe(tag, mlist);
            try {
                gss=new String(mlist.get(0).getData());
                sps=new String(mlist.get(1).getData());
                sl=new String(mlist.get(2).getData());
            }
            catch (Exception e){
            }
            String sn = filterCode(gss);
            if (sn==null)
            {
                Toast.makeText(Tianjiac.this, "请重试", Toast.LENGTH_LONG).show();
                return;
            }
            Cursor cursor3= sDatabase.rawQuery("select * from guke where comname = ?", new String[] {sn});
            cursor3.moveToFirst();
            cursor3.moveToFirst();
            do {
                try {
                    String pername = cursor3.getString(2);
                    String tel= cursor3.getString(5);
                    System.out.println("读取为"+pername);
                    System.out.println("读取为"+tel);
                    lxr.setText(pername);
                    lxdh.setText(tel);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } while (cursor3.moveToNext());

            String snn = filterCode(sps);
            if(snn==null){
                Toast.makeText(Tianjiac.this, "请重试", Toast.LENGTH_LONG).show();
                return;
            }
            Cursor cursor4 = sDatabase.rawQuery("select * from products where pname = ?", new String[] {snn});
            cursor4.moveToFirst();
            do {
                try {
                    String guige = cursor4.getString(2);
                    String danwei= cursor4.getString(3);
                    System.out.println("读取为"+guige);
                    System.out.println("读取为"+danwei);
                    spgg.setText(guige);
                    jldw.setText(danwei);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } while (cursor4.moveToNext());

            String gsname[]={sn};
            String spname[]={snn};
            ArrayAdapter gsnameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gsname);
            gsmc.setAdapter(gsnameAdapter);
            ArrayAdapter spnameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spname);
            spmc.setAdapter(spnameAdapter);
            String snnn = filterCode(sl);
            spgs.setText(snnn);
            Old_Num=snnn;
            Toast.makeText(this, "货物信息读取成功", Toast.LENGTH_LONG).show();
        }
        if(!aSwitch.isChecked()){
            if (spgs.getText().toString().equals("")) {
                DialogDemo.builder(Tianjiac.this, "提示", "请填写完整信息");
            }
            else{
                try {
                    gsmc_r = gsmc.getSelectedItem().toString();
                    spmc_r = spmc.getSelectedItem().toString();
                    spgs_r = spgs.getText().toString();
                    a = Integer.parseInt(Old_Num);
                    b = Integer.parseInt(spgs_r);
                } catch (Exception e) {
                    DialogDemo.builder(Tianjiac.this, "提示", "请先读卡！！");
                    return;
                }


                if(b>a){
                    DialogDemo.builder(Tianjiac.this, "提示", "货物不足");
                }
                else{
                int c=a-b;
                New_Num=String.valueOf(c);
                System.out.println(gsmc_r);
                System.out.println(spmc_r);
                System.out.println(New_Num);
                List<NfcData> mlist = new ArrayList<>();
                NfcData nfcData = new NfcData(2, 0, stringToBytes16(gsmc_r));
                mlist.add(nfcData);
                nfcData = new NfcData(2, 1, stringToBytes16(spmc_r));
                mlist.add(nfcData);
                nfcData = new NfcData(2, 2, stringToBytes16(New_Num));
                mlist.add(nfcData);
                writeTagMe(tag, mlist);
                save();}
            }
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
