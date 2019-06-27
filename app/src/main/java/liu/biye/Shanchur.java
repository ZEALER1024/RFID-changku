package liu.biye;

import java.io.IOException;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


/**
 * 删除入库页面
 *
 * @author 刘自强
 */

public class Shanchur extends Activity {
    /**
     * NFC相关
     */
    public NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    public IntentFilter[] mFilters;
    public String[][] mTechLists;
    String vlog="未确定";
    String id__="";
    String Old_Num,New_Num;
    private ListView listview;
    SqlHelpdemo db;
    int index_list = 0;
    SQLiteDatabase sDatabase = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shanchuc);
        setTitle("删除入库");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
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
                id__=stringObjectMap.get("id").toString();
                builder.setTitle("确认消息");
                builder.setMessage("确定要删除该入库吗？");
                builder.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                      vlog="确定删除";
                        DialogDemo.builder(Shanchur.this, "提示", "请靠近标签，并保持一段时间！");
                    }
                });
                builder.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        vlog="未确定";
                    }
                });
                builder.create().show();
            }
        });

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
        if(id__.equals("")){
            Toast.makeText(this, "请选择删除项", Toast.LENGTH_LONG).show();
        }
        if (vlog.equals("未确定")){
            Toast.makeText(this, "请确定", Toast.LENGTH_SHORT).show();
        }
        if(vlog.equals("确定删除")){
            List<NfcData> mlist1 = new ArrayList<>();
            NfcData nfcData1 = new NfcData(2, 2, " ".getBytes());
            mlist1.add(nfcData1);
            readTagMe(tag, mlist1);
            if(mlist1.get(0).getData()==null){
                Toast.makeText(this, "请重试", Toast.LENGTH_LONG).show();
                return;
            }
            String sl=new String(mlist1.get(0).getData());
            Old_Num=filterCode(sl);
            Cursor cursor4 = sDatabase.rawQuery("select * from ruku where _id = ?", new String[] {id__});
            cursor4.moveToFirst();
            do {
                try {
                    String shuliang= cursor4.getString(7);
                    System.out.println("读取为"+shuliang);
                    String erro_data=filterCode(shuliang);
                    int a = Integer.parseInt(Old_Num);
                    int b = Integer.parseInt(erro_data);
                    int c=a-b;
                    New_Num=String.valueOf(c);
                    System.out.println("老数据"+a);
                    System.out.println("数据库数据"+b);
                    System.out.println("新"+New_Num);
                    List<NfcData> mlist = new ArrayList<>();
                    NfcData nfcData = new NfcData(2, 2, stringToBytes16(New_Num));
                    mlist.add(nfcData);
                    writeTagMe(tag, mlist);
                    sDatabase.execSQL("delete from ruku where _id='" + id__ + "'");
                    Intent intent1 = new Intent();
                    intent1.setClass(Shanchur.this, Shanchur.class);
                    startActivity(intent1);
                    finish();
                    Toast.makeText(this, "入库信息已删除", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } while (cursor4.moveToNext());

        }
        else {
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
