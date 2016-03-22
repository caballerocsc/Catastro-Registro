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

    public String obtenerCeldaXLS(Cell c, Row r) {
        String resultado = "";
        if (c == null) {
            log.info("Celda vacia en fila: " + r.getRowNum());
        } else {
            CellReference cellRef = new CellReference(r.getRowNum(), c.getColumnIndex());
            log.info(cellRef.formatAsString());
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

    public void leerXLSX(XSSFWorkbook wb, String fecha, int numColumnas, int useCase, String nomArchivo) {
        log.info("Creando archivo XLSX para: "+nomArchivo);
        Sheet sheet = wb.getSheetAt(0);
        List<String> fila = new ArrayList<>();
        int rowStart = 1;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
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

    public void leerXLS(HSSFWorkbook wb, String fecha, int numColumnas, int useCase, String nomArchivo) {
        log.info("Creando archivo XLS para: "+nomArchivo);
        Sheet sheet = wb.getSheetAt(0);
        List<String> fila = new ArrayList<>();
        int rowStart = 1;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum <= rowEnd; rowNum++) {
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
                if (vacias==10) {//si se encuentra 10 celdas vacias seguidas, termina el proceso en vez de leer todas las filas vacias
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
                m.mapeoGobAnt2015ITR(fila);
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
}
