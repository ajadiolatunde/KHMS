package com.plate.root.khms;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

public class Jasonparsee {
    private Context context;
    Singleton1 singleton1;
    public Jasonparsee(Context context){
    this.context = context;
    singleton1 = Singleton1.getInstance(context);
    }

    public boolean cantakeAttendnance(String id){
        boolean regstatus = false;
        if (!getStudentinfo(id).equals(Constants.NOT_AVAILABLE)) regstatus =true;
        return regstatus;

    }
    //THis can only work after those button are enabled
    public String getStudentinfo(String id){
        String data = singleton1.getPrefKey(Constants.TABLE_STUDENT);
        String idinfo = Constants.NOT_AVAILABLE;

        try{
            JSONObject jStudent = new JSONObject(data);
            if (jStudent.has(id)){

                idinfo = jStudent.getString(id);
                System.out.println("Tunde idinfo "+idinfo);
            }else {

            }


        }catch (JSONException io){
            io.printStackTrace();
        }

        return idinfo;
    }

    public String getStudentTotal(){
        String data = singleton1.getPrefKey(Constants.TABLE_STUDENT);
        String idinfo ="0";

        try{
            JSONObject jStudent = new JSONObject(data);
            idinfo = String.valueOf(jStudent.names().length());

        }catch (JSONException io){
            io.printStackTrace();
        }

        return idinfo;
    }

    public String getStudentToregister(String id){
        String data = singleton1.getPrefKey(Constants.TABLE_STUDENT);
        String idinfo ="0";

        try{
            JSONObject jStudent = new JSONObject(data);
            idinfo = String.valueOf(jStudent.names().length());

        }catch (JSONException io){
            io.printStackTrace();
        }

        return idinfo;
    }

    public void readFile(String file){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("test.json")));
            String mline;
            while((mline = reader.readLine())!=null){

            }

        }catch (IOException io){

        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("test.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        System.out.println("Tunde "+json);
        readJson(json);
        return json;
    }

    public void readJson(String js){
        try {
            JSONObject obj = new JSONObject(js);
            System.out.println("Tunde  "+obj.names().toString());
            singleton1.addStringSharedPreff(Constants.TABLE_STUDENT,obj.toString());
            Toast.makeText(context,"Download succesful!",Toast.LENGTH_SHORT).show();
        }catch (JSONException io){
            io.printStackTrace();
        }
    }

    public  void markAttendnance(String id,String staff){
        //String data ="{\"2018-09-15\":{\"1512\":{\"time_in\":\"1537015435575\",\"staff\":\"23456\"},\"1212\":{\"time_in\":\"1537014430992\",\"staff\":\"23456\"}}}";
        String data = singleton1.getPrefKey(Constants.TABLE_ATTENDANCE);

        try {
            JSONObject js_main = new JSONObject(data);

            System.out.println("Tunde "+js_main.toString());
            System.out.println("Tunde "+js_main.length());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] tt = timestamp.toString().split(" ");

            //if it contains today
            if (js_main.has(tt[0])){
                System.out.println("Tunde yessssssss "+js_main.get(tt[0]));


                JSONObject js_child = new JSONObject(js_main.get(tt[0]).toString());
                System.out.println("child id "+js_child);
                if (js_child.has(id)){
                    Toast.makeText(context.getApplicationContext(),"Has marked attendnance",Toast.LENGTH_SHORT).show();
                    System.out.println("Yes child id");
                }else {
                    System.out.println("No child id");
                    JSONObject child_content = new JSONObject();
                    child_content.put("time_in",String.valueOf(timestamp.getTime()));
                    child_content.put("staff",staff);
                    js_child.put(id,child_content);
                    js_main.put(tt[0],js_child);
                    System.out.println("Added "+js_main);
                    Toast.makeText(context.getApplicationContext(),"Done,bye ",Toast.LENGTH_SHORT).show();

                    singleton1.addStringSharedPreff(Constants.TABLE_ATTENDANCE,js_main.toString());

                }
                //
            }else{
                System.out.println("Tunde Nooooooooo");
                //js_child.put(tt[0],"{}");
                js_main.put(tt[0],new JSONObject());
                System.out.println("Tunde "+js_main.toString());

                //Repitition from if
                JSONObject js_child = new JSONObject(js_main.get(tt[0]).toString());
                System.out.println("child id "+js_child);
                if (js_child.has(id)){
                    Toast.makeText(context.getApplicationContext(),"Has marked attendnance",Toast.LENGTH_SHORT).show();

                    System.out.println("Yes child id");
                }else {
                    System.out.println("No child id");
                    JSONObject child_content = new JSONObject();
                    child_content.put("time_in",String.valueOf(timestamp.getTime()));
                    child_content.put("staff",staff);
                    js_child.put(id,child_content);
                    js_main.put(tt[0],js_child);
                    System.out.println("Added "+js_main);
                    Toast.makeText(context.getApplicationContext(),"Done, bye ",Toast.LENGTH_SHORT).show();

                    singleton1.addStringSharedPreff(Constants.TABLE_ATTENDANCE,js_main.toString());
                }




            }
        }catch (JSONException io){
            io.printStackTrace();
        }


    }
}
