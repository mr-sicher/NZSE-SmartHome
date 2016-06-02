package de.sicher.sichersmarthome.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.sicher.sichersmarthome.R;
import de.sicher.sichersmarthome.objects.Device;
import de.sicher.sichersmarthome.objects.Heating;
import de.sicher.sichersmarthome.objects.Light;
import de.sicher.sichersmarthome.objects.Room;

/**
 * Created by sicher on 04.05.2016.
 */
public class ObjectHandler {
    public static final String TAG = "ObjectHandler";

    private ArrayList<Device> devices;
    private ArrayList<Room> rooms;

    private static ObjectHandler me;
    private static boolean generated = false;

    private ObjectHandler(){
        devices = new ArrayList<>();
        rooms = new ArrayList<>();
    }


    public static ObjectHandler getObjectHandler(){
        if(me == null){
            me = new ObjectHandler();
        }
        return me;
    }

    public boolean generate(){
        if(generated){
            return false;
        }
        generated = true;

        rooms.add(new Room("Wohnzimmer"));
        Heating h = new Heating("Heizung 1", getRoom("Wohnzimmer"));
        h.setActive(true);
        h.setTemperature(24);
        Light l = new Light("Lampe An", getRoom("Wohnzimmer"));
        l.setActive(true);

        devices.add(h);
        devices.add(new Light("Lampe 1", getRoom("Wohnzimmer")));
        devices.add(new Heating("Heizung 2", getRoom("Wohnzimmer")));
        devices.add(new Light("Lampe 2", getRoom("Wohnzimmer")));
        devices.add(l);
        devices.add(new Heating("Heizung 3", getRoom("Wohnzimmer")));
        devices.add(new Light("Lampe 3", getRoom("Wohnzimmer")));
        devices.add(new Heating("Heizung 4", getRoom("Wohnzimmer")));
        devices.add(new Light("Lampe 4", getRoom("Wohnzimmer")));
        devices.add(new Heating("Heizung 5", getRoom("Wohnzimmer")));
        devices.add(new Light("Lampe 5", getRoom("Wohnzimmer")));

        Room r = new Room("Schlafzimmer");

        rooms.add(r);
        devices.add(new Heating("Fußbodenheizung", r));
        devices.add(new Light("Deckenlampe", r));

        return generated;
    }

