/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * Clase que se encarga de realizar la conexion entre la opcion que selecciono el usuario 
 * y la forma en que el archivo debe ser leido por la aplicación. 
 * @author cesar solano
 */
public class Controlador {

    
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Controlador.class);
    
    
    
    public Controlador() {
    }
    
    
    public boolean  cargarIgac(JFileChooser chooser, String fecha){
        log.info("Método CargarIgac");
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            File doc=new File(chooser.getSelectedFile().toString()+"/"+archivos[i]);
            switch(tipo){
                case "CAT":{
                    log.info("Método CargarIgac CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASIGACCATASTRO, Parametros.IGACCATASTRO);
                    o.run();
                    break;
                }
                case "REG":{
                    log.info("Método CargarREG");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASIGACREGISTRO,Parametros.IGACREGISTRO );
                    o.run();
                    break;
                }
                case "ITR":{
                    log.info("Método CargarITR");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASIGACITR,Parametros.IGACITR);
                    o.run();
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído: "+doc.getName());
                }
            }
            
        }
        return false;
    }
    
    public boolean  cargarMedellin(JFileChooser chooser, String fecha){
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            File doc=new File(chooser.getSelectedFile().toString()+"/"+archivos[i]);
            switch(tipo){
                case "CAT":{
                    log.info("Método CargarMedellin CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASMEDELLINCATASTRO, Parametros.MEDELLINCAT);
                    o.run();
                    break;
                }
                case "REG":{
                    log.info("Método CargaMedellinREG");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASMEDELLINREGISTRO, Parametros.MEDELLINREG);
                    o.run();
                    break;
                }
                case "ITR":{
                    log.info("Método CargarMedellinITR");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASMEDELLINITR, Parametros.MEDELLINITR);
                    o.run();
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído: "+doc.getName());
                }
            }
            
        }
        return false;
    }
     
    public boolean  cargarAntioquia2014(JFileChooser chooser, String fecha){
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            File doc=new File(chooser.getSelectedFile().toString()+"/"+archivos[i]);
            switch(tipo){
                case "CAT":{
                    log.info("Método CargarAntioquia2014CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2014CAT, Parametros.GOBANT2014CAT);
                    o.run();
                    break;
                }
                case "REG":{
                    log.info("Método CargarAntioquia2014REG");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2014REG, Parametros.GOBANT2014REG);
                    o.run();
                    break;
                }
                case "ITR":{
                    log.info("Método CargarAntioquia2014ITR");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2014ITR, Parametros.GOBANT2014ITR);
                    o.run();
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído: "+doc.getName());
                }
            }
        }
        return false;
    }
    
    public boolean  cargarAntioquia2015(JFileChooser chooser,String fecha){
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            File doc=new File(chooser.getSelectedFile().toString()+"/"+archivos[i]);
            switch(tipo){
                case "CAT":{
                    log.info("Método CargarAntioquia2015CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2015CAT, Parametros.GOBANT2015CAT);
                    o.run();
                    break;
                }
                case "REG":{
                    log.info("Método CargarAntioquia2015CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2015REG, Parametros.GOBANT2015REG);
                    o.run();
                    break;
                }
                case "ITR":{
                    log.info("Método CargarAntioquia2015CAT");
                    Orquestador o = new Orquestador(doc, fecha, Parametros.COLUMNASGOBANT2015ITR, Parametros.GOBANT2015ITR);
                    o.run();
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído: "+doc.getName());
                }
            }
        }
        return false;
    }
    
    
}
