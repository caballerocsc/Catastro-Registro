/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import obj.Catastro;
import obj.ITR;
import obj.Propietario;
import obj.Registro;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Usuario
 */
public class LeerExcel {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LeerExcel.class);
    private static final int COLUMNASIGACCATASTRO=12;
    private static final int COLUMNASIGACREGISTRO=6;
    private static final int COLUMNASIGACITR=19;
    private static final int COLUMNASGOBANT2014CAT=9;
    private static final int COLUMNASGOBANT2014REG=6;
    private static final int COLUMNASGOBANT2014ITR=18;
    private static final int COLUMNASGOBANT2015CAT=9;
    private static final int COLUMNASGOBANT2015REG=6;
    private static final int COLUMNASGOBANT2015ITR=18;
    private static final int COLUMNASMEDELLINCATASTRO=6;
    private static final int COLUMNASMEDELLINREGISTRO=4;
    private static final int COLUMNASMEDELLINITR=14;
    
    public LeerExcel() {
    }
    
    public HSSFWorkbook crearLibro(File file) {
        HSSFWorkbook wb = null;
        try {
            FileInputStream fileIS = new FileInputStream(file);
            wb = new HSSFWorkbook(fileIS);
        } catch (IOException ex) {
            log.error("Archivo no encontrado: ", ex);
        }
        return wb;
    }

    public String obtenerCelda(Cell c,Row r){
        String resultado="";
        if (c == null) {
            log.info("Celda vacia");
        } else {
            CellReference cellRef = new CellReference(r.getRowNum(), c.getColumnIndex());
            log.info(cellRef.formatAsString());
            log.info(" - ");
            switch (c.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    log.info(c.getRichStringCellValue().getString());
                    resultado=c.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(c)) {
                        log.info(c.getDateCellValue());
                        resultado=c.getDateCellValue().toString();
                    } else {
                        log.info(c.getNumericCellValue());
                        resultado=String.valueOf(c.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    log.info(c.getBooleanCellValue());
                    resultado=String.valueOf(c.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    log.info(c.getCellFormula());
                    resultado=c.getCellFormula();
                    break;
                default:
                    resultado="";
            }
        }
     return resultado;
    }
    
    
    public List<Propietario> separarPropietarios(String p) {
        List<Propietario> listaProp = new ArrayList<>();
        if (!p.equals("")) {
            String[] arrayProp = p.split("--");//separar multiples propietarios
            for (int i = 0; i < arrayProp.length; i++) {
                Propietario prop = new Propietario();
                String tipoDoc = arrayProp[i].substring(0, 2);//obtener el  tipo de documento
                prop.setTipoDoc(tipoDoc);
                String[] datosProp = arrayProp[i].split("-");//
                switch (tipoDoc) {
                    case "NI": {
                        //el NI puede contener guiones en el numero de documento
                        //el nombre se encuentra en la ultima posicion del arreglo
                        prop.setNombrePropietario(datosProp[datosProp.length - 1].trim());
                        String temp = "";
                        //el numero de documento esta fraccionado, por ello se concatena 
                        //y se omite el tipo de documento
                        for (int j = 0; j < datosProp.length - 1; j++) {
                            temp = temp.concat(datosProp[1]);
                        }
                        prop.setNumDoc((temp.substring(2)).trim());
                        break;
                    }
                    default:
                        //los demas tipos de documentos no contienen guiones
                        prop.setNombrePropietario(datosProp[1].trim());
                        prop.setNumDoc((datosProp[0].substring(2)).trim());
                        break;
                }
                listaProp.add(prop);
            }
        }
        return listaProp;
    }
    
    public List<Integer> guardarPropietarios(List<Propietario> propietarios){
        List<Integer> identificadores=new ArrayList<>();
        Consultas con=new Consultas();
        for (Propietario p : propietarios) {
            identificadores.add(con.insertarPropietario(p));
        }
        return identificadores;
    }
    
    public String obtenerMunicipioNombre(File file){
        String nombre=file.getName();
        String[] temp=nombre.split("-");
        return temp[1];
    }
    
    public boolean igacCatastro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASIGACCATASTRO);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Catastro cat=new Catastro();
            int i=0;
            cat.setNumpredio(fila.get(i++));
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(1).substring(0, 2));
            cat.setAvaluo(fila.get(i++));
            cat.setSector(fila.get(i++));
            cat.setManzana(fila.get(i++));
            cat.setPredio(fila.get(i++));
            cat.setPropiedad(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            String propietario=fila.get(i++);
            cat.setTipo(fila.get(i++));
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idCatastro=con.insertarIgacCatastro(cat);
            for (Integer idProp : ids) {
                if(!con.insertarIgacCatastroProp(idCatastro, idProp))
                    log.error("No se puedo crear la relacion Catastro-propietario idCatastro: "+idCatastro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean igacRegistro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASIGACREGISTRO);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Registro reg=new Registro();
            int i=0;
            String codMun=obtenerMunicipioNombre(ruta);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario=fila.get(i++);
            reg.setTipo(fila.get(i++));
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idRegistro=con.insertarIgacRegistro(reg);
            for (Integer idProp : ids) {
                if(!con.insertarIgacRegistroProp(idRegistro, idProp))
                    log.error("No se puedo crear la relacion Catastro-propietario idCatastro: "+idRegistro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean igacITR(File ruta,String fecharecibido) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASIGACITR);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            ITR itr=new ITR();
            int i=0;
            String codMun=obtenerMunicipioNombre(ruta);
            itr.setDepartamentoidfk(codMun.substring(0, 2));
            itr.setMunicipioidfk(codMun.substring(2));
            itr.setFecharecibido(fecharecibido);
            itr.setNumpredio(fila.get(i++));
            itr.setAvaluos(fila.get(i++));
            itr.setSector(fila.get(i++));
            itr.setManzana(fila.get(i++));
            itr.setPredio(fila.get(i++));
            itr.setPropiedad(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Integer.parseInt(fila.get(i++)));
            itr.setAreaconstruida(Integer.parseInt(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat=fila.get(i++);
            String propietarioReg=fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            List<Integer> idsCat=guardarPropietarios(separarPropietarios(propietarioCat));
            List<Integer> idsReg=guardarPropietarios(separarPropietarios(propietarioReg));
            int idITR=con.insertarIgacITR(itr);
            for (Integer idProp : idsCat) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITRCat-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
            for (Integer idProp : idsReg) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITRreg-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean gobAnt2014Catastro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2014CAT);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Catastro cat=new Catastro();
            int i=0;
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(0).substring(0, 2));
            cat.setNumpredio(fila.get(i++));
            cat.setPredialcat28(fila.get(i++));
            cat.setPredialcat30(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            cat.setTipo(fila.get(i++));
            cat.setDireccion(fila.get(i++));
            String propietario=fila.get(i++);
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idCatastro=con.insertarAntioquiaCatastro(cat);
            for (Integer idProp : ids) {
                if(!con.insertarAntioquiaItrCatProP(idCatastro, idProp))
                    log.error("No se puedo crear la relacion AntioquiaCatastro-propietario idCatastro: "+idCatastro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean GobAnt2014Registro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2014REG);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Registro reg=new Registro();
            int i=0;
            String codMun=obtenerMunicipioNombre(ruta);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setTipo(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario=fila.get(i++);
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idRegistro=con.insertarAntioquiaRegistro(reg);
            for (Integer idProp : ids) {
                if(!con.insertarAntioquiaRegProP(idRegistro, idProp))
                    log.error("No se puedo crear la relacion AntReg-propietario idCatastro: "+idRegistro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean gobAnt2014ITR(File ruta,String fecharecibido) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2014ITR);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            ITR itr=new ITR();
            int i=0;
            itr.setMunicipioidfk(fila.get(i++));
            itr.setDepartamentoidfk(fila.get(0).substring(0, 2));
            itr.setFecharecibido(fecharecibido);
            itr.setNumpredio(fila.get(i++));
            itr.setPredialcat28(fila.get(i++));
            itr.setPredialcat30(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setTipopredio(fila.get(i++));
            itr.setAreaterreno(Integer.parseInt(fila.get(i++)));
            itr.setAreaconstruida(Integer.parseInt(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat=fila.get(i++);
            String propietarioReg=fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            List<Integer> idsCat=guardarPropietarios(separarPropietarios(propietarioCat));
            List<Integer> idsReg=guardarPropietarios(separarPropietarios(propietarioReg));
            int idITR=con.insertarIgacITR(itr);
            for (Integer idProp : idsCat) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITRCat-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
            for (Integer idProp : idsReg) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITRreg-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    
    public boolean gobAnt2015Catastro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2015CAT);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Catastro cat=new Catastro();
            int i=0;
            cat.setNumpredio(fila.get(i++));
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(1).substring(0, 2));
            cat.setAvaluo(fila.get(i++));
            cat.setCorregimiento(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            cat.setDireccion(fila.get(i++));
            String propietario=fila.get(i++);
            cat.setTipo(fila.get(i++));
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idCatastro=con.insertarAntioquiaCatastro(cat);
            for (Integer idProp : ids) {
                if(!con.insertarAntioquiaItrCatProP(idCatastro, idProp))
                    log.error("No se puedo crear la relacion Antioquia2015Catastro-propietario idCatastro: "+idCatastro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean GobAnt2015Registro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2015REG);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Registro reg=new Registro();
            int i=0;
            String codMun=obtenerMunicipioNombre(ruta);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario=fila.get(i++);
            reg.setTipo(fila.get(i++));
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fecha);
            List<Integer> ids=guardarPropietarios(separarPropietarios(propietario));
            int idRegistro=con.insertarAntioquiaRegistro(reg);
            for (Integer idProp : ids) {
                if(!con.insertarAntioquiaRegProP(idRegistro, idProp))
                    log.error("No se puedo crear la relacion Ant2015Reg-propietario idCatastro: "+idRegistro+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean gobAnt2015ITR(File ruta,String fecharecibido) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASGOBANT2015ITR);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            ITR itr=new ITR();
            int i=0;
            itr.setFecharecibido(fecharecibido);
            itr.setNumpredio(fila.get(i++));
            itr.setPredialcat(fila.get(i++));
            itr.setMunicipioidfk(fila.get(i++));
            itr.setDepartamentoidfk(fila.get(2).substring(0, 2));
            itr.setAvaluos(fila.get(i++));
            itr.setCorregimiento(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Integer.parseInt(fila.get(i++)));
            itr.setAreaconstruida(Integer.parseInt(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat=fila.get(i++);
            String propietarioReg=fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            List<Integer> idsCat=guardarPropietarios(separarPropietarios(propietarioCat));
            List<Integer> idsReg=guardarPropietarios(separarPropietarios(propietarioReg));
            int idITR=con.insertarIgacITR(itr);
            for (Integer idProp : idsCat) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITR2015Cat-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
            for (Integer idProp : idsReg) {
                if(!con.insertarIgacRegistroProp(idITR, idProp))
                    log.error("No se puedo crear la relacion ITR2015reg-propietario. idCatastro: "+idITR+" idProp: "+idProp);
            }
        }
        return true;
    }
    
    public boolean medellinCatastro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASMEDELLINCATASTRO);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Catastro cat=new Catastro();
            int i=0;
            cat.setIdpredio(Integer.parseInt(fila.get(i++)));
            cat.setNumpredio(fila.get(i++));
            cat.setAreaterreno(Integer.parseInt(fila.get(i++)));
            cat.setAreaconstruida(Integer.parseInt(fila.get(i++)));
            cat.setDireccion(fila.get(i++));
            cat.setTipo(fila.get(i++));
            cat.setMunicipioidfk("05001");
            cat.setDepartamentoidfk("05");
            cat.setFecharecibido(fecha);
            con.insertarMedellinCatastro(cat);
        }
        return true;
    }
    
    public boolean MedellinRegistro(File ruta,String fecha) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASMEDELLINREGISTRO);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            Registro reg=new Registro();
            int i=0;
            reg.setCirculo(fila.get(i++));
            reg.setMatriculaori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            reg.setTipo(fila.get(i++));
            reg.setFecharecibido(fecha);
            con.insertarIgacRegistro(reg);
        }
        return true;
    }
    
    public boolean medellinITR(File ruta,String fecharecibido) {
        HSSFWorkbook wb = crearLibro(ruta);
        Sheet sheet = wb.getSheetAt(0);
        Consultas con=new Consultas();
        int rowStart = 2;
        int rowEnd = Math.max(2, sheet.getLastRowNum());
        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                log.info("Fila vacia: " + rowNum);
                break;
            }
            int lastColumn = Math.max(r.getLastCellNum(), COLUMNASMEDELLINITR);
            List<String> fila=new ArrayList<>();
            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                String celda=obtenerCelda(c, r);
                fila.add(celda);
            }
            ITR itr=new ITR();
            int i=0;
            itr.setFecharecibido(fecharecibido);
            itr.setIdpredio(Integer.parseInt(fila.get(i++)));
            itr.setNumpredio(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Integer.parseInt(fila.get(i++)));
            itr.setAreaconstruida(Integer.parseInt(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            itr.setTipopredio(fila.get(i++));
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            con.insertarIgacITR(itr);
        }
        return true;
    }
}
