package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String urlServidorLocal = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String password = "1234";
    private static final String bbdd = "sampledb";
    private static final String urlConexion = "jdbc:mysql://localhost:3306/" + bbdd;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n-----Menú----");
            System.out.println("1. Insertar nuevo Usuario");
            System.out.println("2. Actualizar usuario");
            System.out.println("3. Consultar datos de usuario");
            System.out.println("4. Eliminar Usuario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el ID de la cuenta de origen: ");
                    int idCuentaOrigen = scanner.nextInt();

                    break;
                case 2:
                    System.out.print("Ingrese el ID del cliente: ");
                    int idCuentaConsulta = scanner.nextInt();

                    break;
                case 3:
                    System.out.print("Ingrese el ID de la cuenta: ");
                    int idCuentaHistorial = scanner.nextInt();

                    break;
                case 4:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                case 0:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 0);

    }

    public static void insertarUsuario(Usuario usuario) {
        Integer idUsuario = usuario.getIdUsuario();
        String nickUsr = usuario.getUsuario();
        String pwd = usuario.getPassword();
        String nombreUsr = usuario.getNombre();
        String emailUsr = usuario.getEmail();

        try {
            Connection cn = DriverManager.getConnection(urlServidorLocal, user, password);

            comprobarCampos(usuario);



            String in1 = "INSERT INTO clientes (idusuario, usuario, password, nombre, email) VALUES (?,?,?,?);";
            try (PreparedStatement ps = cn.prepareStatement(in1)) {
                ps.setString(1, nickUsr);
                ps.setString(2, pwd);
                ps.setString(3, nombreUsr);
                ps.setString(4, emailUsr);
                ps.executeUpdate();
            }


        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ModificarUsuario() {
        Connection cn = null;
        try {
            cn = DriverManager.getConnection(urlConexion, user, password);
            cn.setAutoCommit(false);


            cn.commit();
        } catch (SQLException e) {
            System.err.println("Error al realizar la operación: " + e.getMessage());
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ConsultarUsuario(Integer id) {

        try {
            Connection cn = DriverManager.getConnection(urlServidorLocal, user, password);
            Statement st = cn.createStatement();

            String query = "SELECT * FROM usuarios WHERE idUsuario = ?";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setInt(1, id);
               ps.executeQuery();
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void eliminarUsuario() {
        try {
            Connection cn = DriverManager.getConnection(urlServidorLocal, user, password);
            Statement st = cn.createStatement();


        } catch (SQLException e) {
            System.err.println("Error al eliminar el Usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean comprobarCampos(Usuario usuario){
        Integer idUsuario = usuario.getIdUsuario();
        String nickUsr = usuario.getUsuario();
        String pwd = usuario.getPassword();
        String nombreUsr = usuario.getNombre();
        String emailUsr = usuario.getEmail();
        try {
            Connection cn = DriverManager.getConnection(urlServidorLocal, user, password);
            Statement st = cn.createStatement();

            String query = "SELECT count(*) FROM usuarios WHERE idUsuario = ? OR WHERE ";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, nickUsr);
                ps.setString(3, pwd);
                ps.setString(4, nombreUsr);
                ps.setString(5, emailUsr);
                ResultSet resultado = ps.executeQuery();
                int numRegistros = 0;
                while (resultado.next()){
                    numRegistros = resultado.getInt(1);
                }
                if (numRegistros >0){
                    //Devuelve falso si hay un resgistro ya en la bbdd.
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


}