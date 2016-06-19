package com.projects.ricardo.infosoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import java.util.Map;

/**
 * Clase para la primer "actividad". Al presionar el botón "iniciar" se llama al método
 * onCreate. Esta "actividad" crea un "servicio" para poder recuperar los datos del usuario aunque
 * el teléfono entre en modo de suspensión.
 */
public class MapsActivityCaller extends Activity{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        //Listener del botón que invoca el servicio.
        Button btnCallMaps = (Button) findViewById(R.id.btnCallMaps);
        btnCallMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), MapsActivityService.class);
                startService(intent);
            }
        });
    }

    //Al presionar el botón de "Atrás" (el del teléfono), el servicio se detiene.
    @Override
    public void onBackPressed() {
        stopService(new Intent(getBaseContext(), MapsActivityService.class));
        super.onBackPressed();
    }


}