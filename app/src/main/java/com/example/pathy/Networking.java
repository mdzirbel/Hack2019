package com.example.pathy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

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
            byte[] lenBuf = new byte[Long.BYTES];
            in.read(lenBuf);
            long length = ByteUtils.bytesToLong(lenBuf);
            byte[] buffer = new byte[1024];
            long i = 0;
            out:
            while (true)
            {
                in.read(buffer);
                for (byte b : buffer)
                {
                    outputStream.write(b);
                    i++;
                    if (i >= length) {
                        break out;
                    }
                }
            }
            outputStream.close();
            Log.d("MMFDebug", "Disconnecting.");
            out.write(3);
            out.flush();
            Thread.sleep(10);
            out.close();
            in.close();
            sock.close();
            Log.d("MMFDebug", "Done, Wrote "+(i)+" bytes");
            Log.d("MMFDebug", IO.readFromFile(con));
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
    private static class ByteUtils {
        private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

        public static byte[] longToBytes(long x) {
            buffer.putLong(0, x);
            return buffer.array();
        }

        public static long bytesToLong(byte[] bytes) {
            buffer.put(bytes, 0, bytes.length);
            buffer.flip();//need flip
            return buffer.getLong();
        }
    }
}
