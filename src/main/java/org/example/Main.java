package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String user = "root";
    private static final String password = "1234";
    private static final String bbdd = "sampledb";
    private static final String urlConexion = "jdbc:mysql://localhost:3306/" + bbdd;

    public static void main(String[] args) {

        //Comentar este método si se realiza una segunda ejecución:
        insercionInicial();


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
                    System.out.print("Indique su nombre de usuario único: ");
                    String userName = scanner.nextLine();
                    System.out.print("Indique su contraseña: ");
                    String password = scanner.nextLine();
                    System.out.print("Indique su Nombre: ");
                    String name = scanner.nextLine();
                    System.out.print("Indique email: ");
                    String mail = scanner.nextLine();

                    Usuario usuario1 = new Usuario(userName, password, name, mail);
                    insertarUsuario(usuario1);

                    break;
                case 2:
                    System.out.print("Indique su nombre de Usuario: ");
                    String nombreUsuario = scanner.nextLine();
                    System.out.print("Indique su contraseña: ");
                    String userPassword = scanner.nextLine();

                    System.out.print("Indique su NUEVO nombre de Usuario: ");
                    String userMOD = scanner.nextLine();
                    System.out.print("Indique su NUEVA contraseña: ");
                    String userPasswordMOD = scanner.nextLine();
                    System.out.print("Indique su NUEVO nombre: ");
                    String userNameMOD = scanner.nextLine();
                    System.out.print("Indique su NUEVO email: ");
                    String userMailMOD = scanner.nextLine();

                    modificarUsuario(nombreUsuario, userPassword, userMOD, userPasswordMOD, userNameMOD, userMailMOD);

                    break;
                case 3:
                    System.out.print("Ingrese su Nombre de usuario: ");
                    String nmbUsuario = scanner.nextLine();

                    consultarUsuario(nmbUsuario);

                    break;
                case 4:
                    System.out.print("Indique su nombre de Usuario: ");
                    String nombreUsuarioDelete = scanner.nextLine();
                    System.out.print("Indique su contraseña: ");
                    String userPasswordDelete = scanner.nextLine();

                    eliminarUsuario(nombreUsuarioDelete, userPasswordDelete);

                    break;
                case 0:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 0);

    }

    /**
     * Este método crea una consulta de inserción sacando los parámetros de los atributos un objeto de tipo Usuario con
     * los datos que pido al usuario en el Main usando uso de los getters. Muestra por pantalla si se ha introducido
     * correctamente.
     *
     * @param usuario - objeto de tipo usuario con los datos que le pedimos al usuario
     */
    public static void insertarUsuario(Usuario usuario) {
        String nickUsr = usuario.getUsuario();
        String pwd = usuario.getPassword();
        String nombreUsr = usuario.getNombre();
        String emailUsr = usuario.getEmail();

        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);

            if (comprobarCampos(usuario)) {
                String in1 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES (?,?,?,?);";
                try (PreparedStatement ps = cn.prepareStatement(in1)) {
                    ps.setString(1, nickUsr);
                    ps.setString(2, pwd);
                    ps.setString(3, nombreUsr);
                    ps.setString(4, emailUsr);
                    ps.executeUpdate();
                    System.out.println("Usuario insertado correctamente.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método actualiza la fila de la bbdd que coincide con el nombre de usuario y la contraseña que se le pide al
     * usuario, para luego cambiar los datos a los que se le pide al usuario en el Main.
     *
     * @param nombreUsuario - nombre del usuario a modificar
     * @param contrasenha   - contraseña del usuario a modificar
     * @param newUserName   - nuevo nombre de usuario en la bbdd
     * @param newPassword   - nueva contraseña del usuario en la bbdd
     * @param newName       - nuevo nombre del usuario en la bbdd
     * @param newEmail      - nuevo email del usuario en la bbdd
     */
    public static void modificarUsuario(String nombreUsuario, String contrasenha, String newUserName, String newPassword, String newName, String newEmail) {
        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);
            Statement st = cn.createStatement();

            String query = "UPDATE usuarios SET usuario = ? , password = ? , nombre = ?, email = ? WHERE usuario = ? AND password = ?;";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setString(1, newUserName);
                ps.setString(2, newPassword);
                ps.setString(3, newName);
                ps.setString(4, newEmail);
                ps.setString(5, nombreUsuario);
                ps.setString(6, contrasenha);
                ps.executeUpdate();
                System.out.println("Usuario modificado.");
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método muestra por consola todos los datos del usuario que coindida con el nombre de usuario pasado por parámetro,
     * en un principio iba a hacerlo con idUsuario, pero me dí cuenta que no se muestra en ningún otro sitio y es más fácil
     * de recordar un nombre.
     * Si no existe el nombre de usuario introducido lo indica por pantalla.
     *
     * @param nmbUsuario - nombre del usuario a buscar en la bbdd
     */
    public static void consultarUsuario(String nmbUsuario) {

        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);
            Statement st = cn.createStatement();

            String query = "SELECT * FROM usuarios WHERE usuario = ?;";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setString(1, nmbUsuario);
                ResultSet resultado = ps.executeQuery();
                boolean existe = false;
                while (resultado.next()) {
                    int idUsuario = resultado.getInt(1);
                    String usuario = resultado.getString(2);
                    String contrasenha = resultado.getString(3);
                    String nombre = resultado.getString(4);
                    String email = resultado.getString(5);

                    System.out.println("\nIDUsuario: " + idUsuario);
                    System.out.println("Usuario: " + usuario);
                    System.out.println("Contraseña: " + contrasenha);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Email: " + email + "\n");
                    existe = true;
                }

                if (!existe) {
                    System.out.println("No se ha encontrado el usuario");

                }
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método busca el usuario el cual tenga el nombre de usuario y la contraseña pedidos al usuario en la bbdd y lo borra.
     * Deben coincidir ambos.
     *
     * @param nombreUsuario - nombre del usuario a buscar en la bbdd
     * @param contrasenha   - - contraseña del usuario a buscar en la bbdd
     */
    public static void eliminarUsuario(String nombreUsuario, String contrasenha) {
        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);
            Statement st = cn.createStatement();

            String query = "DELETE FROM usuarios WHERE usuario = ? AND password = ?;";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setString(1, nombreUsuario);
                ps.setString(2, contrasenha);
                ps.executeUpdate();
                System.out.println("Usuario eliminado.");
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el Usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este método comprueba que no haya un registro con el mismo nombre de usuario o email en la bbdd
     *
     * @param usuario - Objeto de tipo usuario del cual se extraen los datos de nombre de usuario y email para comprobar.
     * @return - devuelve falso si encuentra un usuario, true si no lo encuentra.
     */
    public static boolean comprobarCampos(Usuario usuario) {
        String nickUsr = usuario.getUsuario();
        String emailUsr = usuario.getEmail();
        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);

            String query = "SELECT count(*) FROM usuarios WHERE usuario = ?;";
            try (PreparedStatement ps = cn.prepareStatement(query)) {
                ps.setString(1, nickUsr);
                ResultSet resultado = ps.executeQuery();
                int numRegistros = 0;
                while (resultado.next()) {
                    numRegistros = resultado.getInt(1);
                }
                if (numRegistros > 0) {
                    //Devuelve falso si hay un usuario con el mismo nombre en la bbdd.
                    System.out.println("Error, ya existe un Usuario con el mismo nombre de usuario en la bbdd.");
                    return false;
                }
            }

            String query2 = "SELECT count(*) FROM usuarios WHERE email = ?;";
            try (PreparedStatement ps = cn.prepareStatement(query2)) {
                ps.setString(1, emailUsr);
                ResultSet resultado = ps.executeQuery();
                int numRegistros = 0;
                while (resultado.next()) {
                    numRegistros = resultado.getInt(1);
                }
                if (numRegistros > 0) {
                    //Devuelve falso si hay un usuario con el mismo email en la bbdd.
                    System.out.println("Error, ya existe un Usuario con el mismo email en la bbdd.");
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Este método hace la inserción inicial de 5 registros en la bbdd
     */
    public static void insercionInicial() {
        try {
            Connection cn = DriverManager.getConnection(urlConexion, user, password);
            Statement st = cn.createStatement();


            String in1 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES ('agomgar','adrian1234','adrian','adri@2dam.com');";
            String in2 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES ('elopgor','enrique1234','enrique','enri@2dam.com');";
            String in3 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES ('mmorcol','miguel1234','miguel','migue@2dam.com');";
            String in4 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES ('iloprod','israel1234','israel','isra@2dam.com');";
            String in5 = "INSERT INTO usuarios (usuario, password, nombre, email) VALUES ('gmergut','gonzalo1234','gonzalo','gonza@2dam.com');";

            st.executeUpdate(in1);
            st.executeUpdate(in2);
            st.executeUpdate(in3);
            st.executeUpdate(in4);
            st.executeUpdate(in5);

            st.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

}