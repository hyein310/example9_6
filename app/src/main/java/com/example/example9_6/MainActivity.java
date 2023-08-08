//201921252_강혜인_연습문제9-6

package com.example.example9_6;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;

public class MainActivity extends AppCompatActivity  {
    final static int LINE = 1, CIRCLE = 2, RECTANGLE = 3, THREELINE = 4, DOT = 5;
    static int curShape = LINE;
    static int curColor = Color.DKGRAY;

    // 도형을 저장할 리스트
    static List<MyShape> myshape = new ArrayList<MyShape>();
    static boolean isFinish = false; // ACTION_UP 여부

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this));
        setTitle("연습문제 9-6");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "선 그리기");
        menu.add(0, 2, 0, "원 그리기");
        menu.add(0, 3, 0, "사각형 그리기");
        menu.add(0, 4, 0, "3개 직선 그리기");
        menu.add(0, 5, 0, "점 그리기");
        SubMenu sMenu = menu.addSubMenu("색상 변경 >>");
        sMenu.add(0, 6, 0, "빨강");
        sMenu.add(0, 7, 0, "초록");
        sMenu.add(0, 8, 0, "파랑");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                curShape = LINE; // 선
                return true;
            case 2:
                curShape = CIRCLE; // 원
                return true;
            case 3:
                curShape = RECTANGLE; // 사각형
                return true;
            case 4:
                curShape = THREELINE; // 3개 직선
                return true;
            case 5:
                curShape = DOT; // 점
                return true;
            case 6:
                curColor = Color.RED;
                return true;
            case 7:
                curColor = Color.GREEN;
                return true;
            case 8:
                curColor = Color.BLUE;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class MyGraphicView extends View {
        int startX = -1, startY = -1, stopX = -1, stopY = -1, midX = (startX + stopX)/2;

        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    isFinish = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    midX = (int) event.getX()/2;
                    isFinish = false;
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    // 마우스가 Up되면 최종 적으로 배열에 넣는다.
                    MyShape shape = new MyShape();

                    shape.shapeType = curShape;
                    shape.startX = startX;
                    shape.startY = startY;
                    shape.stopX = stopX;
                    shape.stopY = stopY;
                    shape.midX = midX;

                    shape.color = curColor;

                    myshape.add(shape);
                    isFinish = true;
                    this.invalidate();
                    break;
            }
            return true;
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);

            // 일단 배열의 도형을 모두 그린다.
            for (int i = 0; i < myshape.size(); i++) {
                MyShape shape = myshape.get(i);
                paint.setColor(shape.color);
                switch (shape.shapeType) {
                    case LINE:
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawLine(shape.startX, shape.startY, shape.stopX,
                                shape.stopY, paint);
                        break;
                    case CIRCLE:
                        paint.setStyle(Paint.Style.STROKE);
                        int radius = (int) Math.sqrt(Math.pow(shape.stopX
                                - shape.startX, 2)
                                + Math.pow(shape.stopY - shape.startY, 2));
                        canvas.drawCircle(shape.startX, shape.startY, radius, paint);
                        break;
                    case RECTANGLE:
                        paint.setStyle(Paint.Style.STROKE);
                        Rect rect = new Rect(shape.startX, shape.startY, shape.stopX, shape.stopY);
                        canvas.drawRect(rect, paint);
                        break;
                    case THREELINE:
                        paint.setStyle(Paint.Style.STROKE);
                        Path path=new Path();

                        path.moveTo(shape.startX, shape.startY);
                        path.lineTo(shape.startX + shape.midX, shape.startY);
                        path.moveTo(shape.startX + shape.midX, shape.startY);
                        path.lineTo(shape.startX + shape.midX, shape.stopY);
                        path.lineTo(shape.stopX, shape.stopY);

                        canvas.drawPath(path, paint);
                        path.close();
                        //path.reset();
                        break;
                    case DOT:
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(shape.startX, shape.startY, shape.startX + 30,
                                shape.startY + 30, paint);
                }
            }

            // 그림이 아직 진행 중이면 (= 화면을 터치 중이면) 현재 진행중이 도형을 그린다.
            if (isFinish == false) {

                paint.setColor(curColor);

                switch (curShape) {
                    case LINE:
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawLine(startX, startY, stopX, stopY, paint);
                        break;
                    case CIRCLE:
                        paint.setStyle(Paint.Style.STROKE);
                        int radius = (int) Math.sqrt(Math.pow(stopX - startX, 2)
                                + Math.pow(stopY - startY, 2));
                        canvas.drawCircle(startX, startY, radius, paint);
                        break;
                    case RECTANGLE:
                        paint.setStyle(Paint.Style.STROKE);
                        Rect rect = new Rect(startX, startY, stopX, stopY);
                        canvas.drawRect(rect, paint);
                        break;
                    case THREELINE:
                        paint.setStyle(Paint.Style.STROKE);
                        Path path=new Path();

                        path.moveTo(startX, startY);

                        //path.lineTo(shape.startX, shape.startY);
                        path.lineTo(startX + midX, startY);

                        path.moveTo(startX + midX, startY);
                        path.lineTo(startX + midX, stopY);
                        path.lineTo(stopX, stopY);
                        canvas.drawPath(path, paint);
                        path.close();
                        path.reset();
                        canvas.drawPath(path, paint);
                        path.close();

                        break;

                    case DOT:
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(startX, startY, startX + 30,
                                startY + 30, paint);
                        break;
                }
            }
        }

    }

    // 도형 클래스
    private static class MyShape {
        int shapeType; // 도형 종류
        int startX, startY, stopX, stopY, midX; // 도형의 2점
        int color; // 도형 색상
    }

}