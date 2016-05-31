/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.List;
import obj.Catastro;
import obj.ITR;
import obj.Propietario;
import obj.Registro;

/**
 * Clase que tienen como funcion tomar las lista de 
 * atributos leidos de una fila en excel y convertirla en 
 * un objeto para que pueda ser guardado en la base de datos
 * @author cesar solano
 */
public class Mapeo {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Mapeo.class);
    private boolean guardar;

    public Mapeo() {
    }

    /**
     * Método que toma un String en el cual vienen varios propietarios con sus tipos
     * y numeros de documento y los convierte en una lista de tipo Propietarios
     * @param p cadena de texto en la cual vienen los propietarios con sus tipos
     * y números de documento
     * @param tipo indica por que medio se debe separar los propietarios
     * 1: para separalos por doble guión "--"
     * 2: para separar por un caracter especial "\r\n"
     * @return  lista donde cada posicion contiene un objeto de tipo
     * Propietario
     */
    public List<Propietario> separarPropietarios(String p, int tipo) {
        log.info("Separando Propietarios");
        List<Propietario> listaProp = new ArrayList<>();
        try {
            if (!p.equals("")) {
                String[] arrayProp;
                if (tipo == 1) {
                    arrayProp = p.split("--");//separar multiples propietarios
                } else {
                    arrayProp = p.split("\r\n");
                }
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
                                temp = temp.concat(datosProp[j]);
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
            } else {
                log.warn("El propietario se encuentra vacio");
            }
        } catch (Exception e) {
            log.error(e);
            guardar=false;
        }
        return listaProp;
    }

    /**
     * Método que permite tomar una lista de tipo Propietarios 
     * y guardarlos en la base de datos
     * @param propietarios Lista de tipo Propietario con los objetos que se almacenaran
     * en la base de datos
     * @return lista de tipo int con los id´s de cada propietario
     * guardado en la base de datos
     */
    public List<Integer> guardarPropietarios(List<Propietario> propietarios) {
        log.info("Guardando propietarios");
        List<Integer> identificadores = new ArrayList<>();
        if (guardar) {
            Consultas con = new Consultas();
            for (Propietario p : propietarios) {
                int id = con.verificarPropietario(p);
                if (id == 0) {
                    identificadores.add(con.insertarPropietario(p));
                } else {
                    identificadores.add(id);
                }
            }
        }
        return identificadores;
    }

    /**
     * Método que permite obtener el codigo del municipio 
     * a partir del nombre del archivo
     * @param nombre nombre del archivo en excel
     * @return codigo del municipio 
     */
    public String obtenerMunicipioNombre(String nombre) {
        try {
            log.info("Obtener Nombre Municipios");
            String[] temp = nombre.split("-");
            return temp[1].substring(0, 5);
        } catch (Exception e) {
            log.error(e);
            guardar=false;
            return "";
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel del IGAC y las convierte en un objeto
     * de tipo Catastro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre un registro de catastro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoIgacCatastro(List<String> fila) {
        try {
            guardar = true;
            log.info("mapeoIgacCatastro");
            Consultas con = new Consultas();
            Catastro cat = new Catastro();
            int i = 0;
            cat.setNumpredio(fila.get(i++));
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(1).substring(0, 2));
            cat.setAvaluo(fila.get(i++));
            cat.setSector(fila.get(i++));
            cat.setManzana(fila.get(i++));
            cat.setPredio(fila.get(i++));
            cat.setPropiedad(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            cat.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            cat.setTipo(fila.get(i++));
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 1));
            if (guardar) {
                int idCatastro = con.insertarIgacCatastro(cat);
                for (Integer idProp : ids) {
                    if (!con.insertarIgacCatastroProp(idCatastro, idProp)) {
                        log.error("No se puedo crear la relacion Catastro-propietario idCatastro: " + idCatastro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel del IGAC y las convierte en un objeto
     * de tipo Registro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre un registro de registro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     * @param nomArchivo nombre del archivo en excel, necesario para 
     * obtener el municipio del cual proviene la información
     */
    public void mapeoIgacRegistro(List<String> fila, String nomArchivo) {
        try {
            log.info("mapeoIgacRegistro");
            guardar = true;
            Registro reg = new Registro();
            Consultas con = new Consultas();
            int i = 0;
            String codMun = obtenerMunicipioNombre(nomArchivo);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            reg.setTipo(fila.get(i++));
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 1));
            if (guardar) {
                int idRegistro = con.insertarIgacRegistro(reg);
                for (Integer idProp : ids) {
                    if (!con.insertarIgacRegistroProp(idRegistro, idProp)) {
                        log.error("No se puedo crear la relacion Catastro-propietario idCatastro: " + idRegistro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel del IGAC y las convierte en un objeto
     * de tipo ITR, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación propietario-catastro y propietario-registro
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     * @param nomArch nombre del archivo en excel, necesario para 
     * obtener el municipio del cual proviene la información 
     */
    public void mapeoIgacITR(List<String> fila, String nomArch) {
        try {
            log.info("mapeoIgacITR");
            guardar = true;
            Consultas con = new Consultas();
            ITR itr = new ITR();
            int i = 0;
            String codMun = obtenerMunicipioNombre(nomArch);
            itr.setDepartamentoidfk(codMun.substring(0, 2));
            itr.setMunicipioidfk(codMun.substring(2));
            itr.setNumpredio(fila.get(i++));
            itr.setAvaluos(fila.get(i++));
            itr.setSector(fila.get(i++));
            itr.setManzana(fila.get(i++));
            itr.setPredio(fila.get(i++));
            itr.setPropiedad(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Float.parseFloat(fila.get(i++)));
            itr.setAreaconstruida(Float.parseFloat(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat = fila.get(i++);
            String propietarioReg = fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            itr.setFecharecibido(fila.get(i++));
            List<Integer> idsCat = guardarPropietarios(separarPropietarios(propietarioCat, 1));
            List<Integer> idsReg = guardarPropietarios(separarPropietarios(propietarioReg, 1));
            if (guardar) {
                int idITR = con.insertarIgacITR(itr);
                for (Integer idProp : idsCat) {
                    if (!con.insertarIgacItrCatProp(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITRCat-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
                for (Integer idProp : idsReg) {
                    if (!con.insertarIgacItrRegProp(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITRreg-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la gobernación de Antioquia
     * para el año 2014 y las convierte en un objeto
     * de tipo Catastro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre un registro de catastro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoGobAnt2014Catastro(List<String> fila) {
        try {
            log.info("mapeoGobAnt2014Catastro");
            guardar = true;
            Catastro cat = new Catastro();
            Consultas con = new Consultas();
            int i = 0;
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(0).substring(0, 2));
            cat.setNumpredio(fila.get(i++));
            cat.setPredialcat28(fila.get(i++));
            cat.setPredialcat30(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            cat.setTipo(fila.get(i++));
            cat.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 2));
            if (guardar) {
                int idCatastro = con.insertarAntioquiaCatastro(cat);
                for (Integer idProp : ids) {
                    if (!con.insertarAntioquiaCatProp(idCatastro, idProp)) {
                        log.error("No se puedo crear la relacion AntioquiaCatastro-propietario idCatastro: " + idCatastro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel del la Gobernación de 
     * Antioquia para el 2014 y las convierte en un objeto
     * de tipo Registro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre una tupla de registro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     * @param nomArch nombre del archivo en excel, necesario para 
     * obtener el municipio del cual proviene la información
     */
    public void mapeoGobAnt2014Registro(List<String> fila, String nomArch) {
        try {
            log.info("mapeoGobAnt2014Registro");
            guardar = true;
            Consultas con = new Consultas();
            Registro reg = new Registro();
            int i = 0;
            String codMun = obtenerMunicipioNombre(nomArch);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setTipo(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 2));
            if (guardar) {
                int idRegistro = con.insertarAntioquiaRegistro(reg);
                for (Integer idProp : ids) {
                    if (!con.insertarAntioquiaRegProP(idRegistro, idProp)) {
                        log.error("No se puedo crear la relacion AntReg-propietario idCatastro: " + idRegistro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la Gobernación de Antioquia 
     * para el año 2014 y las convierte en un objeto
     * de tipo ITR, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación propietario-catastro y propietario-registro
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoGobAnt2014ITR(List<String> fila) {
        try {
            log.info("mapeoGobAnt2014ITR");
            guardar = true;
            ITR itr = new ITR();
            Consultas con = new Consultas();
            int i = 0;
            itr.setMunicipioidfk(fila.get(i++));
            itr.setDepartamentoidfk(fila.get(0).substring(0, 2));
            itr.setNumpredio(fila.get(i++));
            itr.setPredialcat28(fila.get(i++));
            itr.setPredialcat30(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setTipopredio(fila.get(i++));
            itr.setAreaterreno(Float.parseFloat(fila.get(i++)));
            itr.setAreaconstruida(Float.parseFloat(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat = fila.get(i++);
            String propietarioReg = fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            itr.setFecharecibido(fila.get(i++));
            List<Integer> idsCat = guardarPropietarios(separarPropietarios(propietarioCat, 2));
            List<Integer> idsReg = guardarPropietarios(separarPropietarios(propietarioReg, 2));
            if (guardar) {
                int idITR = con.insertarAntioquiaITR(itr);
                for (Integer idProp : idsCat) {
                    if (!con.insertarAntioquiaItrCatProP(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITRCat-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
                for (Integer idProp : idsReg) {
                    if (!con.insertarAntioquiaItrRegProP(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITRreg-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la gobernación de Antioquia
     * para el año 2015 y las convierte en un objeto
     * de tipo Catastro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre un registro de catastro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoGobAnt2015Catastro(List<String> fila) {
        try {
            log.info("mapeoGobAnt2015Catastro");
            guardar = true;
            Catastro cat = new Catastro();
            Consultas con = new Consultas();
            int i = 0;
            cat.setNumpredio(fila.get(i++));
            cat.setMunicipioidfk(fila.get(i++));
            cat.setDepartamentoidfk(fila.get(1).substring(0, 2));
            cat.setAvaluo(fila.get(i++));
            cat.setCorregimiento(fila.get(i++));
            cat.setMatricula(fila.get(i++));
            cat.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            cat.setTipo(fila.get(i++));
            cat.setEstado(fila.get(i++));
            cat.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 2));
            if (guardar) {
                int idCatastro = con.insertarAntioquiaCatastro(cat);
                for (Integer idProp : ids) {
                    if (!con.insertarAntioquiaCatProp(idCatastro, idProp)) {
                        log.error("No se puedo crear la relacion Antioquia2015Catastro-propietario idCatastro: " + idCatastro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel del la Gobernación de 
     * Antioquia para el 2015 y las convierte en un objeto
     * de tipo Registro, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación entre una tupla de registro y su propietario
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     * @param nomArch nombre del archivo en excel, necesario para 
     * obtener el municipio del cual proviene la información
     */
    public void mapeoGobAnt2015Registro(List<String> fila, String nomArch) {
        try {
            log.info("mapeoGobAnt2015Registro");
            guardar = true;
            Consultas con = new Consultas();
            Registro reg = new Registro();
            int i = 0;
            String codMun = obtenerMunicipioNombre(nomArch);
            reg.setMunicipioidfk(codMun.substring(2));
            reg.setMatriculaori(fila.get(i++));
            reg.setPredialori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            String propietario = fila.get(i++);
            reg.setTipo(fila.get(i++));
            reg.setEstado(fila.get(i++));
            reg.setFecharecibido(fila.get(i++));
            List<Integer> ids = guardarPropietarios(separarPropietarios(propietario, 2));
            if (guardar) {
                int idRegistro = con.insertarAntioquiaRegistro(reg);
                for (Integer idProp : ids) {
                    if (!con.insertarAntioquiaRegProP(idRegistro, idProp)) {
                        log.error("No se puedo crear la relacion Ant2015Reg-propietario idCatastro: " + idRegistro + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la Gobernación de Antioquia 
     * para el año 2015 y las convierte en un objeto
     * de tipo ITR, para insertarlo en la base de datos, 
     * adicionalmente separa y guarda los propietarios y crea 
     * la relación propietario-catastro y propietario-registro
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoGobAnt2015ITR(List<String> fila, String nom, int tipoArc) {
        try {
            log.info("mapeoGobAnt2015ITR");
            guardar = true;
            ITR itr = new ITR();
            Consultas con = new Consultas();
            int i = 0;
            itr.setNumpredio(fila.get(i++));
            itr.setPredialcat(fila.get(i++));
            if (tipoArc == 0) {
                itr.setMunicipioidfk("05" + nom.substring(4, 7));
                itr.setDepartamentoidfk("05");
            } else if (tipoArc == 1) {
                itr.setMunicipioidfk("05" + fila.get(i++));
                itr.setDepartamentoidfk("05");
                itr.setAvaluos(fila.get(i++));
                itr.setCorregimiento(fila.get(i++));
                itr.setCirculo(fila.get(i++));
            } else if (tipoArc == 2) {
                itr.setMunicipioidfk("05" + nom.substring(4, 7));
                itr.setDepartamentoidfk("05");
                itr.setCirculo(fila.get(i++));
            }
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Float.parseFloat(fila.get(i++)));
            itr.setAreaconstruida(Float.parseFloat(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            String propietarioCat = fila.get(i++);
            String propietarioReg = fila.get(i++);
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            itr.setFecharecibido(fila.get(i++));
            List<Integer> idsCat = guardarPropietarios(separarPropietarios(propietarioCat, 2));
            List<Integer> idsReg = guardarPropietarios(separarPropietarios(propietarioReg, 2));
            if (guardar) {
                int idITR = con.insertarAntioquiaITR(itr);
                for (Integer idProp : idsCat) {
                    if (!con.insertarAntioquiaItrCatProP(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITR2015Cat-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
                for (Integer idProp : idsReg) {
                    if (!con.insertarAntioquiaItrRegProP(idITR, idProp)) {
                        log.error("No se puedo crear la relacion ITR2015reg-propietario. idCatastro: " + idITR + " idProp: " + idProp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la alcaldía de medellín 
     * y las convierte en un objeto
     * de tipo Catastro, para insertarlo en la base de datos.
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoMedellinCatastro(List<String> fila) {
        try {
            log.info("mapeoMedellinCatastro");
            guardar = true;
            Consultas con = new Consultas();
            Catastro cat = new Catastro();
            int i = 0;
            cat.setIdpredio((int) Float.parseFloat(fila.get(i++)));
            cat.setNumpredio(fila.get(i++));
            cat.setAreaterreno(Float.parseFloat(fila.get(i++)));
            cat.setAreaconstruida(Float.parseFloat(fila.get(i++)));
            cat.setDireccion(fila.get(i++));
            cat.setTipo(fila.get(i++));
            cat.setMunicipioidfk("05001");
            cat.setDepartamentoidfk("05");
            cat.setFecharecibido(fila.get(i++));
            if (guardar) {
                con.insertarMedellinCatastro(cat);
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la alcaldía de medellín 
     * y las convierte en un objeto
     * de tipo Registro, para insertarlo en la base de datos.
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoMedellinRegistro(List<String> fila) {
        try {
            log.info("mapeoMedellinRegistro");
            guardar = true;
            Consultas con = new Consultas();
            Registro reg = new Registro();
            int i = 0;
            reg.setCirculo(fila.get(i++));
            reg.setMatriculaori(fila.get(i++));
            reg.setDireccion(fila.get(i++));
            reg.setTipo(fila.get(i++));
            reg.setFecharecibido(fila.get(i++));
            if (guardar) {
                con.insertarMedellinRegistro(reg);
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }

    /**
     * Método que toma una lista de tipo String con las
     * celdas de una fila en excel de la alcaldía de medellín 
     * y las convierte en un objeto
     * de tipo ITR, para insertarlo en la base de datos.
     * @param fila Lista de tipo String donde cada posición es la
     * celda de una determinada fila
     */
    public void mapeoMedellinITR(List<String> fila) {
        try {
            log.info("mapeoMedellinITR");
            guardar = true;
            Consultas con = new Consultas();
            ITR itr = new ITR();
            int i = 0;
            itr.setIdpredio((int) Float.parseFloat(fila.get(i++)));
            itr.setNumpredio(fila.get(i++));
            itr.setCirculo(fila.get(i++));
            itr.setMatricula(fila.get(i++));
            itr.setAreaterreno(Float.parseFloat(fila.get(i++)));
            itr.setAreaconstruida(Float.parseFloat(fila.get(i++)));
            itr.setDireccioncat(fila.get(i++));
            itr.setDireccionreg(fila.get(i++));
            itr.setTipopredio(fila.get(i++));
            itr.setCrpredial(fila.get(i++));
            itr.setCrmatricula(fila.get(i++));
            itr.setCrdireccion(fila.get(i++));
            itr.setCrdocumento(fila.get(i++));
            itr.setCrnombre(fila.get(i++));
            itr.setFecharecibido(fila.get(i++));
            if (guardar) {
                con.insertarMedellinITR(itr);
            }
        } catch (Exception e) {
            log.error("Error en el mapeo: ", e);
        }
    }
}
