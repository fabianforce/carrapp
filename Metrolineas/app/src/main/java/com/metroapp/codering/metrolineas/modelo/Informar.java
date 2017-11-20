package com.metroapp.codering.metrolineas.modelo;

/**
 * Created by linuxforce on 10/15/17.
 */

public class Informar {
	private String titulo;
	private String desc;
	private String Image;


	public Informar()
	{


		
	}


	public Informar(String titulo, String desc, String image) {
		this.titulo = titulo;
		this.desc = desc;
		Image = image;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}
}
