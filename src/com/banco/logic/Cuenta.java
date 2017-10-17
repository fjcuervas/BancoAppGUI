package com.banco.logic;

import java.util.ArrayList;

/**
 * Clase que representa una cuenta bancaria desde la que se podrá obtener la
 * infomación del saldo, IBAN y clientes que pertenecen a dicha cuenta.
 *
 * @author Javi Cuervas
 */
class Cuenta {

    private Float saldo;
    private String iban;
    private ArrayList<Cliente> clientes;

    public Cuenta(Cliente cliente, String iban) {

        saldo = Float.parseFloat("0");
        this.iban = iban;
        clientes = new ArrayList<>();
        clientes.add(cliente);
        cliente.añadirCuenta(this);
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;

    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * @return the clientes
     */
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    /**
     * @param clientes the clientes to set
     */
    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

}
