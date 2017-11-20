package com.metroapp.codering.metrolineas.modelo;

import java.util.Map;

/**
 * Created by linuxforce on 11/4/17.
 */

public class Conductores {

	private Double latitude;
	private Double longitude;
private String hsalida;
private String salida;
private String llegada;
private String usuario;


	public Conductores()
	{



	}

	public Conductores(Double latitude, Double longitude, String hsalida, String salida, String llegada, String usuario) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.hsalida = hsalida;
		this.salida = salida;
		this.llegada = llegada;
		this.usuario = usuario;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getHsalida() {
		return hsalida;
	}

	public void setHsalida(String hsalida) {
		this.hsalida = hsalida;
	}

	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}

	public String getLlegada() {
		return llegada;
	}

	public void setLlegada(String llegada) {
		this.llegada = llegada;
	}


	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public static Conductores  coductores_al_mapa(Map map)
{

	return new Conductores(Double.parseDouble(map.get("latitude").toString()) , Double.parseDouble(map.get("longitude").toString())  , map.get("hsalida").toString(),map.get("salida").toString(), map.get("llegada").toString(), map.get("usuario").toString().trim());


}

}