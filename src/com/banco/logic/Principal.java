/**
 * Aplicación Bancaria como ejercicio del Curso de desarrollo de aplicaciones Web Java.
 *
 * En esta versión de la aplicación se utilizan ficheros .properties para la
 * persistencia de datos. Esto implica que la información se almacena con clave=valor
 * y como consecuencia no puede haber duplicados, por lo que en esta versión
 * se considera que un cliente solo sea titular de una única cuenta. Esto solo
 * ocurrirá a nivel de fichero, ya que durante la ejecución del programa se puede
 * trabajar con relaciones N a N para que un titular disponga de varias cuentas.
 *
 * Los datos se irán guardando en ficheros cada vez que se creen nuevos objetos
 * (clientes y cuentas), de esta forma los datos no se perderán en caso de que
 * la aplicación se cerrara de manera inesperada.
 *
 * En esta versión no se validan los datos introducidos por teclado, con lo que
 * podrían introducirse datos del cliente erróneos como nif y nombres incorrectos.
 *
 * Lugar: Centro educativo Jaume Viladoms (Sabadell)
 * Profesor: Albert
 *
 * @author Javi Cuervas
 * @version 1.0 30/09/2017
 */
package com.banco.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase principal que contiene el método main desde la que se inicia la
 * aplicación, así como diferentes métodos para interactuar con los objetos
 * que intervienen en la aplicación.
 *
 * @author Javi Cuervas
 */
public class Principal {

    private static Banco banco;
    private static File ficheroClientes;
    private static File ficheroCuentaCliente;
    private static File rutaFichero;
    private static final Properties PROP_CLIENTES = new Properties();
    private static final Properties PROP_CUENTA_CLIENTE = new Properties();

    public Principal() {
        banco = new Banco("Popular");
        crearFicheroDatos();
        cargarDatosDesdeFichero();
    }



    /**
     * Método para iniciar la aplicación. Desde aquí se crea el objeto banco, y
     * se hacen las llamadas a los métodos encargados de crear los ficheros de
     * datos .properties y de volcar los datos a los arrays del programa.
     */
    public void iniciarApp() {

        banco = new Banco("Popular");
        crearFicheroDatos();
        cargarDatosDesdeFichero();
        //mostrarMenu();
    }

    /**
     * Método para motrar el menú con las opciones que se pueden realizar.
     */
    public void mostrarMenu() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            int opcion = 0;
            do {
                System.out.println("\n********************************************");
                System.out.println("Bienvenido al operador Bancario de Viladoms");
                System.out.println("********************************************");
                System.out.println("\t 1. Crear cliente");
                System.out.println("\t 2. Crear cuenta");
                System.out.println("\t 3. Añadir cliente a cuenta");
                System.out.println("\t 4. Ingresar");
                System.out.println("\t 5. Reembolsar");
                System.out.println("\t 6. Ver saldo");
                System.out.println("\t 7. Ver listado de cuentas");
                System.out.println("\t 8. Ver listado de clientes");
                System.out.println("\t 9. Salir");
                System.out.println("********************************************\n");

                System.out.print("Elija una opción: ");

                try {
                    opcion = Integer.parseInt(br.readLine());
                } catch (NumberFormatException ex) {
                    System.out.println("\nLa opción introducida no es correcta.");
                    System.out.println("Elija una opción del 1 al 9.");
                    System.out.print("Pulse intro para volver al Menú...");
                    opcion = 0;
                    pedirDatos();
                }

                switch (opcion) {
                    case 1:
                       
                        break;
                    case 2:
                        crearCuenta();
                        break;
                    case 3:
                        incluirTitularCuenta();
                        break;
                    case 4:
                        ingresar();
                        break;
                    case 5:
                        reembolsar();
                        break;
                    case 6:
                        verSaldo();
                        break;
                    case 7:
                        verListadoCuentas();
                        break;
                    case 8:
                        verListadoClientes();
                        break;
                    case 9:
                        System.out.println("\nGracias por utilizar nuestros servicios. Hasta pronto!!");
                        break;
                    default:
                        System.out.println("\nLa opción introducida no es correcta.");
                        System.out.println("Elija una opción del 1 al 9.");
                        System.out.print("Pulse intro para volver al Menú...");
                        pedirDatos();
                        break;
                }
            } while (opcion != 9);
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para crear un cliente a través del NIF introducido por teclado.
     */
    public String crearCliente(String nif, String nombre, int ingresosAnuales) {
      try { 
          
            if (banco.existeCliente(nif)) {
                return "\nActualmente ya existe un cliente con ese NIF.";
            } else {

                banco.crearCliente(nombre, nif, ingresosAnuales);
                
                guardarFicheroClientes(nif, "0.0", "insertar");
                
                return "\nEl cliente se ha creado correctamente.";

            }
        }catch(Exception e){
          System.out.println("falla...");
          return "\nFallo.";
        }

    }

