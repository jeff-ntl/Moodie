package android.example.com.moodie.activities

import android.content.Intent
import android.example.com.moodie.R
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //makng thiss activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        //1.5 seconds splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashActivity, DiaryListActivity::class.java))
            //finish this activity
            finish()
        }, 1500)
    }
}
