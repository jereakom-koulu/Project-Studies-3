package group1.oamk.ringo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.ContactsContract.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class downloadPackages extends AppCompatActivity {
    private static final int PICK_CONTACT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_packages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView lv = (ListView) findViewById(R.id.download_list);

        MyPackagesAdapter adapter = null;
            new asyncGetDownloads().execute();
        while (List.size() < 1){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }}

            adapter = new MyPackagesAdapter(List, this);

        lv.setAdapter(adapter);

    }


    public class MyPackagesAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private Context context;



        public MyPackagesAdapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.download_row_layout, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position));

            //Handle buttons and add onClickListeners

            Button downloadBtn = (Button)view.findViewById(R.id.download_btn);

            downloadBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
    ArrayList<String> List = new ArrayList<String>();
    private class asyncGetDownloads extends AsyncTask<Void, Void, Void> {

        Activity downloadPackages;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List = getDownloads();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {


    }}

    public ArrayList<String> getDownloads() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        URL url = new URL("http://www.students.oamk.fi/~t5moda00/soundpacks/list.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            Log.d("beer",url.toString());
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(readStream(in));
                JSONArray jArray = jObject.getJSONArray("soundpacks");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObj = jArray.getJSONObject(i);
                    if(!jObj.getString("folder").toString().equals("list.php") ){
                    list.add(jObj.getString("folder"));}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }

        return list;
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }



}