    /**
     * Método para crear una cuenta a través del IBAN (generado automáticamente
     * en la aplicación) y el NIF del titular introducido por teclado.
     */
    public void crearCuenta() {

        System.out.print("\nIntroduzca el NIF del titular: ");
        String nif = pedirDatos();

        Cliente cliente = null;
        if (banco.existeCliente(nif)) {

            cliente = banco.getCliente(nif);
            String iban = banco.crearCuenta(cliente);

            if (iban != null) {
                System.out.println("\nLa cuenta se ha creado correctamente.");
                System.out.println("IBAN de la nueva cuenta: " + iban + "\n");
                guardarFicheroCuentaCliente(nif, iban);
            }
        } else {
            System.out.println("\nNo existe ningún cliente con ese nif.");
        }
        System.out.print("Pulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para añadir un titular a una cuenta existente. Se pide por teclado
     * el NIF del titular y el IBAN de la cuenta para crear la relación.
     */
    public void incluirTitularCuenta() {

        System.out.print("\nIntroduzca el NIF del titular: ");
        String nifCliente = pedirDatos();

        if (banco.existeCliente(nifCliente)) {

            System.out.print("Introduzca el IBAN de la cuenta: ");
            String iban = pedirDatos();

            if (banco.existeCuenta(iban)) {
                Cliente cliente = banco.getCliente(nifCliente);
                Cuenta cuenta = banco.getCuenta(iban);

                banco.incluirTitularCuenta(cliente, cuenta);
                System.out.println("\nLa cuenta se ha asignado correctamente.");

                guardarFicheroClientes(nifCliente, String.valueOf(cuenta.getSaldo()), "actualizar");
                guardarFicheroCuentaCliente(nifCliente, iban);

            } else {
                System.out.println("\nNo existe ninguna cuenta con ese IBAN.");
            }
        } else {
            System.out.println("\nNo existe ningún cliente con ese NIF.");
        }
        System.out.print("Pulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para ingresar dinero en una cuenta mediante el IBAN introducido
     * por teclado.
     */
    public void ingresar() {

        System.out.print("\nIntroduzca el IBAN de la cuenta: ");
        String iban = pedirDatos();

        if (banco.existeCuenta(iban)) {

            System.out.print("Introduzca el importe a ingresar: ");
            Float importe = Float.parseFloat(pedirDatos());

            Cuenta cuenta = banco.getCuenta(iban);

            banco.ingresar(importe, cuenta);
            System.out.println("\nIngreso realizado correctamente.");
            System.out.println("SALDO ACTUAL: " + cuenta.getSaldo() + " euros.\n");

            List<Cliente> clientes = cuenta.getClientes();

            for (Cliente cliente : clientes) {
                guardarFicheroClientes(cliente.getNif(), String.valueOf(cuenta.getSaldo()), "actualizar");
            }
        } else {
            System.out.println("\nLo siento, el IBAN introducido no es correcto.");
        }
        System.out.print("Pulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para retirar dinero de una cuenta mediante el IBAN introducido por
     * teclado.
     */
    public void reembolsar() {

        System.out.print("\nIntroduzca el IBAN de la cuenta: ");
        String iban = pedirDatos();

        if (banco.existeCuenta(iban)) {

            System.out.print("Introduzca el importe a reembolsar: ");
            Float importe = Float.parseFloat(pedirDatos());

            Cuenta cuenta = banco.getCuenta(iban);

            Boolean correcto = banco.reembolsar(importe, cuenta);
            if (correcto) {
                System.out.println("\nEl reembolso se ha realizado correctamente.");
                System.out.println("SALDO ACTUAL: " + cuenta.getSaldo() + " euros.\n");

                List<Cliente> clientes = cuenta.getClientes();

                for (Cliente cliente : clientes) {
                    guardarFicheroClientes(cliente.getNif(), String.valueOf(cuenta.getSaldo()), "actualizar");
                }
            }
        } else {
            System.out.println("\nLo siento, el IBAN no es correcto.");
        }
        System.out.print("Pulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para mostrar el saldo de una cuenta concreta mediante el IBAN
     * introducido por teclado.
     */
    public void verSaldo() {
        System.out.print("\nIntroduzca el IBAN de la cuenta: ");
        String iban = pedirDatos();

        if (banco.existeCuenta(iban)) {
            System.out.println("\nSALDO ACTUAL: " + banco.verSaldo(banco.getCuenta(iban)) + " euros.");
        } else {
            System.out.println("\nLo siento, el IBAN no es correcto.");
        }
        System.out.print("Pulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para mostrar un listado con la información de las cuentas del
     * banco y de los titulares que pertenecen a dicha cuenta.
     */
    public void verListadoCuentas() {

        List<Cuenta> cuentas = banco.getCuentas();

        for (Cuenta cuenta : cuentas) {
            System.out.println("\n**************** CUENTA " + (cuentas.indexOf(cuenta) + 1) + " *****************");
            System.out.println(" IBAN:  " + cuenta.getIban());
            System.out.println(" SALDO: " + cuenta.getSaldo());
            System.out.println(" Nº TITULARES ASOCIADOS: " + cuenta.getClientes().size());

            for (Cliente cliente : cuenta.getClientes()) {
                System.out.println("\n\t  NOMBRE: " + cliente.getNombre());
                System.out.println("\t  NIF: " + cliente.getNif());
                System.out.println("\t  INGRESOS ANUALES: " + cliente.getIngresosAnuales());
            }
            System.out.println("*********************************************");
        }
        System.out.print("\nPulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para mostrar un listado con los clientes del banco y las cuentas
     * de las que el cliente sea titular.
     */
    public void verListadoClientes() {

        List<Cliente> clientes = banco.getClientes();

        for (Cliente cliente : clientes) {
            System.out.println("\n**************** CLIENTE " + (clientes.indexOf(cliente) + 1) + " *****************");
            System.out.println(" NOMBRE:  " + cliente.getNombre());
            System.out.println(" NIF: " + cliente.getNif());
            System.out.println(" INGRESOS ANUALES: " + cliente.getIngresosAnuales());

            if (cliente.getCuentas().size() != 0) {

                System.out.println(" CUENTAS DEL TITULAR:");

                for (Cuenta cuenta : cliente.getCuentas()) {
                    System.out.println("\n\t  IBAN: " + cuenta.getIban());
                    System.out.println("\t  SALDO: " + cuenta.getSaldo());
                }
            } else {
                System.out.println(" ACTUALMENTE NINGUNA CUENTA ASOCIADA");
            }
            System.out.println("*********************************************");
        }

        System.out.print("\nPulse intro para volver al Menú...");
        pedirDatos();
    }

    /**
     * Método para crear los ficheros properties que almacenarán la información
     * de titulares y cuentas, y de los que se extraré dicha información al
     * iniciar la aplicación. Este método se ejecutará 1 sola vez al arrancar la
     * aplicación.
     */
    public void crearFicheroDatos() {
        rutaFichero = new File("ficherosDatos");
        ficheroClientes = new File("ficherosDatos\\clientes.properties");
        ficheroCuentaCliente = new File("ficherosDatos\\cuentacliente.properties");
        try {
            if (!rutaFichero.exists()) {
                rutaFichero.mkdirs();
            }
            if (!ficheroClientes.exists()) {
                ficheroClientes.createNewFile();
            }
            if (!ficheroCuentaCliente.exists()) {
                ficheroCuentaCliente.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para volcar la información almacenada en los ficheros, a los
     * arrays que se utilizan durante la ejecución del programa para la
     * manipulación de los datos.
     */
    public void cargarDatosDesdeFichero() {

        String nif;        
        FileReader fc;
        FileReader fcc;
        Boolean clienteConCuenta = false;

        try {
            fc = new FileReader(ficheroClientes);
            fcc = new FileReader(ficheroCuentaCliente);
            PROP_CLIENTES.load(fc);
            PROP_CUENTA_CLIENTE.load(fcc);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        /* Se recorre el fichero de clientes.properties y se mira si para cada uno de ellos, tiene asociada 
           una cuenta en el fichero de cuentaclientes.properties. De esta forma se irán creando los titulares
           con los saldos y las cuentas correspondientes, y a su vez añadiéndolas a sus arrays correspondientes */
        for (Enumeration enumClientes = PROP_CLIENTES.keys(); enumClientes.hasMoreElements();) {

            Object nifFicheroCliente = enumClientes.nextElement();

            for (Enumeration enumCuentaCliente = PROP_CUENTA_CLIENTE.keys(); enumCuentaCliente.hasMoreElements();) {

                Object nifFicheroCuentaCliente = enumCuentaCliente.nextElement();

                //En caso de que el cliente sea titular de alguna cuenta, se crea tanto el cliente como la cuenta.
                if (nifFicheroCuentaCliente.toString().equals(nifFicheroCliente.toString())) {

                    String saldo = PROP_CLIENTES.getProperty(nifFicheroCliente.toString());
                    String iban = PROP_CUENTA_CLIENTE.getProperty(nifFicheroCuentaCliente.toString());
                    nif = nifFicheroCliente.toString();

                    banco.crearCliente("**creado desde fichero**",nif,0);

                    if (!banco.existeCuenta(iban)) {
                        banco.crearCuenta(banco.getCliente(nif), iban);
                        banco.getCuenta(iban).setSaldo(Float.parseFloat(saldo));
                    } else {
                        banco.incluirTitularCuenta(banco.getCliente(nif), banco.getCuenta(iban));
                    }
                    clienteConCuenta = true;
                    break;
                }
            }
            //Si el cliente no es titular de ninguna cuenta, tan solo se crea el nuevo cliente.
            if (!clienteConCuenta) {
                nif = nifFicheroCliente.toString();
                banco.crearCliente("**creado desde fichero**",nif,0);
            } else {
                clienteConCuenta = false;
            }
        }
    }

    /**
     * Método para almacenar los datos de un titular en un fichero properties
     *
     * @param clave Cadena de texto que contiene el nif del cliente
     * @param valor Cadena de texto que contiene el saldo correspondiente al
     * titular
     * @param operacion Cadena de texto que indica si se trata de una inserción
     * o actualización
     */
    public void guardarFicheroClientes(String clave, String valor, String operacion) {

        FileWriter os;

        //En lugar de añadir un nuevo registro en el fichero, se modifica el que ya existe
        if (operacion.equals("actualizar")) {
            
            FileReader is;
            try {
                is = new FileReader(ficheroClientes);
                PROP_CLIENTES.load(is);
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            for (Enumeration e = PROP_CLIENTES.keys(); e.hasMoreElements();) {

                Object obj = e.nextElement();

                if (obj.toString().equals(clave)) {

                    try {
                        os = new FileWriter(ficheroClientes);
                        PROP_CLIENTES.replace(clave, valor);
                        PROP_CLIENTES.store(os, "Actualizada información de titulares (nif:saldo)");
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                }
            }
        } else if (operacion.equals("insertar")) {
            try {
                os = new FileWriter(ficheroClientes);
                PROP_CLIENTES.setProperty(clave, valor);
                PROP_CLIENTES.store(os, "Almacenada información de titulares (nif:saldo)");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Método para mostrar almacenar la información que relaciona un cliente con
     * la cuenta en caso de disponer de alguna, en un fichero
     * cuentacliente.propoerties.
     *
     * @param clave Cadena de texto que indica la clave que identificará al
     * cliente, en este caso el nif.
     * @param valor Cadena de texto que indica el saldo que dispone el cliente
     * en su cuenta.
     */
    public void guardarFicheroCuentaCliente(String clave, String valor) {

        FileWriter os = null;
        try {
            os = new FileWriter(ficheroCuentaCliente);
            PROP_CUENTA_CLIENTE.put(clave, valor);
            PROP_CUENTA_CLIENTE.store(os, "Almacenada relación Cuenta-Cliente");
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Método para mostrar la información de clientes almacenada en el fichero
     * clientes.properties
     */
    public void leerFicheroClientes() {

        FileReader is;

        try {
            is = new FileReader(ficheroClientes);
            PROP_CLIENTES.load(is);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        System.out.println("-------------------------------");
        System.out.println("Información del fichero Clientes");
        System.out.println("-------------------------------");

        for (Enumeration e = PROP_CLIENTES.keys(); e.hasMoreElements();) {
            Object obj = e.nextElement();
            System.out.println(obj + ": " + PROP_CLIENTES.getProperty(obj.toString()));
        }
    }

    /**
     * Método para pedir datos por teclado.
     *
     * @return Devuelve una cadena de texto con los datos intdroducidos por
     * teclado
     */
    public String pedirDatos() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
