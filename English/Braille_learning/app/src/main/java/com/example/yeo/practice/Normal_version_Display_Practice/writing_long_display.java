package com.example.yeo.practice.Normal_version_Display_Practice;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.View;

import com.example.yeo.practice.Common_braille_data.dot_quiz_word;
import com.example.yeo.practice.Common_braille_data.dot_quiz_abbreviation_word;
import com.example.yeo.practice.Common_braille_data.dot_quiz_abbreviation_head;
import com.example.yeo.practice.Common_braille_data.dot_quiz_abbreviation_ending;
import com.example.yeo.practice.Menu_info;
import com.example.yeo.practice.WHclass;

import java.util.Random;

/**
 * Created by yoonc on 2016-07-25.
 */
class writing_long_display extends View {
    /*
4칸 이상의 점자를 화면에 출력해주는 클래스
4칸부터 7칸까지 점자를 표현할 수 있도록 설정되어 있음
단, 퀴즈이기 때문에, 점자를 의미하는 글자를 표현하지 않음
 */
    public int first_x=-100, first_y=-100;
    public int second_x=10, second_y=-100;
    public int third_x=-100, third_y=-100;

    Random random;
    static boolean next = false;
    int max, min=0; // 랜덤변수 최대값과 최소값
    float width= WHclass.width; //가로
    float height= WHclass.height; //세로
    int x=0, y=0; // 점자를 터치할때 사용할 좌표를 저장할 변수
    Vibrator vibrator; //진동 변수
    int dot_count=0;

    //점자를 출력하기 위한 화면의 좌표값을 미리 선언하였고, 돌출점자와 비돌출점자의 크기를 미리 선언하였음
    float width1=width*(float)0.05,width2=width*(float)0.15,width3=width*(float)0.3,width4=width*(float)0.4,width5=width*(float)0.55,width6=width*(float)0.65,width7=width*(float)0.8, width8=width*(float)0.9, width9=width*(float)1.05, width10=width*(float)1.15, width11=width*(float)1.3, width12=width*(float)1.4, width13=width*(float)1.55, width14=width*(float)1.65;
    float height1=width*(float)0.75,height2=width*(float)0.85,height3=width*(float)0.95;
    float minicircle, bigcircle; // 비돌출점자와 돌출점자 메크로

    //점자를 출력하는 좌표값들을 배열에 저장함
    float width_7[][]={{width1,width2,width3,width4,width5,width6,width7,width8,width9,width10,width11,width12,width13,width14},{width1,width2,width3,width4,width5,width6,width7,width8,width9,width10,width11,width12,width13,width14},{width1,width2,width3,width4,width5,width6,width7,width8,width9,width10,width11,width12,width13,width14}};
    float height_7[][]={{height1,height1,height1,height1,height1,height1,height1,height1,height1,height1,height1,height1,height1,height1},
            {height2,height2,height2,height2,height2,height2,height2,height2,height2,height2,height2,height2,height2,height2},
            {height3,height3,height3,height3,height3,height3,height3,height3,height3,height3,height3,height3,height3,height3}};

    static float target7_width[][] = new float[3][14];//돌출된 점자의 x 좌표값을 저장하는 배열 변수
    static float target7_height[][] = new float[3][14];//돌출된 점자의 y 좌표값을 저장하는 배열 변수
    static float notarget7_width[][] = new float[3][14];//비돌출 점자의 x 좌표값을 저장하는 배열 변수
    static float notarget7_height[][] =new float[3][14]; //비돌출 점자의 y 좌표값을 저장하는 배열 변수
    static int text_7[][] = new int[3][14];  // 7칸의 점자를 저장하는 배열 변수. 점자 배열정보가 담긴 클래스로부터 불러온 점자를 해당 배열변수에 저장함
    static String textname_7;// 불러온 점자가 의미하는 글자를 저장하는 변수
    static float Braille_insert[][] = new float [3][14] ;

    static int page=0;

    int k=0; //점자 칸을 반영하는 변수

    static dot_quiz_abbreviation_word Dot_quiz_abbreviation_word;
    static dot_quiz_abbreviation_head Dot_quiz_abbreviation_head;
    static dot_quiz_abbreviation_ending Dot_quiz_abbreviation_ending;
    static dot_quiz_word Dot_quiz_word;

    int question = 1; // 문제 수를 지정할 변수

    public void quiz_target_init(){
        for(int i=0 ; i<3; i++){ // 돌출점자와 비돌출점자의 x,y값을 저장하는 배열변수를 초기화함
            for(int j=0 ; j<14; j++) {
                target7_width[i][j] = 0;
                target7_height[i][j] = 0;
                notarget7_width[i][j] = 0;
                notarget7_height[i][j] = 0;
                Braille_insert[i][j] = 0;
            }
        }
    }

