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
                TaskCuentaAdelante tarea = new TaskCuentaAdelante(tvTexto1);
                TaskCuentaAtras tarea2 = new TaskCuentaAtras(tvTexto2);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    tarea.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    tarea2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    tarea.execute();
                    tarea2.execute();
                }
            }
        });
    }

    private class TaskCuentaAdelante extends AsyncTask<Void, Integer, Void> {

        private TextView tv;

        public TaskCuentaAdelante(TextView tv) {
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
                }
                publishProgress(cuentaAdelante);
            }
            publishProgress(new Integer[]{});
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }

    private class TaskCuentaAtras extends AsyncTask<Void, Integer, Void> {

        private TextView tv;

        public TaskCuentaAtras(TextView tv) {
            this.tv = tv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int cuentaAtras;
            Log.v(TAG, Thread.currentThread().getName());
            for (cuentaAtras = TOPE_SUPERIOR; cuentaAtras >= TOPE_INFERIOR; cuentaAtras--) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                }
                publishProgress(cuentaAtras);
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }
}