    public boolean generateObjects(Context context){
        String ret = "";


        try {
            InputStream inputStream = context.openFileInput(context.getString(R.string.json_file));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
            return false;
        }
        try{
            JSONObject elements = new JSONObject(ret);
            JSONArray jsonRooms = elements.optJSONArray(context.getString(R.string.json_rooms));
            rooms = new ArrayList<>();
            devices = new ArrayList<>();
            for(int i = 0; i < jsonRooms.length(); i ++){
                JSONObject jsonRoom = jsonRooms.getJSONObject(i);
                String name = jsonRoom.getString(context.getString(R.string.json_room_name));
                rooms.add(new Room(name));
            }
            JSONArray jsonDevices = elements.getJSONArray(context.getString(R.string.json_devices));
            for(int i = 0; i < jsonDevices.length(); i ++){
                JSONObject jsonDevice = jsonDevices.getJSONObject(i);
                String type = jsonDevice.getString(context.getString(R.string.json_device_type));
                String name = jsonDevice.getString(context.getString(R.string.json_device_name));
                String room = jsonDevice.getString(context.getString(R.string.json_device_room));
                String active = jsonDevice.getString(context.getString(R.string.json_device_active));
                if(type.equals(context.getString(R.string.json_device_type_light))){
                    Light l = new Light(name, getRoom(room));
                    l.setActive(active.equals("true"));
                    devices.add(l);
                }else if(type.equals(context.getString(R.string.json_device_type_heating))){
                    String temperature = jsonDevice.getString(context.getString(R.string.json_device_heating_temperature));
                    Heating h = new Heating(name, getRoom(room));
                    h.setActive(active.equals("true"));
                    h.setTemperature(Integer.parseInt(temperature));
                    devices.add(h);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        //Toast.makeText(context, "Successfully loaded", Toast.LENGTH_SHORT).show();
        return true;

    }
    public void generateFile(Context context){
        context.deleteFile(context.getString(R.string.json_file));
        try {
            JSONObject elements = new JSONObject();

            JSONArray jsonRooms = new JSONArray();
            for(Room room : rooms) {
                JSONObject jsonRoom = new JSONObject();
                jsonRoom.put(context.getString(R.string.json_room_name), room.getName());
                jsonRooms.put(jsonRoom);
            }
            elements.put(context.getString(R.string.json_rooms), jsonRooms);
            JSONArray jsonDevices = new JSONArray();
            for(Device device : devices){
                JSONObject jsonDevice = new JSONObject();
                String type = "";
                if(device instanceof Light){
                    type = context.getString(R.string.json_device_type_light);
                }else if(device instanceof Heating){
                    type = context.getString(R.string.json_device_type_heating);
                }
                jsonDevice.put(context.getString(R.string.json_device_type), type);
                jsonDevice.put(context.getString(R.string.json_device_name), device.getName());
                jsonDevice.put(context.getString(R.string.json_device_room), device.getRoom().getName());
                jsonDevice.put(context.getString(R.string.json_device_active), device.isActive() + "");
                if(device instanceof Heating){
                    Heating h = (Heating) device;
                    jsonDevice.put(context.getString(R.string.json_device_heating_temperature), h.getTemperature() + "");
                }
                jsonDevices.put(jsonDevice);
            }
            elements.put(context.getString(R.string.json_devices), jsonDevices);
            Log.d(TAG, elements.toString());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(context.getString(R.string.json_file), Context.MODE_PRIVATE));
            outputStreamWriter.write(elements.toString());
            outputStreamWriter.close();
        }catch(JSONException fu){
            fu.printStackTrace();
        }catch (IOException e) {
            Log.e("TAG", "File write failed: " + e.toString());
        }

        //Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();
    }

    public void addRoom(Room room){
        rooms.add(room);
    }
    public void addDevice(Device device){
        devices.add(device);
    }
    public Room getRoom(String name){
        for(Room room : rooms){
            if(room.getName().equals(name))
                return room;
        }
        return null;
    }
    public Device getDevice(String name){
        for(Device device : devices){
            if(device.getName().equals(name))
                return device;
        }
        return null;
    }
    public Device getDevice(int id){
        return devices.get(id);
    }
    public void removeRoom(Room room){
        rooms.remove(room);
    }
    public void removeDevice(Device device){
        devices.remove(device);
    }
    public int getActiveDevices(){
        int counter = 0;
        for(Device device : devices){
            if(device.isActive()){
                counter ++;
            }
        }
        return counter;
    }
    public int getActiveHeatings(){
        int counter = 0;
        for(Device device : devices){
            if(device instanceof Heating) {
                if (device.isActive()) {
                    counter++;
                }
            }
        }
        return counter;
    }
    public int getActiveLights(){
        int counter = 0;
        for(Device device : devices){
            if(device instanceof Light) {
                if (device.isActive()) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public ArrayList<Device> getAllDevices() {
        return devices;
    }
    public ArrayList<Device> getAllDevices(String name){
        Room room = getRoom(name);
        if(room == null){
            return getAllDevices();
        }
        ArrayList<Device> roomDevices = new ArrayList<>();
        for(Device device : devices){
            if(device.getRoom().equals(room)){
                roomDevices.add(device);
            }
        }
        return roomDevices;
    }
    public ArrayList<Device> getHeatings() {
        ArrayList<Device> heating = new ArrayList<>();
        for(Device device : devices){
            if(device instanceof Heating){
                heating.add(device);
            }
        }
        return heating;
    }
    public ArrayList<Device> getLights() {
        ArrayList<Device> lights = new ArrayList<>();
        for(Device device : devices){
            if(device instanceof Light){
                lights.add(device);
            }
        }
        return lights;
    }
    public ArrayList<Room> getRooms(){
        return rooms;
    }

}
