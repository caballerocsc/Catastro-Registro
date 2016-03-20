/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Usuario
 */
public class Controlador {

    
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Controlador.class);
    
    
    
    public Controlador() {
    }
    
    
    public boolean  cargarIgac(JFileChooser chooser, String fecha){
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            File doc=new File(chooser.getSelectedFile().toString()+"/"+archivos[i]);
            Orquestador o = new Orquestador();
            switch(tipo){
                case "CAT":{
                    //excel.igacCatastro(new File(arch), fecha);
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASIGACCATASTRO, Parametros.IGACCATASTRO);
                    break;
                }
                case "REG":{
                    //excel.igacRegistro(new File(arch), fecha);
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASIGACREGISTRO,Parametros.IGACREGISTRO );
                    break;
                }
                case "ITR":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASIGACITR,Parametros.IGACITR);
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído: "+tipo);
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
            Orquestador o = new Orquestador();
            switch(tipo){
                case "CAT":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASMEDELLINCATASTRO, Parametros.MEDELLINCAT);
                    break;
                }
                case "REG":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASMEDELLINREGISTRO, Parametros.MEDELLINREG);
                    break;
                }
                case "ITR":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASMEDELLINITR, Parametros.MEDELLINITR);
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído");
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
            Orquestador o = new Orquestador();
            switch(tipo){
                case "CAT":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2014CAT, Parametros.GOBANT2014CAT);
                    break;
                }
                case "REG":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2014REG, Parametros.GOBANT2014REG);
                    break;
                }
                case "ITR":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2014ITR, Parametros.GOBANT2014ITR);
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído");
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
            Orquestador o = new Orquestador();
            switch(tipo){
                case "CAT":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2015CAT, Parametros.GOBANT2015CAT);
                    break;
                }
                case "REG":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2015REG, Parametros.GOBANT2015REG);
                    break;
                }
                case "ITR":{
                    o.seleccionarMetodo(doc, fecha, Parametros.COLUMNASGOBANT2015ITR, Parametros.GOBANT2015ITR);
                    break;
                }
                default:{
                    log.error("El nombre del archivo esta mal formado y no fue leído");
                }
            }
        }
        return false;
    }
    
    
}
