package de.sicher.sichersmarthome.objects;

/**
 * Created by sicher on 03.05.2016.
 */
public abstract class StartMenuItem {
    private int resource;
    private String description;
    public StartMenuItem(String description, int resource){
        this.description = description;
        this.resource = resource;
    }

    public int getImageResourceID() {
        return resource;
    }
    @Override
    public String toString(){
        return description;
    }

    public abstract void onClick();
}
