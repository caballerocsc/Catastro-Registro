/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuario
 */
public class Conexion {
    private static final Logger log = Logger.getLogger(Conexion.class);
    
    public Conexion() {
    }

    String driver = "org.postgresql.Driver";
    String connectString = "jdbc:postgresql://localhost:5432/cata_reg";
    String user = "postgres";
    String password = "123456";

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user, password);
        } catch (SQLException e) {
            log.error("SQLexception: ",e);
            System.out.println("sqlexcepcion");
        } catch (ClassNotFoundException e) {
            log.error("classnofoundexception: ",e);
            System.out.println("classnofoundexception");
        }
        return con;
    }

    /**
     * Método que se encarga de cerrar el objeto de conexión
     *
     * @param cn Objeto de tipo Connection instanciado
     */
    public void cerrar(Connection cn) {
        if (cn != null) {
            try {
                //	System.out.println("cierra conexion");
                cn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                log.error("No se ha cerrado correctamente la conexion: ", e);
            }
        }
    }

    /**
     * Método que se encarga de cerrar el objeto de PreparedStatement
     * @param pst Objeto de tipo PreparedStatement instanciado
     */
    public void cerrar(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                 log.error("No se ha cerrado correctamente el PreparedStatement: ", e);
            }
        }
    }

    /**
     *  Método que se encarga de cerrar el objeto de ResultSet
     * @param rs Objeto de tipo ResultSet instanciado
     */
    @SuppressWarnings("empty-statement")
    public void cerrar(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                 log.error("No se ha cerrado correctamente el resultset: ", e);
            }
        }
    }
    
    
}