    public void quiz_view2_init(){//화면 초기화 함수. 화면이 이동될 때 점자를 다시 그려줌.
        for(int i=0 ; i<3; i++){ // 돌출점자와 비돌출점자의 x,y값을 저장하는 배열변수를 초기화함
            for(int j=0 ; j<14; j++) {
                target7_width[i][j] = 0;
                target7_height[i][j] = 0;
                notarget7_width[i][j] = 0;
                notarget7_height[i][j] = 0;
                Braille_insert[i][j] = 0;
            }
        }

        switch(Menu_info.MENU_QUIZ_INFO){
            case 3: //한칸 퀴즈
                max = Dot_quiz_abbreviation_word.abbreviation_word_count;
                random = new Random();
                page = random.nextInt(max) + min;
                dot_count = Dot_quiz_abbreviation_word.abbreviation_word_dot_count.get(page);
                textname_7 = Dot_quiz_abbreviation_word.abbreviation_word_name.get(page);
                break;
            case 4: //단어 퀴즈
                max = Dot_quiz_abbreviation_head.abbreviation_head_count;
                random = new Random();
                page = random.nextInt(max) + min;
                dot_count = Dot_quiz_abbreviation_head.abbreviation_head_dot_count.get(page);
                textname_7 = Dot_quiz_abbreviation_head.abbreviation_head_name.get(page);
                break;
            case 5: //단어 퀴즈
                max = Dot_quiz_abbreviation_ending.abbreviation_ending_count;
                random = new Random();
                page = random.nextInt(max) + min;
                dot_count = Dot_quiz_abbreviation_ending.abbreviation_ending_dot_count.get(page);
                textname_7 = Dot_quiz_abbreviation_ending.abbreviation_ending_name.get(page);
                break;
            case 6: //단어 퀴즈
                max = Dot_quiz_word.wordcount;
                random = new Random();
                page = random.nextInt(max) + min;
                dot_count = Dot_quiz_word.word_dot_count.get(page);
                textname_7 = Dot_quiz_word.word_name.get(page);
                break;
        }

        k=dot_count*2; //k= 점자 칸 수 * 2

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < k; j++) {
                if(Menu_info.MENU_QUIZ_INFO==3)
                    text_7[i][j] = Dot_quiz_abbreviation_word.abbreviation_word_Array.get(page)[i][j];
                else if(Menu_info.MENU_QUIZ_INFO==4)
                    text_7[i][j] = Dot_quiz_abbreviation_head.abbreviation_head_Array.get(page)[i][j];
                else if(Menu_info.MENU_QUIZ_INFO==5)
                    text_7[i][j] = Dot_quiz_abbreviation_ending.abbreviation_ending_Array.get(page)[i][j];
                else if(Menu_info.MENU_QUIZ_INFO==6)
                    text_7[i][j] = Dot_quiz_word.word_Array.get(page)[i][j];
            }
        }
    }

    public String getQuestion(){
        String questionText="";
        String countText="";
        switch(dot_count){
            case 1:
                countText=", 한칸";
                break;
            case 2:
                countText=", 두칸";
                break;
            case 3:
                countText=", 세칸";
                break;
            case 4:
                countText=", 네칸";
                break;
            case 5:
                countText=", 다섯칸";
                break;
            case 6:
                countText=", 여섯칸";
                break;
            case 7:
                countText=", 일곱칸";
                break;
        }
        questionText = textname_7+countText;
        return questionText;
    }

    public writing_long_display(Context context) {
        super(context);
      //  Dot_quiz_letter = new dot_quiz_letter();
        Dot_quiz_word = new dot_quiz_word();
        Dot_quiz_abbreviation_word = new dot_quiz_abbreviation_word();
        Dot_quiz_abbreviation_head = new dot_quiz_abbreviation_head();
        Dot_quiz_abbreviation_ending = new dot_quiz_abbreviation_ending();

        quiz_view2_init();
        minicircle = width*(float)0.01; //작은점자  크기 메크로
        bigcircle = width*(float)0.049; //큰 점자 크기 메크로
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);//점자의 색을 지정
        paint.setTextSize(width*(float)0.21);//점자를 의미하는 글자의 크기를 지정
        paint.setAntiAlias(true);// 점자의 표면을 부드럽게 그려줌

        canvas.drawText(textname_7, height * (float) 0.4, width * (float) 0.2, paint);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < k; j++) {
                if (Braille_insert[i][j] == 0)
                    canvas.drawCircle(width_7[i][j], height_7[i][j], minicircle, paint);
                else {
                    canvas.drawCircle(width_7[i][j], height_7[i][j], bigcircle, paint);
                    target7_width[i][j] = width_7[i][j];
                    target7_height[i][j] = height_7[i][j];
                }
                notarget7_width[i][j] = width_7[i][j];
                notarget7_height[i][j] = height_7[i][j];

            }
        }
        Paint finger = new Paint();
        finger.setARGB(180,255,00,00);
        finger.setAntiAlias(true);
        canvas.drawCircle(first_x,first_y,minicircle*2,finger);
        canvas.drawCircle(second_x,second_y,minicircle*2,finger);
        canvas.drawCircle(third_x,third_y,minicircle*2,finger);

    }

    public void finger_set(int x1, int y1, int x2, int y2, int x3, int y3){
        first_x=x1;
        first_y=y1;
        second_x=x2;
        second_y=y2;
        third_x=x3;
        third_y=y3;
        invalidate();
    }

}