package org.izv.aad.dosasynctask;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/4068984/running-multiple-asynctasks-at-the-same-time-not-possible

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int TOPE_SUPERIOR = 20, TOPE_INFERIOR = 0;

    private Button btDo;
    private TextView tvTexto1;
    private TextView tvTexto2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, Thread.currentThread().getName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        this.btDo = findViewById(R.id.btDo);
        this.tvTexto1 = findViewById(R.id.tvTexto1);
        this.tvTexto2 = findViewById(R.id.tvTexto2);

        this.btDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskCuentaAdelante tarea1 = new TaskCuentaAdelante(tvTexto1);
                TaskCuentaAtras tarea2 = new TaskCuentaAtras(tvTexto2);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    tarea1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    tarea2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (short)150);
                } else {
                    tarea1.execute();
                    tarea2.execute();
                }
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                tarea1.cancel(false);
                tarea2.cancel(false);*/
            }
        });
    }

    private static class TaskCuentaAdelante extends AsyncTask<Void, Integer, Void> {

        private TextView tv;

        TaskCuentaAdelante(TextView tv) {
            this.tv = tv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int cuentaAdelante;
            Log.v(TAG, Thread.currentThread().getName());
            for (cuentaAdelante = TOPE_INFERIOR; cuentaAdelante <= TOPE_SUPERIOR; cuentaAdelante++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    break;
                }
                if(isCancelled()) {
                    break;
                }
                publishProgress(cuentaAdelante);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.v(TAG, Thread.currentThread().getName());
            for(Integer i: values) {
                tv.append(i + "\n");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            tv.append("cancelado\n");
        }

    }

    private static class TaskCuentaAtras extends AsyncTask<Short, Integer, String> {

        private TextView tv;

        TaskCuentaAtras(TextView tv) {
            this.tv = tv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("");
        }

        @Override
        protected String doInBackground(Short... voids) {
            int cuentaAtras;
            long initial = System.nanoTime();
            short time = 300;
            if(voids.length > 0) {
                time = voids[0];
            }
            Log.v(TAG, Thread.currentThread().getName());
            for (cuentaAtras = TOPE_SUPERIOR; cuentaAtras >= TOPE_INFERIOR; cuentaAtras--) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                    break;
                }
                if(isCancelled()) {
                    break;
                }
                publishProgress(cuentaAtras);
            }
            return "tiempo: " + (System.nanoTime() - initial);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            tv.append("cancelado: " + s + "\n");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            for(Integer i: values) {
                tv.append(i + "\n");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tv.append(result + "\n");
        }

    }
}
