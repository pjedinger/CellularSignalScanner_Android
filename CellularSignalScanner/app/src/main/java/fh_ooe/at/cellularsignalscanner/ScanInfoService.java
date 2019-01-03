package fh_ooe.at.cellularsignalscanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.ProgressBar;

public class ScanInfoService extends Service {
    Handler handler;
    Runnable runnable;
    TelephonyManager telephonyManager;
    MainActivity context;
    ProgressBar progressBar;
    private final int REFRESH_RATE_MS = 500;

    public ScanInfoService(final TelephonyManager telephonyManager, final MainActivity context) {
        this.telephonyManager = telephonyManager;
        this.context = context;
        progressBar = context.findViewById(R.id.signal_progressbar);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                ValueAnimator animator = ValueAnimator.ofInt(0, progressBar.getMax());
                animator.setDuration(REFRESH_RATE_MS);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation){
                        progressBar.setProgress((Integer)animation.getAnimatedValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        new ScanInfoTask(context).execute(telephonyManager);
                    }
                });
                animator.start();
                handler.postDelayed(runnable, REFRESH_RATE_MS);

            }
        };
        handler.postDelayed(runnable, REFRESH_RATE_MS);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
