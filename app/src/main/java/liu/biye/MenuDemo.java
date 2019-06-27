package liu.biye;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 主菜单页面
 *
 * @author 刘自强
 */

public class MenuDemo extends TabActivity implements OnTabChangeListener{
    /**
     * NFC相关
     */
    public NfcAdapter mAdapter;
    public PendingIntent mPendingIntent;
    public IntentFilter[] mFilters;
    public String[][] mTechLists;
    /**
     * 添加用户键
     */
    private Button tianjia1;
    /**
     * 修改用户键
     */
    private Button xiugai1;
    /**
     * 删除用户键
     */
    private Button shanchu1;
    /**
     * 查询用户键
     */
    private Button chaxun1;
    /**
     * 添加商品键
     */
    private Button tianjia2;
    /**
     * 修改商品键
     */
    private Button xiugai2;
    /**
     * 删除商品键
     */
    private Button shanchu2;
    /**
     * 查询商品键
     */
    private Button chaxun2;

    /**
     * 添加入库键
     */
    private Button tianjia4;
    /**
     * 修改入库键
     */
    private Button xiugai4;
    /**
     * 删除入库键
     */
    private Button shanchu4;
    /**
     * 查询入库键
     */
    private Button chaxun4;
    /**
     * 添加出库键
     */
    private Button tianjia5;
    /**
     * 修改出库键
     */
    private Button xiugai5;
    /**
     * 删除出库键
     */
    private Button shanchu5;
    /**
     * 查询出库键
     */
    private Button chaxun5;
    /**
     * 用户管理键
     */
    private Button yonghu;
    /**
     * 修改密码键
     */
    private Button mima;
    String names = null;
    private TextView RFID_g, RFID_s, RFID_n;
    String GGB="error";
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        TabHost tab = getTabHost();
        tab.setPadding(0, -20, 0, 0);
        tab.setDrawingCacheBackgroundColor(Color.BLUE);
        LayoutInflater inf = getLayoutInflater();
        inf.inflate(R.layout.menudemo, tab.getTabContentView());
        Bundle name = getIntent().getExtras();
        names = name.getString("username");
        /**
         * 所有按钮初始化
         */
        tianjia1 = (Button) findViewById(R.id.tianjia1);
        xiugai1 = (Button) findViewById(R.id.xiugai1);
        shanchu1 = (Button) findViewById(R.id.shanchu1);
        chaxun1 = (Button) findViewById(R.id.chaxun1);
        tianjia2 = (Button) findViewById(R.id.tianjia2);
        xiugai2 = (Button) findViewById(R.id.xiugai2);
        shanchu2 = (Button) findViewById(R.id.shanchu2);
        chaxun2 = (Button) findViewById(R.id.chaxun2);
        tianjia4 = (Button) findViewById(R.id.tianjia4);
        xiugai4 = (Button) findViewById(R.id.xiugai4);
        shanchu4 = (Button) findViewById(R.id.shanchu4);
        chaxun4 = (Button) findViewById(R.id.chaxun4);
        tianjia5 = (Button) findViewById(R.id.tianjia5);
        xiugai5 = (Button) findViewById(R.id.xiugai5);
        shanchu5 = (Button) findViewById(R.id.shanchu5);
        chaxun5 = (Button) findViewById(R.id.chaxun5);
        yonghu = (Button) findViewById(R.id.yonghu);
        mima = (Button) findViewById(R.id.mima);
        RFID_g = (TextView) findViewById(R.id.RFID_Gname);
        RFID_s = (TextView) findViewById(R.id.RFID_Pname);
        RFID_n = (TextView) findViewById(R.id.RFID_num);
        /**
         * tabhost.tabspec创建
         */
        final TabHost.TabSpec tabs1 = tab.newTabSpec("基本信息");
        tabs1.setContent(R.id.li1);
        tabs1.setIndicator("基本信息", null);
        final TabHost.TabSpec tabs2 = tab.newTabSpec("库存管理");
        tabs2.setContent(R.id.li2);
        tabs2.setIndicator("仓库管理", null);
        final TabHost.TabSpec tabs3 = tab.newTabSpec("信息查询");
        tabs3.setContent(R.id.li3);
        tabs3.setIndicator("库存信息", null);
        final TabHost.TabSpec tabs4 = tab.newTabSpec("标签采集");
        tabs4.setContent(R.id.li4);
        tabs4.setIndicator("标签采集", null);
        final TabHost.TabSpec tabs5 = tab.newTabSpec("用户管理");
        tabs5.setContent(R.id.li5);
        tabs5.setIndicator("用户管理", null);
        tab.addTab(tabs1);
        tab.addTab(tabs2);
        tab.addTab(tabs3);
        tab.addTab(tabs5);
        tab.addTab(tabs4);
        tab.setOnTabChangedListener(this);
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
            Toast.makeText(this, "不支持MifareClassic", Toast.LENGTH_LONG).show();
            return;
        }
        if (GGB.equals("标签采集")) {

            try {
                List<NfcData> mlist = new ArrayList<>();
                NfcData nfcData = new NfcData(2, 0, " ".getBytes());
                mlist.add(nfcData);
                nfcData = new NfcData(2, 1, " ".getBytes());
                mlist.add(nfcData);
                nfcData = new NfcData(2, 2, " ".getBytes());
                mlist.add(nfcData);
                readTagMe(tag, mlist);
                RFID_g.setText(new String(mlist.get(0).getData()));
                RFID_s.setText(new String(mlist.get(1).getData()));
                RFID_n.setText(new String(mlist.get(2).getData()));
                Toast.makeText(this, "读取成功", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                System.out.println(e.toString());
                Toast.makeText(this, "请重试", Toast.LENGTH_LONG).show();
            }

        }
        else {
            System.out.println(GGB);
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
                    Toast.makeText(this, "读取失败", Toast.LENGTH_LONG).show();
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
    public void onTabChanged(String tabId){
        if(tabId.equals("标签采集")){
            GGB="标签采集";
            System.out.println(GGB);
        }
        else {
            GGB="禁止读写";
            System.out.println(GGB);
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
    /**
     * 商品信息按钮监听
     *
     * @param v
     */
    public void onshangpin(View v) {
        tianjia1.setVisibility(View.VISIBLE);
        xiugai1.setVisibility(View.VISIBLE);
        shanchu1.setVisibility(View.VISIBLE);
        chaxun1.setVisibility(View.VISIBLE);
        tianjia2.setVisibility(View.INVISIBLE);
        xiugai2.setVisibility(View.INVISIBLE);
        shanchu2.setVisibility(View.INVISIBLE);
        chaxun2.setVisibility(View.INVISIBLE);
    }
    /**
     * 客户信息按钮监听
     *
     * @param v
     */
    public void onkehu(View v) {
        tianjia2.setVisibility(View.VISIBLE);
        xiugai2.setVisibility(View.VISIBLE);
        shanchu2.setVisibility(View.VISIBLE);
        chaxun2.setVisibility(View.VISIBLE);
        tianjia1.setVisibility(View.INVISIBLE);
        xiugai1.setVisibility(View.INVISIBLE);
        shanchu1.setVisibility(View.INVISIBLE);
        chaxun1.setVisibility(View.INVISIBLE);
    }
    /**
     * 商品入库信息按钮监听
     *
     * @param v
     */
    public void onruku(View v) {
        tianjia4.setVisibility(View.VISIBLE);
        xiugai4.setVisibility(View.VISIBLE);
        shanchu4.setVisibility(View.VISIBLE);
        chaxun4.setVisibility(View.VISIBLE);
        tianjia5.setVisibility(View.INVISIBLE);
        xiugai5.setVisibility(View.INVISIBLE);
        shanchu5.setVisibility(View.INVISIBLE);
        chaxun5.setVisibility(View.INVISIBLE);
    }

    /**
     * 商品出库信息按钮监听
     *
     * @param v
     */
    public void onchuku(View v) {
        tianjia5.setVisibility(View.VISIBLE);
        xiugai5.setVisibility(View.VISIBLE);
        shanchu5.setVisibility(View.VISIBLE);
        chaxun5.setVisibility(View.VISIBLE);
        tianjia4.setVisibility(View.INVISIBLE);
        xiugai4.setVisibility(View.INVISIBLE);
        shanchu4.setVisibility(View.INVISIBLE);
        chaxun4.setVisibility(View.INVISIBLE);
    }


    /**
     * 添加入库信息按钮监听
     *
     * @param v
     */
    public void tianjiar(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Tianjiar.class);
        startActivity(intent);
    }

    /**
     * 查询入库信息按钮监听
     *
     * @param v
     */
    public void chaxunruku(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Chaxunr.class);
        startActivity(intent);
    }
    /**
     * 添加商品信息按钮监听
     *
     * @param v
     */
    public void ontianjias(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Tianjias.class);
        startActivity(intent);
    }

    /**
     * 添加客户信息按钮监听
     *
     * @param v
     */
    public void tianjiak(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Tianjiak.class);
        startActivity(intent);
    }
    /**
     * 查询客户信息按钮监听
     *
     * @param v
     */
    public void chaxunk(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Chaxunk.class);
        startActivity(intent);
    }

    /**
     * 添加出库按钮监听
     *
     * @param v
     */
    public void tianjiac(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Tianjiac.class);
        startActivity(intent);
    }

    /**
     * 查询出库按钮监听
     *
     * @param v
     */
    public void chaxunc(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Chaxunc.class);
        startActivity(intent);
    }

    /**
     * 修改密码按钮监听
     *
     * @param v
     */

    public void mima(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("username", names);
        intent.putExtras(bundle);
        intent.setClass(getApplicationContext(), Xiugai.class);
        startActivity(intent);
    }

    /**
     * 用户管理按钮监听
     *
     * @param v
     */
    public void yonghu(View v) {
        if (!names.equals("admin")) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("没有管理员权限")
                    .setPositiveButton("确定", null)
                    .show();

        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("username", names);
            intent.putExtras(bundle);
            intent.setClass(getApplicationContext(), Yonghu.class);
            startActivity(intent);
        }
    }

    /**
     * 删除商品按钮监听
     *
     * @param v
     */
    public void shanchus(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Shanchus.class);
        startActivity(intent);
    }

    /**
     * 删除客户按钮监听
     *
     * @param v
     */


    public void shanchuk(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Shanchuk.class);
        startActivity(intent);
    }

    /**
     * 查询商品按钮监听
     *
     * @param v
     */
    public void chaxuns(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Chaxuns.class);
        startActivity(intent);
    }


    /**
     * 修改商品按钮监听
     *
     * @param v
     */
    public void xiugais(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Xiugais.class);
        startActivity(intent);
    }

    /**
     * 修改客户按钮监听
     *
     * @param v
     */
    public void xiugaik(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Xiugaik.class);
        startActivity(intent);
    }


    /**
     * 删除出库按钮监听
     *
     * @param v
     */
    public void shanchuc(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Shanchuc.class);
        startActivity(intent);
    }

    /**
     * 删除入库按钮监听
     *
     * @param v
     */
    public void shanchur(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Shanchur.class);
        startActivity(intent);
    }

    /**
     * 修改入库按钮监听
     *
     * @param v
     */
    public void xiugair(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Xiugair.class);
        startActivity(intent);
    }

    /**
     * 修改出库按钮监听
     *
     * @param v
     */
    public void xiugaic(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Xiugaic.class);
        startActivity(intent);
    }
    /**
     * 库存信息查询按钮监听
     *
     * @param v
     */
    public void kucun(View v) {
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Kucun.class);
        startActivity(intent);
    }
    public void pandian(View v){
        Intent intent = new Intent();
        intent.setClass(MenuDemo.this, Pandian.class);
        startActivity(intent);
    }
}
