package com.ye.player.player.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ye.player.R;
import com.ye.player.ad.bean.AdInfo;
import com.ye.player.ad.services.AdInfoService;
import com.ye.player.ad.ui.CustomBrowserActivity;
import com.ye.player.common.PicassoImageLoader;
import com.ye.player.common.bean.VideoInfo;
import com.ye.player.common.ui.activity.BaseActivity;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.TimeUtils;
import com.ye.player.player.adapter.CacheStufferAdapter;
import com.ye.player.player.widget.MyVideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class PlayerActivity extends BaseActivity implements View.OnClickListener {

    private IDanmakuView mDanmakuView;

    private View mMediaController;

    private CheckBox btnPause;

    private EditText editTextComment;

    private Button btnSend;

    private ImageButton btnBack;

    private TextView textViewTitle;

    private TextView textViewTime;

    private CheckBox btnHide;

    private VideoInfo videoInfo;

    private MyVideoView mVideoView;

    private DanmakuContext mContext;

    private BaseDanmakuParser mParser;

    private SeekBar seekBar;

    private CacheStufferAdapter mCacheStufferAdapter = new CacheStufferAdapter(mDanmakuView);

    private static final int DELAY_MISS = 4000;

    private Handler mHandler = new Handler();

    private Timer timer= new Timer();

    private TimeOutTask task;

    private int currentPosition = 0;

    private ImageView adImageView;

    private AdInfoService adInfoService;

    private AdInfo currentAdInfo;

    private List<AdInfo> adList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        videoInfo = (VideoInfo) getIntent().getSerializableExtra("videoInfo");
        adInfoService = new AdInfoService(PlayerActivity.this);
        adList = adInfoService.getAds(videoInfo);
        initViews();
        setDanmaku();
       // setAds();
        task = new TimeOutTask();
        timer.schedule(task,DELAY_MISS);
    }

    /*private void setAds() {
        for(final AdInfo adInfo : adList){
            mHandler.postDelayed(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    adImageView.setVisibility(View.VISIBLE);
                    adLink = adInfo.getLink();
                    //需要改成从指定网址获取
                    adImageView.setBackground(getResources().getDrawable(R.drawable.icon_logo));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adImageView.setVisibility(View.GONE);
                        }
                    }, adInfo.getDuration());
                }
            }, adInfo.getStartTime());
        }
    }*/

    private void initViews() {
        seekBar = (SeekBar)findViewById(R.id.seekbar) ;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoView.seekTo(mVideoView.getDuration()*seekBar.getProgress()/100);
                mDanmakuView.seekTo((long)mVideoView.getDuration()*seekBar.getProgress()/100);
            }
        });
        btnBack = (ImageButton)findViewById(R.id.navi_bar_left_btn);
        btnBack.setImageResource(R.drawable.back_king);
        btnBack.setOnClickListener(this);
        textViewTitle = (TextView)findViewById(R.id.title);
        textViewTitle.setText(videoInfo.getTitle());
        textViewTime = (TextView)findViewById(R.id.time) ;

        mMediaController = findViewById(R.id.media_controller);
        mMediaController.setOnClickListener(this);

        btnPause = (CheckBox) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(this);
        editTextComment = (EditText) findViewById(R.id.et_comment);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnHide = (CheckBox) findViewById(R.id.btn_hide);
        btnHide.setOnClickListener(this);

        mVideoView = (MyVideoView) findViewById(R.id.videoview);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                PlayerActivity.this.finish();
            }
        });

        adImageView = (ImageView) findViewById(R.id.ad_imageview);
        adImageView.setOnClickListener(this);
    }

    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    private void setDanmaku() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (mDanmakuView != null) {
           // mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            if (videoInfo.getDanmaPath() != null){
                File file = new File(videoInfo.getDanmaPath());
                try {
                    InputStream inputStream = new FileInputStream(file);

                    mParser = createParser(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            }

            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
                @Override
                public void onDanmakuClick(BaseDanmaku latest) {
                    Log.d("DFM", "onDanmakuClick text:" + latest.text);
                }

                @Override
                public void onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick danmakus size:" + danmakus.size());
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
            ((View) mDanmakuView).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mMediaController.setVisibility(View.VISIBLE);
                    delayDissController();
                }
            });
        }

        if (mVideoView != null) {
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    mHandler.post(run);
                }
            });
            mVideoView.setVideoPath(videoInfo.getPath());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
        if (mVideoView != null){
            mVideoView.pause();
            currentPosition = mVideoView.getCurrentPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.seekTo(currentPosition);
        mVideoView.start();
        mDanmakuView.resume();
        btnPause.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(run);
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == mMediaController) {
            mMediaController.setVisibility(View.GONE);
            hideKeyBoard();
        }
        if (v == adImageView){
            Intent intent = new Intent(PlayerActivity.this, CustomBrowserActivity.class);
            intent.putExtra("url", currentAdInfo.getLink());
            startActivity(intent);
        }
        if (v == btnBack) {
            this.finish();
        } else if (v ==btnPause) {
            if (btnPause.isChecked()){
                mVideoView.pause();
                currentPosition = mVideoView.getCurrentPosition();
                if (mDanmakuView != null && mDanmakuView.isPrepared())
                mDanmakuView.pause();
            } else {
                mVideoView.start();
                if (mDanmakuView != null && mDanmakuView.isPrepared())
                    mDanmakuView.resume();
            }
        } else if (v == btnSend && mDanmakuView != null && mDanmakuView.isPrepared()){
            sendComment();
            delayDissController();
        } else if (v ==btnHide && mDanmakuView != null && mDanmakuView.isPrepared()){
            if (btnHide.isChecked()) {
                mDanmakuView.hide();
            } else {
                mDanmakuView.show();
            }
        }
    }

    private void sendComment() {
        String comment = editTextComment.getText().toString();
        if (null == comment||StringUtil.isEmpty(comment)) {
            return;
        }
        sendCommentToServer(comment);
        addDanmaku(comment);
        editTextComment.setText("");
        editTextComment.clearFocus();
        hideKeyBoard();

    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
    }

    private void sendCommentToServer(String comment) {

    }


    private void addDanmaku(String comment){
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        //danmaku.text = "这是一条弹幕" + System.nanoTime();
        danmaku.text =comment;
        danmaku.padding = 5;
        danmaku.priority = 10;  // 不会被各种过滤器过滤并隐藏显示
        danmaku.isLive = false;
        danmaku.time = mDanmakuView.getCurrentTime() + 600;
        danmaku.textSize = 30f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        // danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        // danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }

    private void delayDissController() {
        task.cancel();
        task = new TimeOutTask();
        timer.schedule(task,DELAY_MISS);
    }

    public class TimeOutTask extends TimerTask{

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!editTextComment.hasFocus()&&mMediaController!=null){
                        mMediaController.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = StringUtil.createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1200;
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }

    private Runnable run = new Runnable() {
        long currentPosition, duration;

        public void run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = mVideoView.getCurrentPosition();
            duration = mVideoView.getDuration();
            long progress = ((currentPosition * 100) / duration);
            // 设置进度条的主要进度，表示当前的播放时间
            seekBar.setProgress((int)progress);
            textViewTime.setText(TimeUtils.longToString(currentPosition)+"/"+TimeUtils.longToString(videoInfo.getDuration()));
            mHandler.postDelayed(run, 500);

            if (hasAd(currentPosition)){
                setAd();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setAd() {

        //需要改成从指定网址获取
          //效率低
        //adImageView.setBackground(getResources().getDrawable(R.drawable.sock));
       // PicassoImageLoader.loadImage(R.drawable.sock, adImageView);
        adImageView.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adImageView.setVisibility(View.GONE);
            }
        }, currentAdInfo.getDuration());
    }

    private boolean hasAd(long currentPosition) {
        for (AdInfo adInfo: adList){
            long temp = adInfo.getStartTime()-currentPosition;
            if (temp<500&&temp>=0){
                currentAdInfo = adInfo;
                return true;
            }
        }
        return false;
    }

    public boolean hasNavigationBar() {
        return false;
    }

}