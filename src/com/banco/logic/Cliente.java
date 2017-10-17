package com.banco.logic;

import java.util.ArrayList;

/**
 * Clase que representa un cliente del banco desde el que se podrá obtener la
 * infomación de sus ingresos anuales, nombre, NIF y CUENTAS de las que es
 * titular.
 *
 * @author Javi Cuervas
 */
class Cliente {

    private String nombre;
    private String nif;
    private int ingresosAnuales;
    private final ArrayList<Cuenta> CUENTAS = new ArrayList<>();

    public Cliente(String nombre, String nif, int ingresosAnuales) {

        this.nombre = nombre;
        this.nif = nif;
        this.ingresosAnuales = ingresosAnuales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIngresosAnuales() {
        return ingresosAnuales;
    }

    public void setIngresosAnuales(int ingresoAnual) {
        this.ingresosAnuales = ingresoAnual;
    }

    public void añadirCuenta(Cuenta cuenta) {
        this.getCuentas().add(cuenta);
    }

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param nif the nif to set
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * @return the CUENTAS
     */
    public ArrayList<Cuenta> getCuentas() {
        return CUENTAS;
    }

}
