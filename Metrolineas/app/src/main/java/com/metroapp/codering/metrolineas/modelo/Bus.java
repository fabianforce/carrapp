package com.metroapp.codering.metrolineas.modelo;

import java.util.Objects;

/**
 * Created by linuxforce on 10/6/17.
 */

public class Bus {

private String name;
    private double latitud;
    private double longitud;
    private boolean active;
    private Object created;

    public Bus() {

    }

    public Bus(String name , double latitud, double longitud , boolean active)
    {
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
        this.active = active;


    }
    public Bus(String name, double latitude, double longitude, boolean active,Object created) {
        this.name = name;
        this.latitud = latitude;
        this.longitud = longitude;
        this.active = active;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }
}
