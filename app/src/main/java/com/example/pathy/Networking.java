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

public class Networking {

    private static final String IP = "134.228.155.169";
    private static final int PORT = 8080;

    static void downloadFromServer(Context context, String file) throws IOException {
        Log.d("MMFDebug", "Starting Download...");
        Socket sock = new Socket(IP, PORT);
        BufferedInputStream in = new BufferedInputStream(sock.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
        out.write(1);
        out.write(file.getBytes());
        out.write(0);
        out.flush();
        Log.d("MMFDebug", "Connected");

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
            byte[] lenBuf = new byte[Long.BYTES];
            in.read(lenBuf);
            long length = ByteUtils.bytesToLong(lenBuf);
            byte[] buffer = new byte[1024];
            long i = 0;
            out:
            while (true) {
                in.read(buffer);
                for (byte b : buffer) {
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

            Log.d("MMFDebug", "Done, Wrote " + (i) + " bytes");

        } catch (Exception e) {
            Log.e("MMFDebug", e.getLocalizedMessage());
        }

    }

    static void startDownloadTask(Context context, String building) {
        DownloadFileFromServerTask task = new DownloadFileFromServerTask(context, building);
        task.execute("");
    }

    private static class DownloadFileFromServerTask extends AsyncTask<String, Void, String> {
        Context context;
        String building;

        public DownloadFileFromServerTask(Context context, String building) {
            this.context = context;
            this.building = building;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                downloadFromServer(context, building + ".map");
                downloadFromServer(context, building + ".met");
                MappingController.allocateMap(context);
                /*for(int x = 0; x < 125; x++)
                {
                    for(int y = 0; y < 115; y++)
                    {
                        MapPanning.drawPoints.add(MappingController.getNode(x, y));
                        Log.d("MMFDebug", "added: "+x+":"+y);
                    }
                }*/
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
            buffer.clear();
            buffer.putLong(0, x);
            return buffer.array();
        }

        public static long bytesToLong(byte[] bytes) {
            buffer.clear();
            buffer.put(bytes, 0, bytes.length);
            buffer.flip();//need flip
            return buffer.getLong();
        }
    }
}
