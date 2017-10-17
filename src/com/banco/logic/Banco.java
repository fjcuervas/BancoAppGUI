package com.banco.logic;

import java.util.ArrayList;

/**
 * Clase que representa la figura de un banco en la que se pueden realizar
 * diferentes operaciones como crear clientes y cuentas, mostrar información de
 * una cuenta o titular, realizar ingresos o reembolsos.
 *
 * @author Javi Cuervas
 */
class Banco {

    private String nombre;
    private ArrayList<Cuenta> cuentas;
    private ArrayList<Cliente> clientes;

    public Banco(String nombre) {
        this.nombre = nombre;
        cuentas = new ArrayList<>();
        clientes = new ArrayList<>();
    }

    public void crearCliente(String nombre, String nif, int ingresosAnuales) {
        Cliente cliente = new Cliente(nombre, nif, ingresosAnuales);
        clientes.add(cliente);
    }

    public String crearCuenta(Cliente cliente) {
        String iban = crearIban();
        cuentas.add(new Cuenta(cliente, iban));
        return iban;
    }

    public void crearCuenta(Cliente cliente, String iban) {
        cuentas.add(new Cuenta(cliente, iban));
    }

    public String crearIban() {
        String iban = String.format("%010d", cuentas.size() + 1);
        return "ES82-1465-0100-94-" + iban;
    }

    public void incluirTitularCuenta(Cliente cliente, Cuenta cuenta) {
        cliente.getCuentas().add(cuenta);
        cuenta.getClientes().add(cliente);
    }

    public Float verSaldo(Cuenta cuenta) {
        return cuenta.getSaldo();
    }

    public void ingresar(Float importe, Cuenta cuenta) {
        cuenta.setSaldo(cuenta.getSaldo() + importe);
    }

    public Boolean reembolsar(Float importe, Cuenta cuenta) {
        if (cuenta.getSaldo() - importe < 0) {
            System.out.println("No existe suficiente saldo en la cuenta para hacer el reembolso.");
            return false;
        } else {
            cuenta.setSaldo(cuenta.getSaldo() - importe);
            return true;
        }
    }

    public Boolean existeCliente(String nif) {

        return clientes.stream().anyMatch((cliente) -> (cliente.getNif().equals(nif)));
        /*
        for (Cliente cliente : clientes) {
            if (cliente.getNif().equals(nif)) {
                return true;
            }
        }
        return false;
         */
    }

    public Boolean existeCuenta(String iban) {

        return cuentas.stream().anyMatch((cuenta) -> (cuenta.getIban().equals(iban)));
        /*
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(iban)) {
                return true;
            }
        }
        return false;
         */
    }

    public Cliente getCliente(String nif) {

        Cliente clienteNoEncontrado = null;
        for (Cliente cliente : clientes) {
            if (cliente.getNif().equals(nif)) {
                return cliente;
            }
        }
        return clienteNoEncontrado;
    }

    public Cuenta getCuenta(String iban) {

        Cuenta cuentaNoEncontrada = null;
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIban().equals(iban)) {
                return cuenta;
            }
        }
        return cuentaNoEncontrada;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the cuentas
     */
    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    /**
     * @param cuentas the cuentas to set
     */
    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
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
