/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import obj.Catastro;
import obj.ITR;
import obj.Propietario;
import obj.Registro;
import org.apache.log4j.Logger;

/**
 *
 * @author Usuario
 */
public class Consultas {

    public Consultas() {
    }
    
    private static final Logger log = Logger.getLogger(Conexion.class);
     
    public int verificarPropietario(Propietario p){
        Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.VERIFICARPROPIETARIO;
        try{
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(1 , p.getTipoDoc());
            ps.setString(2 , p.getNumDoc());
            rs=ps.executeQuery();
            int id=0;
            while(rs.next()){
                id=rs.getInt(1);
            }
            if(id!=0)
                return id;
            
        }catch(SQLException e){
            log.error("SQL Exception:verificarPropietario", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return 0; 
    }
    
    public int insertarPropietario(Propietario p){
        Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARPROPIETARIO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , p.getTipoDoc());
            ps.setString(i++ , p.getNumDoc());
            ps.setString(i++ , p.getNombrePropietario());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarPropietario", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public int insertarIgacCatastro(Catastro c){
        Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACCATASTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , c.getDepartamentoidfk());
            ps.setString(i++ , c.getMunicipioidfk());
            ps.setString(i++ , c.getNumpredio());
            ps.setString(i++ , c.getAvaluo());
            ps.setString(i++ , c.getSector());
            ps.setString(i++ , c.getManzana());
            ps.setString(i++ , c.getPredio());
            ps.setString(i++ , c.getPropiedad());
            ps.setString(i++ , c.getMatricula());
            ps.setString(i++ , c.getDireccion());
            ps.setString(i++ , c.getTipo());
            ps.setString(i++ , c.getEstado());
            ps.setString(i++ , c.getFecharecibido());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacCatastro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public int insertarIgacRegistro(Registro r){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACREGISTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , r.getMatriculaori());
            ps.setString(i++ , r.getPredialori());
            ps.setString(i++ , r.getDireccion());
            ps.setString(i++ , r.getTipo());
            ps.setString(i++ , r.getEstado());
            ps.setString(i++ , r.getFecharecibido());
            ps.setString(i++ , r.getMunicipioidfk());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacRegistro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public int insertarIgacITR(ITR itr){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACITR;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , itr.getDepartamentoidfk());
            ps.setString(i++ , itr.getMunicipioidfk());
            ps.setString(i++ , itr.getFecharecibido());
            ps.setString(i++ , itr.getNumpredio());
            ps.setString(i++ , itr.getAvaluos());
            ps.setString(i++ , itr.getSector());
            ps.setString(i++ , itr.getManzana());
            ps.setString(i++ , itr.getPredio());
            ps.setString(i++ , itr.getPropiedad());
            ps.setString(i++ , itr.getCirculo());
            ps.setString(i++ , itr.getMatricula());
            ps.setFloat(i++ , itr.getAreaterreno());
            ps.setFloat(i++ , itr.getAreaconstruida());
            ps.setString(i++ , itr.getDireccioncat());
            ps.setString(i++ , itr.getDireccionreg());
            ps.setString(i++ , itr.getCrpredial());
            ps.setString(i++ , itr.getCrmatricula());
            ps.setString(i++ , itr.getCrdireccion());
            ps.setString(i++ , itr.getCrdocumento());
            ps.setString(i++ , itr.getCrnombre());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacITR", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public boolean insertarIgacCatastroProp(int idCat,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACCATASTROPROPIETARIO;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idCat);
            ps.setInt(i++ , idProp);
            System.out.println("consulta: "+ps.toString());
            ps.executeUpdate();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacCatastroProp", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public boolean insertarIgacRegistroProp(int idReg,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACREGISTROPROPIETARIO;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idReg);
            ps.setInt(i++ , idProp);
            ps.executeUpdate();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacRegistroProp", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public boolean insertarIgacItrCatProp(int idItr,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACITRCATPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idItr);
            ps.setInt(i++ , idProp);
            ps.executeUpdate();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacItrCatProp", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public boolean insertarIgacItrRegProp(int idItr,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARIGACITRREGPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idItr);
            ps.setInt(i++ , idProp);
            ps.executeUpdate();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarIgacItrRegProp", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public int insertarAntioquiaCatastro(Catastro c){
        Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIACATASTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , c.getMunicipioidfk());
            ps.setString(i++ , c.getDepartamentoidfk());
            ps.setString(i++ , c.getPredio());
            if(c.getPredialcat28().equals(""))
                ps.setString(i++ , c.getPredialcat28());
            else
                ps.setString(i++ , "");
            if(c.getPredialcat30().equals(""))
                ps.setString(i++ , c.getPredialcat30());
            else
                ps.setString(i++ , "");
            ps.setString(i++ , c.getAvaluo());
            ps.setString(i++ , c.getCorregimiento());
            ps.setString(i++ , c.getMatricula());
            ps.setString(i++ , c.getDireccion());
            ps.setString(i++ , c.getTipo());
            ps.setString(i++ , c.getEstado());
            ps.setString(i++ , c.getFecharecibido());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaCatastro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public boolean insertarAntioquiaCatProp(int idAntCat,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIACATASTROPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idAntCat);
            ps.setInt(i++ , idProp);
            rs=ps.executeQuery();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaCatProp", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public int insertarAntioquiaRegistro(Registro r){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIAREGISTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , r.getMatriculaori());
            ps.setString(i++ , r.getPredialori());
            ps.setString(i++ , r.getDireccion());
            ps.setString(i++ , r.getTipo());
            ps.setString(i++ , r.getEstado());
            ps.setString(i++ , r.getMunicipioidfk());
            ps.setString(i++ , r.getFecharecibido());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaRegistro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public boolean insertarAntioquiaRegProP(int idAntReg,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIAREGISTROPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idAntReg);
            ps.setInt(i++ , idProp);
            rs=ps.executeQuery();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaRegProP", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public int insertarAntioquiaITR(ITR itr){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIAITR;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , itr.getDepartamentoidfk());
            ps.setString(i++ , itr.getMunicipioidfk());
            ps.setString(i++ , itr.getNumpredio());
            if(!itr.getPredialcat().equals(""))
                ps.setString(i++ , itr.getPredialcat());
            else
                ps.setString(i++ , "");
            ps.setString(i++ , itr.getPredialcat28());
            ps.setString(i++ , itr.getPredialcat30());
            if(!itr.getAvaluos().equals(""))
                ps.setString(i++ , itr.getAvaluos());
            else
                ps.setString(i++, "");
            if(itr.getCorregimiento().equals(""))
                ps.setString(i++ , itr.getCorregimiento());
            else 
                ps.setString(i++ , "");
            ps.setString(i++ , itr.getCirculo());
            ps.setString(i++ , itr.getMatricula());
            ps.setFloat(i++ , itr.getAreaterreno());
            ps.setFloat(i++ , itr.getAreaconstruida());
            ps.setString(i++ , itr.getDireccioncat());
            ps.setString(i++ , itr.getDireccionreg());
            ps.setString(i++ , itr.getCrpredial());
            ps.setString(i++ , itr.getCrmatricula());
            ps.setString(i++ , itr.getCrdireccion());
            ps.setString(i++ , itr.getCrdocumento());
            ps.setString(i++ , itr.getCrnombre());
            ps.setString(i++ , itr.getFecharecibido());
            if(itr.getTipopredio().equals(""))
                ps.setString(i++, itr.getTipopredio());
            else
                ps.setString(i++, "");
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaITR", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    
    public boolean insertarAntioquiaItrCatProP(int idAntcat,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIAITRCATASTROPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idAntcat);
            ps.setInt(i++ , idProp);
            rs=ps.executeQuery();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaItrCatProP", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    
    public boolean insertarAntioquiaItrRegProP(int idAntReg,int idProp){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARANTIOQUIAITRREGISTROPROP;
        boolean result=false;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , idAntReg);
            ps.setInt(i++ , idProp);
            rs=ps.executeQuery();
            result=true;
        }catch(SQLException e){
            log.error("SQL Exception: insertarAntioquiaItrRegProP", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return result;
    }
    
    public int insertarMedellinCatastro(Catastro c){
        Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARMEDELLINCATASTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , c.getIdpredio());
            ps.setString(i++ , c.getNumpredio());
            ps.setFloat(i++ , c.getAreaterreno());
            ps.setFloat(i++ , c.getAreaconstruida());
            ps.setString(i++ , c.getDireccion());
            ps.setString(i++ , c.getTipopredio());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarMedellinCatastro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public int insertarMedellinRegistro(Registro r){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARMEDELLINREGISTRO;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setString(i++ , r.getCirculo());
            ps.setString(i++ , r.getMatriculaori());
            ps.setString(i++ , r.getDireccion());
            ps.setString(i++ , r.getTipo());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarMedellinRegistro", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
    
    public int insertarMedellinITR(ITR itr){
    Conexion con=new Conexion();
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection cn=con.getConexion();
        Sentencias sbd=Sentencias.INSERTARMEDELLINITR;
        int id=0;
        try{
            int i=1;
            ps=cn.prepareStatement(sbd.getSentencia());
            ps.setInt(i++ , itr.getIdpredio());
            ps.setString(i++ , itr.getNumpredio());
            ps.setString(i++ , itr.getMatricula());
            ps.setFloat(i++ , itr.getAreaterreno());
            ps.setFloat(i++ , itr.getAreaconstruida());
            ps.setString(i++ , itr.getDireccioncat());
            ps.setString(i++ , itr.getDireccionreg());
            ps.setString(i++ , itr.getTipopredio());
            ps.setString(i++ , itr.getCrpredial());
            ps.setString(i++ , itr.getCrmatricula());
            ps.setString(i++ , itr.getCrdireccion());
            ps.setString(i++ , itr.getCrdocumento());
            ps.setString(i++ , itr.getCrnombre());
            ps.setString(i++ , itr.getFecharecibido());
            rs=ps.executeQuery();
            i=1;
            if(rs.next())
                id=rs.getInt(i);
        }catch(SQLException e){
            log.error("SQL Exception: insertarMedellinITR", e);
        }finally{
            con.cerrar(cn);
            con.cerrar(ps);
            con.cerrar(rs);
        }
       return id;
    }
}
