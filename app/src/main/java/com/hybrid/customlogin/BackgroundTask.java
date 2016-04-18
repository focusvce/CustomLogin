package com.hybrid.customlogin;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BackgroundTask extends AsyncTask<String, Void, String> {

    int flag;
    int count;
    Context ctx;
    RecyclerView view;
    SwipeRefreshLayout v;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerAdapter adapter;
    RecyclerAdapter_Comment adapter_c;
    RecyclerView.LayoutManager layoutManager;
    String TAG = "async_task";
    ArrayList<Card_Object> objects = new ArrayList<>();
    ArrayList<Card_Object> objectr = new ArrayList<>();
    int id;
    String method;
    TextView img;
    int up_count;
    ProgressDialog progressDialog;
    ArrayList<Card_Object> search_objects = new ArrayList<>();
    ArrayList<Comment_Object> c_objects = new ArrayList<>();

    public BackgroundTask(Context ct, RecyclerView v, SwipeRefreshLayout vi) {
        this.ctx = ct;
        this.view = v;
        this.v = vi;
        flag = 0;
        count = 0;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        //progressDialog.show();
    }
    public  BackgroundTask(Context ctx){
        this.ctx=ctx;
    }

    public BackgroundTask(int id, Context ctx,TextView img,int up_count) {
        this.id = id;
        this.ctx = ctx;
        this.img = img;
        this.up_count = up_count;
    }



    @Override
    protected String doInBackground(String... params) {
        method = params[0];

        if(method.equals("html_link")) {
            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new StringRequest(Utility.getIPADDRESS()+"json_test.php", future, future);
            String response = "";

            requestQueue.add(request);
            Log.e("ADA", "here");
            try {
                response = future.get().toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String html = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("link");
                    String title = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("title");
                    String description = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("description");
                    int up = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("up");
                    int down = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("down");
                    int sr_key = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("sr_key");
                    int comment = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("comment_count");
                    Card_Object ob = new Card_Object(html, title, description, up, down, sr_key,comment);
                    objects.add(i, ob);
                    Log.e(TAG, html);}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //progressDialog.dismiss();
            return response;

        } else if (method.equals("up_counter")) {

            String up_php = Utility.getIPADDRESS()+"up_count.php";
            String down_php = Utility.getIPADDRESS()+"down_count.php";
            String response="";
            try {
                URL up_p;
                if(params[1].equals("0"))
                up_p = new URL(up_php);
                else
                up_p = new URL(down_php);
                HttpURLConnection httpURLConnection = (HttpURLConnection) up_p.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(id), "UTF-8")+"&"+
                        URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(2), "UTF-8");
                Log.e(TAG, data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                Log.e("UP RESPONSE",response);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return response;
        } else if (method.equals("refresh")) {
            RequestQueue requestQueue = Volley.newRequestQueue(ctx);
            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request = new StringRequest(Utility.getIPADDRESS()+"json_test.php", future, future);
            String response = "";

            requestQueue.add(request);
            Log.e("ADA", "here");
            try {
                response = future.get().toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String html = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("link");
                    String title = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("title");
                    String description = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("description");
                    int up = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("up");
                    int down = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("down");
                    int sr_key = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("sr_key");
                    int comment = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("comment_count");
                    Card_Object ob = new Card_Object(html, title, description, up, down, sr_key,comment);
                    if (flag == 0)
                        objectr.add(i, ob);
                    else if (flag == 1 && i < count)
                        objectr.set(i, ob);
                    else
                        objectr.add(i, ob);
                }
                count = jsonArray.length();
                flag = 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
        else if(method.equals("search")){
            String search_php = Utility.getIPADDRESS()+"search.php";
            String response="";
            String search=params[1];
            try{
                URL up_p = new URL(search_php);
                HttpURLConnection httpURLConnection = (HttpURLConnection) up_p.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8");
                Log.e(TAG, data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.e("ADA",response);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String html = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("link");
                    String title = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("title");
                    String description = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("description");
                    int up = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("up");
                    int down = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("down");
                    int sr_key = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("sr_key");
                    int comment = jsonArray.getJSONObject(jsonArray.length()-i-1).getInt("comment_count");
                    Card_Object ob = new Card_Object(html, title, description, up, down, sr_key,comment);
                    search_objects.add(i, ob);
                    Log.e(TAG, html);}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            return response;
        }
        else if(method.equals("add")){
            String add_php= Utility.getIPADDRESS()+"add.php";
            String response="";
            try{
                URL up_p = new URL(add_php);
                HttpURLConnection httpURLConnection = (HttpURLConnection) up_p.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8")+"&"+
                        URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+"&"+
                        URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
                Log.e(TAG, data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                Log.e("ADD",response);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return "added";

        }
        else if(method.equals("get_comments")){
            String getcomment_php = Utility.getIPADDRESS()+"comment_json.php";
            String response="";
            String key=params[1];
            try {
                URL up_p = new URL(getcomment_php);
                HttpURLConnection httpURLConnection = (HttpURLConnection) up_p.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");
                Log.e(TAG, data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                Log.e("ADA_COM",response);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String uid = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("uid");
                    String comment = jsonArray.getJSONObject(jsonArray.length()-i-1).getString("comment");

                    Comment_Object ob = new Comment_Object(comment,uid);

                    c_objects.add(i, ob);
                    Log.e("OBJECT",c_objects.get(i).getComment());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            return response;
        }
        else if(method.equals("add_comment")){
            String getcomment_php = Utility.getIPADDRESS()+"add_comment.php";
            String response="";
            String key=params[2];
            String comment = params[1];
            try {
                URL up_p = new URL(getcomment_php);
                HttpURLConnection httpURLConnection = (HttpURLConnection) up_p.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8")+"&"+
                        URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(1), "UTF-8")+"&"+
                        URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
                Log.e(TAG, data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                Log.e("ADA_COM",response);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (ProtocolException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return "";
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {

        //Log.e("UPPER",s);
        if (method.equals("html_link")) {

            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
            adapter = new RecyclerAdapter(objects, ctx);
            layoutManager = new LinearLayoutManager(ctx);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        } else if (method.equals("refresh")) {
            progressDialog.dismiss();
            swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
            adapter = new RecyclerAdapter(objectr, ctx);
            layoutManager = new LinearLayoutManager(ctx);
            Log.e("ADA", "adapter created");
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            swipeRefreshLayout.setRefreshing(false);
            recyclerView.setHasFixedSize(true);
        }else if (method.equals("up_counter"))
            img.setText(s);
        else if(method.equals("search")) {
            if(s.equals("[]e"))
                Toast.makeText(ctx,"No Results!!!!",Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(ctx, "Search Over", Toast.LENGTH_LONG).show();
                swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe);
                recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                adapter = new RecyclerAdapter(search_objects, ctx);
                layoutManager = new LinearLayoutManager(ctx);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        else if(s.equals("added"))
            Toast.makeText(ctx,"Post Added",Toast.LENGTH_LONG).show();
        else if(method.equals("get_comments")){
            Log.e("GET_COMM","onPost");
            SwipeRefreshLayout swipeRefreshLayout1 = (SwipeRefreshLayout) v.findViewById(R.id.swipe_comment_focus);
            RecyclerView recyclerView1 = (RecyclerView) view.findViewById(R.id.commentsRecyclerView); //change
            //Log.e("ADA_COMM",c_objects.get(0).getComment());
            adapter_c = new RecyclerAdapter_Comment(c_objects, ctx);
            layoutManager = new LinearLayoutManager(ctx);
            recyclerView1.setAdapter(adapter_c);
            recyclerView1.setLayoutManager(new LinearLayoutManager(ctx));
        }


    }


}
