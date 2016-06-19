package com.projects.ricardo.infosoc;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Se encarga de ir guardando cada valor en el archivo de texto.
 * Luego se puede hacer más fancy como en un xml, csv, etc.
 */
public class CoordinatesPath {

    public void crear_camino(String coordinadas) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date now = new Date(); //Al parecer existe otra clase Date para sql
        String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt


        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+ File.separator+"Rutas");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(coordinadas+"\n");
            writer.flush();
            writer.close();
            Log.d("LocationActivity", coordinadas);
            //Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }
}
