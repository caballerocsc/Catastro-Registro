/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import Controlador.Parametros;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class LeerArchivo {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LeerArchivo.class);

    public LeerArchivo() {
    }

    /**
     * Método que se encarga de obtener el contenido de una celda 
     * de un archivo de excel
     * @param c Celda del archivo
     * @param r Fila del archivo 
     * @return contenido de la celda, si se encuentra vacia retorna ""
     */
    public String obtenerCeldaXLS(Cell c, Row r) {
        String resultado = "";
        if (c == null) {
//            log.info("Celda vacia en fila: " + r.getRowNum());
        } else {
            CellReference cellRef = new CellReference(r.getRowNum(), c.getColumnIndex());
//            log.info(cellRef.formatAsString());
//            log.info(" - ");
            switch (c.getCellType()) {
                case Cell.CELL_TYPE_STRING:
//                    log.info(c.getRichStringCellValue().getString());
                    resultado = c.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(c)) {
//                        log.info(c.getDateCellValue());
                        resultado = c.getDateCellValue().toString();
                    } else {
//                        log.info(c.getNumericCellValue());
                        resultado = String.valueOf(c.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
//                    log.info(c.getBooleanCellValue());
                    resultado = String.valueOf(c.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
//                    log.info(c.getCellFormula());
                    resultado = c.getCellFormula();
                    break;
                default:
                    resultado = "";
            }
        }
        return resultado.replace("'", "");
    }

    /**
     * Método que se encarga de crear leer un archivo con extensión xlsx.
     * Por cada fila hace una iteracion y llama al método que decide a que funcion llamar
     * para hacer el mapeo correspondiente
     * @param wb archivo XLSX
     * @param fecha fecha de ingreso de la información al repositorio
     * @param numColumnas numero de columnas del archivo
     * @param useCase numero de caso, para identificar a que case debe entrar 
     * @param nomArchivo nombre del archivo, para obtener el municipio
     */
    public void leerXLSX(XSSFWorkbook wb, String fecha, int numColumnas, int useCase, String nomArchivo) {
        log.info("Creando archivo XLSX para: "+nomArchivo);
        Sheet sheet = wb.getSheetAt(0);
        int rowStart = 1;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
            log.info("Leyendo fila: "+rowNum);
            List<String> fila = new ArrayList<>();
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
            } else {
                int lastColumn = Math.max(r.getLastCellNum(), numColumnas);
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    String celda = obtenerCeldaXLS(c, r);
                    fila.add(celda);
                }
                fila.add(fecha);
                seleccionarTabla(nomArchivo, useCase, fila);
            }
        }
    }

    /**
     * Método que se encarga de crear leer un archivo con extensión xls.
     * Por cada fila hace una iteracion y llama al método que decide a que funcion llamar
     * para hacer el mapeo correspondiente
     * @param wb archivo XLSX
     * @param fecha fecha de ingreso de la información al repositorio
     * @param numColumnas numero de columnas del archivo
     * @param useCase numero de caso, para identificar a que case debe entrar 
     * @param nomArchivo nombre del archivo, para obtener el municipio
     */
    public void leerXLS(HSSFWorkbook wb, String fecha, int numColumnas, int useCase, String nomArchivo) {
        log.info("Creando archivo XLS para: "+nomArchivo);
        Sheet sheet = wb.getSheetAt(0);
        int rowStart = 1;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
            log.info("Leyendo fila: "+rowNum);
            List<String> fila = new ArrayList<>();
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
            } else {
                int lastColumn = Math.max(r.getLastCellNum(), numColumnas);
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    String celda = obtenerCeldaXLS(c, r);
                    fila.add(celda);
                }
                fila.add(fecha);
                seleccionarTabla(nomArchivo, useCase, fila);
            }
        }
    }

    /**
     * Método que tiene como funcionalidad leer el archivo de excel como texto plano,
     * pues algunos archivos no podian ser abiertos por la libreria POI para excel.
     * Por cada fila hace una iteracion y llama al método que decide a que funcion llamar
     * para hacer el mapeo correspondiente
     * @param file Archivo de texto plano
     * @param fecha fecha de ingreso de la información al repositorio
     * @param numColumnas numero de columnas del archivo
     * @param useCase numero de caso, para identificar a que case debe entrar 
     * @param nomArchivo nombre del archivo, para obtener el municipio
     */
    public void leerArchivoPlano(File file, String fecha, int numColumnas, int useCase, String nomArchivo) {
        log.info("Creando archivo TXT para: "+nomArchivo);
        FileReader f = null;
        try {
            String cadena;
            f = new FileReader(file);
            BufferedReader br = new BufferedReader(f);
            int j=1;
            int vacias=0;
            while ((cadena = br.readLine()) != null) {
                if(!cadena.contains("\t\t\t")){
                    vacias=0;
                    if (j!=1) {
                    String[] datos = cadena.split("\t");
                    List<String> fila = new ArrayList<>();
                    log.info("Leyendo fila: "+j);
                    for (int i = 0; i < datos.length; i++) {
                        fila.add(datos[i].replace("''", ""));
                    }
                    fila.add(fecha);
                    seleccionarTabla(nomArchivo, useCase, fila);
                    }
                }else{
                    vacias++;
                    log.info("Fila vacia: "+j);
                }
                if (vacias==30) {//si se encuentra 10 celdas vacias seguidas, termina el proceso en vez de leer todas las filas vacias
                    break;
                }
                j++;
            }
        } catch (IOException ex) {
            log.error("Error al leer el archivo plano", ex);
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (IOException ex) {
                log.error("Error cerrando el archivo plano", ex);
            }
        }
    }

    /**
     * Método encargado de direccionar la fila leida del archivo en excel a la
     * funcion adecuada para que haga el mapeo respectivo
     * @param nomArchivo Nombre del archivo, necesario para identificar el municipio
     * @param useCase numero de caso, con esta variable se decide que tipo de mapeo realizar
     * @param fila lista de String donde cada posicion contiene el 
     * contenido de una celda de la fila leída
     */
    public void seleccionarTabla(String nomArchivo, int useCase, List<String> fila) {
        Mapeo m = new Mapeo();
        switch (useCase) {
            case Parametros.IGACCATASTRO: {
                m.mapeoIgacCatastro(fila);
                break;
            }
            case Parametros.IGACREGISTRO: {
                m.mapeoIgacRegistro(fila, nomArchivo);
                break;
            }
            case Parametros.IGACITR: {
                m.mapeoIgacITR(fila, nomArchivo);
                break;
            }
            case Parametros.GOBANT2014CAT: {
                m.mapeoGobAnt2014Catastro(fila);
                break;
            }
            case Parametros.GOBANT2014REG: {
                m.mapeoGobAnt2014Registro(fila, nomArchivo);
                break;
            }
            case Parametros.GOBANT2014ITR: {
                m.mapeoGobAnt2014ITR(fila);
                break;
            }
            case Parametros.GOBANT2015CAT: {
                m.mapeoGobAnt2015Catastro(fila);
                break;
            }
            case Parametros.GOBANT2015REG: {
                m.mapeoGobAnt2015Registro(fila, nomArchivo);
                break;
            }
            case Parametros.GOBANT2015ITR: {
                m.mapeoGobAnt2015ITR(fila,nomArchivo,seleccionTipo(nomArchivo));
                break;
            }
            case Parametros.MEDELLINCAT: {
                m.mapeoMedellinCatastro(fila);
                break;
            }
            case Parametros.MEDELLINREG: {
                m.mapeoMedellinRegistro(fila);
                break;
            }
            case Parametros.MEDELLINITR: {
                m.mapeoMedellinITR(fila);
                break;
            }
            default: {
                log.error("NO entro a ningun caso del metodo seleccionarTabla");
            }
        }
    }
    
    /**
     * Método que determina que metodo de mapeo usar, pues en para los 
     * archivos itr 2015 de antioquia habian varios formatos de archivos
     * @param nombre nombre del archivo
     * @return tipo de mapeo a utilizar
     */
    public int seleccionTipo(String nombre){
        String[] tmp=nombre.split("=");
        return Integer.parseInt(tmp[1].substring(0, 1));
    }
}
