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
 *
 * @author Usuario
 */
public class Orquestador {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Orquestador.class);
    private HSSFWorkbook wbXLS = null;
    private XSSFWorkbook workbookXLSX = null;
    
    
    public Orquestador() {
    }
    
    private boolean crearLibroXLS(File file) {
        try {
            FileInputStream fileIS = new FileInputStream(file);
            wbXLS = new HSSFWorkbook(fileIS);
        } catch (IOException | OfficeXmlFileException  ex) {
            log.error("EL documento no es una archivo xls: "+file.getName());
            return false;
        }
        return true;
    }
    
    private boolean crearLibroXLSX(File file) {
        try {
            FileInputStream fileIS = new FileInputStream(file);
            workbookXLSX = new XSSFWorkbook(fileIS);
        } catch (IOException | NoClassDefFoundError | POIXMLException ex) {
            log.error("EL documento no es una archivo xlsx: "+file.getName());
            return false;
        }
        return true;
    }
    
    public void seleccionarMetodo(File file,String fecha,int numColumnas, int useCase){
        String nomArch=file.getName();
        LeerArchivo le = new LeerArchivo();
        if(crearLibroXLS(file)){
            le.leerXLS(wbXLS, fecha, numColumnas, useCase,nomArch);
        }
            
        else if(crearLibroXLSX(file))
            le.leerXLSX(workbookXLSX, fecha, numColumnas, useCase, nomArch);
        else
            le.leerArchivoPlano(file, fecha, numColumnas, useCase, nomArch);
    }
    
    
    
}
