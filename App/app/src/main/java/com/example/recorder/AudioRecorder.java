package com.example.recorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by home on 6/9/16.
 */
public class AudioRecorder extends MediaRecorder {
    MediaRecorder recorder;
    String outputFile;
    void startRecording(){
        recorder=new MediaRecorder();
        outputFile= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording.amr";
        recorder.setAudioSource(AudioSource.MIC);
        recorder.setOutputFormat(OutputFormat.AMR_WB);
        recorder.setAudioEncoder(AudioEncoder.AMR_WB);
        recorder.setAudioSamplingRate(16000);
        try{
            recorder.setOutputFile(outputFile);
        }
        catch(Exception e){
            Log.e("Output File","failed");
        }

        try{
            recorder.prepare();
        }
        catch(IOException e){
            Log.e("Audio Record", "prepare() failed");
        }
        recorder.start();
    }

    void stopRecording(){
        recorder.stop();
        recorder.reset();
        recorder.release();
        upload();
    }

    void readFile(){
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(outputFile));
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        finally {
            if (in != null)
                try{
                    in.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
        }
    }

    public void upload() {
        String response=null;
        AudioSender sender=new AudioSender(outputFile);
        FutureTask<String> uploadTask=new FutureTask<String>(sender);
        ExecutorService executorService= Executors.newFixedThreadPool(1);
        executorService.execute(uploadTask);
        while(true){
            if(uploadTask.isDone()){
                try{
                    response=uploadTask.get();
                    break;
                }catch(InterruptedException|ExecutionException e){
                    e.printStackTrace();
                    Log.e("Upload","Exception",e.getCause());
                }
            }
        }


    }

}
