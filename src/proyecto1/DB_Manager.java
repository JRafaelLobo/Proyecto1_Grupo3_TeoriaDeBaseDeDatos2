package proyecto1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DB_Manager {
//Datos

    public Connection con = null;

    public DB_Manager() {
        // Llama al método para crear la conexión
        crearConexion();
    }

    public void crearConexion() {
        try {
            //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bienesraicesdb", "root", "laikaluz");
            con = DriverManager.getConnection("jdbc:mysql://db-teoriadebasededatos1-intento2.cv0s004w2wx5.us-east-1.rds.amazonaws.com:3306/bienesraicesdb", "admin", "lamejorcontra");
            System.out.println("Se conecto :D!");
        } catch (SQLException e) {
            System.err.println("No se conecto :(!");
            e.printStackTrace();
        }
    }

    /*public void crearTablas() {
        // Tabla de prueba para probar conexion
        if (con != null) {
            try {
                //Crea instancia de statement en la base de datos, nos permite comunicarnos con ella
                Statement comunicarBase = con.createStatement();

                //Crea agentes
                String createTableAgentes = "CREATE TABLE IF NOT EXISTS agentes ("
                        + "id INT PRIMARY KEY NOT NULL,"
                        + "nombre CHAR(40) NOT NULL,"
                        + "direccion CHAR(30) NOT NULL,"
                        + "celular INT NOT NULL,"
                        + "telefonoOficina INT NOT NULL"
                        + ")";

                comunicarBase.executeUpdate(createTableAgentes);
                System.out.println("Table 'agentes' created successfully.");

                //Crea vendedores
                String createTableVendedores = "CREATE TABLE IF NOT EXISTS vendedores ("
                        + "id INT PRIMARY KEY NOT NULL,"
                        + "nombre CHAR(40) NOT NULL,"
                        + "direccion CHAR(30) NOT NULL,"
                        + "celular INT NOT NULL"
                        + ")";
                comunicarBase.executeUpdate(createTableVendedores);
                System.out.println("Table 'vendedores' created successfully.");

                //Crear compradores
                String createTableCompradores = "CREATE TABLE IF NOT EXISTS compradores ("
                        + "id INT PRIMARY KEY NOT NULL,"
                        + "nombre CHAR(40) NOT NULL,"
                        + "direccion CHAR(30) NOT NULL,"
                        + "celular INT NOT NULL"
                        + ")";
                comunicarBase.executeUpdate(createTableCompradores);
                System.out.println("Table 'compradores' created successfully.");

                //Crea propiedades vendidas
                String createTablePropiedades = "CREATE TABLE IF NOT EXISTS propiedades_vendidas ("
                        + "idPropiedad INT PRIMARY KEY NOT NULL,"
                        + "nombre CHAR(40) NOT NULL,"
                        + "ciudad CHAR(40) NOT NULL,"
                        + "direccion CHAR(30) NOT NULL,"
                        + "cantidadDormitorios INT NOT NULL,"
                        + "caracteristicas CHAR(100) NOT NULL,"
                        + "precio INT NOT NULL,"
                        + "fechaPublicacion DATE NOT NULL,"
                        + "fechaVenta DATE NOT NULL,"
                        + "noIdentidad_Agente INT NOT NULL,"
                        + "FOREIGN KEY (noIdentidad_Agente) REFERENCES agentes(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                        + "noIdentidad_Vendedor INT NOT NULL,"
                        + "FOREIGN KEY (noIdentidad_Vendedor) REFERENCES vendedores(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                        + "noIdentidad_Comprador INT NOT NULL,"
                        + "FOREIGN KEY (noIdentidad_Comprador) REFERENCES compradores(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                        + "comisionVenta INT NOT NULL"
                        + ")";
                comunicarBase.executeUpdate(createTablePropiedades);
                System.out.println("Table 'Propiedades_vendidas' created successfully.");

                //Crea propiedades en mercado
                String createTablePropiedadesMercado = "CREATE TABLE IF NOT EXISTS propiedades_en_mercado ("
                        + "idPropiedad INT PRIMARY KEY NOT NULL,"
                        + "nombre CHAR(40) NOT NULL,"
                        + "ciudad CHAR(40) NOT NULL,"
                        + "direccion CHAR(30) NOT NULL,"
                        + "cantidadDormitorios INT NOT NULL,"
                        + "caracteristicas CHAR(100) NOT NULL,"
                        + "precio INT NOT NULL,"
                        + "fechaPublicacion DATE NOT NULL,"
                        + "noIdentidad_Agente INT NOT NULL,"
                        + "FOREIGN KEY (noIdentidad_Agente) REFERENCES agentes(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                        + "noIdentidad_Vendedor INT NOT NULL,"
                        + "FOREIGN KEY (noIdentidad_Vendedor) REFERENCES vendedores(id) ON DELETE CASCADE ON UPDATE CASCADE"
                        + ")";
                comunicarBase.executeUpdate(createTablePropiedadesMercado);
                System.out.println("Table 'Propiedades_en_mercado' created successfully.");

                //Cerrar conexion
                comunicarBase.close();
            } catch (SQLException ex) {
                Logger.getLogger(DB_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("Connection is null. Unable to create table.");
        }
    }*/

    public void HacerConsulta(String consulta) {
        try {
            // Verifica si la conexión está establecida
            if (con != null) {
                // Utiliza la conexión para crear un statement
                Statement comunicarBase = con.createStatement();
                comunicarBase.execute(consulta);
                System.out.println("Consulta realizada con éxito");
                // Cerrar el statement
                comunicarBase.close();
            } else {
                System.err.println("La conexión no está establecida. No se puede hacer la consulta.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB_Manager.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrió un error al hacer la consulta");
        }
    }

    public ResultSet mostrarElementos(String query) {
        ResultSet rs = null;
        if (con != null) {
            try {
                Statement statement = con.createStatement(); 
                rs = statement.executeQuery(query);
                return rs;
                
            } catch (SQLException ex) {
                Logger.getLogger(DB_Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }else{
            return null;
        }
        return rs;
    }
}