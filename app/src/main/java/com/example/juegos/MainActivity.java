package com.example.juegos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int[][] matrix;
    TableLayout tableLayout;
    private GestureDetector mGestureDetector;

    private int posx=2;
    private int posy=2;

    private static final int ARRIBA = 0;
    private static final int ABAJO = 1;
    private static final int IZQUIERDA = 2;
    private static final int DERECHA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = findViewById(R.id.tableLayout);
        init();
        escribirVista();
        mGestureDetector = new GestureDetector(this, new EscucharGestos());
        //        Button b = (Button) findViewById(R.id.button);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                algo();
//            }
//        });
    }

    private void init() {
        matrix = new int[4][4];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = 0;
            }
        }

        matrix[posx][posy] = 2;
    }

    private void escribirVista() {
        for (int i = 0; i < 4; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 4; j++) {
                TextView textView = (TextView) row.getChildAt(j);

                if(matrix[i][j]==0){

                    textView.setText("");
                }else{
                    textView.setText(String.valueOf(matrix[i][j]));
                }

            }
        }
    }

    private void algo() {

    }

    private void hacerVibracion(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Verifica si el dispositivo admite la vibración
        if (vibrator != null && vibrator.hasVibrator()) {
            // El tiempo de vibración en milisegundos
            long tiempoVibracion = 100;

            // Si estás utilizando una versión de Android anterior a Oreo (API nivel 26)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                vibrator.vibrate(tiempoVibracion);
            } else {
                // Para versiones de Android Oreo (API nivel 26) y superiores
                VibrationEffect effect = VibrationEffect.createOneShot(tiempoVibracion, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(effect);
            }
        }
    }

    private void hacerMovimiento(int movimiento) {
        switch (movimiento){
            case ARRIBA:
                if(posx>0){
                    matrix[posx-1][posy]=2;
                    matrix[posx][posy]=0;
                    posx--;
                }else{
                    hacerVibracion();
                }
                break;

            case ABAJO:
                if(posx<matrix.length-1){
                    matrix[posx+1][posy]=2;
                    matrix[posx][posy]=0;
                    posx++;
                }else{
                    hacerVibracion();
                }
                break;

            case IZQUIERDA:
                if(posy>0){
                    matrix[posx][posy-1]=2;
                    matrix[posx][posy]=0;
                    posy--;
                }else{
                    hacerVibracion();
                }
                break;

            case DERECHA:
                if(posy<matrix[0].length-1){
                    matrix[posx][posy+1]=2;
                    matrix[posx][posy]=0;
                    posy++;
                }else{
                    hacerVibracion();
                }
                break;

            default:
                Log.e("hay un error","epaa");
        }
        escribirVista();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class EscucharGestos extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            float ancho = Math.abs(e2.getX() - e1.getX());
            float alto = Math.abs(e2.getY() - e1.getY());

            if (ancho > alto) {
                if (e2.getX() > e1.getX()) {
                    hacerMovimiento(DERECHA);
//                    mTextView.setText("Pa la derecha");
                } else {
                    hacerMovimiento(IZQUIERDA);
//                    mTextView.setText("Pa la izq");
                }
            } else {
                if (e1.getY() > e2.getY()) {
                    hacerMovimiento(ARRIBA);
//                    mTextView.setText("Pa la arriba");
                } else {
                    hacerMovimiento(ABAJO);
//                    mTextView.setText("Pa la abajo");
                }
            }
            return true;
        }
    }
}