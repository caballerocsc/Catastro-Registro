/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.util.ArrayList;
import java.util.List;
import obj.Catastro;
import obj.Propietario;

/**
 *
 * @author Usuario
 */
public class Mapeo {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Mapeo.class);
    
    
    public Mapeo() {
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
        }else
            log.warn("El propietario se encuentra vacio");
        return listaProp;
    }
    
    public List<Integer> guardarPropietarios(List<Propietario> propietarios){
        List<Integer> identificadores=new ArrayList<>();
        Consultas con=new Consultas();
        for (Propietario p : propietarios) {
            int id=con.verificarPropietario(p);
            if(id==0)
                identificadores.add(con.insertarPropietario(p));
            else 
                identificadores.add(id);
        }
        return identificadores;
    }
    public void mapeoIgacCatastro(List<String> fila){
        Consultas con= new Consultas();
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
                List<Integer> ids = guardarPropietarios(separarPropietarios(propietario));
                int idCatastro = con.insertarIgacCatastro(cat);
                for (Integer idProp : ids) {
                    if (!con.insertarIgacCatastroProp(idCatastro, idProp)) {
                        log.error("No se puedo crear la relacion Catastro-propietario idCatastro: " + idCatastro + " idProp: " + idProp);
                    }
                }
    }
    
}
