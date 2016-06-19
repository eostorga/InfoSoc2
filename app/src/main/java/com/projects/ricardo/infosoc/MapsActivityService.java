package com.projects.ricardo.infosoc;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

/**
 * Clase Servicio que tiene 2 métodos principales:
 *     onStartCommand - Al iniciar el servicio, se obtiene un candado para que el sistema operativo
 *         no pause o cancele la actividad MapsActivity.
 *     onDestroy - libera el candado para que el sistema operativo vuelva a tener el control
 *         automático del servicio.
 */
public class MapsActivityService extends Service {

    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    /*
    Cuando el usuario presiona "iniciar", se crea un servicio, que a su vez llama a la actividad
    MapsActivity. Como la actividad se invoca con un servicio, se ejecutará en primer o segundo
    plano sin ningún problema.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        //activity 2
        Toast.makeText(this, "Iniciando...", Toast.LENGTH_SHORT).show();
        Intent ma = new Intent(this, MapsActivity.class);
        ma.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ma);

        wakeLock.acquire();

        return START_STICKY;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Terminando...", Toast.LENGTH_SHORT).show();
        wakeLock.release();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
