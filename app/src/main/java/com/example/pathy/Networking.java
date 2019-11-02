package com.example.pathy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class Networking
{
    static void downloadFromServer(Context con) throws IOException
    {
        Log.d("MMFDebug", "Starting Download...");
        Socket sock = new Socket("10.0.2.2", 8080);
        BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
        out.write(1);
        out.flush();
        Log.d("MMFDebug", "Connected");

        String filename = "test.txt";
        FileOutputStream outputStream;

        try {
            outputStream = con.openFileOutput(filename, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            out.write(3);
            out.flush();
            Thread.sleep(100);
            out.close();
            in.close();
            sock.close();
            Log.d("MMFDebug", "Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    static void startDownloadTask(Context con)
    {
        DownloadFileFromServerTask task = new DownloadFileFromServerTask(con);
        task.execute("");
    }
    private static class DownloadFileFromServerTask extends AsyncTask<String, Void, String> {
        Context con;
        public DownloadFileFromServerTask(Context con)
        {
            this.con = con;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                downloadFromServer(con);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
