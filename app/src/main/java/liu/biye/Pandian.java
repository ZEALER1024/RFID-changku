package liu.biye;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pandian extends Activity {
    /**
     * NFC相关
     */
    public NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    public IntentFilter[] mFilters;
    public String[][] mTechLists;
    private ListView old_list, new_list;
    String old_id;
    String old_name;
    String old_num;
    String new_name;
    String new_num;
    SqlHelpdemo db;
    int i = 0;
    SimpleAdapter simple1;
    SQLiteDatabase sDatabase = null;
    List<Map<String, Object>> slist1 = new ArrayList<Map<String, Object>>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pandian);
        setTitle("物资盘点");
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
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_SHORT).show();
            return;
        }
        new_list = (ListView) findViewById(R.id.new_list);
        old_list = (ListView) findViewById(R.id.old_list);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<Map<String, Object>> slist = new ArrayList<Map<String, Object>>();
        db = new SqlHelpdemo(getApplicationContext(), "store.db", null, 1);
        sDatabase = db.getWritableDatabase();
        sDatabase.execSQL("delete from pandian");
        String selectStr = "select _id,pname,num from kucun";
        Cursor cursor = sDatabase.rawQuery(selectStr, null);
        cursor.moveToFirst();
        do {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                old_id = cursor.getString(0);
                old_name = cursor.getString(1);
                old_num = cursor.getString(2);
                i++;
                map.put("did", old_id);
                map.put("dname", old_name);
                map.put("num", old_num);
                slist.add(map);
                System.out.println("序号" + old_id);
                System.out.println("产品名" + old_name);
                System.out.println("数量" + old_num);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }

        } while (cursor.moveToNext());
        SimpleAdapter simple = new SimpleAdapter(this, slist,
                R.layout.pandianadapter,
                new String[]{"did", "dname", "num"},
                new int[]{R.id.t1, R.id.t2, R.id.t3
                });
        old_list.setAdapter(simple);
        update_list();
        simple1 = new SimpleAdapter(this, slist1,
                R.layout.pandianadapter,
                new String[]{"did", "dname", "num"},
                new int[]{R.id.t1, R.id.t2, R.id.t3
                });
        new_list.setAdapter(simple1);

    }

    void update_list() {
        slist1.clear();
        String selectStr1 = "select _id,pname,num from pandian";
        Cursor cursor1 = sDatabase.rawQuery(selectStr1, null);
        cursor1.moveToFirst();
        do {
            try {
                Map<String, Object> map1 = new HashMap<String, Object>();
                old_id = cursor1.getString(0);
                old_name = cursor1.getString(1);
                old_num = cursor1.getString(2);
                i++;
                map1.put("did", old_id);
                map1.put("dname", old_name);
                map1.put("num", old_num);
                slist1.add(map1);
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e);
            }

        } while (cursor1.moveToNext());

    }

    /*
  rfid写入
   */
    protected void onNewIntent(Intent intent) {
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
            Toast.makeText(this, "不支持MifareClassic", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "正在读取，保持接触", Toast.LENGTH_SHORT).show();
            List<NfcData> mlist = new ArrayList<>();
            NfcData nfcData = new NfcData(2, 0, " ".getBytes());
            mlist.add(nfcData);
            nfcData = new NfcData(2, 1, " ".getBytes());
            mlist.add(nfcData);
            nfcData = new NfcData(2, 2, " ".getBytes());
            mlist.add(nfcData);
            readTagMe(tag, mlist);
            try {
                new_name = new String(mlist.get(1).getData());
                new_num = new String(mlist.get(2).getData());
                System.out.println("读取产品名" + new_name);
                System.out.println("读取数量" + new_num);

            } catch (Exception e) {
            }
            String spm = filterCode(new_name);
            String spn = filterCode(new_num);
            if (spm == null) {
                Toast.makeText(this, "重新读取SPM！！！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spn == null) {
                Toast.makeText(this, "重新读取SPN", Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor cursor2 = sDatabase.rawQuery("select * from pandian where pname = ?", new String[]{spm});
            cursor2.moveToFirst();
            String string = null;
            String lo = null;
            do {
                try {
                    string = cursor2.getString(1);
                    lo = cursor2.getString(2);
                } catch (Exception e) {
                    // TODO: handle exception
                    string = "";
                }
                if (!string.equals("")) {
                    if (lo == null) {
                        Toast.makeText(this, "重新读取NUM", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int a = Integer.parseInt(spn);
                    int b = Integer.parseInt(filterCode(lo));
                    int c = a + b;
                    sDatabase.execSQL("update pandian set  num='" + c + "'where pname='" + spm + "'");
                    update_list();
                    simple1.notifyDataSetChanged();Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "读取成功", Toast.LENGTH_SHORT).show();
                    save(new_name, new_num);
                    return;
                }
            } while (cursor2.moveToNext());
        }
    }

    public void save(String spname, String spnum) {
        // 定义ID
        int id = 1;
        String spmn = filterCode(spname);
        String spnn = filterCode(spnum);
        String select = "select max(_id) from pandian";
        Cursor seCursor = sDatabase.rawQuery(select, null);
        try {
            seCursor.moveToFirst();
            id = Integer.parseInt(seCursor.getString(0));
            id += 1;
        } catch (Exception e) {
            // TODO: handle exception
            id = 1;
        }

        sDatabase.execSQL("insert into pandian values('" + id + "','" + spmn + "','" + spnn + "')");
        seCursor.close();
        update_list();
        simple1.notifyDataSetChanged();
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

    private String filterCode(String string) {
        if (string != null) {
            byte[] zero = new byte[1];
            zero[0] = (byte) 0;
            String s = new String(zero);
            string = string.replace(s, "");
        }
        return string;
    }
}

