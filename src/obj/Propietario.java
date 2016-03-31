/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package obj;

/**
 * Clase que permite mapearlos propietarios de los archivos de excel a objetos java,
 * para luego ser guardados en la base de datos
 * @author cesar solano
 */
public class Propietario {

    public Propietario() {
    }
    
    private int propietarioid;
    private String tipoDoc;
    private String numDoc;
    private String nombrePropietario;

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public int getPropietarioid() {
        return propietarioid;
    }

    public void setPropietarioid(int propietarioid) {
        this.propietarioid = propietarioid;
    }
}
