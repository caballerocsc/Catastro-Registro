/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import modelo.LeerArchivo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.POIXMLException;

/**
 * Clase que se encarga de determina cual sera el metodo apropiado para 
 * leer cada uno de los archivos que les llegan
 * @author Usuario
 */
public class Orquestador extends Thread{

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Orquestador.class);
    private HSSFWorkbook wbXLS = null;
    private XSSFWorkbook workbookXLSX = null;
    private final File file;
    private final String fecha;
    private final int numColumnas;
    private final int useCase;
    private long tInicio;
    
    public Orquestador(File file,String fecha,int numColumnas, int useCase) {
        this.file=file;
        this.fecha=fecha;
        this.numColumnas=numColumnas;
        this.useCase=useCase;
    }
    
    private boolean crearLibroXLS(File file) {
        try {
            FileInputStream fileIS = new FileInputStream(file);
            wbXLS = new HSSFWorkbook(fileIS);
        } catch (IOException | OfficeXmlFileException  ex) {
            //log.error("EL documento no es una archivo xls: "+file.getName());
            return false;
        }
        return true;
    }
    
    private boolean crearLibroXLSX(File file) {
        try {
            FileInputStream fileIS = new FileInputStream(file);
            workbookXLSX = new XSSFWorkbook(fileIS);
        } catch (IOException | NoClassDefFoundError | POIXMLException ex) {
//            log.error("EL documento no es una archivo xlsx: "+file.getName());
            return false;
        }
        return true;
    }
     
    public void run(){
        tInicio=System.currentTimeMillis();
        String nomArch=file.getName();
        LeerArchivo le = new LeerArchivo();
        if(crearLibroXLS(file)){
            le.leerXLS(wbXLS, fecha, numColumnas, useCase,nomArch);
        }
        else if(crearLibroXLSX(file))
            le.leerXLSX(workbookXLSX, fecha, numColumnas, useCase, nomArch);
        else
            le.leerArchivoPlano(file, fecha, numColumnas, useCase, nomArch);
        System.out.println(calcularTiempo(nomArch));
    }
    
    public String calcularTiempo(String nomArc){
        return (System.currentTimeMillis()-tInicio)/1000+" Segundos: "+nomArc;
    }
}
