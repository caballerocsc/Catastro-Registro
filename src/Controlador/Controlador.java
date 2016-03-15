/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.io.File;
import javax.swing.JFileChooser;
import modelo.LeerExcel;

/**
 *
 * @author Usuario
 */
public class Controlador {

    
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Controlador.class);
    
    public Controlador() {
    }
    
    
    public boolean  cargarIgac(JFileChooser chooser, String fecha){
        LeerExcel excel=new LeerExcel();
        String[] archivos=chooser.getSelectedFile().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            String arch=chooser.getSelectedFile().toString()+"/"+archivos[i];
            switch(tipo){
                case "CAT":{
                    excel.igacCatastro(new File(arch), fecha);
                    break;
                }
                case "REG":{
                    excel.igacRegistro(new File(arch), fecha);
                    break;
                }
                case "ITR":{
                    excel.igacITR(new File(arch), fecha);
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
        LeerExcel excel=new LeerExcel();
        String[] archivos=chooser.getCurrentDirectory().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            String arch=chooser.getCurrentDirectory().toString()+"/"+archivos[i];
            switch(tipo){
                case "CAT":{
                    excel.medellinCatastro(new File(arch), fecha);
                    break;
                }
                case "REG":{
                    excel.medellinRegistro(new File(arch), fecha);
                    break;
                }
                case "ITR":{
                    excel.medellinITR(new File(arch), fecha);
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
        LeerExcel excel=new LeerExcel();
        String[] archivos=chooser.getCurrentDirectory().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            String arch=chooser.getCurrentDirectory().toString()+"/"+archivos[i];
            switch(tipo){
                case "CAT":{
                    excel.gobAnt2014Catastro(new File(arch), fecha);
                    break;
                }
                case "REG":{
                    excel.GobAnt2014Registro(new File(arch), fecha);
                    break;
                }
                case "ITR":{
                    excel.gobAnt2014ITR(new File(arch), fecha);
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
        LeerExcel excel=new LeerExcel();
        String[] archivos=chooser.getCurrentDirectory().list();
        String tipo;
        for (int i = 0; i < archivos.length; i++) {
            tipo=archivos[i].substring(0, 3);
            String arch=chooser.getCurrentDirectory().toString()+"/"+archivos[i];
            switch(tipo){
                case "CAT":{
                    excel.gobAnt2015Catastro(new File(arch), fecha);
                    break;
                }
                case "REG":{
                    excel.GobAnt2015Registro(new File(arch), fecha);
                    break;
                }
                case "ITR":{
                    excel.gobAnt2015ITR(new File(arch), fecha);
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
