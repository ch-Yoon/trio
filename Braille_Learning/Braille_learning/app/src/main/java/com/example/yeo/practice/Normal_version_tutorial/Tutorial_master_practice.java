package com.example.yeo.practice.Normal_version_tutorial;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.yeo.practice.Common_menu_display.Common_menu_display;
import com.example.yeo.practice.Common_menu_sound.Menu_detail_service;
import com.example.yeo.practice.Common_menu_sound.Menu_main_service;
import com.example.yeo.practice.Common_sound.slied;
import com.example.yeo.practice.MainActivity;
import com.example.yeo.practice.Menu_info;
import com.example.yeo.practice.Normal_version_menu.Menu_Tutorial;
import com.example.yeo.practice.Normal_version_menu.Menu_master_practice;
import com.example.yeo.practice.R;
import com.example.yeo.practice.WHclass;

public class Tutorial_master_practice extends AppCompatActivity {
    Common_menu_display m;

    int oldDragX,newDragX;
    int oldDragY,newDragY;
    private SoundThread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        Menu_main_service.menu_page = Menu_info.MENU_MASTER_PRACTICE;
        startService(new Intent(this, Menu_main_service.class));

        thread = new SoundThread();
        thread.start();
    }


    class SoundThread extends Thread{
        @Override
        public void run(){
            super.run();
            while(true){
                if(WHclass.SoundCheck==true){
                    MainActivity.Braille_TTS.Tutorial_lock();
                    MainActivity.Braille_TTS.TTS_Play("잘하셨습니다. 현재 메뉴는 7개의 대 메뉴 중, 세번째 메뉴인 숙련과정 메뉴이며, 글자, 단어와 같이 숙련된 점자 연습이 하위 메뉴들로 구성되어 있습니다." +
                            "숙련과정 메뉴는 기초과정 메뉴와 비교하였을 때, 점자의 크기가 다소 작습니다. 이점 유의하시기 바랍니다. 다음 메뉴인 점자번역 메뉴로 이동하겠습니다. " +
                            "준비되었으면, 다음 메뉴로 이동하시기 바랍니다. ");
                    WHclass.SoundCheck=false;
                    break;
                }
            }
        }
    }
    public void init(){
        View decorView = getWindow().getDecorView();
        int uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );
        Menu_info.DISPLAY = Menu_info.DISPLAY_MASTER;
        m = new Common_menu_display(this);
        m.setBackgroundColor(Color.rgb(22,26,44));

        setContentView(m);
    }

    @Override
    public void onPause(){
        super.onPause();
        m.free();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        init();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(MainActivity.Braille_TTS.Tutorial_lock==false) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDragX = (int)event.getX();
                    oldDragY = (int)event.getY();
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    newDragX = (int)event.getX();
                    newDragY = (int)event.getY();
                    if(oldDragX-newDragX> WHclass.Drag_space) {  //손가락 2개를 이용하여 오른쪽에서 왼쪽으로 드래그할 경우 다음 메뉴로 이동
                        Intent translation = new Intent(Tutorial_master_practice.this, Tutorial_translation.class);
                        startActivity(translation);
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                        slied.slied = Menu_info.next;
                        startService(new Intent(this, slied.class));
                        finish();
                    }
                    else if(newDragY-oldDragY> WHclass.Drag_space) {  //손가락 2개를 이용하여 상단에서 하단으로 드래그할 경우 현재 메뉴의 상세정보 음성 출력
                        MainActivity.Braille_TTS.Tutorial_lock();
                        MainActivity.Braille_TTS.TTS_Play("잘하셨습니다. 현재 메뉴는 7개의 대 메뉴 중, 세번째 메뉴인 숙련과정 메뉴이며, 글자, 단어와 같이 숙련된 점자 연습이 하위 메뉴들로 구성되어 있습니다." +
                                "숙련과정 메뉴는 기초과정 메뉴와 비교하였을 때, 점자의 크기가 다소 작습니다. 이점 유의하시기 바랍니다. 다음 메뉴인 점자번역 메뉴로 이동하겠습니다. " +
                                "준비되었으면, 다음 메뉴로 이동하시기 바랍니다. ");
                    }
                    else if (oldDragY - newDragY > WHclass.Drag_space) {//손가락 2개를 이용하여 하단에서 상단으로 드래그할 경우 현재 메뉴를 종료
                        onBackPressed();
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Menu_main_service.finish=true;
        startService(new Intent(this,Menu_main_service.class));
        finish();
    }

}
