package com.example.thewikiofthedragon;

public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private String bandoFavorito;
    private String casaFavorita;

    // Constructor
    public Usuario(int id, String nombre, String email, String contrasena, String bandoFavorito, String casaFavorita) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.bandoFavorito = bandoFavorito;
        this.casaFavorita = casaFavorita;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getBandoFavorito() {
        return bandoFavorito;
    }

    public void setBandoFavorito(String bandoFavorito) {
        this.bandoFavorito = bandoFavorito;
    }

    public String getCasaFavorita() {
        return casaFavorita;
    }

    public void setCasaFavorita(String casaFavorita) {
        this.casaFavorita = casaFavorita;
    }
}
