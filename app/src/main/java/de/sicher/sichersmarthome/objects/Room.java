package de.sicher.sichersmarthome.objects;

/**
 * Created by sicher on 03.05.2016.
 */
public class Room {
    private String name;
    public Room(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public boolean equals(Object o){
        if(o instanceof Room){
            Room room = (Room) o;
            if(room.getName() == name){
                return true;
            }
        }
        return false;
    }
}
